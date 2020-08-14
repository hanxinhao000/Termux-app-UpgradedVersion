package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;

import java.io.File;
import java.io.IOException;

import main.java.com.termux.app.BackupActivity;
import main.java.com.termux.app.RestoreActivity;
import main.java.com.termux.app.TestActivity;
import main.java.com.termux.datat.TermuxData;
import main.java.com.termux.utils.UUtils;
import main.java.com.termux.view.XHWaveView;

public class ListDataActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private CardView web_data;
    private TextView down_text;
    private ProgressBar pro;


    private CardView web_data_1;
    private TextView down_text_1;
    private ProgressBar pro_1;

    private CardView web_data_2;
    private TextView down_text_2;
    private ProgressBar pro_2;

    private CardView web_data_3;
    private TextView down_text_3;
    private ProgressBar pro_3;

    private CardView web_data_4;
    private TextView down_text_4;
    private ProgressBar pro_4;


    private CardView web_data_5;
    private TextView down_text_5;
    private ProgressBar pro_5;

    private CardView web_data_6;
    private TextView down_text_6;
    private ProgressBar pro_6;

    private CardView web_data_7;
    private TextView down_text_7;
    private ProgressBar pro_7;

    private CardView web_data_8;
    private TextView down_text_8;
    private ProgressBar pro_8;


    private TextView down_text_size;
    private TextView down_text_size_1;
    private TextView down_text_size_2;
    private TextView down_text_size_3;
    private TextView down_text_size_4;
    private TextView down_text_size_5;
    private TextView down_text_size_6;
    private TextView down_text_size_7;
    private TextView down_text_size_8;


    private boolean isRun = true;

    private File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/termux_maoxian.zip");
    private File file1 = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/ubuntu-xinhao.iso");
    private File file2 = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/debian_linux.tar.gz");//
    private File file3 = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/debian_linux_2.tar.gz");//
    private File file4 = new File(Environment.getExternalStorageDirectory(), "/xinhao/data/ubuntu_gui.tar.gz");

    private File file5 = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/C_C++.zip");
    private File file6 = new File(Environment.getExternalStorageDirectory(), "/xinhao/data/kail_termux.tar.gz");
    private File file7 = new File(Environment.getExternalStorageDirectory(), "/xinhao/data/termux_qemu.tar.gz");
    private File file8 = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/shengtou_msf.zip");

    private File test = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/xinhao.test");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data);

        down_text_size = findViewById(R.id.down_text_size);
        down_text_size_1 = findViewById(R.id.down_text_size_1);
        down_text_size_2 = findViewById(R.id.down_text_size_2);
        down_text_size_3 = findViewById(R.id.down_text_size_3);
        down_text_size_4 = findViewById(R.id.down_text_size_4);
        down_text_size_5 = findViewById(R.id.down_text_size_5);
        down_text_size_6 = findViewById(R.id.down_text_size_6);
        down_text_size_7 = findViewById(R.id.down_text_size_7);
        down_text_size_8 = findViewById(R.id.down_text_size_8);

        down_text_size.setVisibility(View.GONE);
        down_text_size_1.setVisibility(View.GONE);
        down_text_size_2.setVisibility(View.GONE);
        down_text_size_3.setVisibility(View.GONE);
        down_text_size_4.setVisibility(View.GONE);
        down_text_size_5.setVisibility(View.GONE);
        down_text_size_6.setVisibility(View.GONE);
        down_text_size_7.setVisibility(View.GONE);
        down_text_size_8.setVisibility(View.GONE);


        if (!test.exists()) {
            try {
                boolean newFile = test.createNewFile();

                if (!newFile) {

                    Toast.makeText(this, UUtils.getString(R.string.你没有SD卡权限88889), Toast.LENGTH_SHORT).show();
                    finish();
                }

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, UUtils.getString(R.string.你没有SD卡权限88889), Toast.LENGTH_SHORT).show();
                finish();
            }
        }


        web_data = findViewById(R.id.web_data);
        web_data.setOnClickListener(this);
        web_data.setOnLongClickListener(this);
        down_text = findViewById(R.id.down_text);
        pro = findViewById(R.id.pro);

        web_data_1 = findViewById(R.id.web_data_1);
        web_data_1.setOnClickListener(this);
        web_data_1.setOnLongClickListener(this);
        down_text_1 = findViewById(R.id.down_text_1);
        pro_1 = findViewById(R.id.pro_1);

        web_data_2 = findViewById(R.id.web_data_2);
        web_data_2.setOnClickListener(this);
        web_data_2.setOnLongClickListener(this);
        down_text_2 = findViewById(R.id.down_text_2);
        pro_2 = findViewById(R.id.pro_2);

        web_data_3 = findViewById(R.id.web_data_3);
        web_data_3.setOnClickListener(this);
        web_data_3.setOnLongClickListener(this);
        down_text_3 = findViewById(R.id.down_text_3);
        pro_3 = findViewById(R.id.pro_3);

        web_data_4 = findViewById(R.id.web_data_4);
        web_data_4.setOnClickListener(this);
        web_data_4.setOnLongClickListener(this);
        down_text_4 = findViewById(R.id.down_text_4);
        pro_4 = findViewById(R.id.pro_4);

        web_data_5 = findViewById(R.id.web_data_5);
        web_data_5.setOnClickListener(this);
        web_data_5.setOnLongClickListener(this);
        down_text_5 = findViewById(R.id.down_text_5);
        pro_5 = findViewById(R.id.pro_5);


        web_data_6 = findViewById(R.id.web_data_6);
        web_data_6.setOnClickListener(this);
        web_data_6.setOnLongClickListener(this);
        down_text_6 = findViewById(R.id.down_text_6);
        pro_6 = findViewById(R.id.pro_6);

        web_data_7 = findViewById(R.id.web_data_7);
        web_data_7.setOnClickListener(this);
        web_data_7.setOnLongClickListener(this);
        down_text_7 = findViewById(R.id.down_text_7);
        pro_7 = findViewById(R.id.pro_7);

        web_data_8 = findViewById(R.id.web_data_8);
        web_data_8.setOnClickListener(this);
        web_data_8.setOnLongClickListener(this);
        down_text_8 = findViewById(R.id.down_text_8);
        pro_8 = findViewById(R.id.pro_8);

        if (file.exists()) {

            down_text.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text.setText(UUtils.getString(R.string.点击下载));
        }

        if (file1.exists()) {

            down_text_1.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text_1.setText(UUtils.getString(R.string.点击下载));
        }

        if (file2.exists()) {

            down_text_2.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text_2.setText(UUtils.getString(R.string.点击下载));
        }

        if (file3.exists()) {

            down_text_3.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text_3.setText(UUtils.getString(R.string.点击下载));
        }

        if (file4.exists()) {

            down_text_4.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text_4.setText(UUtils.getString(R.string.点击下载));
        }


        if (file5.exists()) {

            down_text_5.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text_5.setText(UUtils.getString(R.string.点击下载));
        }


        if (file6.exists()) {

            down_text_6.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text_6.setText(UUtils.getString(R.string.点击下载));
        }


        if (file7.exists()) {

            down_text_7.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text_7.setText(UUtils.getString(R.string.点击下载));
        }


        if (file8.exists()) {

            down_text_8.setText(UUtils.getString(R.string.点击安装));

        } else {
            down_text_8.setText(UUtils.getString(R.string.点击下载));
        }

        startDown();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.web_data:
                // Toast.makeText(this, "开始下载", Toast.LENGTH_SHORT).show();

                if (DownLoadService.isDown) {
                    Log.e("XINHAO_HAN", "onClick: " + "由于服务器带宽紧张,一次只能下载一个!");
                    Toast.makeText(this, UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (file.exists()) {

                    TermuxData.getInstall().isB_R = 1;

                    TermuxData.getInstall().mFile = file;

                    startActivity(new Intent(ListDataActivity.this, BackupActivity.class));

                } else {
                    down_text.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 0;
                    startService(new Intent(this, DownLoadService.class));
                }


                //startDown();
                break;
            case R.id.web_data_1:
                if (DownLoadService.isDown) {
                    Toast.makeText(this,  UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (file1.exists()) {
                    startActivity(new Intent(ListDataActivity.this, TestActivity.class));

                } else {
                    down_text_1.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 1;
                    startService(new Intent(this, DownLoadService.class));
                }

                break;

            case R.id.web_data_2:
                if (DownLoadService.isDown) {
                    Toast.makeText(this, UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }


                if (file2.exists()) {
                    startActivity(new Intent(ListDataActivity.this, UbuntuListActivity.class));

                } else {
                    down_text_2.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 2;
                    startService(new Intent(this, DownLoadService.class));
                }


                break;
            case R.id.web_data_3:
                if (DownLoadService.isDown) {
                    Toast.makeText(this, UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (file3.exists()) {
                    startActivity(new Intent(ListDataActivity.this, UbuntuListActivity.class));

                } else {
                    down_text_3.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 3;
                    startService(new Intent(this, DownLoadService.class));
                }


                break;
            case R.id.web_data_4:
                if (DownLoadService.isDown) {
                    Toast.makeText(this, UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (file4.exists()) {
                    startActivity(new Intent(ListDataActivity.this, BackNewActivity.class));

                } else {
                    down_text_4.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 4;
                    startService(new Intent(this, DownLoadService.class));
                }

                break;

            //--------------------------------------------------------------------------


            case R.id.web_data_5:
                if (DownLoadService.isDown) {
                    Toast.makeText(this, UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (file5.exists()) {

                    TermuxData.getInstall().isB_R = 1;

                    TermuxData.getInstall().mFile = file5;

                    startActivity(new Intent(ListDataActivity.this, BackupActivity.class));

                } else {
                    down_text_5.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 5;
                    startService(new Intent(this, DownLoadService.class));
                }

                break;

            case R.id.web_data_6:
                if (DownLoadService.isDown) {
                    Toast.makeText(this, UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (file6.exists()) {
                    startActivity(new Intent(ListDataActivity.this, BackNewActivity.class));

                } else {
                    down_text_6.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 6;
                    startService(new Intent(this, DownLoadService.class));
                }

                break;

            case R.id.web_data_7:
                if (DownLoadService.isDown) {
                    Toast.makeText(this, UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (file7.exists()) {
                    startActivity(new Intent(ListDataActivity.this, BackNewActivity.class));

                } else {
                    down_text_7.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 7;
                    startService(new Intent(this, DownLoadService.class));
                }

                break;

            case R.id.web_data_8:
                if (DownLoadService.isDown) {
                    Toast.makeText(this, UUtils.getString(R.string.由于服务器带宽紧张fgfg), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (file8.exists()) {
                    TermuxData.getInstall().isB_R = 1;

                    TermuxData.getInstall().mFile = file8;

                    startActivity(new Intent(ListDataActivity.this, BackupActivity.class));

                } else {
                    down_text_8.setText(UUtils.getString(R.string.正在连接));
                    DownLoadService.FLAG = 8;
                    startService(new Intent(this, DownLoadService.class));
                }

                break;


        }
    }


    //开始下载

    private void startDown() {

        new Thread(new Runnable() {
            @Override
            public void run() {

             /*   try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

                while (isRun) {


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (DownLoadService.proInt != 0) {

                                switch (DownLoadService.FLAG) {


                                    case 0:
                                        down_text.setText(DownLoadService.pro);

                                        pro.setMax(100);

                                        pro.setProgress(DownLoadService.proInt);

                                        if (DownLoadService.proInt == 100) {
                                            down_text.setText(UUtils.getString(R.string.点击安装));
                                            pro.setProgress(0);
                                        }

                                        //Log.e("XINHAO_HAN", "run: " +  DownLoadService.isDownError);

                                        break;
                                    case 1:
                                        down_text_1.setText(DownLoadService.pro);

                                        pro_1.setMax(100);

                                        pro_1.setProgress(DownLoadService.proInt);
                                        if (DownLoadService.proInt == 100) {
                                            down_text_1.setText(UUtils.getString(R.string.点击安装));
                                            pro_1.setProgress(0);
                                        }
                                        break;
                                    case 2:
                                        down_text_2.setText(DownLoadService.pro);

                                        pro_2.setMax(100);

                                        pro_2.setProgress(DownLoadService.proInt);

                                        if (DownLoadService.proInt == 100) {
                                            down_text_2.setText(UUtils.getString(R.string.点击安装));
                                            pro_2.setProgress(0);
                                        }
                                        break;
                                    case 3:
                                        down_text_3.setText(DownLoadService.pro);

                                        pro_3.setMax(100);

                                        pro_3.setProgress(DownLoadService.proInt);

                                        if (DownLoadService.proInt == 100) {
                                            down_text_3.setText(UUtils.getString(R.string.点击安装));
                                            pro_3.setProgress(0);
                                        }
                                        break;
                                    case 4:
                                        down_text_4.setText(DownLoadService.pro);

                                        pro_4.setMax(100);

                                        pro_4.setProgress(DownLoadService.proInt);

                                        if (DownLoadService.proInt == 100) {
                                            down_text_4.setText(UUtils.getString(R.string.点击安装));
                                            pro_4.setProgress(0);
                                        }
                                        break;


                                    case 5:
                                        down_text_5.setText(DownLoadService.pro);

                                        pro_5.setMax(100);

                                        pro_5.setProgress(DownLoadService.proInt);

                                        if (DownLoadService.proInt == 100) {
                                            down_text_5.setText(UUtils.getString(R.string.点击安装));
                                            pro_5.setProgress(0);
                                        }
                                        break;
                                    case 6:
                                        down_text_6.setText(DownLoadService.pro);

                                        pro_6.setMax(100);

                                        pro_6.setProgress(DownLoadService.proInt);

                                        if (DownLoadService.proInt == 100) {
                                            down_text_6.setText(UUtils.getString(R.string.点击安装));
                                            pro_6.setProgress(0);
                                        }
                                        break;
                                    case 7:
                                        down_text_7.setText(DownLoadService.pro);

                                        pro_7.setMax(100);

                                        pro_7.setProgress(DownLoadService.proInt);

                                        if (DownLoadService.proInt == 100) {
                                            down_text_7.setText(UUtils.getString(R.string.点击安装));
                                            pro_7.setProgress(0);
                                        }
                                        break;
                                    case 8:
                                        down_text_8.setText(DownLoadService.pro);

                                        pro_8.setMax(100);

                                        pro_8.setProgress(DownLoadService.proInt);

                                        if (DownLoadService.proInt == 100) {
                                            down_text_8.setText(UUtils.getString(R.string.点击安装));
                                            pro_8.setProgress(0);
                                        }
                                        break;


                                }


                            }


                            if (DownLoadService.isDownError) {


                                switch (DownLoadService.FLAG) {


                                    case 0:
                                        if (DownLoadService.isDownError) {
                                            down_text.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;
                                    case 1:
                                        if (DownLoadService.isDownError) {
                                            down_text_1.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;
                                    case 2:
                                        if (DownLoadService.isDownError) {
                                            down_text_2.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;
                                    case 3:
                                        if (DownLoadService.isDownError) {
                                            down_text_3.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;
                                    case 4:
                                        if (DownLoadService.isDownError) {
                                            down_text_4.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;
                                    case 5:
                                        if (DownLoadService.isDownError) {
                                            down_text_5.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;
                                    case 6:
                                        if (DownLoadService.isDownError) {
                                            down_text_6.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;
                                    case 7:
                                        if (DownLoadService.isDownError) {
                                            down_text_7.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;
                                    case 8:
                                        if (DownLoadService.isDownError) {
                                            down_text_8.setText(UUtils.getString(R.string.服务器连接失败errererer));
                                            //  DownLoadService.proInt = 0;
                                        }
                                        break;

                                }


                            }

                        }
                    });


                }


            }
        }).start();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRun = false;

    }

    @Override
    public boolean onLongClick(View v) {

        switch (v.getId()) {


            case R.id.web_data:
                break;
            case R.id.web_data_1:
                break;
            case R.id.web_data_2:
                break;
            case R.id.web_data_3:
                break;
            case R.id.web_data_4:
                break;
            case R.id.web_data_5:
                break;
            case R.id.web_data_6:
                break;
            case R.id.web_data_7:
                break;
            case R.id.web_data_8:
                break;


        }


        return true;
    }


}
