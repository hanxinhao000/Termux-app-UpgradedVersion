package main.java.com.termux.download;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Cheny on 2017/4/29.
 */

public class DownloadTask extends Handler {

    private final int THREAD_COUNT = 4;//线程数
    private FilePoint mPoint;
    private long mFileLength;

    private volatile boolean isDownloading = false;
    private AtomicInteger childCanleCount = new AtomicInteger(0);//子线程取消数量
    private AtomicInteger childPauseCount = new AtomicInteger(0);//子线程暂停数量
    private AtomicInteger childFinshCount = new AtomicInteger(0);//子线程完成数量
    private HttpUtil mHttpUtil;
    private long[] mProgress;
    private File[] mCacheFiles;
    private File mTmpFile;//临时占位文件
    private volatile boolean pause;//是否暂停
    private volatile boolean cancel;//是否取消下载
    private static final int MSG_PROGRESS = 1;//进度
    private static final int MSG_FINISH = 2;//完成下载
    private static final int MSG_PAUSE = 3;//暂停
    private static final int MSG_CANCEL = 4;//暂停
    private DownloadListner mListner;//下载回调监听


    /**
     * 任务管理器初始化数据
     *
     * @param point
     * @param l
     */
    DownloadTask(FilePoint point, DownloadListner l) {
        this.mPoint = point;
        this.mListner = l;
        this.mProgress = new long[THREAD_COUNT];
        this.mCacheFiles = new File[THREAD_COUNT];
        this.mHttpUtil = HttpUtil.getInstance();
    }

    /**
     * 任务回调消息
     *
     * @param msg
     */
    @Override
    public void handleMessage(Message msg) {
        if (null == mListner) {
            return;
        }
        switch (msg.what) {
            case MSG_PROGRESS://进度
                long progress = 0;
                for (int i = 0, length = mProgress.length; i < length; i++) {
                    progress += mProgress[i];
                }
                mListner.onProgress(progress * 1.0f / mFileLength);
                break;
            case MSG_PAUSE://暂停
                if (confirmStatus(childPauseCount)) return;
                resetStutus();
                mListner.onPause();
                break;
            case MSG_FINISH://完成
                if (confirmStatus(childFinshCount)) return;
                mTmpFile.renameTo(new File(mPoint.getFilePath(), mPoint.getFileName()));//下载完毕后，重命名目标文件名
                resetStutus();
                mListner.onFinished();
                break;
            case MSG_CANCEL://取消
                if (confirmStatus(childCanleCount)) return;
                resetStutus();
                mProgress = new long[THREAD_COUNT];
                mListner.onCancel();
                break;
        }
    }

    private static final String TAG = "DownloadTask";

    public synchronized void start() {
        try {
            Log.e(TAG, "start: " + isDownloading + "\t" + mPoint.getUrl());
            if (isDownloading) return;
            isDownloading = true;
            mHttpUtil.getContentLength(mPoint.getUrl(), new okhttp3.Callback() {
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    Log.e(TAG, "start: " + response.code() + "\t isDownloading:" + isDownloading + "\t" + mPoint.getUrl());
                    if (response.code() != 200) {
                        close(response.body());
                        resetStutus();
                        return;
                    }
                    // 获取资源大小
                    mFileLength = response.body().contentLength();
                    close(response.body());
                    // 在本地创建一个与资源同样大小的文件来占位
                    mTmpFile = new File(mPoint.getFilePath(), mPoint.getFileName() + ".tmp");
                    if (!mTmpFile.getParentFile().exists()) mTmpFile.getParentFile().mkdirs();
                    RandomAccessFile tmpAccessFile = new RandomAccessFile(mTmpFile, "rw");
                    tmpAccessFile.setLength(mFileLength);
                    /*将下载任务分配给每个线程*/
                    long blockSize = mFileLength / THREAD_COUNT;// 计算每个线程理论上下载的数量.

                    /*为每个线程配置并分配任务*/
                    for (int threadId = 0; threadId < THREAD_COUNT; threadId++) {
                        long startIndex = threadId * blockSize; // 线程开始下载的位置
                        long endIndex = (threadId + 1) * blockSize - 1; // 线程结束下载的位置
                        if (threadId == (THREAD_COUNT - 1)) { // 如果是最后一个线程,将剩下的文件全部交给这个线程完成
                            endIndex = mFileLength - 1;
                        }
                        download(startIndex, endIndex, threadId);// 开启线程下载
                    }
                }

                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG, "start:Exception " + e.getMessage() + "\n" + mPoint.getUrl());
                    resetStutus();
                    mListner.error();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            resetStutus();
        }
    }

    private void download(final long startIndex, final long endIndex, final int threadId) throws IOException {
        long newStartIndex = startIndex;
        // 分段请求网络连接,分段将文件保存到本地.
        // 加载下载位置缓存文件
        final File cacheFile = new File(mPoint.getFilePath(), "thread" + threadId + "_" + mPoint.getFileName() + ".cache");
        mCacheFiles[threadId] = cacheFile;
        final RandomAccessFile cacheAccessFile = new RandomAccessFile(cacheFile, "rwd");
        if (cacheFile.exists()) {// 如果文件存在
            String startIndexStr = cacheAccessFile.readLine();
            try {
                newStartIndex = Integer.parseInt(startIndexStr);//重新设置下载起点
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        final long finalStartIndex = newStartIndex;
        mHttpUtil.downloadFileByRange(mPoint.getUrl(), finalStartIndex, endIndex, new okhttp3.Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e(TAG, "download: " + response.code() + "\t isDownloading:" + isDownloading + "\t" + mPoint.getUrl());
                if (response.code() != 206) {// 206：请求部分资源成功码
                    resetStutus();
                    return;
                }
                InputStream is = response.body().byteStream();// 获取流
                RandomAccessFile tmpAccessFile = new RandomAccessFile(mTmpFile, "rw");// 获取前面已创建的文件.
                tmpAccessFile.seek(finalStartIndex);// 文件写入的开始位置.
                /*  将网络流中的文件写入本地*/
                byte[] buffer = new byte[1024 << 2];
                int length = -1;
                int total = 0;// 记录本次下载文件的大小
                long progress = 0;
                while ((length = is.read(buffer)) > 0) {
                    if (cancel) {
                        //关闭资源
                        close(cacheAccessFile, is, response.body());
                        cleanFile(cacheFile);
                        sendEmptyMessage(MSG_CANCEL);
                        return;
                    }
                    if (pause) {
                        //关闭资源
                        close(cacheAccessFile, is, response.body());
                        //发送暂停消息
                        sendEmptyMessage(MSG_PAUSE);
                        return;
                    }
                    tmpAccessFile.write(buffer, 0, length);
                    total += length;
                    progress = finalStartIndex + total;

                    //将当前现在到的位置保存到文件中
                    cacheAccessFile.seek(0);
                    cacheAccessFile.write((progress + "").getBytes("UTF-8"));
                    //发送进度消息
                    mProgress[threadId] = progress - startIndex;
                    sendEmptyMessage(MSG_PROGRESS);
                }
                //关闭资源
                close(cacheAccessFile, is, response.body());
                // 删除临时文件
                cleanFile(cacheFile);
                //发送完成消息
                sendEmptyMessage(MSG_FINISH);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                isDownloading = false;
                mListner.error();
            }
        });
    }

    /**
     * 关闭资源
     *
     * @param closeables
     */
    private void close(Closeable... closeables) {
        int length = closeables.length;
        try {
            for (int i = 0; i < length; i++) {
                Closeable closeable = closeables[i];
                if (null != closeable)
                    closeables[i].close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            for (int i = 0; i < length; i++) {
                closeables[i] = null;
            }
        }
    }

    /**
     * 删除临时文件
     */
    private void cleanFile(File... files) {
        for (int i = 0, length = files.length; i < length; i++) {
            if (null != files[i])
                files[i].delete();
        }
    }

    /**
     * 暂停
     */
    public void pause() {
        pause = true;
    }

    /**
     * 取消
     */
    public void cancel() {
        cancel = true;
        cleanFile(mTmpFile);
        if (!isDownloading) {
            if (null != mListner) {
                cleanFile(mCacheFiles);
                resetStutus();
                mListner.onCancel();
            }
        }
    }

    /**
     * 重置下载状态
     */
    private void resetStutus() {
        pause = false;
        cancel = false;
        isDownloading = false;
    }

    /**
     * 确认下载状态
     *
     * @param count
     * @return
     */
    private boolean confirmStatus(AtomicInteger count) {
        return count.incrementAndGet() % THREAD_COUNT != 0;
    }

    public boolean isDownloading() {
        return isDownloading;
    }
}
