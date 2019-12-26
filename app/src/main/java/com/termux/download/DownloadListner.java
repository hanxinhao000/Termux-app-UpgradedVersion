package main.java.com.termux.download;

/**
 * 下载监听
 *
 * @author Cheny
 */
public interface DownloadListner {
    void onFinished();

    void onProgress(float progress);

    void onPause();

    void onCancel();

    void error();
}
