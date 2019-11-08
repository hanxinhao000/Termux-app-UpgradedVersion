package main.java.com.termux.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;

import main.java.com.termux.datat.TermuxData;


public class BackupActivity extends Activity {

    private ImageView start;
    private ImageView ico;
    private TextView name;
    private ProgressBar pro;

    private TextView msg;
    //
    private File mFile = new File("/data/data/com.termux/files/");
    private File mFile12 = new File("/data/data/com.termux/files/usr");
    private File mFileRestore = new File("/data/data/com.termux/");
    //private File mFile = new File("/data/data/com.termux/files/usr/bin/pkg");
    private File mSdFile = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/");
    private SimpleDateFormat mSimpleDateFormat;
    private static final String ACTION_STOP_SERVICE = "com.termux.service_stop";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        start = findViewById(R.id.start);
        ico = findViewById(R.id.ico);
        pro = findViewById(R.id.pro);
        msg = findViewById(R.id.msg);
        name = findViewById(R.id.name);
        mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        int isB_r = TermuxData.getInstall().isB_R;


        //备份
        if (isB_r == 0) {
            name.setText("点击开始备份");
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder ab = new AlertDialog.Builder(BackupActivity.this);
                    ab.setTitle("请看提示");
                    //ab.setMessage("提示\n如果有SD卡软连接[termux-setup-storage]执行过这个命令\n请不要直接备份linux系统\n因为会把整个SD卡的内容也备份进去\n解决方法:\n在storage所在目录[rm -rf storage]即可");
                    ab.setMessage("提示\n为了您的数据安全，\n我们在备份和恢复当中\n强制去除了sd卡连接!");
                    ab.setNegativeButton("我知道了!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                            Toast.makeText(BackupActivity.this, "开始", Toast.LENGTH_SHORT).show();
                            name.setText("开始备份,请不要离开此页面!");
                            start.setVisibility(View.GONE);
                            try {
                                startBackUp();
                            } catch (Exception e) {
                                name.setText("备份失败!" + e.toString());
                                e.printStackTrace();

                                throw new RuntimeException(e);
                            }
                        }
                    });
                 /*   ab.setPositiveButton("不要备份", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });*/

                    ab.show();


                }
            });


        } else {
            //恢复

            name.setText("点击开始恢复");
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder ab = new AlertDialog.Builder(BackupActivity.this);
                    ab.setTitle("请看提示");
                    // ab.setMessage("警告[严重,可能造成数据丢失]!!!\n警告[严重,可能造成数据丢失]!!!\n警告[严重,可能造成数据丢失]!!!\n如果有SD卡软连接[termux-setup-storage]执行过这个命令\n请不要直接恢复linux系统!!!\n否则会删除掉你整个sd卡的内容!!!\n解决方法:清除全部APP用户数据在进行恢复即可\n如果是新用户，可忽略警告.");
                    ab.setMessage("提示\n为了您的数据安全，\n我们在备份和恢复当中\n强制去除了sd卡连接!");
                    ab.setNegativeButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                            Toast.makeText(BackupActivity.this, "开始", Toast.LENGTH_SHORT).show();
                            name.setText("我知道了");
                            start.setVisibility(View.GONE);
                            startRestore();
                        }
                    });
                 /*   ab.setPositiveButton("不要恢复", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });*/

                    ab.show();


                }
            });


        }


        startAnim();
    }

    //开始恢复
    private void startRestore() {

/*

        name.setText("正在清空系统目录...");

        boolean delete = mFile.delete();

        if (delete) {
            name.setText("删除失败!");
            Log.e("XINHAO_HAN", "startRestore: " + "删除失败!");
        } else {
            name.setText("文件清空成功,开始准备恢复");
            Log.e("XINHAO_HAN", "startRestore: " + "成功!");
        }
*/
        new Thread(new Runnable() {
            @Override
            public void run() {

                ZipUtils.delFolder(mFile12.getAbsolutePath(), new ZipUtils.ZipNameListener() {
                    @Override
                    public void zip(String FileName, int size, int position) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                name.setText("清空操作区域[" + FileName + "]");
                                msg.setText("恢复中,请不要离开此页面!温馨提示:管理web目录【你的ip:8080】\n请在【usr/share/nginx/html/】操作\n要复制新的文件，请在:/home/下复制\n复制完成后，请设置chmod 777 【文件/文件夹名称】");

                            }
                        });

                    }

                    @Override
                    public void complete() {

                        // startUp();
                        //throw new RuntimeException("执行了不该执行的东西");

                    }

                    @Override
                    public void progress(long size, long position) {

                    }
                });


                Log.e("删除", "run: " + "删除完成....!");

                startUp();

            }
        }).start();


    }

    //开始恢复

    private void startUp() {

        ZipUtils.unZip(TermuxData.getInstall().mFile, mFileRestore.getAbsolutePath(), new ZipUtils.ZipNameListener() {
            @Override
            public void zip(String FileName, int size, int position) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        name.setText(FileName);
                        msg.setText("恢复中,请不要离开此页面!温馨提示:管理web目录【你的ip:8080】\n请在【usr/share/nginx/html/】操作\n要复制新的文件，请在:/home/下复制\n复制完成后，请设置chmod 777 【文件/文件夹名称】");

                    }
                });


            }

            @Override
            public void complete() {

                if (new File("/data/data/com.termux/files/home/BootCommand").exists()) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            name.setText("正在启动恢复包中自带的命令...[命令目录/home/BootCommand]命令之间用'&&'隔开");
                        }
                    });

                    TermuxActivity.startCmmd(name);
                }

                runOnUiThread(new Runnable() {//15710762843
                    @Override
                    public void run() {
                        Toast.makeText(BackupActivity.this, "恢复完成!", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder ab = new AlertDialog.Builder(BackupActivity.this);

                        ab.setTitle("注意");

                        ab.setCancelable(false);

                        ab.setMessage("为了使你的配置生效，请稍后重启APP[大退,不要按返回键退出]\n不重启无法正常使用.");

                        ab.setPositiveButton("立即重启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startService(new Intent(BackupActivity.this, TermuxService.class).setAction(ACTION_STOP_SERVICE));
                            }
                        });


                        ab.setNegativeButton("稍后重启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ab.create().dismiss();
                                finish();
                            }
                        });
                        ab.show();

                    }
                });

            }

            @Override
            public void progress(long size, long position) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pro.setMax((int) size);
                        pro.setProgress((int) position);
                    }
                });
            }
        });

    }


    //开始备份
    private void startBackUp() {

        if (!mSdFile.exists()) {
            mSdFile.mkdirs();
        }

        Date date = new Date();
        String format = mSimpleDateFormat.format(date);

        new Thread(new Runnable() {
            @Override
            public void run() {
                //mSdFile.getAbsolutePath() + "/" + format + ".zip"
                try {
                    ZipUtils.toZip(BackupActivity.this, mFile.getAbsolutePath(), mSdFile.getAbsolutePath() + "/" + format + ".zip", new ZipUtils.ZipNameListener() {
                        @Override
                        public void zip(String FileName, int size, int position) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    name.setText(FileName);

                                }
                            });

                            Log.e("XINHAO_HAN", "zip: " + FileName);

                        }

                        @Override
                        public void complete() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    name.setText("备份完成!");
                                    Toast.makeText(BackupActivity.this, "备份完成!目录为:" + mSdFile.getAbsolutePath() + format + ".zip", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });

                        }

                        @Override
                        public void progress(long size, long position) {


                            runOnUiThread(new Runnable() {
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {
                                    pro.setMax(100);
                                    // pro.setMin((int) position);
                                    pro.setProgress((int) ((double) position / size * 100));

                                    msg.setText("[" + position + "/≈" + size + "] \n[" + getPrintSize(ZipUtils.getFileThisSize()) + "/" + getPrintSize(ZipUtils.getFileSize()) + "]");
                                    msg.setText(msg.getText() + "\n" + "备份中,请不要离开此页面!温馨提示:管理web目录【你的ip:8080】\n请在【usr/share/nginx/html/】操作\n要复制新的文件，请在:/home/下复制\n复制完成后，请设置chmod 777 【文件/文件夹名称】");
                                }
                            });
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();

                    Log.e("出错了!", "run: " + e.toString());
                }
            }
        }).start();

    }


    //单位换算
    public String getPrintSize(long size) {
        //如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
        //如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        //因为还没有到达要使用另一个单位的时候
        //接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
            //因为如果以MB为单位的话，要保留最后1位小数，
            //因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "."
                + String.valueOf((size % 100)) + "MB";
        } else {
            //否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "."
                + String.valueOf((size % 100)) + "GB";
        }
    }


    //动画
    private void startAnim() {

        final int[] i = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {


                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    i[0]++;
                    if (i[0] > 360) {
                        i[0] = 0;
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ico.setRotationY(i[0]);
                        }
                    });
                }


            }
        }).start();

    }

    //开始备份

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("警告!");

            ab.setMessage("你确定要返回吗?\n如果返回你之前所有的操作都将作废\n");

            ab.setNegativeButton("我确定要退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();
                    finish();
                }
            });

            ab.setPositiveButton("不要退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();

                }
            });
            ab.show();

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void CreatAlertDialog(String str) {
        new AlertDialog.Builder(this).setIcon(R.drawable.ic_launcher)
            .setMessage(str)
            .show();
    }


}
