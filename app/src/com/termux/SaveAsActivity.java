package com.termux;

import androidx.appcompat.app.AppCompatActivity;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.filemanage.filemanager.util.UIUtils;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveAsActivity extends AppCompatActivity {

    private TextView msg_file;
    private ImageView start_fz;
    private ProgressBar pro;
    private TextView msg_pro;
    private long temp;
    private boolean isRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_as);
        Intent receivedIntent = getIntent();
        isRun = true;

        msg_file = findViewById(R.id.msg_file);

        start_fz = findViewById(R.id.start_fz);

        pro = findViewById(R.id.pro);

        msg_pro = findViewById(R.id.msg_pro);

        if (receivedIntent != null) {
            Uri uri = receivedIntent.getData();

            String encodedPath = uri.getEncodedPath();

            File file = new File(encodedPath);

            msg_file.setText("文件信息:\n\n文件名:" + file.getName() + "\n\n文件大小:" + (((float) (file.length()) / 1024 / 1024  )) + "MB\n\n路径:" + file.getAbsolutePath() + "\n\n即将移动到:/home/\n\n复制过程中请勿离开此页面!!!!\n\n复制过程中请勿离开此页面!!!!\n\n");


            Log.e("XINHAO_HHHH", "onCreate: " + encodedPath);

            start_fz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    start_fz.setVisibility(View.GONE);
                    pro.setVisibility(View.VISIBLE);

                    msg_pro.setText("开始复制...");

                    startCopy(file);
                }
            });
            whileText(file.length());
        } else {
            Toast.makeText(this, "打开文件出错,请再试一次!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;
    }

    //刷新 视图专用
    private void whileText(long size){


        new Thread(new Runnable() {
            @Override
            public void run() {


                while (isRun){


                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            msg_pro.setText("进度:" + (((float) (temp) / 1024 / 1024  )) + "MB/"+ (((float) (size) / 1024 / 1024 )) + "MB");

                            int l = (int) (((float)temp / (float) size) * 100);

                            Log.e("XINHAO_SIZE", "run: " + l );
                            Log.e("XINHAO_SIZE", "temp: " + temp +"--size:" + size );
                            pro.setMax(100);
                            pro.setProgress(l);


                        }
                    });


                }


            }
        }).start();


    }


    private void startCopy(File file) {


        File file1 = new File("/data/data/com.termux/files/home/" + file.getName());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file1);
                    FileInputStream fileInputStream = new FileInputStream(file);


                    int len = 0;

                    temp = 0;

                    byte[] b = new byte[4096];

                    if (file.length() > 3000) {

                        while ((len = fileInputStream.read(b)) != -1) {

                            temp += len;

                            fileOutputStream.write(b, 0, len);

                        }

                        fileOutputStream.flush();
                        fileOutputStream.close();
                        isRun = false;

                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(SaveAsActivity.this, "复制完成!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });

                    } else {

                        while ((len = fileInputStream.read()) != -1) {

                            fileOutputStream.write(len);

                        }

                        fileOutputStream.flush();
                        fileOutputStream.close();
                        isRun = false;

                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                //Toast.makeText(SaveAsActivity.this, "复制完成!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }


                } catch (Exception e) {
                    //Toast.makeText(SaveAsActivity.this, "你没有打开SD卡权限!", Toast.LENGTH_SHORT).show();
                    isRun = false;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            msg_pro.setText("你没打开sd卡权限!");
                            //finish();
                        }
                    });
                }


            }
        }).start();


    }


}
