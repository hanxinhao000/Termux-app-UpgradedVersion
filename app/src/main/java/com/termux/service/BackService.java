package main.java.com.termux.service;

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
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.termux.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import main.java.com.termux.app.TermuxInstaller;
import main.java.com.termux.app.TermuxService;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.utils.ExeCommand;

import static main.java.com.termux.app.TermuxService.ACTION_STOP_SERVICE;
import static main.java.com.termux.floatwindows.TermuxFloatService.ACTION_HIDE;
import static main.java.com.termux.floatwindows.TermuxFloatService.ACTION_SHOW;

public class BackService extends Service {

    private boolean mVisibleWindow = false;

    private File mFileHomeFiles = new File("/data/data/com.termux/files/");
    private File mFileHomeFilesHome = new File("/data/data/com.termux/");
    private File mFileHomeFilesTemp = new File("/data/data/com.termux/files/temp/");
    private File mFileHomeFilesTempFiles = new File("/data/data/com.termux/files/temp/xinhao.tar");
    private File mFileHomeFilesGz = new File(Environment.getExternalStorageDirectory() + "/xinhao/data/");

    private static final int NOTIFICATION_ID = 0xdead1338;

    private static final String NOTIFICATION_CHANNEL_ID = "termux_notification_channel_back";
    public static final String BACK_FILES = "back_files";
    public static final String RESTORE_FILES = "restore_files";

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    private String fileName = mFileHomeFilesGz.getAbsolutePath() + "/" + simpleDateFormat.format(new Date()) + ".tar";

    private File mFileHome = new File("/data/data/com.termux/busybox");


    private boolean isRun = false;

    public static boolean RES_ISRUN = false;

    public BackService() {
    }

    @Override
    public IBinder onBind(Intent intent) {


        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // startBack();


    }

/*

    private void startBack() {

        if (isRun) {
            Toast.makeText(this, "上一个任务还没有完成,请耐心等待!", Toast.LENGTH_SHORT).show();
            return;
        }


        startForeground(NOTIFICATION_ID, buildNotification("备份正在运行", "后台正在备份,您仍然可以继续使用termux,备份目录[sdcard ->/xinhao/data/]"));


        String cmd = getCompressCmd(mFileHomeFiles.getAbsolutePath(),
            fileName, "tar");
        //压缩


        new Thread(new Runnable() {
            @Override
            public void run() {
                isRun = true;
                int i = P7ZipApi.executeCommand(cmd);

                if (i == 0) {

                    String cmd = getCompressCmd(fileName,
                        fileName + ".gz", "gzip");

                    int i1 = P7ZipApi.executeCommand(cmd);

                    if (i1 == 0) {
                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                new File(fileName).delete();
                                isRun = false;
                                startForeground(NOTIFICATION_ID, buildNotification("备份完成!", "备份已完成"));
                                Toast.makeText(BackService.this, "备份成功!请到[sdcard ->/xinhao/data/]下进行查看", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                startForeground(NOTIFICATION_ID, buildNotification("备份失败!", "备份失败,请重试!"));
                                Toast.makeText(BackService.this, "备份失败!请重试", Toast.LENGTH_SHORT).show();
                                isRun = false;
                            }
                        });

                    }

                } else {
                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            startForeground(NOTIFICATION_ID, buildNotification("备份失败!", "备份失败,请重试!"));
                            Toast.makeText(BackService.this, "备份失败!请重试", Toast.LENGTH_SHORT).show();
                            isRun = false;
                        }
                    });

                }


            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        if (BACK_FILES.equals(intent.getAction())) {

            startBack();

        } else if (RESTORE_FILES.equals(intent.getAction())) {

            String res_file_url = intent.getStringExtra("res_file_url");

            startRestore(res_file_url);
        } else {

            Toast.makeText(this, "不能识别的请求!", Toast.LENGTH_SHORT).show();

        }

        // buildNotification();
        return super.onStartCommand(intent, flags, startId);

    }


    private void startRestore(String fileUrl) {

        if(RES_ISRUN){

            Toast.makeText(this, "已经有一个任务在进行了!", Toast.LENGTH_SHORT).show();
            return ;
        }

        RES_ISRUN = true;

        startForeground(NOTIFICATION_ID, buildNotification("恢复正在运行", "后台正在恢复,在此期间你无法使用termux！"));


        Toast.makeText(this, "开始清除数据...", Toast.LENGTH_SHORT).show();


        String cpu = TermuxInstaller.determineTermuxArchName();

        switch (cpu) {
            case "aarch64":
                writerFile("arm_64/busybox", mFileHome, 1024);
                break;
            case "arm":
                writerFile("arm/busybox", mFileHome, 1024);
                break;
            case "x86_64":
                writerFile("x86/busybox", mFileHome, 1024);
                break;
        }

        try {
            Runtime.getRuntime().exec("chmod 777 " + mFileHome.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BackService.this, "开始擦除数据..", Toast.LENGTH_SHORT).show();
                    }
                });

                ExeCommand cmd2 = new ExeCommand(false).run(mFileHome.getAbsolutePath() + " rm -rf " + mFileHomeFiles.getAbsolutePath(), 60000);
                while (cmd2.isRunning()) {
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {

                    }
                    String buf = cmd2.getResult();
                    //do something}

                    Log.e("XINHAO_HAN", "run: " + buf);
                }


                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BackService.this, "数据擦除完成,正在写入..", Toast.LENGTH_SHORT).show();
                    }
                });

                if (!mFileHomeFilesTemp.exists()) {
                    mFileHomeFilesTemp.mkdirs();
                }

                String cmd = getExtractCmd(fileUrl,
                    mFileHomeFilesTempFiles.getAbsolutePath());

                int i = P7ZipApi.executeCommand(cmd);

                if (i == 0) {


                    String cmd1 = getExtractCmd(mFileHomeFilesTempFiles.getAbsolutePath(),
                        mFileHomeFilesHome.getAbsolutePath());


                    int i1 = P7ZipApi.executeCommand(cmd1);


                    if (i1 == 0) {
                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                new File(fileName).delete();

                                startForeground(NOTIFICATION_ID, buildNotification("恢复完成!", "恢复已完成,请重启app"));
                                Toast.makeText(BackService.this, "恢复完成!正在重启app,如果重启之后,还是不正常,请再次重启!", Toast.LENGTH_SHORT).show();
                                RES_ISRUN = false;
                                mFileHomeFilesTemp.delete();
                                startService(new Intent(BackService.this, TermuxService.class).setAction(ACTION_STOP_SERVICE));
                            }
                        });

                    } else {
                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                startForeground(NOTIFICATION_ID, buildNotification("恢复失败!", "恢复失败,请重试!"));
                                Toast.makeText(BackService.this, "恢复失败!请重试", Toast.LENGTH_SHORT).show();
                                RES_ISRUN = false;
                            }
                        });

                    }


                } else {

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            startForeground(NOTIFICATION_ID, buildNotification("恢复失败!", "恢复失败,请重试!"));
                            Toast.makeText(BackService.this, "恢复失败!请重试", Toast.LENGTH_SHORT).show();
                            RES_ISRUN = false;
                        }
                    });

                }
            }
        }).start();


    }

    private Notification buildNotification(String title, String msg) {
        final Resources res = getResources();
        final String contentTitle = title;
        final String contentText = msg;

        final String intentAction = mVisibleWindow ? ACTION_HIDE : ACTION_SHOW;
        // Intent actionIntent = new Intent(this, TermuxFloatService.class).setAction(intentAction);

        Notification.Builder builder = new Notification.Builder(this).setContentTitle(contentTitle).setContentText(contentText)
            .setPriority(Notification.PRIORITY_MIN).setSmallIcon(R.mipmap.ic_service_notification)
            .setColor(0xFF000000)
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

    */
/*

         /* String cmd = getCompressCmd(info.getFilePath(),
            info.getFilePath() + ".tar", "tar");*//*

    //压缩
    // P7ZipApi.executeCommand("");

    */
/*//*
/解压
     *//*
*/
/*
        String cmd = Command.getExtractCmd(info.getFilePath(),
                info.getFilePath() + "-ext");
         *//*



    //解压
    public static String getExtractCmd(String archivePath, String outPath) {
        return String.format("7z x '%s' '-o%s' -aoa", archivePath, outPath);
    }

    //压缩

    public static String getCompressCmd(String filePath, String outPath, String type) {
        return String.format("7z a -t%s '%s' '%s'", type, outPath, filePath);
    }


    private void writerFile(String name, File mFile, int size) {

        try {
            InputStream open = this.getAssets().open(name);

            int len = 0;

            byte[] b = new byte[size];

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read(b)) != -1) {
                fileOutputStream.write(b, 0, len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }
*/

}
