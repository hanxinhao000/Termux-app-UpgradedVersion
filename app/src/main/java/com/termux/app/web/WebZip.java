package main.java.com.termux.app.web;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;

import com.termux.R;

import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;

import main.java.com.termux.app.dialog.LoadingDialog;
import main.java.com.termux.core.CoreLinux;
import main.java.com.termux.utils.CustomTextView;
import main.java.com.termux.utils.UUtils;

/**
 * @author ZEL
 * @create By ZEL on 2020/12/22 16:49
 **/
public class WebZip {



    public static  boolean isRun = false;
    ///sdcard/xinhao/web_config/lighttpd/hosts
    public static File webFile = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_php_html");
    public static File webFile1 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_php_html/port_html");
    public static File webFile3 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_php_html/port_html_utermux");
    public static File configFile = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config");
    public static File configFileNginx = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config/conf/nginx/");
    public static File configFileNginx2 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config/hosts/nginx/");
    public static File configFileNginx3 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config/logs");
    public static File configFileNginx4 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config/lighttpd/hosts");
    public static File configFileNginx5 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config/tmp");
    public static File configFileNginx6 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config/sessions");
    public static File configFileNginx7 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config/packages");
    public static File configFileNginx8 = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config/hosts/lighttpd");
    public static File configFileNginxXinhao = new File(Environment.getExternalStorageDirectory(),"/xinhao");
    public static File configFileNginx9 = new File("/data/data/com.termux/home/.components/tmp");
    public static File configFileData = new File(Environment.getExternalStorageDirectory(),"/xinhao/data");


    public static void unZipWeb(LinearLayout linearLayout){


        File file = new File("/data/data/com.termux/files/home/.components/components");

        if (!file.exists()) {
            RepoInstallerTask task = new RepoInstallerTask(linearLayout,null,null);
            task.execute("", Constants.INTERNAL_LOCATION.concat("/"));
        }



    }

    public static boolean isInstall(){

        return new File("/data/data/com.termux/files/home/.components/components").exists();

    }

    public static void installQZ(LinearLayout linearLayout, InputStream inputStream, Activity activity){

        WebZip.createWJJ();
        LoadingDialog loadingDialog = new LoadingDialog(activity);
        loadingDialog.show();
        loadingDialog.msg_dialog.setText(UUtils.getString(R.string.正在安装));
        RepoInstallerTask task = new RepoInstallerTask(linearLayout,inputStream,loadingDialog);
        task.execute("", Constants.INTERNAL_LOCATION.concat("/"));

    }


    public static void startServer(String port, LinearLayout linearLayout, CustomTextView customTextView){
        isRun = true;
       /* UUtils.getContext().startService(new Intent(UUtils.getContext(), ServerService.class));
        CommandTask task = CommandTask.createForConnect(UUtils.getContext(), "lighttpd", port);
        task.enableSU(false);
        task.execute();*/
        WebZip.createWJJ();


        linearLayout.setVisibility(View.VISIBLE);
        customTextView.setText(UUtils.getString(R.string.正在启动));
        UUtils.showLog("运行状态[启动]:1" + CoreLinux.getIsRun());
        if(!(CoreLinux.getIsRun())){
            CoreLinux.runCoreLinux();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    UUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            CoreLinux.runCmd("termux-chroot \n");
                            UUtils.showLog("运行状态[启动]:" + CoreLinux.getText());
                        }
                    });
                }
            }).start();

        }

        UUtils.showLog("运行状态[启动]:" + CoreLinux.getIsRun());


        UUtils.showLog("运行状态:" + CoreLinux.getText());
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                UUtils.runOnUIThread(new Runnable() {
                    @Override
                    public void run() {
                        //CoreLinux.runCmd("chmod 0755 ~/.components/scripts/* && chmod 0755 -R ~/.components/components/ && ~/.components/scripts/server-sh.sh lighttpd 8080\n");
                        CoreLinux.runCmd("chmod 0755 ~/.components/scripts/* && ~/.components/scripts/server-sh.sh lighttpd 8080 & \n");
                        UUtils.showLog("运行状态[启动]:" + CoreLinux.getText());
                        linearLayout.setVisibility(View.GONE);
                    }
                });
            }
        }).start();



    }

    public static void stopServer(){

        isRun = false;
       /* CommandTask task = CommandTask.createForDisconnect(UUtils.getContext());
        task.enableSU(false);
        task.execute();
*/
        NotificationManager notify = (NotificationManager) UUtils.getContext().
            getSystemService(Context.NOTIFICATION_SERVICE);
        notify.cancel(143);

        if(!CoreLinux.getIsRun()){
            return;
        }


       // CoreLinux.runCmd("chmod 0755 ~/.components/scripts/* && ~/.components/scripts/shutdown-sh.sh \n");
        CoreLinux.runCmd("cd ~/.components/scripts/ && ./shutdown-sh.sh\n\n\n");
        UUtils.showLog("运行状态[结束]:" + CoreLinux.getText());

    }

    public static void createWJJ(){
        UUtils.showLog("运行状态:创建文件夹" );
        createLJ(configFileNginxXinhao);

        if (!(WebZip.webFile.exists())) {
            WebZip.webFile.mkdirs();
        }


        if (!(WebZip.configFile.exists())) {
            WebZip.configFile.mkdirs();
        }
        if (!(WebZip.configFileNginx.exists())) {
            WebZip.configFileNginx.mkdirs();
        }

        if (!(WebZip.configFileNginx2.exists())) {
            WebZip.configFileNginx2.mkdirs();
        }



        createLJ(configFileNginx3);
        createLJ(configFileNginx4);
        createLJ(configFileNginx5);
        createLJ(configFileNginx6);
        createLJ(configFileNginx7);
        createLJ(configFileNginx8);
        createLJ(webFile1);
        createLJ(webFile3);
        createLJ(configFileNginx9);
        createLJ(configFileData);

    }

    private static void createLJ(File file){

        if (!(file.exists())) {
            file.mkdirs();
        }


    }
}
