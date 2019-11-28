package main.java.com.termux.core;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import main.java.com.termux.app.TestActivity;
import main.java.com.termux.app.ZipUtils;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.view.MyDialog;

public class CoreGuiInstall {


    private static File mFileRootfs = new File("/data/data/com.termux/files/support/rootfs.tar.gz");
    private static File mFileSupport = new File("/data/data/com.termux/files/support/");
    private static File mFileAssets = new File("/data/data/com.termux/files/support/assets.tar.gz");
    private static File mFileHome = new File("/data/data/com.termux/files/");

    private static String NAME;


    public static void install(String sdPath, String name, CoreGuiBean coreGuiBean, CoreGuiInstallListener coreGuiInstallListener) {

        NAME = name;

        new File(mFileHome, "/" + name).mkdirs();
        ///data/data/com.termux/files/winlog_1/support/.proot_version

        new File(mFileHome, "/" + name + "/support/.proot_version").mkdirs();

        coreGuiInstallListener.position("开始复制文件", 0, 0);


        if (!mFileSupport.exists()) {
            mFileSupport.mkdirs();
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        File file = new File(mFileHome, "/" + name + "/support/rootfs.tar.gz");

        copyFile(sdPath, file.getAbsolutePath(), coreGuiInstallListener);

        coreGuiInstallListener.position("开始解压工具包", 0, 0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        unzipTool(coreGuiInstallListener);

        coreGuiInstallListener.position("开始安装...", 0, 0);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        installLinux(coreGuiInstallListener, coreGuiBean);


    }

    //安装
    private static void installLinux(CoreGuiInstallListener coreGuiInstallListener, CoreGuiBean coreGuiBean) {


        try {
            Runtime.getRuntime().exec("chmod 777 /data/data/com.termux/files/support/rootfs.tar.gz");

            coreGuiInstallListener.position("chmod 777 /data/data/com.termux/files/support/rootfs.tar.gz", 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("/data/data/com.termux/files/support/busybox");
        arrayList.add("sh");
        arrayList.add("/data/data/com.termux/files/support/execInProot.sh");
        arrayList.add("/data/data/com.termux/files/support/extractFilesystem.sh");

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("INITIAL_USERNAME", coreGuiBean.username);
        hashMap.put("INITIAL_PASSWORD", coreGuiBean.password);
        hashMap.put("INITIAL_VNC_PASSWORD", coreGuiBean.vncPassword);


        hashMap.put("PROOT_DEBUG_LEVEL", "-1");
        hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
        hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/" + NAME);
        hashMap.put("OS_VERSION", "4.4.153-perf+");
        hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
        hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/" + NAME + "/temp:/temp/internal");

        hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");


        ProcessBuilder processBuilder = new ProcessBuilder(arrayList);


        processBuilder.environment().putAll(hashMap);

        processBuilder.redirectErrorStream(true);

        coreGuiInstallListener.position("即将开始安装,安装过程大概需要40分钟左右,请耐心等待...", 0, 0);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            Process start = processBuilder.start();

            InputStream inputStream = start.getInputStream();

            int l = 0;

            byte[] b = new byte[1024];

            while ((l = inputStream.read(b)) != -1) {

                String s = new String(b, "UTF-8");

                Log.e("XINHAO_HANCMMOD", "startInstallLinux: " + s);

                coreGuiInstallListener.position(s, 0, 0);

            }

            inputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
            coreGuiInstallListener.error("错误!:" + e.toString());
        }


        coreGuiInstallListener.position("安装完成!请点击启动按钮", 0, 0);


        writerFile("startSSHServer.sh", new File(mFileSupport, "/startSSHServer.sh"));

        try {
            Runtime.getRuntime().exec("chmod 777 " + new File(mFileSupport, "/startSSHServer.sh").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        writerFile("startVNCServer.sh", new File(mFileSupport, "/startVNCServer.sh"));
        writerFile("startVNCServerStep2.sh", new File(mFileSupport, "/startVNCServerStep2.sh"));
        try {
            Runtime.getRuntime().exec("chmod 777 " + new File(mFileSupport, "/startVNCServer.sh").getAbsolutePath());
            Runtime.getRuntime().exec("chmod 777 " + new File(mFileSupport, "/startVNCServerStep2.sh").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        coreGuiInstallListener.complete();

    }

    //开始解压工具包

    private static void unzipTool(CoreGuiInstallListener coreGuiInstallListener) {

        coreGuiInstallListener.position("工具包正在输出...", 0, 0);
        writerFile("sh.zip", new File(mFileSupport, "/sh.xh.tar"));
        coreGuiInstallListener.position("工具包输出完成!", 0, 0);


        ZipUtils.unZip(new File(mFileSupport, "/sh.xh.tar"), mFileSupport.getAbsolutePath(), new ZipUtils.ZipNameListener() {
            @Override
            public void zip(String FileName, int size, int position) {


                coreGuiInstallListener.position(FileName, 0, 0);

            }

            @Override
            public void complete() {
                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        coreGuiInstallListener.position("解压完成!", 0, 0);

                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

            @Override
            public void progress(long size, long position) {

            }
        });


    }


    //复制文件
    private static void copyFile(String inPath, String outPath, CoreGuiInstallListener coreGuiInstallListener) {


        File inPathFile = new File(inPath);
        File outPathFile = new File(outPath);

        int i = 0;
        byte[] b = new byte[8192];

        long totalSpace = inPathFile.getTotalSpace();

        coreGuiInstallListener.position("文件总大小:" + totalSpace, 0, 0);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        int temp = 0;

        try {
            FileInputStream fileInputStream = new FileInputStream(inPathFile);
            FileOutputStream fileOutputStream = new FileOutputStream(outPathFile);


            while ((i = fileInputStream.read(b)) != -1) {

                fileOutputStream.write(b, 0, i);
                temp += i;
                coreGuiInstallListener.position("复制文件:" + temp + "/" + totalSpace, 0, 0);
            }

            fileOutputStream.flush();
            fileInputStream.close();
            fileOutputStream.close();

            coreGuiInstallListener.position("复制完成!", 0, 0);


        } catch (Exception e) {
            e.printStackTrace();
            coreGuiInstallListener.position("出现错误!" + e.toString(), 0, 0);
            coreGuiInstallListener.error(e.toString());
        }


    }




    //       writerFile("startXSDLServer.sh", new File(mFileSupport, "/startXSDLServer.sh"));
    //                writerFile("startXSDLServerStep2.sh", new File(mFileSupport, "/startXSDLServerStep2.sh"));



    public static void startSystemXSDL(String name, CoreGuiBean coreGuiBean) {

        writerFile("startXSDLServer.sh", new File(mFileSupport, "/startXSDLServer.sh"));
        writerFile("startXSDLServerStep2.sh", new File(mFileSupport, "/startXSDLServerStep2.sh"));


        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("/data/data/com.termux/files/support/busybox");
        arrayList.add("sh");
        arrayList.add("/data/data/com.termux/files/support/execInProot.sh");
        arrayList.add("/data/data/com.termux/files/support/startXSDLServerStep2.sh");
        arrayList.add("/data/data/com.termux/files/support/isServerInProcTree.sh");
        arrayList.add("17620");

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("INITIAL_USERNAME", coreGuiBean.username);
        hashMap.put("INITIAL_PASSWORD", coreGuiBean.password);
        hashMap.put("INITIAL_VNC_PASSWORD", coreGuiBean.vncPassword);

        hashMap.put("PROOT_DEBUG_LEVEL", "-1");
        hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
        hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/" + name);
        hashMap.put("OS_VERSION", "4.4.153-perf+");
        hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
        hashMap.put("HOME", "/data/data/com.termux/files/" + name + "/home/" + coreGuiBean.username);
        hashMap.put("USER", coreGuiBean.username);
        hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/" + name + "/temp:/temp/internal:/home");
        hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");


        ProcessBuilder processBuilder = new ProcessBuilder(arrayList);
        Log.e("XINHAO_HAN", "run: " + "完成");

        processBuilder.environment().putAll(hashMap);

        processBuilder.redirectErrorStream(true);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Process start = processBuilder.start();

                    InputStream inputStream = start.getInputStream();

                    int l = 0;

                    byte[] b = new byte[1024];

                    logOut("总共字节数:" + Arrays.toString(b));

                    while ((l = inputStream.read(b)) != -1) {

                        String s = new String(b, "GBK");

                        Log.e("XINHAO_HANCMMOD", "startInstallLinux: " + s);

                        logOut("XINHAO_HANCMMOD" + "startInstallLinux: " + s);
                    }

                    inputStream.close();


                } catch (IOException e) {
                    logOut("错误:" + e.toString());
                    e.printStackTrace();

                }

            }
        }).start();


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }




    public static void startSystemVnc(String name, CoreGuiBean coreGuiBean) {

        writerFile("startVNCServer.sh", new File(mFileSupport, "/startVNCServer.sh"));
        writerFile("startVNCServerStep2.sh", new File(mFileSupport, "/startVNCServerStep2.sh"));


        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("/data/data/com.termux/files/support/busybox");
        arrayList.add("sh");
        arrayList.add("/data/data/com.termux/files/support/execInProot.sh");
        arrayList.add("/data/data/com.termux/files/support/startVNCServerStep2.sh");
        arrayList.add("/data/data/com.termux/files/support/isServerInProcTree.sh");
        arrayList.add("17620");

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("INITIAL_USERNAME", coreGuiBean.username);
        hashMap.put("INITIAL_PASSWORD", coreGuiBean.password);
        hashMap.put("INITIAL_VNC_PASSWORD", coreGuiBean.vncPassword);

        hashMap.put("PROOT_DEBUG_LEVEL", "-1");
        hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
        hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/" + name);
        hashMap.put("OS_VERSION", "4.4.153-perf+");
        hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
        hashMap.put("HOME", "/data/data/com.termux/files/" + name + "/home/" + coreGuiBean.username);
        hashMap.put("USER", coreGuiBean.username);
        hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/" + name + "/temp:/temp/internal:/home");
        hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");


        ProcessBuilder processBuilder = new ProcessBuilder(arrayList);
        Log.e("XINHAO_HAN", "run: " + "完成");

        processBuilder.environment().putAll(hashMap);

        processBuilder.redirectErrorStream(true);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Process start = processBuilder.start();

                    InputStream inputStream = start.getInputStream();

                    int l = 0;

                    byte[] b = new byte[1024];

                    logOut("总共字节数:" + Arrays.toString(b));

                    while ((l = inputStream.read(b)) != -1) {

                        String s = new String(b, "GBK");

                        Log.e("XINHAO_HANCMMOD", "startInstallLinux: " + s);

                        logOut("XINHAO_HANCMMOD" + "startInstallLinux: " + s);
                    }

                    inputStream.close();


                } catch (IOException e) {
                    logOut("错误:" + e.toString());
                    e.printStackTrace();

                }

            }
        }).start();


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void startSystemSsh(String name, CoreGuiBean coreGuiBean) {

        File file = new File(mFileHome, name);

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("/data/data/com.termux/files/support/busybox");
        arrayList.add("sh");
        arrayList.add("/data/data/com.termux/files/support/execInProot.sh");
        arrayList.add("/data/data/com.termux/files/support/startSSHServer.sh");
        arrayList.add("/data/data/com.termux/files/support/isServerInProcTree.sh");
        arrayList.add("23101");

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("INITIAL_USERNAME", coreGuiBean.username);
        hashMap.put("INITIAL_PASSWORD", coreGuiBean.password);
        hashMap.put("INITIAL_VNC_PASSWORD", coreGuiBean.vncPassword);

        hashMap.put("PROOT_DEBUG_LEVEL", "-1");
        hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
        hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/" + name);
        hashMap.put("OS_VERSION", "4.4.153-perf+");
        hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
        hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/" + name + "/temp:/temp/internal");
        hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");


        ProcessBuilder processBuilder = new ProcessBuilder(arrayList);


        writerFile("startSSHServer.sh", new File(mFileSupport, "/startSSHServer.sh"));
        try {
            Runtime.getRuntime().exec("chmod 777 " + mFileSupport.getAbsolutePath() + "/startSSHServer.sh");
        } catch (IOException e) {
            e.printStackTrace();
        }

        processBuilder.environment().putAll(hashMap);

        processBuilder.redirectErrorStream(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process start = processBuilder.start();

                    InputStream inputStream = start.getInputStream();

                    int l = 0;

                    byte[] b = new byte[1024];

                    logOut("总共字节数:" + Arrays.toString(b));

                    while ((l = inputStream.read(b)) != -1) {

                        String s = new String(b, "GBK");

                        Log.e("XINHAO_HANCMMOD", "startInstallLinux: " + s);

                        logOut("XINHAO_HANCMMOD" + "startInstallLinux: " + s);
                    }

                    inputStream.close();


                } catch (IOException e) {
                    logOut("错误:" + e.toString());
                    e.printStackTrace();


                }
            }
        }).start();




        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //写出文件
    private static void writerFile(String name, File mFile) {

        try {
            Runtime.getRuntime().exec("chmod 777 " + mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream open = TermuxApplication.mContext.getAssets().open(name);

            int len = 0;

            byte[] b = new byte[1024];
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


    //输出日志

    private static void logOut(String s) {


        Log.e("XINHAO_HAN_LL", "logOut: " + s);


    }

}
