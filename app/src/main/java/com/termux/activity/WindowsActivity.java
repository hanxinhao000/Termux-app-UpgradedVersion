package main.java.com.termux.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import main.java.com.termux.adapter.WindowAdapter;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.utils.UUtils;
import main.java.com.termux.utils.VNCActivityUtils;

public class WindowsActivity extends AppCompatActivity {


    private ListView mListView;

    private TextView title_window;

    private File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/windows");
    private File file1 = new File("/data/data/com.termux/files/usr/bin/qemu-system-x86_64");
    private File file2 = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/window_data.zip");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_windows);
        mListView = findViewById(R.id.list_view);
        title_window = findViewById(R.id.title_window);
        isInstall();
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();
            if (!mkdirs) {
                Toast.makeText(this, UUtils.getString(R.string.请确定你是否打开了SD卡权限ddll), Toast.LENGTH_LONG).show();
            }
        }

        File[] files = file.listFiles();

        if (files != null && files.length > 0) {

            ArrayList<File> objects = new ArrayList<>(Arrays.asList(files));

            mListView.setAdapter(new WindowAdapter(objects));

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    File file = files[position];

                    if (!new File("/data/data/com.termux/files/home/storage/shared/xinhao/windows/" + file.getName()).exists()) {

                        AlertDialog.Builder ab = new AlertDialog.Builder(WindowsActivity.this);

                        ab.setTitle(UUtils.getString(R.string.错误));

                        ab.setMessage(UUtils.getString(R.string.找不到相关文件的链接确定你是否执行了));

                        ab.setNegativeButton(UUtils.getString(R.string.帮我创建), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ab.create().dismiss();
                                finish();

                                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~  \n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~  \n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~  \n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~  \n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~  \n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("termux-setup-storage \n");


                            }
                        });
                        ab.setPositiveButton(UUtils.getString(R.string.我自己创建), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ab.create().dismiss();
                            }
                        });

                        ab.show();

                        return;
                    }


                    TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ && qemu-system-x86_64 -hda /data/data/com.termux/files/home/storage/shared/xinhao/windows/" + file.getName() + " -boot d -m 512  -device e1000,id=d-net1 -vnc :1 & \n");


                    AlertDialog.Builder ab = new AlertDialog.Builder(WindowsActivity.this);

                    ab.setTitle(UUtils.getString(R.string.启动完成));

                    ab.setMessage(UUtils.getString(R.string.错误使用的镜像无法启动必须打开内存卡权限));

                    ab.setPositiveButton(UUtils.getString(R.string.使用自带vnc打开), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                            finish();

                            Toast.makeText(WindowsActivity.this, UUtils.getString(R.string.请到内置VNC打开推荐使用全能版), Toast.LENGTH_SHORT).show();
/*


                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setType("application/vnd.vnc");
                            //vnc://127.0.0.1:5951/?VncUsername=$username&VncPassword=$vncPassword
                            intent.setData(Uri.parse("vnc://127.0.0.1:5901/"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            List<ResolveInfo> resolveInfos = WindowsActivity.this.getPackageManager().queryIntentActivities(intent, 0);
                            if (resolveInfos.size() > 0) {
                                startActivity(intent);
                            } else {
                                Toast.makeText(WindowsActivity.this, "你没有安装群中的vnc!", Toast.LENGTH_LONG).show();
                            }*/


                           // Intent vncIntent = VNCActivityUtils.getVNCIntent(WindowsActivity.this, "5901", "127.0.0.1", "");

                           // startActivity(vncIntent);

                        }
                    });

                    ab.setNegativeButton(UUtils.getString(R.string.我用我自己的VNC), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });
                    ab.show();
                }
            });
        } else {

            title_window.setText(UUtils.getString(R.string.没有镜像或SD卡未打开));
        }
    }
    //1.A2.A3.A4.B5.B

    //检测是否安装
    private void isInstall() {


        if (!file1.exists()) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

            alertDialog.setTitle(UUtils.getString(R.string.错误));

            alertDialog.setCancelable(false);

            alertDialog.setMessage(UUtils.getString(R.string.你没有安装相关环境qemupppp));

            alertDialog.setNegativeButton(UUtils.getString(R.string.在线安装), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    TermuxActivity.mTerminalView.sendTextToTerminal("pkg install x11-repo unstable-repo -y && pkg install qemu-utils qemu-system-x86_64-headless -y && termux-setup-storage\n");
                    alertDialog.create().dismiss();
                    Toast.makeText(WindowsActivity.this, UUtils.getString(R.string.请等待安装完成在进入), Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
/*
            alertDialog.setPositiveButton("离线安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (file2.exists()) {

                        alertDialog.create().dismiss();

                        MyDialog myDialog = new MyDialog(WindowsActivity.this);

                        myDialog.getDialog_title().setText("正在复制到工作区域");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                File file = new File("/data/data/com.termux/files/home/window_data.zip");

                                try {


                                    file.createNewFile();


                                    FileInputStream fileInputStream = new FileInputStream(file2);

                                    FileOutputStream fileOutputStream = new FileOutputStream(file);

                                    int leng = 0;

                                    byte[] b = new byte[1024];

                                    int temp = 0;

                                    while ((leng = fileInputStream.read(b)) != -1) {

                                        fileOutputStream.write(b, 0, leng);

                                        temp += leng;
                                        int finalTemp = temp;
                                        TermuxApplication.mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                myDialog.getDialog_pro_prog().setMax((int) file2.length());
                                                myDialog.getDialog_pro_prog().setProgress(finalTemp);
                                            }
                                        });

                                    }
                                    fileInputStream.close();
                                    fileOutputStream.flush();
                                    fileOutputStream.close();

                                    TermuxApplication.mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            myDialog.getDialog_title().setText("复制完成!马上解压.");
                                        }
                                    });

                                    Thread.sleep(1000);

                                    File file3 = new File("/data/data/com.termux/files/home/qemu/");

                                    if (!file3.exists()) {

                                        boolean mkdirs = file3.mkdirs();

                                        if (!mkdirs) {
                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    myDialog.dismiss();
                                                    Toast.makeText(WindowsActivity.this, "文件夹创建错误,请重试!", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                }
                                            });
                                        }
                                    }

                                    ZipUtils.unZip(file, file3.getAbsolutePath(), new ZipUtils.ZipNameListener() {
                                        @Override
                                        public void zip(String FileName, int size, int position) {

                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    myDialog.getDialog_pro_prog().setMax(size);
                                                    myDialog.getDialog_pro_prog().setProgress(position);
                                                    myDialog.getDialog_pro().setText(FileName);
                                                }
                                            });
                                        }

                                        @Override
                                        public void complete() {

                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(WindowsActivity.this, "完成!请等待安装完成!", Toast.LENGTH_SHORT).show();

                                                    TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                                                    TermuxActivity.mTerminalView.sendTextToTerminal("cd qemu \n");
                                                    TermuxActivity.mTerminalView.sendTextToTerminal("dpkg -i * \n");
                                                    myDialog.dismiss();
                                                    finish();

                                                }
                                            });

                                        }

                                        @Override
                                        public void progress(long size, long position) {

                                        }
                                    });


                                } catch (IOException e) {
                                    e.printStackTrace();
                                    TermuxApplication.mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            myDialog.dismiss();
                                            Toast.makeText(WindowsActivity.this, "操作过程中出现了错误!请重新尝试" + e.toString(), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                    TermuxApplication.mHandler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            myDialog.dismiss();
                                            Toast.makeText(WindowsActivity.this, "操作过程中出现了错误!请重新尝试" + e.toString(), Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }

                            }
                        }).start();


                        myDialog.show();

                    } else {

                        alertDialog.create().dismiss();
                        Toast.makeText(WindowsActivity.this, "没有找到数据包![sdcard -> /xinhao/iso/window_data.zip]", Toast.LENGTH_SHORT).show();
                        finish();
                    }


                }
            });*/
            alertDialog.show();
        }

    }
}

