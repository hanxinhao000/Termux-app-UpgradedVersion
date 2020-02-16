package main.java.com.termux.http;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import androidx.core.content.FileProvider;
import main.java.com.termux.application.TermuxApplication;

public class CheckUpDateCodeUtils {


    private static File mFile = new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/");

    public static void updateCode(HttpCode httpCode) {


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL(HttpURL.UPDATE_URL);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestMethod("GET");
                    urlConnection.setReadTimeout(3000);
                    if (urlConnection.getResponseCode() == 200) {

                        InputStream inputStream = urlConnection.getInputStream();

                        int leng = 0;
                        byte[] b = new byte[1024];

                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        while ((leng = inputStream.read(b)) != -1) {

                            byteArrayOutputStream.write(b, 0, leng);


                        }

                        byteArrayOutputStream.flush();
                        String s = byteArrayOutputStream.toString();
                        byteArrayOutputStream.close();
                        httpCode.onRes(s);
                        Log.e("XINHAO_HAN", "run: " + s);

                    } else {

                        /*TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(TermuxApplication.mContext, "服务器请求异常," + urlConnection.getResponseCode(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    httpCode.onError(e.toString());
                                    TermuxApplication.mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            Toast.makeText(TermuxApplication.mContext, "服务器请求异常," + e.toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    e.printStackTrace();
                                }
                            }
                        });*/

                    }

                } catch (MalformedURLException e) {
                    httpCode.onError(e.toString());
                    e.printStackTrace();
                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                           // Toast.makeText(TermuxApplication.mContext, "服务器请求异常," + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                } catch (IOException e) {
                    httpCode.onError(e.toString());
                    e.printStackTrace();
                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            //Toast.makeText(TermuxApplication.mContext, "服务器请求异常," + e.toString(), Toast.LENGTH_SHORT).show();

                        }
                    });
                }


            }
        }).start();


    }

    public static interface HttpCode {


        void onRes(String msg);

        void onError(String errorMsg);

    }

    //立即升级
    public static void update(String urlStr, Pro pro) {

        if (!mFile.exists()) {
            mFile.mkdirs();
        }

        File file = new File(mFile, "termux.apk");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(TermuxApplication.mContext, "开始下载", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setReadTimeout(2000);
                    urlConnection.setRequestMethod("GET");
                    int contentLength = urlConnection.getContentLength();
                    pro.size(contentLength);

                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == 200) {
                        InputStream inputStream = urlConnection.getInputStream();


                        FileOutputStream fileOutputStream = new FileOutputStream(file);

                        int len = 0;

                        int postion = 0;

                        byte[] b = new byte[1024];

                        while ((len = inputStream.read(b)) != -1) {

                            postion += len;
                            fileOutputStream.write(b, 0, len);
                            pro.thisSize(postion, contentLength);
                        }

                        fileOutputStream.flush();
                        fileOutputStream.close();
                        pro.com();

                    } else {

                        pro.error("请求地址返回的code码不正确!");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    pro.error(e.toString());

                    Log.e("XINHAO_HAN", "run: " + e.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    pro.error(e.toString());
                    Log.e("XINHAO_HAN", "run: " + e.toString());
                }


            }
        }).start();


    }



    public static boolean installApk(Context con, String filePath) {

        Toast.makeText(con, "开始安装........:"  + filePath, Toast.LENGTH_SHORT).show();
        try {
            if (TextUtils.isEmpty(filePath)) {
                Toast.makeText(con, "目录为空？", Toast.LENGTH_SHORT).show();
                return false;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                Toast.makeText(con, "没有内存卡权限？" + filePath, Toast.LENGTH_SHORT).show();
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//增加读写权限
            }
            intent.setDataAndType(getPathUri(con, filePath), "application/vnd.android.package-archive");
            con.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(con, "安装失败，请重新下载", Toast.LENGTH_LONG).show();
            return false;
        } catch (Error error) {
            error.printStackTrace();
            Toast.makeText(con, "安装失败，请重新下载", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public static Uri getPathUri(Context context, String filePath) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String packageName = context.getPackageName();
            uri = FileProvider.getUriForFile(context, packageName + ".fileProvider", new File(filePath));
        } else {
            uri = Uri.fromFile(new File(filePath));
        }
        return uri;
    }


    public static interface Pro {

        void size(int size);

        void thisSize(int postion, int size);

        void error(String msg);

        void com();

    }
}
