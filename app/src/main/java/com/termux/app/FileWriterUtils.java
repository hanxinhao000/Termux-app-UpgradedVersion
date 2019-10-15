package main.java.com.termux.app;

import android.content.Context;
import android.os.Environment;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileWriterUtils {


    public static File mFile = new File("/data/data/com.termux/files");

    public static File mFileEx = new File("/data/data/com.termux/files/usr/bin/pkg");

    public static File mSdFile = new File(Environment.getExternalStorageDirectory(), "/xinhao/mysql");

    public static File MSD_DATA_FILE_7Z = new File(mSdFile, "/data.7z");
    // public static File MSD_DATA_FILE_ZIP = new File(mSdFile, "/data.zip");


    public static String mb = "53MB";

    public static void start(Context mContext, ZipUtils.ZipNameListener zipNameListener) {


        if (mFileEx.exists()) {
            zipNameListener.zip("文件已存在!", 0, 0);
            zipNameListener.complete();
            return;
        }
        ///data/data/com.termux/files/usr/share/nginx/html/showdoc-master

        new Thread(new Runnable() {
            @Override
            public void run() {


                if (!mSdFile.exists()) {
                    mSdFile.mkdirs();
                }

                zipNameListener.zip("正在准备文件...(" + mb + ")", 0, 0);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //------------


                //------------

                try {
                    InputStream open = mContext.getAssets().open("data.7z");

                    FileOutputStream fileOutputStream = new FileOutputStream(MSD_DATA_FILE_7Z);


                    int i = 0;

                    byte[] b = new byte[1024];

                    long temp = 0;

                    while ((i = open.read(b)) != -1) {

                        fileOutputStream.write(b, 0, i);
                        temp += 1024;
                        zipNameListener.zip("正在创建缓存文件...(" + (temp / 1024 / 1024) + "MB /" + mb + " )", 0, 0);

                    }

                    open.close();
                    fileOutputStream.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                zipNameListener.zip("缓存文件创建完毕!开始解压!", 0, 0);

                //  ZipUtils.unZip(MSD_DATA_FILE_7Z, mFile.getAbsolutePath(), zipNameListener);\

                //解压7Z文件
                ZipUtils.un7Z(MSD_DATA_FILE_7Z, mFile.getAbsolutePath(), zipNameListener);


            }
        }).start();


    }


}
