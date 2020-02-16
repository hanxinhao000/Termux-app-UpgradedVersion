package main.java.com.termux.activity;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.termux.R;

import java.io.File;

import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.download.DownloadListner;
import main.java.com.termux.download.DownloadManager;

public class DownLoadService extends Service {

    public static int FLAG = 0;

    public static boolean isDown = false;

    public static boolean isDownError = false;


    private static final String NOTIFICATION_CHANNEL_ID = "termux_notification_channel_down";

    private static final int NOTIFICATION_ID = 0xdead1345;

    public static String pro = "0%";
    public static int proInt = 0;

    private DownloadManager mDownloadManager;

    public DownLoadService() {
    }


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //开始下载
        startForeground(NOTIFICATION_ID, buildNotification("下载服务已启动", "当前无下载任务!"));

        downLoad();
        return super.onStartCommand(intent, flags, startId);
    }

    //下载
    private void downLoad() {

        String url = "";
        String path = "";
        String name = "";

        isDownError = false;

        switch (FLAG) {

            case 0:
                url = "http://23.235.155.110:29952/listdata/termux_maoxian.zip";//

                File file1 = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/");
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                path = file1.getAbsolutePath();
                name = "termux_maoxian.zip";

                break;
            case 1:
                url = "http://23.235.155.110:29952/listdata/ubuntu-xinhao.iso";//

                File file2 = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/");
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                path = file2.getAbsolutePath();
                name = "ubuntu-xinhao.iso";
                break;
            case 2:
                url = "http://23.235.155.110:29952/listdata/debian_linux.tar.gz";//
                File file3 = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/");
                if (!file3.exists()) {
                    file3.mkdirs();
                }
                path = file3.getAbsolutePath();
                name = "debian_linux.tar.gz";

                break;
            case 3:
                url = "http://23.235.155.110:29952/listdata/debian_linux_2.tar.gz";//
                File file4 = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/");
                if (!file4.exists()) {
                    file4.mkdirs();
                }
                path = file4.getAbsolutePath();
                name = "debian_linux_2.tar.gz";
                break;
            case 4:
                url = "http://23.235.155.110:29952/listdata/ubuntu_gui.tar.gz";//
                File file5 = new File(Environment.getExternalStorageDirectory(), "/xinhao/data/");
                if (!file5.exists()) {
                    file5.mkdirs();
                }
                path = file5.getAbsolutePath();
                name = "ubuntu_gui.tar.gz";
                break;


            case 5:
                url = "http://23.235.155.110:29952/listdata/C_C++.zip";
                File file6 = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/");
                if (!file6.exists()) {
                    file6.mkdirs();
                }
                path = file6.getAbsolutePath();
                name = "C_C++.zip";
                break;
            case 6:
                url = "http://23.235.155.110:29952/listdata/kail_termux.tar.gz";//
                File file7 = new File(Environment.getExternalStorageDirectory(), "/xinhao/data/");
                if (!file7.exists()) {
                    file7.mkdirs();
                }
                path = file7.getAbsolutePath();
                name = "kail_termux.tar.gz";
                break;
            case 7:
                url = "http://23.235.155.110:29952/listdata/termux_qemu.tar.gz";//
                File file8 = new File(Environment.getExternalStorageDirectory(), "/xinhao/data/");
                if (!file8.exists()) {
                    file8.mkdirs();
                }
                path = file8.getAbsolutePath();
                name = "termux_qemu.tar.gz";
                break;
            case 8:
                url = "http://23.235.155.110:29952/listdata/shengtou_msf.zip";
                File file9 = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/");
                if (!file9.exists()) {
                    file9.mkdirs();
                }
                path = file9.getAbsolutePath();
                name = "shengtou_msf.zip";
                break;


        }

        if (isDown) {
            Toast.makeText(this, "由于服务器带宽紧张,一次只能下载一个!", Toast.LENGTH_SHORT).show();
            return;
        }

        startDown(url, path, name);

    }

    //开始下载
    private void startDown(String url, String path, String fileName) {

        isDown = true;
        startForeground(NOTIFICATION_ID, buildNotification("下载服务已启动", "当前正在执行任务!"));
        mDownloadManager = DownloadManager.getInstance();

        mDownloadManager.add(url, path, fileName, new DownloadListner() {
            @Override
            public void onFinished() {
                startForeground(NOTIFICATION_ID, buildNotification("下载服务已启动", "下载完成!"));
                Toast.makeText(DownLoadService.this, "下载完成", Toast.LENGTH_SHORT).show();
                isDown = false;
            }

            @Override
            public void onProgress(float progress) {
                pro = String.format("%.2f", progress * 100) + "%";

                // startForeground(NOTIFICATION_ID, buildNotification("下载服务现已工作", pro));


                proInt = (int) (progress * 100);

                if (proInt == 100) {
                    isDown = false;
                }

            }

            @Override
            public void onPause() {


                Toast.makeText(DownLoadService.this, "下载暂停", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(DownLoadService.this, "下载取消", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error() {
                isDown = false;
                //DownLoadService.proInt = 1;
                isDownError = true;
                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(DownLoadService.this, "服务器连接失败!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        mDownloadManager.download(url);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private Notification buildNotification(String title, String msg) {
        final Resources res = getResources();
        final String contentTitle = title;
        final String contentText = msg;

        // final String intentAction = mVisibleWindow ? ACTION_HIDE : ACTION_SHOW;
        // Intent actionIntent = new Intent(this, TermuxFloatService.class).setAction(intentAction);

        Notification.Builder builder = new Notification.Builder(this).setContentTitle(contentTitle).setContentText(contentText)
            .setPriority(Notification.PRIORITY_MIN).setSmallIcon(R.mipmap.ic_launcher)
            // .setColor(0xFF000000)
            //.setContentIntent(PendingIntent.getService(this, 0, actionIntent, 0))
            .setOngoing(false)
            .setShowWhen(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupNotificationChannel();
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        //final int messageId = mVisibleWindow ? R.string.toggle_hide : R.string.toggle_show;
        //builder.addAction(android.R.drawable.ic_menu_preferences, res.getString(messageId), PendingIntent.getService(this, 0, actionIntent, 0));
        return builder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupNotificationChannel() {
        String channelName = "Termux_Backer";
        String channelDescription = "Notifications from Termux_Backer";
        int importance = NotificationManager.IMPORTANCE_LOW;

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance);
        channel.setDescription(channelDescription);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);
    }
}
