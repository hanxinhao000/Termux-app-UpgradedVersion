package main.java.com.termux.app.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.system.Os;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;
import com.termux.terminal.EmulatorDebug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import androidx.annotation.NonNull;
import main.java.com.termux.activity.BackNewActivity;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.app.TermuxInstaller;
import main.java.com.termux.app.TermuxService;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.utils.UUtils;
import main.java.com.termux.view.MyDialog;

/**
 * @author ZEL
 * @create By ZEL on 2020/11/23 11:43
 **/
public class SystemInstallDialog extends BaseDialogCentre {


    private LinearLayout location_install;
    private LinearLayout file_install;
    private LinearLayout termux_install;
    private LinearLayout tar;
    private LinearLayout download_server;
    private LinearLayout install_location_server;
    private Activity activity;
    private Runnable whenDone;
    final File PREFIX_FILE = new File(TermuxService.PREFIX_PATH);
    private TextShowDialog textShowDialog;
    private MyDialog myDialog;

    public SystemInstallDialog(@NonNull Context context, Activity activity,  Runnable whenDone) {
        super(context);
        this.activity = activity;
        this.whenDone = whenDone;
    }

    public SystemInstallDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private File switchFile;

    @Override
    void initViewDialog(View mView) {

        location_install = mView.findViewById(R.id.location_install);
        file_install = mView.findViewById(R.id.file_install);
        termux_install = mView.findViewById(R.id.termux_install);
        download_server = mView.findViewById(R.id.download_server);
        install_location_server = mView.findViewById(R.id.install_location_server);
        tar = mView.findViewById(R.id.tar);
        tar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // activity.startActivity(new Intent(activity, BackNewActivity.class));
                TextShowDialog textShowDialog = new TextShowDialog(activity);
                textShowDialog.edit_text.setText(UUtils.getString(R.string.没有基础系统会导致));
                textShowDialog.show();
                textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textShowDialog.dismiss();
                    }
                });

            }
        });

        install_location_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                installSystem(3);
            }
        });


        location_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                installSystem(0);

            }
        });

        download_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /***
                 *
                 *
                 * 链接: https://pan.baidu.com/s/1LXkPQCGSh32sjjLxeOxnHg 提取码: f24q 复制这段内容后打开百度网盘手机App，操作更方便哦
                 * --来自百度网盘超级会员v3的分享
                 *
                 */
                //链接：https://pan.baidu.com/s/1j7s2F_stI7cJ4A96rnV7jg
                //提取码：573u
                AlertDialog.Builder ab = new AlertDialog.Builder(activity);

                ab.setTitle(UUtils.getString(R.string.提示));

                ab.setMessage(UUtils.getString(R.string.离线本地系统yyyyuuu));

                ab.setPositiveButton(UUtils.getString(R.string.前往), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ab.create().dismiss();

                        Intent intent = new Intent();
                        intent.setData(Uri.parse("https://pan.baidu.com/s/1LXkPQCGSh32sjjLxeOxnHg"));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        activity.startActivity(intent); //启动浏览器

                    }
                });

                ab.setNegativeButton(UUtils.getString(R.string.取消), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });

                ab.show();


            }
        });
        file_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //
                SystemInstallListDialog systemInstallDialog = new SystemInstallListDialog(activity);
                systemInstallDialog.show();
                systemInstallDialog.setOnItemFileClickListener(new SystemInstallListDialog.OnItemFileClickListener() {
                    @Override
                    public void onItemClick(File file) {
                        systemInstallDialog.dismiss();
                        UUtils.showLog("显示路径:" + file.getAbsolutePath());
                        switchFile = file;
                        installSystem(1);
                    }
                });
            }
        });

        termux_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //installSystem(2);

                //链接：https://pan.baidu.com/s/1j7s2F_stI7cJ4A96rnV7jg
                //提取码：573u
                AlertDialog.Builder ab = new AlertDialog.Builder(activity);

                ab.setTitle(UUtils.getString(R.string.提示));

                ab.setMessage(UUtils.getString(R.string.离线系统yyyyuuu));

                ab.setPositiveButton(UUtils.getString(R.string.前往), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ab.create().dismiss();

                        Intent intent = new Intent();
                        intent.setData(Uri.parse("https://pan.baidu.com/s/1j7s2F_stI7cJ4A96rnV7jg"));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        activity.startActivity(intent); //启动浏览器

                    }
                });

                ab.setNegativeButton(UUtils.getString(R.string.取消), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });

                ab.show();
            }
        });

    }

    private void installSystem(int index){


        myDialog = new MyDialog(activity);

        myDialog.setCancelable(false);

        Log.e("XINHAO_HAN", "setupIfNeeded: " + "弹出dialog");

        TextView dialog_title = myDialog.getDialog_title();

        dialog_title.setText("正在安装[群:714730084]...\n现已全部使用本地安装方式\n本程序已稳定,除非出现和官方一样的BUG");

        myDialog.getDialog_pro_prog().setMax(2066);

        myDialog.show();
        Log.e("XINHAO_HAN", "setupIfNeeded: " + "开始进程");
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{

                    final String STAGING_PREFIX_PATH = TermuxService.FILES_PATH + "/usr-staging";
                    final File STAGING_PREFIX_FILE = new File(STAGING_PREFIX_PATH);

                    if (STAGING_PREFIX_FILE.exists()) {
                        deleteFolder(STAGING_PREFIX_FILE);
                    }

                    final byte[] buffer = new byte[8096];
                    final List<Pair<String, String>> symlinks = new ArrayList<>(50);

                    final URL zipUrl = determineZipUrl();

                    InputStream inputStream = null;

                    Log.e("XINHAO_HAN", "高版本: " + determineTermuxArchName());


                    switch (index){

                        case 0:
                            if ("aarch64".equals(determineTermuxArchName())) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                    inputStream = activity.getAssets().open("bootstrap-aarch64-v25.zip");

                                    Log.e("XINHAO_HAN", "高版本: " + inputStream);
                                }
                            }

                            break;
                        case 1:



                            inputStream = new FileInputStream(switchFile);

                            break;
                        case 2:
                            inputStream = zipUrl.openStream();
                            break;
                        case 3:

                            String s = TermuxInstaller.determineTermuxArchName();

                            switch (s){


                                case "aarch64":
                                    String urlArm64 = "http://127.0.0.1:19955/utermux/bootstrap-aarch64-v32.zip";
                                    inputStream = new URL(urlArm64).openStream();
                                    break;
                                case "arm":
                                    String urlArm = "http://127.0.0.1:19955/utermux/bootstrap-arm-v32.zip";
                                    inputStream = new URL(urlArm).openStream();
                                    break;
                                case "x86_64":
                                    String urlAmd = "http://127.0.0.1:19955/utermux/bootstrap-x86_64-v32.zip";
                                    inputStream = new URL(urlAmd).openStream();
                                    break;
                                case "i686":
                                    String urlI686 = "http://127.0.0.1:19955/utermux/bootstrap-i686-v32.zip";
                                    inputStream = new URL(urlI686).openStream();
                                    break;

                            }



                            break;



                    }


                    Log.e("XINHAO_HAN", "外边: " + inputStream);
                    try (ZipInputStream zipInput = new ZipInputStream(inputStream == null ? zipUrl.openStream() : inputStream)) {

                        ZipEntry zipEntry;
                        int i = 0;
                        while ((zipEntry = zipInput.getNextEntry()) != null) {

                            i++;

                            Log.e("XINHAO_HAN_JS", "run: " + i);
                            int finalI = i;
                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    myDialog.getDialog_pro_prog().setProgress(finalI);
                                }
                            });

                            Log.e("XINHAO_HAN", "run: " + zipEntry.getSize());


                            ZipEntry finalZipEntry = zipEntry;
                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    myDialog.getDialog_pro().setText(finalZipEntry.getName());
                                }
                            });

                            if (zipEntry.getName().equals("SYMLINKS.txt")) {
                                BufferedReader symlinksReader = new BufferedReader(new InputStreamReader(zipInput));
                                String line;
                                while ((line = symlinksReader.readLine()) != null) {
                                    String[] parts = line.split("←");
                                    if (parts.length != 2)
                                        throw new RuntimeException("Malformed symlink line: " + line);
                                    String oldPath = parts[0];
                                    String newPath = STAGING_PREFIX_PATH + "/" + parts[1];
                                    symlinks.add(Pair.create(oldPath, newPath));

                                    ensureDirectoryExists(new File(newPath).getParentFile());
                                }
                            } else {
                                String zipEntryName = zipEntry.getName();
                                File targetFile = new File(STAGING_PREFIX_PATH, zipEntryName);
                                boolean isDirectory = zipEntry.isDirectory();

                                ensureDirectoryExists(isDirectory ? targetFile : targetFile.getParentFile());

                                if (!isDirectory) {
                                    try (FileOutputStream outStream = new FileOutputStream(targetFile)) {
                                        int readBytes;
                                        while ((readBytes = zipInput.read(buffer)) != -1)
                                            outStream.write(buffer, 0, readBytes);
                                    }
                                    if (zipEntryName.startsWith("bin/") || zipEntryName.startsWith("libexec") || zipEntryName.startsWith("lib/apt/methods")) {
                                        //noinspection OctalInteger
                                        Os.chmod(targetFile.getAbsolutePath(), 0700);
                                    }
                                }
                            }
                        }
                    }

                    if (symlinks.isEmpty())
                        throw new RuntimeException("No SYMLINKS.txt encountered");
                    for (Pair<String, String> symlink : symlinks) {
                        Os.symlink(symlink.first, symlink.second);
                    }

                    if (!STAGING_PREFIX_FILE.renameTo(PREFIX_FILE)) {
                        throw new RuntimeException("Unable to rename staging folder");
                    }
                    try {
                        if(textShowDialog!= null){
                            textShowDialog.dismiss();
                        }
                        myDialog.dismiss();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    activity.runOnUiThread(whenDone);

                }catch (Exception e){


                   UUtils.runOnUIThread(new Runnable() {
                       @Override
                       public void run() {

                           if (PREFIX_FILE.isDirectory()) {
                               if(textShowDialog!= null){
                                   textShowDialog.dismiss();
                               }
                               return;
                           }
                           if(textShowDialog != null){
                               textShowDialog.dismiss();
                           }
                           textShowDialog = new TextShowDialog(activity);
                           textShowDialog.edit_text.setText(UUtils.getString(R.string.你当前的操作失败了sdfsdf)+"\n" + e.toString());

                           textShowDialog.show();

                           textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                                                                       @Override
                                                                       public void onClick(View v) {
                                                                           if(myDialog!= null){
                                                                               myDialog.dismiss();
                                                                           }
                                                                           if(textShowDialog!= null){
                                                                               textShowDialog.dismiss();
                                                                           }

                                                                           dismiss();
                                                                           TermuxInstaller.setupIfNeeded(activity, whenDone);
                                                                       }
                                                                   }
                           );

                           final File PREFIX_FILE = new File(TermuxService.PREFIX_PATH);
                           if (PREFIX_FILE.isDirectory()) {
                               try {
                                   if(textShowDialog!= null){
                                       textShowDialog.dismiss();
                                   }
                                   myDialog.dismiss();
                               }catch (Exception e){
                                   e.printStackTrace();
                               }
                               return;
                           }

                       }
                   });

                } finally {
                    activity.runOnUiThread(() -> {
                        try {
                            //activity.startActivity(new Intent(activity,TestActivity.class));

                            dismiss();
                        } catch (RuntimeException e) {
                            // Activity already dismissed - ignore.
                        }
                    });
                }




            }
        }).start();





    }



    @Override
    int getContentView() {
        return R.layout.dialog_system_install;
    }




   /*
    private void test(){

        MyDialog myDialog = new MyDialog(activity);

        myDialog.setCancelable(false);

        Log.e("XINHAO_HAN", "setupIfNeeded: " + "弹出dialog");

        TextView dialog_title = myDialog.getDialog_title();

        dialog_title.setText("正在安装[群:714730084]...\n不挂vpn是比较绝望(慢)的...\n本程序已稳定,除非出现和官方一样的BUG");

        myDialog.getDialog_pro_prog().setMax(2066);

        myDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String STAGING_PREFIX_PATH = TermuxService.FILES_PATH + "/usr-staging";
                    final File STAGING_PREFIX_FILE = new File(STAGING_PREFIX_PATH);

                    if (STAGING_PREFIX_FILE.exists()) {
                        deleteFolder(STAGING_PREFIX_FILE);
                    }

                    final byte[] buffer = new byte[8096];
                    final List<Pair<String, String>> symlinks = new ArrayList<>(50);

                    final URL zipUrl = determineZipUrl();

                    InputStream inputStream = null;

                    Log.e("XINHAO_HAN", "高版本: " + determineTermuxArchName());

                    if(isOnLine){





                        switch (determineTermuxArchName()){


                            case "aarch64":

                            *//*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                    inputStream = activity.getAssets().open("bootstrap-aarch64-v25.zip");

                                    Log.e("XINHAO_HAN", "高版本: " + inputStream);
                                }
*//*
                                File file5 = new File(Environment.getExternalStorageDirectory(), "/xinhao/online_system/bootstrap-aarch64-v25.zip");

                                if(!file5.exists()){
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-aarch64-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    throw new RuntimeException("没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-aarch64-v25.zip");
                                }

                                try {
                                    inputStream = new FileInputStream(file5);
                                }catch (Exception e){

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-aarch64-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    throw e;


                                }
                                break;

                            case "arm":

                                File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/online_system/bootstrap-arm-v25.zip");

                                if(!file.exists()){

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-arm-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();


                                        }
                                    });
                                    isOnLine = false;
                                    throw new RuntimeException("没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-arm-v25.zip");
                                }



                                try {
                                    inputStream = new FileInputStream(file);
                                }catch (Exception e){

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-aarch64-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    throw e;


                                }

                                break;

                            case "x86_64":

                                File file1 = new File(Environment.getExternalStorageDirectory(), "/xinhao/online_system/bootstrap-x86_64-v25.zip");

                                if(!file1.exists()){
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-x86_64-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();


                                        }
                                    });
                                    isOnLine = false;
                                    throw new RuntimeException("没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-x86_64-v25.zip.zip");
                                }


                                // inputStream = new FileInputStream(file1);



                                try {
                                    inputStream = new FileInputStream(file1);
                                }catch (Exception e){

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-aarch64-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    throw e;


                                }
                                break;

                            case "i686":


                                File file2 = new File(Environment.getExternalStorageDirectory(), "/xinhao/online_system/bootstrap-i686-v25.zip");

                                if(!file2.exists()){

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-i686-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    isOnLine = false;
                                    throw new RuntimeException("没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-i686-v25.zip");
                                }


                                // inputStream = new FileInputStream(file2);


                                try {
                                    inputStream = new FileInputStream(file2);
                                }catch (Exception e){

                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-aarch64-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                    throw e;


                                }
                                break;

                        }

                    }else{

                        if ("aarch64".equals(determineTermuxArchName())) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                inputStream = activity.getAssets().open("bootstrap-aarch64-v25.zip");

                                Log.e("XINHAO_HAN", "高版本: " + inputStream);
                            }
                        }

                    }








                    Log.e("XINHAO_HAN", "外边: " + inputStream);
                    try (ZipInputStream zipInput = new ZipInputStream(inputStream == null ? zipUrl.openStream() : inputStream)) {

                        ZipEntry zipEntry;
                        int i = 0;
                        while ((zipEntry = zipInput.getNextEntry()) != null) {

                            i++;

                            Log.e("XINHAO_HAN_JS", "run: " + i);
                            int finalI = i;
                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    myDialog.getDialog_pro_prog().setProgress(finalI);
                                }
                            });

                            Log.e("XINHAO_HAN", "run: " + zipEntry.getSize());


                            ZipEntry finalZipEntry = zipEntry;
                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    myDialog.getDialog_pro().setText(finalZipEntry.getName());
                                }
                            });

                            if (zipEntry.getName().equals("SYMLINKS.txt")) {
                                BufferedReader symlinksReader = new BufferedReader(new InputStreamReader(zipInput));
                                String line;
                                while ((line = symlinksReader.readLine()) != null) {
                                    String[] parts = line.split("←");
                                    if (parts.length != 2)
                                        throw new RuntimeException("Malformed symlink line: " + line);
                                    String oldPath = parts[0];
                                    String newPath = STAGING_PREFIX_PATH + "/" + parts[1];
                                    symlinks.add(Pair.create(oldPath, newPath));

                                    ensureDirectoryExists(new File(newPath).getParentFile());
                                }
                            } else {
                                String zipEntryName = zipEntry.getName();
                                File targetFile = new File(STAGING_PREFIX_PATH, zipEntryName);
                                boolean isDirectory = zipEntry.isDirectory();

                                ensureDirectoryExists(isDirectory ? targetFile : targetFile.getParentFile());

                                if (!isDirectory) {
                                    try (FileOutputStream outStream = new FileOutputStream(targetFile)) {
                                        int readBytes;
                                        while ((readBytes = zipInput.read(buffer)) != -1)
                                            outStream.write(buffer, 0, readBytes);
                                    }
                                    if (zipEntryName.startsWith("bin/") || zipEntryName.startsWith("libexec") || zipEntryName.startsWith("lib/apt/methods")) {
                                        //noinspection OctalInteger
                                        Os.chmod(targetFile.getAbsolutePath(), 0700);
                                    }
                                }
                            }
                        }
                    }

                    if (symlinks.isEmpty())
                        throw new RuntimeException("No SYMLINKS.txt encountered");
                    for (Pair<String, String> symlink : symlinks) {
                        Os.symlink(symlink.first, symlink.second);
                    }

                    if (!STAGING_PREFIX_FILE.renameTo(PREFIX_FILE)) {
                        throw new RuntimeException("Unable to rename staging folder");
                    }

                    activity.runOnUiThread(whenDone);
                } catch (final Exception e) {
                    Log.e(EmulatorDebug.LOG_TAG, "Bootstrap error", e);
                    activity.runOnUiThread(() -> {
                        try {
                            new AlertDialog.Builder(activity).setTitle(R.string.bootstrap_error_title).setMessage(R.string.bootstrap_error_body)
                                .setNegativeButton(R.string.bootstrap_error_abort, (dialog, which) -> {
                                    //  dialog.dismiss();


                                    //链接：https://pan.baidu.com/s/1j7s2F_stI7cJ4A96rnV7jg
                                    //提取码：573u

                                    AlertDialog.Builder ab = new AlertDialog.Builder(activity);
                                    ab.setCancelable(false);
                                    ab.setTitle("提示!");

                                    //链接: https://pan.baidu.com/s/17l6_bJ3EQN41I7Axs0USvQ 提取码: bxht

                                    ab.setMessage("是否前往下载?\n\n提取码:573u");

                                    ab.setPositiveButton("前往", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            ab.create().dismiss();

                                            Intent intent = new Intent();
                                            intent.setData(Uri.parse("https://pan.baidu.com/s/1j7s2F_stI7cJ4A96rnV7jg "));//Url 就是你要打开的网址
                                            intent.setAction(Intent.ACTION_VIEW);
                                            activity.startActivity(intent); //启动浏览器

                                        }
                                    });

                                    ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ab.create().dismiss();
                                        }
                                    });

                                    ab.show();


                                    //  activity.finish();
                                }).setPositiveButton(R.string.bootstrap_error_try_again, (dialog, which) -> {
                                dialog.dismiss();
                                TermuxInstaller.setupIfNeeded(activity, whenDone);
                            }).setNeutralButton("开始离线安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    isOnLine = true;
                                    TermuxInstaller.setupIfNeeded(activity, whenDone);

                                }
                            })  .setCancelable(false).show();
                        } catch (WindowManager.BadTokenException e1) {
                            // Activity already dismissed - ignore.
                        }
                    });
                } finally {
                    activity.runOnUiThread(() -> {
                        try {
                            //activity.startActivity(new Intent(activity,TestActivity.class));
                            myDialog.dismiss();
                        } catch (RuntimeException e) {
                            // Activity already dismissed - ignore.
                        }
                    });
                }

            }
        }).start();



    }*/


    static void deleteFolder(File fileOrDirectory) throws IOException {
        if (fileOrDirectory.getCanonicalPath().equals(fileOrDirectory.getAbsolutePath()) && fileOrDirectory.isDirectory()) {
            File[] children = fileOrDirectory.listFiles();

            if (children != null) {
                for (File child : children) {
                    deleteFolder(child);
                }
            }
        }

        if (!fileOrDirectory.delete()) {
            throw new RuntimeException("Unable to delete " + (fileOrDirectory.isDirectory() ? "directory " : "file ") + fileOrDirectory.getAbsolutePath());
        }
    }


    private static URL determineZipUrl() throws MalformedURLException {
        String archName = determineTermuxArchName();
        String url = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            ? "https://termux.org/bootstrap-" + archName + ".zip"
            : "https://termux.net/bootstrap/bootstrap-" + archName + ".zip";

        return new URL(url);
    }

    public static String determineTermuxArchName() {
        // Note that we cannot use System.getProperty("os.arch") since that may give e.g. "aarch64"
        // while a 64-bit runtime may not be installed (like on the Samsung Galaxy S5 Neo).
        // Instead we search through the supported abi:s on the device, see:
        // http://developer.android.com/ndk/guides/abis.html
        // Note that we search for abi:s in preferred order (the ordering of the
        // Build.SUPPORTED_ABIS list) to avoid e.g. installing arm on an x86 system where arm
        // emulation is available.
        for (String androidArch : Build.SUPPORTED_ABIS) {
            switch (androidArch) {
                case "arm64-v8a":
                    return "aarch64";
                case "armeabi-v7a":
                    return "arm";
                case "x86_64":
                    return "x86_64";
                case "x86":
                    return "i686";
            }
        }
        throw new RuntimeException("Unable to determine arch from Build.SUPPORTED_ABIS =  " +
            Arrays.toString(Build.SUPPORTED_ABIS));
    }

    private static void ensureDirectoryExists(File directory) {
        if (!directory.isDirectory() && !directory.mkdirs()) {
            throw new RuntimeException("Unable to create directory: " + directory.getAbsolutePath());
        }
    }

}
