package main.java.com.termux.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.UserManager;
import android.system.Os;
import android.util.Log;
import android.util.Pair;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;
import com.termux.terminal.EmulatorDebug;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import main.java.com.termux.app.dialog.EditTextDialog;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.datat.TermuxData;
import main.java.com.termux.utils.UUtils;
import main.java.com.termux.view.MyDialog;

/**
 * Install the Termux bootstrap packages if necessary by following the below steps:
 * <p/>
 * (1) If $PREFIX already exist, assume that it is correct and be done. Note that this relies on that we do not create a
 * broken $PREFIX folder below.
 * <p/>
 * (2) A progress dialog is shown with "Installing..." message and a spinner.
 * <p/>
 * (3) A staging folder, $STAGING_PREFIX, is {@link #deleteFolder(File)} if left over from broken installation below.
 * <p/>
 * (4) The architecture is determined and an appropriate bootstrap zip url is determined in {@link #determineZipUrl()}.
 * <p/>
 * (5) The zip, containing entries relative to the $PREFIX, is is downloaded and extracted by a zip input stream
 * continuously encountering zip file entries:
 * <p/>
 * (5.1) If the zip entry encountered is SYMLINKS.txt, go through it and remember all symlinks to setup.
 * <p/>
 * (5.2) For every other zip entry, extract it into $STAGING_PREFIX and set execute permissions if necessary.
 */
public final class TermuxInstaller {

    //是否离线安装
    private static boolean isOnLine = false;
    /**
     * Performs setup if necessary.
     */
    public static void setupIfNeeded(final Activity activity, final Runnable whenDone) {
        // Termux can only be run as the primary user (device owner) since only that
        // account has the expected file system paths. Verify that:
        UserManager um = (UserManager) activity.getSystemService(Context.USER_SERVICE);
        boolean isPrimaryUser = um.getSerialNumberForUser(android.os.Process.myUserHandle()) == 0;
   /*     if (!isPrimaryUser) {
            new AlertDialog.Builder(activity).setTitle(R.string.bootstrap_error_title).setMessage(R.string.bootstrap_error_not_primary_user_message)
                .setOnDismissListener(dialog -> System.exit(0)).setPositiveButton(android.R.string.ok, null).show();
            return;
        }
*/
        final File PREFIX_FILE = new File(TermuxService.PREFIX_PATH);
        if (PREFIX_FILE.isDirectory()) {
            whenDone.run();
            return;
        }


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

                            /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                                    inputStream = activity.getAssets().open("bootstrap-aarch64-v25.zip");

                                    Log.e("XINHAO_HAN", "高版本: " + inputStream);
                                }
*/
                                File file5 = new File(Environment.getExternalStorageDirectory(), "/xinhao/online_system/bootstrap-aarch64-v25.zip");

                                if(!file5.exists()){
                                    activity.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(activity, "没有存储权限或无法在:[内部存储/xinhao/online_system/]找到bootstrap-aarch64-v25.zip,自动切回在线模式", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    isOnLine = false;
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





    /*    final ProgressDialog progress = ProgressDialog.show(activity, null, activity.getString(R.string.bootstrap_installer_body), true, false);
        new Thread() {
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
                    try (ZipInputStream zipInput = new ZipInputStream(zipUrl.openStream())) {
                        ZipEntry zipEntry;
                        while ((zipEntry = zipInput.getNextEntry()) != null) {

                            Log.e("XINHAO_HAN", "run: " + zipEntry.getSize());

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
                                    dialog.dismiss();
                                    activity.finish();
                                }).setPositiveButton(R.string.bootstrap_error_try_again, (dialog, which) -> {
                                dialog.dismiss();
                                TermuxInstaller.setupIfNeeded(activity, whenDone);
                            }).show();
                        } catch (WindowManager.BadTokenException e1) {
                            // Activity already dismissed - ignore.
                        }
                    });
                } finally {
                    activity.runOnUiThread(() -> {
                        try {
                            progress.dismiss();
                        } catch (RuntimeException e) {
                            // Activity already dismissed - ignore.
                        }
                    });
                }
            }
        }.start();*/
    }


    public static void setupIfNeeded3(String title, final Activity activity, final Runnable whenDone) {
        // Termux can only be run as the primary user (device owner) since only that
        // account has the expected file system paths. Verify that:
        UserManager um = (UserManager) activity.getSystemService(Context.USER_SERVICE);
        boolean isPrimaryUser = um.getSerialNumberForUser(android.os.Process.myUserHandle()) == 0;
     /*   if (!isPrimaryUser) {
            new AlertDialog.Builder(activity).setTitle(R.string.bootstrap_error_title).setMessage(R.string.bootstrap_error_not_primary_user_message)
                .setOnDismissListener(dialog -> System.exit(0)).setPositiveButton(android.R.string.ok, null).show();
            return;
        }*/

        final File PREFIX_FILE = new File(TermuxService.PREFIX_PATH);
      /*  if (PREFIX_FILE.isDirectory()) {
            whenDone.run();
            return;
        }*/


        MyDialog myDialog = new MyDialog(activity);

        myDialog.setCancelable(false);

        Log.e("XINHAO_HAN", "setupIfNeeded: " + "弹出dialog");

        TextView dialog_title = myDialog.getDialog_title();

        dialog_title.setText(title + " [群:714730084]...\n不挂vpn是比较绝望(慢)的...\n本程序已稳定,除非出现和官方一样的BUG");

        myDialog.getDialog_pro_prog().setMax(2066);

        myDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final String STAGING_PREFIX_PATH = TermuxService.FILES_PATH + "/usr";
                    //  final File STAGING_PREFIX_FILE = new File(STAGING_PREFIX_PATH);

                   /* if (STAGING_PREFIX_FILE.exists()) {
                        deleteFolder(STAGING_PREFIX_FILE);
                    }*/

                    final byte[] buffer = new byte[8096];
                    final List<Pair<String, String>> symlinks = new ArrayList<>(50);

                    final URL zipUrl = determineZipUrl();

                    InputStream inputStream = null;

                    Log.e("XINHAO_HAN", "高版本: " + determineTermuxArchName());
                    if ("aarch64".equals(determineTermuxArchName())) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

                            inputStream = activity.getAssets().open("bootstrap-aarch64_d1.zip");

                            Log.e("XINHAO_HAN", "高版本: " + inputStream);
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
                                    try {
                                        FileOutputStream outStream = new FileOutputStream(targetFile);
                                        int readBytes;
                                        while ((readBytes = zipInput.read(buffer)) != -1)
                                            outStream.write(buffer, 0, readBytes);

                                        if (zipEntryName.startsWith("bin/") || zipEntryName.startsWith("libexec") || zipEntryName.startsWith("lib/apt/methods")) {
                                            //noinspection OctalInteger
                                            Os.chmod(targetFile.getAbsolutePath(), 0700);
                                        }
                                    } catch (Exception e) {

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

                    //  if (!STAGING_PREFIX_FILE.renameTo(PREFIX_FILE)) {
                    throw new RuntimeException("Unable to rename staging folder");
                    // }

                   // activity.runOnUiThread(whenDone);
                } catch (final Exception e) {
                    Log.e(EmulatorDebug.LOG_TAG, "Bootstrap error", e);
                    activity.runOnUiThread(() -> {
                        try {
                            myDialog.dismiss();
                            new AlertDialog.Builder(activity).setTitle("修复成功!").setMessage("你必须重启APP来实现修复的作用\n\n注意！！！！如果出现\n\nProcess comleted (code xxx) -press Enter 错误\n\n,请再次大退(重启)Termux")
                                .setNegativeButton("重启", (dialog, which) -> {
                                    dialog.dismiss();
                                    activity.finish();
                                }).setPositiveButton("再次修复", (dialog, which) -> {
                                dialog.dismiss();
                                TermuxInstaller.setupIfNeeded(activity, whenDone);
                            }).show();
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
        }).

            start();





    /*    final ProgressDialog progress = ProgressDialog.show(activity, null, activity.getString(R.string.bootstrap_installer_body), true, false);
        new Thread() {
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
                    try (ZipInputStream zipInput = new ZipInputStream(zipUrl.openStream())) {
                        ZipEntry zipEntry;
                        while ((zipEntry = zipInput.getNextEntry()) != null) {

                            Log.e("XINHAO_HAN", "run: " + zipEntry.getSize());

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
                                    dialog.dismiss();
                                    activity.finish();
                                }).setPositiveButton(R.string.bootstrap_error_try_again, (dialog, which) -> {
                                dialog.dismiss();
                                TermuxInstaller.setupIfNeeded(activity, whenDone);
                            }).show();
                        } catch (WindowManager.BadTokenException e1) {
                            // Activity already dismissed - ignore.
                        }
                    });
                } finally {
                    activity.runOnUiThread(() -> {
                        try {
                            progress.dismiss();
                        } catch (RuntimeException e) {
                            // Activity already dismissed - ignore.
                        }
                    });
                }
            }
        }.start();*/
    }


    public static void setupIfNeededbl(final Activity activity, final Runnable whenDone) {
        // Termux can only be run as the primary user (device owner) since only that
        // account has the expected file system paths. Verify that:
        UserManager um = (UserManager) activity.getSystemService(Context.USER_SERVICE);
        boolean isPrimaryUser = um.getSerialNumberForUser(android.os.Process.myUserHandle()) == 0;
        if (!isPrimaryUser) {
            new AlertDialog.Builder(activity).setTitle(R.string.bootstrap_error_title).setMessage(R.string.bootstrap_error_not_primary_user_message)
                .setOnDismissListener(dialog -> System.exit(0)).setPositiveButton(android.R.string.ok, null).show();
            return;
        }

        final File PREFIX_FILE = new File(TermuxService.PREFIX_PATH);
      /*  if (PREFIX_FILE.isDirectory()) {
            whenDone.run();
            return;
        }*/
        final ProgressDialog progress = ProgressDialog.show(activity, null, "正在进行暴力修复,请耐心等待...", true, false);
        new Thread() {
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
                    try (ZipInputStream zipInput = new ZipInputStream(zipUrl.openStream())) {
                        ZipEntry zipEntry;
                        while ((zipEntry = zipInput.getNextEntry()) != null) {


                            Log.e("XINHAO_HAN", "run: " + zipEntry.getName());


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

                   /* if (!STAGING_PREFIX_FILE.renameTo(PREFIX_FILE)) {
                        throw new RuntimeException("Unable to rename staging folder");
                    }
*/
                    activity.runOnUiThread(whenDone);
                } catch (final Exception e) {
                    Log.e(EmulatorDebug.LOG_TAG, "Bootstrap error", e);
                    activity.runOnUiThread(() -> {
                        try {
                            new AlertDialog.Builder(activity).setTitle(R.string.bootstrap_error_title).setMessage(R.string.bootstrap_error_body)
                                .setNegativeButton(R.string.bootstrap_error_abort, (dialog, which) -> {
                                    dialog.dismiss();
                                    activity.finish();
                                }).setPositiveButton(R.string.bootstrap_error_try_again, (dialog, which) -> {
                                dialog.dismiss();
                                TermuxInstaller.setupIfNeeded(activity, whenDone);
                            }).show();
                        } catch (WindowManager.BadTokenException e1) {
                            // Activity already dismissed - ignore.
                        }
                    });
                } finally {
                    activity.runOnUiThread(() -> {
                        try {
                            progress.dismiss();
                        } catch (RuntimeException e) {
                            // Activity already dismissed - ignore.
                        }
                    });
                }
            }
        }.start();
    }

    private static void ensureDirectoryExists(File directory) {
        if (!directory.isDirectory() && !directory.mkdirs()) {
            throw new RuntimeException("Unable to create directory: " + directory.getAbsolutePath());
        }
    }

    /**
     * Get bootstrap zip url for this systems cpu architecture.
     */
    private static URL determineZipUrl() throws MalformedURLException {
        String archName = determineTermuxArchName();
        String url = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            ? "https://termux.org/bootstrap-" + archName + ".zip"
            : "https://termux.net/bootstrap/bootstrap-" + archName + ".zip";

        return new URL(url);
    }


    //访问

    private static String http() {

        try {


            URL url = new URL("http://127.0.0.1:2000/file");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(9000);

            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200) {

                InputStream inputStream = urlConnection.getInputStream();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                int l = 0;

                while ((l = inputStream.read()) != -1) {

                    byteArrayOutputStream.write(l);
                }

                inputStream.close();
                byteArrayOutputStream.flush();
                String s = byteArrayOutputStream.toString();
                Log.e("XINHAO_HAN_HTTP", "http: " + s);
                byteArrayOutputStream.close();
                return s;


            }

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("XINHAO_HAN_HTTP", "http: " + e.toString());
        }


        return "";

    }


    /**
     * Get bootstrap zip url for this systems cpu architecture.
     */
    private static String determineZipUrl1() throws MalformedURLException {
        String archName = determineTermuxArchName();
        String url = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
            ? "https://termux.org/bootstrap-" + archName + ".zip"
            : "https://termux.net/bootstrap/bootstrap-" + archName + ".zip";


        return url;
    }


    //192.168.1.4:1995/file?filename=apk

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

    /**
     * Delete a folder and all its content or throw. Don't follow symlinks.
     */
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

    static void editTextDialog(String path,Activity mActivity){

       // UUtils.showMsg("开始弹出编辑框:" + path);
        TermuxData.getInstall().fileUrl = path;
        Intent intent = new Intent(mActivity, EditTextActivity.class);
        mActivity.startActivity(intent);

       // EditTextDialog editTextDialog = new EditTextDialog(mActivity);

       // editTextDialog.setStringData(path);

        //editTextDialog.show();
    }

    static void setupStorageSymlinks(final Context context) {
        final String LOG_TAG = "termux-storage";
        new Thread() {
            public void run() {
                try {
                    File storageDir = new File(TermuxService.HOME_PATH, "storage");

                    if (storageDir.exists()) {
                        try {
                            deleteFolder(storageDir);
                        } catch (IOException e) {
                            Log.e(LOG_TAG, "Could not delete old $HOME/storage, " + e.getMessage());
                            return;
                        }
                    }

                    if (!storageDir.mkdirs()) {
                        Log.e(LOG_TAG, "Unable to mkdirs() for $HOME/storage");
                        return;
                    }

                    File sharedDir = Environment.getExternalStorageDirectory();
                    Os.symlink(sharedDir.getAbsolutePath(), new File(storageDir, "shared").getAbsolutePath());

                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    Os.symlink(downloadsDir.getAbsolutePath(), new File(storageDir, "downloads").getAbsolutePath());

                    File dcimDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    Os.symlink(dcimDir.getAbsolutePath(), new File(storageDir, "dcim").getAbsolutePath());

                    File picturesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    Os.symlink(picturesDir.getAbsolutePath(), new File(storageDir, "pictures").getAbsolutePath());

                    File musicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                    Os.symlink(musicDir.getAbsolutePath(), new File(storageDir, "music").getAbsolutePath());

                    File moviesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
                    Os.symlink(moviesDir.getAbsolutePath(), new File(storageDir, "movies").getAbsolutePath());

                    final File[] dirs = context.getExternalFilesDirs(null);
                    if (dirs != null && dirs.length > 1) {
                        for (int i = 1; i < dirs.length; i++) {
                            File dir = dirs[i];
                            if (dir == null) continue;
                            String symlinkName = "external-" + i;
                            Os.symlink(dir.getAbsolutePath(), new File(storageDir, symlinkName).getAbsolutePath());
                        }
                    }
                } catch (Exception e) {
                    Log.e(LOG_TAG, "Error setting up link", e);
                }
            }
        }.start();
    }

}
