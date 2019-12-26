package main.java.com.termux.app;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.termux.R;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import main.java.com.termux.activity.LinuxDeployActivity;
import main.java.com.termux.activity.SwitchActivity;
import main.java.com.termux.activity.WindowsActivity;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.bean.CreateSystemBean;
import main.java.com.termux.bean.ReadSystemBean;
import main.java.com.termux.utils.FileUtil;
import main.java.com.termux.utils.VNCActivityUtils;
import main.java.com.termux.view.MyDialog;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {


    private LinearLayout ubuntu;

    private LinearLayout kail;

    private LinearLayout archlinux;
    private LinearLayout debian;
    private LinearLayout alpine;
    private LinearLayout huohu;
    private LinearLayout xfce4;
    private LinearLayout lxde;
    private LinearLayout other;

    private TextView ubuntu_text;

    private File mFile = new File("/data/data/com.termux/");
    private File mFileTEMP = new File("/data/data/com.termux/temp");
    private File mDefFile = new File("/data/data/com.termux/files/xinhao_system.infoJson");
    private File mFileRootfs = new File("/data/data/com.termux/files/xinhao/support/rootfs.tar.gz");
    private File mFileRootfs1 = new File("/data/data/com.termux/files/xinhao/support/");
    private File mFileAssets = new File("/data/data/com.termux/files/support/assets.tar.gz");
    private File mFileTemp = new File("/data/data/com.termux/files/temp");
    private File mFileSupport = new File("/data/data/com.termux/files/support");
    private File mFileCom = new File("/data/data/com.termux/files/xinhao/usr/lib/com_xinhao");
    private File mFileComGUI = new File("/data/data/com.termux/files/xinhao/usr/lib/ubuntu_gui");
    private File mFileComKail = new File("/data/data/com.termux/files/xinhao_kail/usr/lib/com_xinhao");

    File fileOnline = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/ubuntu-xinhao.iso");

    private CreateSystemBean createSystemBean;
    private CreateSystemBean createSystemBean1;
    private CreateSystemBean createSystemBean2;
    private String jd;
    private int contentLength;
    private int leng;
    private static PrintWriter printWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_test);

        ubuntu = findViewById(R.id.ubuntu);

        kail = findViewById(R.id.kail);

        archlinux = findViewById(R.id.archlinux);

        ubuntu_text = findViewById(R.id.ubuntu_text);

        debian = findViewById(R.id.debian);
        other = findViewById(R.id.other);

        alpine = findViewById(R.id.alpine);
        huohu = findViewById(R.id.huohu);
        xfce4 = findViewById(R.id.xfce4);
        lxde = findViewById(R.id.lxde);
        isIofo();
        ubuntu.setOnClickListener(this);
        kail.setOnClickListener(this);
        archlinux.setOnClickListener(this);

        debian.setOnClickListener(this);
        alpine.setOnClickListener(this);
        huohu.setOnClickListener(this);
        xfce4.setOnClickListener(this);
        lxde.setOnClickListener(this);
        other.setOnClickListener(this);

        if (!mFileRootfs1.exists()) {
            mFileRootfs1.mkdirs();
        }

        try {
            if (printWriter == null)
                printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "/termux_xinhao.log"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        isSsh();
        isInstallSystem();
    }

    //判断一安装了哪些系统

    private void isInstallSystem() {

        if (mFileCom.exists()) {

            ubuntu_text.setText("启动ubuntu");

        }


    }

    //判断是否有ssh

    private void isSsh() {


        File file = new File("/data/data/com.termux/files/usr/bin/ssh");

        if (!file.exists()) {

            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("环境不达标");

            ab.setCancelable(false);

            ab.setMessage("请安装完openSSH,在进入安装系统页面!");

            ab.setNegativeButton("帮我安装ssh", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    TermuxActivity.mTerminalView.sendTextToTerminal("pkg in openssh -y\n");

                    finish();
                }
            });
            ab.setPositiveButton("现在不安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();
                    finish();
                }
            });

            ab.show();


        }

        File file2 = new File("/data/data/com.termux/files/home/.ssh");

        if (file2.exists()) {
            file2.delete();
        }


    }

    //判断默认系统
    private void isIofo() {


        if (!mDefFile.exists()) {

            try {
                mDefFile.createNewFile();

                CreateSystemBean createSystemBean = new CreateSystemBean();

                createSystemBean.systemName = "默认系统";

                createSystemBean.dir = "/data/data/com.termux/files";

                String s = new Gson().toJson(createSystemBean);

                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(mDefFile)));

                printWriter.print(s);

                printWriter.flush();

                printWriter.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }


    //创建
    private void createSystem(String name) {
        //先扫描有多少文件
        File[] files = mFile.listFiles();

        if (files.length == 1) {
            //默认只有一个系统
            //直接创建
            File createFile = new File(mFile, "files1");
            createFile.mkdirs();
            createSystemBean1 = new CreateSystemBean();
            createSystemBean1.dir = createFile.getAbsolutePath();
            createSystemBean1.systemName = name;

            String s = new Gson().toJson(createSystemBean1);


            File fileInfo = new File(createFile, "/xinhao_system.infoJson");
            PrintWriter printWriter = null;
            try {

                fileInfo.createNewFile();
                printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileInfo)));

                printWriter.print(s);
                printWriter.flush();
                printWriter.close();

            } catch (IOException e) {
                Toast.makeText(this, "系统创建失败!请重试", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }

            }


        } else {
            //有多个系统

            ArrayList<Integer> arrayList = new ArrayList<>();


            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().startsWith("files")) {
                    // Log.e("XINHAO_HAN", "readFile: " + files[i].getAbsolutePath());
                    String name1 = files[i].getName();
                    String substring = name1.substring(5, name1.length());

                    if (substring.isEmpty()) {
                        arrayList.add(0);
                    } else {
                        arrayList.add(Integer.parseInt(substring));
                    }

                }
            }

            // Log.e("XINHAO_HAN", "createSystem: " + arrayList);


            int max = getMax(arrayList);
            Log.e("XINHAO_HAN", "最大值: " + max);


            //直接创建
            File createFile = new File(mFile, "files" + (max + 1));
            createFile.mkdirs();
            createSystemBean2 = new CreateSystemBean();
            createSystemBean2.dir = createFile.getAbsolutePath();
            createSystemBean2.systemName = name;

            String s = new Gson().toJson(createSystemBean2);


            File fileInfo = new File(createFile, "/xinhao_system.infoJson");
            PrintWriter printWriter = null;
            try {

                fileInfo.createNewFile();
                printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileInfo)));

                printWriter.print(s);
                printWriter.flush();
                printWriter.close();

            } catch (IOException e) {
                Toast.makeText(this, "系统创建失败!请重试", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                return;
            } finally {
                if (printWriter != null) {
                    printWriter.close();
                }

            }
        }


    }

    //比大小
    private int getMax(ArrayList<Integer> number) {

        int temp = number.get(0);

        for (int i = 0; i < number.size(); i++) {

            if (number.get(i) > temp) {
                temp = number.get(i);
            }

        }


        return temp;
    }

    //读取当前目录

    private String readSystem() {

        if (!mDefFile.exists()) {

            return "默认系统";
        }


        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(mDefFile)));

            String temp = "";
            String json = "";

            while ((temp = bufferedReader.readLine()) != null) {

                json += temp;
            }

            bufferedReader.close();

            CreateSystemBean readSystemBean1 = new Gson().fromJson(json, CreateSystemBean.class);

            return readSystemBean1.systemName;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return "默认系统";
    }

    private void startBootVnc() {


        MyDialog myDialog = new MyDialog(this);

        myDialog.getDialog_title().setText("正在启动...");

        myDialog.getDialog_pro().setText("-");

        myDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ubuntu_text.setText("启动ubuntu\n[正在启动...]");
                    }
                });


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

                hashMap.put("INITIAL_USERNAME", "hanxinhao");
                hashMap.put("INITIAL_PASSWORD", "123456");
                hashMap.put("INITIAL_VNC_PASSWORD", "123456");

                hashMap.put("PROOT_DEBUG_LEVEL", "-1");
                hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
                hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/xinhao");
                hashMap.put("OS_VERSION", "4.4.153-perf+");
                hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
                hashMap.put("HOME", "/data/data/com.termux/files/xinhao/home/hanxinhao");
                hashMap.put("USER", "hanxinhao");
                hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/xinhao/temp:/temp/internal:/home");
                hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");


                ProcessBuilder processBuilder = new ProcessBuilder(arrayList);

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {


                        logOut("完成!");


                        Log.e("XINHAO_HAN", "run: " + "完成");
                    }


                });

                Log.e("XINHAO_HAN", "run: " + "完成");

                processBuilder.environment().putAll(hashMap);

                processBuilder.redirectErrorStream(true);


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


                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ubuntu_text.setText("启动ubuntu");
                        }
                    });
                } catch (IOException e) {
                    logOut("错误:" + e.toString());
                    e.printStackTrace();

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ubuntu_text.setText("启动ubuntu\n请尝试重新启动");
                        }
                    });
                }


            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(TestActivity.this, "启动完成", Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();

                        AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);
                        ab.setTitle("启动成功!");
                        ab.setMessage("请在vnc中连接\n127.0.0.1:5951\n账号:hanxinhao\n\n密码:123456\n\n");
                        ab.setNeutralButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ab.create().dismiss();


 /*                               Intent intent = new Intent();
                                intent.setAction(Intent.ACTION_VIEW);
                                intent.setType("application/vnd.vnc");
                                //vnc://127.0.0.1:5951/?VncUsername=$username&VncPassword=$vncPassword
                                intent.setData(Uri.parse("vnc://127.0.0.1:5901/?VncUsername=hanxinhao&VncPassword=123456"));
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                List<ResolveInfo> resolveInfos = TestActivity.this.getPackageManager().queryIntentActivities(intent, 0);
                                if (resolveInfos.size() > 0) {
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(TestActivity.this, "你没有安装群中的vnc!", Toast.LENGTH_LONG).show();
                                }*/

                                VNCActivityUtils.getVNCIntent(TestActivity.this,"5951","127.0.0.1","123456");

                            }
                        });
                        ab.show();
                    }
                });

            }
        }).start();


    }


    private void startBootXSDL() {


        if(true){
            Toast.makeText(this, "暂不支持", Toast.LENGTH_SHORT).show();
            return ;
        }

        MyDialog myDialog = new MyDialog(this);

        myDialog.getDialog_title().setText("正在启动...");

        myDialog.getDialog_pro().setText("-");

        myDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ubuntu_text.setText("启动ubuntu\n[正在启动...]");
                    }
                });


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

                hashMap.put("INITIAL_USERNAME", "hanxinhao");
                hashMap.put("INITIAL_PASSWORD", "123456");
                hashMap.put("INITIAL_VNC_PASSWORD", "123456");

                hashMap.put("PROOT_DEBUG_LEVEL", "-1");
                hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
                hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/xinhao");
                hashMap.put("OS_VERSION", "4.4.153-perf+");
                hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
                hashMap.put("HOME", "/data/data/com.termux/files/xinhao/home/hanxinhao");
                hashMap.put("USER", "hanxinhao");
                hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/xinhao/temp:/temp/internal:/home");
                hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");


                ProcessBuilder processBuilder = new ProcessBuilder(arrayList);

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {


                        logOut("完成!");


                        Log.e("XINHAO_HAN", "run: " + "完成");
                    }


                });

                Log.e("XINHAO_HAN", "run: " + "完成");

                processBuilder.environment().putAll(hashMap);

                processBuilder.redirectErrorStream(true);


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


                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ubuntu_text.setText("启动ubuntu");
                        }
                    });
                } catch (IOException e) {
                    logOut("错误:" + e.toString());
                    e.printStackTrace();

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ubuntu_text.setText("启动ubuntu\n请尝试重新启动");
                        }
                    });
                }


            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Toast.makeText(TestActivity.this, "启动完成", Toast.LENGTH_SHORT).show();
                        myDialog.dismiss();

                        AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);
                        ab.setTitle("启动成功!");
                        ab.setMessage("请在vnc中连接\n127.0.0.1:5951\n账号:hanxinhao\n\n密码:123456\n\n");
                        ab.setNeutralButton("好的", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                ab.create().dismiss();

                                try {
                                    Intent i = new Intent(Intent.ACTION_MAIN, Uri.parse("x11://give.me.display:4713"));
                                    startActivityForResult(i, 1);
                                } catch(Exception e) {
                                    Toast.makeText(TestActivity.this, "面对疾风吧:没有找到XSDL,请到群文件中下载并安装XSDL", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        ab.show();
                    }
                });

            }
        }).start();


    }


    //启动系统

    private void startBoot() {


        MyDialog myDialog = new MyDialog(this);

        myDialog.getDialog_title().setText("正在启动...");

        myDialog.getDialog_pro().setText("-");

        myDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ubuntu_text.setText("启动ubuntu\n[正在启动...]");
                    }
                });

                ArrayList<String> arrayList = new ArrayList<>();

                arrayList.add("/data/data/com.termux/files/support/busybox");
                arrayList.add("sh");
                arrayList.add("/data/data/com.termux/files/support/execInProot.sh");
                arrayList.add("/data/data/com.termux/files/support/startSSHServer.sh");
                arrayList.add("/data/data/com.termux/files/support/isServerInProcTree.sh");
                arrayList.add("23101");

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("INITIAL_USERNAME", "hanxinhao");
                hashMap.put("INITIAL_PASSWORD", "123456");
                hashMap.put("INITIAL_VNC_PASSWORD", "123456");

                hashMap.put("PROOT_DEBUG_LEVEL", "-1");
                hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
                hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/xinhao");
                hashMap.put("OS_VERSION", "4.4.153-perf+");
                hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
                hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/xinhao/temp:/temp/internal");
                hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");


                ProcessBuilder processBuilder = new ProcessBuilder(arrayList);


                writerFile("startSSHServer.sh", new File(mFileSupport, "/startSSHServer.sh"));

                try {
                    Runtime.getRuntime().exec("chmod 777 " + mFileSupport.getAbsolutePath() + "/startSSHServer.sh");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");

                        logOut("完成!");


                        Log.e("XINHAO_HAN", "run: " + "完成");
                    }


                });

                Log.e("XINHAO_HAN", "run: " + "完成");
                processBuilder.environment().putAll(hashMap);

                processBuilder.redirectErrorStream(true);


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


                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ubuntu_text.setText("启动ubuntu");
                        }
                    });
                } catch (IOException e) {
                    logOut("错误:" + e.toString());
                    e.printStackTrace();

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ubuntu_text.setText("启动ubuntu\n请尝试重新启动");
                        }
                    });
                }


            }
        }).start();


        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.dismiss();
                        TermuxActivity.mTerminalView.sendTextToTerminal("clear \n");
                        TermuxActivity.mTerminalView.sendTextToTerminal("ssh hanxinhao@127.0.0.1 -p 2022 \n");

                        startActivity(new Intent(TestActivity.this, TermuxActivity.class));
                    }
                });

            }
        }).start();


    }

    private void installSystemUbuntu() {

        if (mFileCom.exists()) {

            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("请认证阅读!");

            ab.setMessage("开始立即启动你的ubuntu系统\n用户名:hanxinhao\n密码(ssh):123456\nvnc密码:123456\n注意,点完之后请耐心等待，不要多次点击!!!!\n\n注意，如果调到命令页面出现一大堆英文字母和@@@@@，请删除home/.ssh目录,命令:\n[cd ~][rm -rf .ssh/]\n\n\n注意!!如果启动没反应,你也是aarch64的架构\n请在termux中删除proot\npkg un proot\n再次尝试进入安装/启动\n\n\n\n直到跳转到命令行页面即可\n如果需要重新安装\n请点击重新安装\n点击重新安装会退出当前窗口，再次点击进来\n就开始安装了");

            ab.setNegativeButton("ssh启动!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    startBoot();
                    ab.create().dismiss();

                }
            });
            ab.setNeutralButton("vnc启动!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();

                    AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                    ab.setMessage("面对疾风吧!哈sai kei");

                    ab.setMessage("你是要VNC还是XSDL?");

                    ab.setPositiveButton("VNC", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                            startBootVnc();

                        }
                    });

                    ab.setNegativeButton("XSDL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                            startBootXSDL();
                        }
                    });
                    ab.show();




                }
            });

            ab.setPositiveButton("重新安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFileCom.delete();
                    Toast.makeText(TestActivity.this, "请再次点击你要进入的系统，以重新安装", Toast.LENGTH_SHORT).show();
                    ab.create().dismiss();
                }
            });
            ab.show();

            return;
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("注意");
        ab.setMessage("开始下载ubuntu\n过程中会新建一个操作目录\n如果要切换到原来的系统\n请到切换系统中，切换回来\n然后重启APP\n确定下载？");
        ab.setNegativeButton("我确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //  Toast.makeText(TestActivity.this, "开始下载...", Toast.LENGTH_SHORT).show();

                ab.create().dismiss();
                AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                ab.setTitle("提示");

                ab.setMessage("是否需要重新创建一个新的操作目录\n当前目录为:" + readSystem() + "\n已被禁用");

                ab.setPositiveButton("重新创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                              /*  createSystem("ubuntu" + "-UserLAnd");
                                switchSystem("ubuntu" + "-UserLAnd");*/


                        if (false) {

                            AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                            ab.setTitle("发现文件");

                            ab.setMessage("发现已有下载的文件!\n如果上一次下载报错退出的，请重新下载\n不要点击开始安装，如果顺利下载完成的\n请点击开始安装");

                            ab.setNegativeButton("开始安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ab.create().dismiss();
                                    MyDialog myDialog = new MyDialog(TestActivity.this);
                                    myDialog.show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            max = 8389;
                                            installSystem(myDialog);
                                        }
                                    }).start();


                                }
                            });
                            ab.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downloadSystem(
                                        "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-rootfs.tar.gz",
                                        "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-assets.tar.gz",
                                        "ubuntu",
                                        mFileRootfs,
                                        mFileAssets
                                    );
                                }
                            });
                            ab.show();

                        } else {
                            downloadSystem(
                                "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-rootfs.tar.gz",
                                "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-assets.tar.gz",
                                "ubuntu",
                                mFileRootfs,
                                mFileAssets
                            );
                        }


                    }
                });

                ab.setNegativeButton("不需要", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (false) {

                            AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                            ab.setTitle("发现文件");

                            ab.setMessage("发现已有下载的文件!\n如果上一次下载报错退出的，请重新下载\n不要点击开始安装，如果顺利下载完成的\n请点击开始安装");

                            ab.setNegativeButton("开始安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ab.create().dismiss();
                                    MyDialog myDialog = new MyDialog(TestActivity.this);
                                    myDialog.show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            max = 8389;
                                            installSystem(myDialog);
                                        }
                                    }).start();


                                }
                            });
                            ab.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downloadSystem(
                                        "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-rootfs.tar.gz",
                                        "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-assets.tar.gz",
                                        "ubuntu",
                                        mFileRootfs,
                                        mFileAssets
                                    );
                                }
                            });
                            ab.show();

                        } else {
                            downloadSystem(
                                "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-rootfs.tar.gz",
                                "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-assets.tar.gz",
                                "ubuntu",
                                mFileRootfs,
                                mFileAssets
                            );
                        }
                    }
                });
                ab.show();


            }
        });
        ab.setPositiveButton("我不确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ab.create().dismiss();
            }
        });
        ab.show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.ubuntu:


                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new AlertDialog.Builder(this).setTitle("安全问题已被禁用，强制安装需要密码!")
                    .setIcon(R.drawable.ic_launcher)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (et.getText().toString().equals("jrql")) {

                                installSystemUbuntu();
                            } else {
                                Toast.makeText(TestActivity.this, "开发者密码错误!" + et.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).setNegativeButton("取消", null).show();


                if (true)
                    return;


                break;
            case R.id.kail:


                //https://github.com/hanxinhao000/MyLinuxFile/raw/master/app/src/main/assets/apps-ubuntu-rootfs.tar.gz
                Toast.makeText(this, "由于服务器不稳定,请使用离线安装!", Toast.LENGTH_SHORT).show();
                // installGuiUbuntu();
                break;
            case R.id.archlinux:

                Toast.makeText(this, "系统已默认安装,无需再次安装!", Toast.LENGTH_SHORT).show();

                break;

            case R.id.debian:

                startActivity(new Intent(this, SettingActivity.class));

                break;
            case R.id.alpine:


                if (mFileCom.exists()) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(this);

                    ab.setTitle("请认证阅读!");

                    ab.setMessage("开始立即启动你的ubuntu系统\n用户名:hanxinhao\n密码(ssh):123456\nvnc密码:123456\n注意,点完之后请耐心等待，不要多次点击!!!!\n\n注意，如果调到命令页面出现一大堆英文字母和@@@@@，请删除home/.ssh目录,命令:\n[cd ~][rm -rf .ssh/]\n\n\n注意!!如果启动没反应,你也是aarch64的架构\n请在termux中删除proot\npkg un proot\n再次尝试进入安装/启动\n\n\n\n直到跳转到命令行页面即可\n如果需要重新安装\n请点击重新安装\n点击重新安装会退出当前窗口，再次点击进来\n就开始安装了");

                    ab.setNegativeButton("ssh启动!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startBoot();
                            ab.create().dismiss();

                        }
                    });
                    ab.setNeutralButton("vnc启动!", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startBootVnc();
                            ab.create().dismiss();
                        }
                    });

                    ab.setPositiveButton("重新安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mFileCom.delete();
                            Toast.makeText(TestActivity.this, "请再次点击你要进入的系统，以重新安装", Toast.LENGTH_SHORT).show();
                            ab.create().dismiss();
                        }
                    });
                    ab.show();

                    return;


                }

                if (!fileOnline.exists()) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(this);
                    ab.setTitle("提示");
                    ab.setMessage("未在[sdcard/xinhao/iso]目录下发现 命名为:ubuntu-xinhao.iso文件!");

                    ab.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });
                    ab.show();

                    return;

                }


                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setTitle("提示");
                ab.setMessage("发现安装文件,是否立即开始安装?");

                ab.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                copyWork(new Pro() {
                                    @Override
                                    public void com(MyDialog myDialog) {
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                max = 140026;
                                                installSystem(myDialog);
                                            }
                                        }).start();

                                    }
                                });

                            }
                        }).start();
                    }
                });
                ab.setNegativeButton("我在考虑考虑", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });
                ab.show();


                break;
            case R.id.huohu:

                break;
            case R.id.xfce4:

                break;
            case R.id.lxde:

                break;
            case R.id.other:

                break;

        }

    }

    //安装图形化ubuntu

    private void installGuiUbuntu() {


        if (mFileCom.exists()) {

            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("请认证阅读!");

            ab.setMessage("开始立即启动你的ubuntu系统\n用户名:hanxinhao\n密码(ssh):123456\nvnc密码:123456\n注意,点完之后请耐心等待，不要多次点击!!!!\n\n注意，如果调到命令页面出现一大堆英文字母和@@@@@，请删除home/.ssh目录,命令:\n[cd ~][rm -rf .ssh/]\n\n\n注意!!如果启动没反应,你也是aarch64的架构\n请在termux中删除proot\npkg un proot\n再次尝试进入安装/启动\n\n\n\n直到跳转到命令行页面即可\n如果需要重新安装\n请点击重新安装\n点击重新安装会退出当前窗口，再次点击进来\n就开始安装了");

            ab.setNegativeButton("ssh启动!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    startBoot();
                    ab.create().dismiss();

                }
            });
            ab.setNeutralButton("vnc启动!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //startBootVnc();
                    ab.create().dismiss();
                }
            });

            ab.setPositiveButton("重新安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFileCom.delete();
                    Toast.makeText(TestActivity.this, "请再次点击你要进入的系统，以重新安装", Toast.LENGTH_SHORT).show();
                    ab.create().dismiss();
                }
            });
            ab.show();

            return;
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("注意");
        ab.setMessage("开始下载ubuntu\n过程中会新建一个操作目录\n如果要切换到原来的系统\n请到切换系统中，切换回来\n然后重启APP\n确定下载？");
        ab.setNegativeButton("我确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //  Toast.makeText(TestActivity.this, "开始下载...", Toast.LENGTH_SHORT).show();

                ab.create().dismiss();
                AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                ab.setTitle("提示");

                ab.setMessage("是否需要重新创建一个新的操作目录\n当前目录为:" + readSystem() + "\n已被禁用");

                ab.setPositiveButton("重新创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                              /*  createSystem("ubuntu" + "-UserLAnd");
                                switchSystem("ubuntu" + "-UserLAnd");*/


                        if (fileOnline.exists()) {

                            AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                            ab.setTitle("发现文件");

                            ab.setMessage("发现已有离线的文件!\n在iso目录下,是否从本地安装?");

                            ab.setNegativeButton("开始安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ab.create().dismiss();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            copyWork(new Pro() {
                                                @Override
                                                public void com(MyDialog myDialog) {
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            max = 140026;
                                                            installSystem(myDialog);
                                                        }
                                                    }).start();

                                                }
                                            });

                                        }
                                    }).start();


                                }
                            });
                            ab.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downloadSystem(
                                        "http://py9ugi931.bkt.clouddn.com/ubuntu.iso",
                                        "http://py9ugi931.bkt.clouddn.com/arm64-assets.tar.gz",
                                        "ubuntu",
                                        mFileRootfs,
                                        mFileAssets
                                    );
                                }
                            });
                            ab.show();

                        } else {
                            downloadSystem(
                                "http://py9ugi931.bkt.clouddn.com/ubuntu.iso",
                                "http://py9ugi931.bkt.clouddn.com/arm64-assets.tar.gz",
                                "ubuntu",
                                mFileRootfs,
                                mFileAssets
                            );
                        }


                    }
                });

                ab.setNegativeButton("不需要", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (fileOnline.exists()) {

                            AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                            ab.setTitle("发现文件");

                            ab.setMessage("发现已有离线的文件!\n在iso目录下,是否从本地安装?");

                            ab.setNegativeButton("开始安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ab.create().dismiss();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            copyWork(new Pro() {
                                                @Override
                                                public void com(MyDialog myDialog) {
                                                    new Thread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            max = 140026;
                                                            installSystem(myDialog);
                                                        }
                                                    }).start();

                                                }
                                            });

                                        }
                                    }).start();


                                }
                            });
                            ab.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downloadSystem(
                                        "http://py9ugi931.bkt.clouddn.com/ubuntu.iso",
                                        "http://py9ugi931.bkt.clouddn.com/arm64-assets.tar.gz",
                                        "ubuntu",
                                        mFileRootfs,
                                        mFileAssets
                                    );
                                }
                            });
                            ab.show();

                        } else {
                            downloadSystem(
                                "http://py9ugi931.bkt.clouddn.com/ubuntu.iso",
                                "http://py9ugi931.bkt.clouddn.com/arm64-assets.tar.gz",
                                "ubuntu",
                                mFileRootfs,
                                mFileAssets
                            );
                        }
                    }
                });
                ab.show();


            }
        });
        ab.setPositiveButton("我不确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ab.create().dismiss();
            }
        });
        ab.show();

    }


    static interface Pro {

        public void com(MyDialog myDialog);

    }
    //复制到工作区域

    private void copyWork(Pro pro) {


        if (!fileOnline.exists()) {

            Toast.makeText(this, "未发现文件", Toast.LENGTH_SHORT).show();
            return;

        }
        File file = new File("/data/data/com.termux/files/support/rootfs.tar.gz");

        if (file.exists()) {
            file.delete();
        }

        final MyDialog[] myDialog = {null};
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                myDialog[0] = new MyDialog(TestActivity.this);

                myDialog[0].setCancelable(false);

                myDialog[0].getDialog_title().setText("正在复制文件到工作区域\n如果复制很慢请耐心等待\n页面刷新可能有延时\n可能一直卡在某个数,但是内部复制没停\n如果没有耐心，大退app重新执行[不建议!!!]");

                myDialog[0].getDialog_pro().setText("-");


                myDialog[0].show();

                if (!mFileSupport.exists()) {
                    mFileSupport.mkdirs();
                    try {
                        fileOnline.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            FileInputStream fileInputStream = new FileInputStream(fileOnline);


                            FileOutputStream fileOutputStream = new FileOutputStream(mFileRootfs);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myDialog[0].getDialog_pro_prog().setMax((int) fileOnline.length());
                                }
                            });

                            byte[] b = new byte[8192];

                            int l = 0;

                            int i[] = {0};

                            boolean[] xianshi = {true};

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (xianshi[0]) {

                                        try {
                                            Thread.sleep(10);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                myDialog[0].getDialog_pro_prog().setProgress(i[0]);
                                                myDialog[0].getDialog_pro().setText((i[0] / 1024 / 1024 * 1.0) + "MB/" + (fileOnline.length() / 1024 / 1024 * 1.0) + "MB]");
                                                Log.e("XINHAO_HAN", "run: " + "我还在运行...");
                                            }
                                        });


                                    }
                                }
                            }).start();

                            while ((l = fileInputStream.read(b)) != -1) {


                                i[0] += l;

                                fileOutputStream.write(b, 0, l);

                            }
                            fileOutputStream.flush();
                            fileInputStream.close();
                            fileOutputStream.close();
                            xianshi[0] = false;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    pro.com(myDialog[0]);
                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();


                            Log.e("XINHAO_HAN_ERROR", "run: " + e.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(TestActivity.this, "复制出错!" + e.toString(), Toast.LENGTH_SHORT).show();
                                    myDialog[0].dismiss();

                                    AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);
                                    ab.setTitle("错误");
                                    ab.setMessage("你没有SD卡权限,报错信息:\n" + e.toString());
                                    ab.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ab.create().dismiss();
                                        }
                                    });
                                    ab.show();
                                }
                            });

                        }


                    }
                }).start();

            }
        });


    }

    //安装kail
    private void install_kail() {

        if (mFileCom.exists()) {

            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("请认证阅读!");

            ab.setMessage("开始立即启动你的kali系统\n用户名:hanxinhao\n密码(ssh):123456\nvnc密码:123456\n注意,点完之后请耐心等待，不要多次点击!!!!\n\n注意，如果调到命令页面出现一大堆英文字母和@@@@@，请删除\nhome/.ssh\n目录,命令:\n[cd ~][rm -rf .ssh/]\n\n\n直到跳转到命令行页面即可\n如果需要重新安装\n请点击重新安装\n点击重新安装会退出当前窗口，再次点击进来\n就开始安装了");

            ab.setNegativeButton("启动!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    startBoot();
                    ab.create().dismiss();

                }
            });

            ab.setPositiveButton("重新安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mFileCom.delete();
                    Toast.makeText(TestActivity.this, "请再次点击你要进入的系统，以重新安装", Toast.LENGTH_SHORT).show();
                    ab.create().dismiss();
                }
            });
            ab.show();

            return;
        }

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("注意");
        ab.setMessage("开始下载kali\n过程中会新建一个操作目录\n如果要切换到原来的系统\n请到切换系统中，切换回来\n然后重启APP\n确定下载？");
        ab.setNegativeButton("我确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(TestActivity.this, "开始下载...", Toast.LENGTH_SHORT).show();

                ab.create().dismiss();
                AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                ab.setTitle("提示");

                ab.setMessage("是否需要重新创建一个新的操作目录\n当前目录为:" + readSystem() + "\n已被禁用");

                ab.setPositiveButton("重新创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                              /*  createSystem("ubuntu" + "-UserLAnd");
                                switchSystem("ubuntu" + "-UserLAnd");*/


                        if (false) {

                            AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                            ab.setTitle("发现文件");

                            ab.setMessage("发现已有下载的文件!\n如果上一次下载报错退出的，请重新下载\n不要点击开始安装，如果顺利下载完成的\n请点击开始安装");

                            ab.setNegativeButton("开始安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ab.create().dismiss();
                                    MyDialog myDialog = new MyDialog(TestActivity.this);
                                    myDialog.show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //installSystem(myDialog);
                                        }
                                    }).start();


                                }
                            });
                            ab.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    /*downloadSystem(
                                        "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-rootfs.tar.gz",
                                        "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-assets.tar.gz",
                                        "ubuntu",
                                        mFileRootfs,
                                        mFileAssets
                                    );*/
                                }
                            });
                            ab.show();

                        } else {
                           /* downloadSystem(
                                "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-rootfs.tar.gz",
                                "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-assets.tar.gz",
                                "ubuntu",
                                mFileRootfs,
                                mFileAssets
                            );*/
                        }


                    }
                });

                ab.setNegativeButton("不需要", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (false) {

                            AlertDialog.Builder ab = new AlertDialog.Builder(TestActivity.this);

                            ab.setTitle("发现文件");

                            ab.setMessage("发现已有下载的文件!\n如果上一次下载报错退出的，请重新下载\n不要点击开始安装，如果顺利下载完成的\n请点击开始安装");

                            ab.setNegativeButton("开始安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ab.create().dismiss();
                                    MyDialog myDialog = new MyDialog(TestActivity.this);
                                    myDialog.show();
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            installSystem(myDialog);
                                        }
                                    }).start();


                                }
                            });
                            ab.setPositiveButton("重新下载", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    downloadSystem(
                                        "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-rootfs.tar.gz",
                                        "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-assets.tar.gz",
                                        "ubuntu",
                                        mFileRootfs,
                                        mFileAssets
                                    );
                                }
                            });
                            ab.show();

                        } else {
                            downloadSystem(
                                "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-rootfs.tar.gz",
                                "https://github.com/CypherpunkArmory/UserLAnd-Assets-Ubuntu/releases/download/v0.0.3/arm64-assets.tar.gz",
                                "ubuntu",
                                mFileRootfs,
                                mFileAssets
                            );
                        }
                    }
                });
                ab.show();


            }
        });

    }


    //先切换系统

    private void switchSystem(String name) {


        try {


            //本目录系统
            String temp;

            String tempStr = "";
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(mDefFile)));

            while ((temp = bufferedReader.readLine()) != null) {
                tempStr += temp;
            }


            //要被替换的系统
            ReadSystemBean readSystemBean = new ReadSystemBean();

            if (createSystemBean1 == null) {
                readSystemBean.dir = createSystemBean2.dir;

                readSystemBean.name = createSystemBean2.systemName;
            } else {
                readSystemBean.dir = createSystemBean1.dir;

                readSystemBean.name = createSystemBean1.systemName;
            }


            //本目录的系统
            CreateSystemBean readSystemBean1 = new Gson().fromJson(tempStr, CreateSystemBean.class);

            //要被替换的
            String path = readSystemBean.dir;
            //  Log.e("XINHAO_HAN", "要被替换的: " + path);
            //本目录的
            String pathThis = readSystemBean1.dir;
            // Log.e("XINHAO_HAN", "本目录的: " + pathThis);


            File filePath = new File(path);

            File filePathThis = new File(pathThis);


            File fileOutPath = new File(filePath, "/xinhao_system.infoJson");
            // /data/data/com.termux/files1/xinhao_system.infoJson

            File fileOutPathThis = new File(filePathThis, "/xinhao_system.infoJson");
            // /data/data/com.termux/files/xinhao_system.infoJson

            try {
                fileOutPathThis.delete();

                fileOutPathThis.createNewFile();

                readSystemBean1.dir = readSystemBean.dir;

                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileOutPathThis)));
                printWriter.print(new Gson().toJson(readSystemBean1));
                Log.e("XINHAO_HAN", "写入json: " + new Gson().toJson(readSystemBean1));
                printWriter.flush();

                printWriter.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                fileOutPath.delete();

                fileOutPath.createNewFile();

                readSystemBean1.dir = "/data/data/com.termux/files";
                readSystemBean1.systemName = readSystemBean.name;
                // readSystemBean1.systemName = readSystemBean.name;
                // readSystemBean1.systemName = readSystemBean1.systemName;
                //  Log.e("XINHAO_HAN", "本目录的: " + pathThis);

                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileOutPath)));


                printWriter.print(new Gson().toJson(readSystemBean1));

                printWriter.flush();

                printWriter.close();


            } catch (IOException e) {
                e.printStackTrace();
            }


            filePath.renameTo(mFileTEMP);
            filePathThis.renameTo(filePath);
            mFileTEMP.renameTo(filePathThis);


        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(TestActivity.this, "读取失败!", Toast.LENGTH_SHORT).show();
        }


    }


    private boolean isTure = true;
    //负责刷新的

    private void refView(MyDialog myDialog) {

        new Thread(new Runnable() {
            @Override
            public void run() {


                while (isTure) {


                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            String jd = "-%";

                            if (contentLength != 0) {
                                int i = (int) ((((float) leng / (float) contentLength)) * 100);
                                jd = i + "%";
                                myDialog.getDialog_pro_prog().setMax(100);
                                myDialog.getDialog_pro_prog().setProgress((int) i);
                            }

                            if (myDialog != null)
                                myDialog.getDialog_pro().setText("[" + jd + "  -  " + FileUtil.FormetFileSize(leng) + "/" + (contentLength / 1024 / 1024 * 1.0) + "MB]");


                        }
                    });
                }

            }
        }).start();

    }

    //下载系统
    private void downloadSystem(String url, String url2, String name, File mFileRootfs, File mFileAssets) {
        isTure = true;
        if (!mFileSupport.exists()) {
            mFileSupport.mkdirs();
        }
        // refView();
        if (!mFileTemp.exists()) {
            mFileTemp.mkdirs();
        }


        MyDialog myDialog = new MyDialog(TestActivity.this, R.style.MyDialog);
        myDialog.show();
        refView(myDialog);
        new Thread(new Runnable() {
            @Override
            public void run() {


                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        myDialog.getDialog_title().setText("正在下载系统 1/2 [arm64-rootfs.tar.gz]");


                    }
                });


                try {
                    mFileRootfs.createNewFile();
                    mFileAssets.createNewFile();
                    URL url1 = new URL(url);
                    HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();

                    urlConnection.setRequestMethod("GET");

                    urlConnection.setReadTimeout(30000);

                    urlConnection.connect();

                    contentLength = urlConnection.getContentLength();


                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            if (myDialog != null)
                                myDialog.getDialog_pro().setText("[0%  -  0kb/" + (contentLength / 1024 / 1024 * 1.0) + "MB]");


                        }
                    });

                    //FileUtil

                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == 200) {

                        InputStream inputStream = urlConnection.getInputStream();

                        FileOutputStream fileOutputStream = new FileOutputStream(mFileRootfs);

                        byte[] b = new byte[5120];

                        int l = 0;

                        leng = 0;

                        while ((l = inputStream.read(b)) != -1) {

                            leng += l;
                            //Thread.sleep(5);


                            fileOutputStream.write(b, 0, l);
                        }

                        inputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();

                    } else {
                        //  myDialog.getDialog_title().setText("正在下载系统 1/2 [arm64-rootfs.tar.gz] -- 失败 " + responseCode);
                        // Toast.makeText(TestActivity.this, "正在下载系统 1/2 [arm64-rootfs.tar.gz] -- 失败 " + responseCode, Toast.LENGTH_SHORT).show();

                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (myDialog != null)
                                    myDialog.dismiss();
                                shouDialog("正在下载系统 1/2 [arm64-rootfs.tar.gz] -- 失败 \n" + responseCode + "\n建议挂VPN");
                            }
                        });
                    }


                } catch (Exception e) {
                    e.printStackTrace();

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (myDialog != null)
                                myDialog.dismiss();
                            shouDialog("正在下载系统 1/2 [arm64-rootfs.tar.gz] -- 失败 \n" + e.toString() + "\n建议挂VPN");
                        }
                    });
                }

                //-----------------------------


                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (myDialog != null)
                            myDialog.getDialog_title().setText("正在下载安装程序 2/2 [arm64-assets.tar.gz]");


                    }
                });


                try {

                    URL url1 = new URL(url2);
                    HttpURLConnection urlConnection = (HttpURLConnection) url1.openConnection();

                    urlConnection.setRequestMethod("GET");

                    urlConnection.setReadTimeout(30000);

                    urlConnection.connect();

                    contentLength = urlConnection.getContentLength();


                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (myDialog != null)
                                myDialog.getDialog_pro().setText("[0%  -  0kb/" + (contentLength / 1024 / 1024 * 1.0) + "MB]");


                        }
                    });

                    //FileUtil

                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == 200) {

                        InputStream inputStream = urlConnection.getInputStream();

                        FileOutputStream fileOutputStream = new FileOutputStream(mFileAssets);

                        byte[] b = new byte[5120];

                        int l = 0;

                        leng = 0;

                        while ((l = inputStream.read(b)) != -1) {

                            leng += l;

                            fileOutputStream.write(b, 0, l);
                        }

                        inputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();


                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                myDialog.getDialog_title().setText("下载完成!等待继续安装，请稍等");

                            }
                        });

                    } else {
                        //  myDialog.getDialog_title().setText("正在下载系统 1/2 [arm64-rootfs.tar.gz] -- 失败 " + responseCode);

                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (myDialog != null)
                                    myDialog.dismiss();
                                shouDialog("正在下载安装程序 2/2 [arm64-assets.tar.gz] -- 失败 \n" + responseCode + "\n建议挂VPN");
                            }
                        });
                    }

                    Thread.sleep(1000);
                    installSystem(myDialog);
                } catch (Exception e) {


                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (myDialog != null)
                                myDialog.dismiss();
                            shouDialog("正在下载安装程序 2/2 [arm64-assets.tar.gz] -- 失败 \n" + e.toString() + "\n建议挂VPN");
                        }
                    });
                    e.printStackTrace();
                }


            }
        }).start();


    }

    //开始安装

    private void installSystem(MyDialog myDialog) {

        isTure = false;
        MyDialog finalMyDialog = myDialog;
        TermuxApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {

                finalMyDialog.getDialog_title().setText("开始安装...");
                finalMyDialog.getDialog_pro().setText("-");

                logOut("开始安装");
            }
        });

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TermuxApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {

                // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");
                finalMyDialog.getDialog_title().setText("正在复制工具包[/support]...");
                logOut("正在复制工具包[/support]...");

            }
        });


        try {
            Runtime.getRuntime().exec("chmod 777 " + new File(mFileTemp, "busybox").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }


        switch (determineTermuxArchName()) {
            case "arm64-v8a":
                writerFile("sh.zip", new File(mFileSupport, "/sh.xh.tar"));
                logOut("正在复制工具包[sh_arm64_v8a]...");
                break;
            case "armeabi-v7a":
                writerFile("sh.zip", new File(mFileSupport, "/sh.xh.tar"));
                logOut("正在复制工具包[sh_armeabi_v7a]...");
                break;
            default:
                writerFile("sh.zip", new File(mFileSupport, "/sh.xh.tar"));
                logOut("默认的没有!" + determineTermuxArchName());
                break;

        }


        logOut("写出 sh.xh.tar 文件");
        ZipUtils.unZip(new File(mFileSupport, "/sh.xh.tar"), mFileSupport.getAbsolutePath(), new ZipUtils.ZipNameListener() {
            @Override
            public void zip(String FileName, int size, int position) {
                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");
                        finalMyDialog.getDialog_title().setText("正在解压安装工具包[" + FileName + "]");

                        logOut("正在解压安装工具包[" + FileName + "]");
                    }
                });
            }

            @Override
            public void complete() {
                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");
                        finalMyDialog.getDialog_title().setText("完成!");
                        logOut("完成!");
                    }
                });

                startInstallLinux(finalMyDialog);
            }

            @Override
            public void progress(long size, long position) {

            }
        });


    }


    private static String determineTermuxArchName() {
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
                    return "arm64-v8a";
                case "armeabi-v7a":
                    return "armeabi-v7a";
                case "x86_64":
                    return "x86_64";
                case "x86":
                    return "x86";
            }
        }
        throw new RuntimeException("Unable to determine arch from Build.SUPPORTED_ABIS =  " +
            Arrays.toString(Build.SUPPORTED_ABIS));
    }


    private int max = 100;
    //开始安装linux

    private void startInstallLinux(MyDialog myDialog) {


        Log.e("XINHAO_HAN", "startInstallLinux: " + "开始安装...");
        logOut("XINHAO_HAN" + " startInstallLinux: " + "开始安装...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TermuxApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {

                // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");
                myDialog.getDialog_title().setText("开始展开linux文件[切勿离开此界面,否则可能导致安装失败!!!!]\n\n图形ubuntu需:10~20分钟");
                //startInstallLinux(myDialog);
                myDialog.getDialog_pro_prog().setMax(max);
                logOut("设置进度条最大位置:" + max);
            }
        });
        Log.e("XINHAO_HAN", "startInstallLinux: " + "开始解压linux...");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        File file = new File("/data/data/com.termux/files/xinhao/support");
        if (!file.exists()) {
            file.mkdirs();
        }

        ArrayList<String> arrayList = new ArrayList<>();

        try {
            Runtime.getRuntime().exec("chmod 777 /data/data/com.termux/files/support/rootfs.tar.gz");

            logOut("chmod 777 /data/data/com.termux/files/support/rootfs.tar.gz");
        } catch (IOException e) {
            e.printStackTrace();
        }

        arrayList.add("/data/data/com.termux/files/support/busybox");
        arrayList.add("sh");
        arrayList.add("/data/data/com.termux/files/support/execInProot.sh");
        arrayList.add("/data/data/com.termux/files/support/extractFilesystem.sh");

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("INITIAL_USERNAME", "hanxinhao");
        hashMap.put("INITIAL_PASSWORD", "123456");
        hashMap.put("INITIAL_VNC_PASSWORD", "123456");
/*

        hashMap.put("INITIAL_USERNAME", "redmi");
        hashMap.put("INITIAL_PASSWORD", "12345678");
        hashMap.put("INITIAL_VNC_PASSWORD", "12345678");
*/

        hashMap.put("PROOT_DEBUG_LEVEL", "-1");
        hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
        hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/xinhao");
        hashMap.put("OS_VERSION", "4.4.153-perf+");
        hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
        hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/xinhao/temp:/temp/internal");
        hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");


        ProcessBuilder processBuilder = new ProcessBuilder(arrayList);


        processBuilder.environment().putAll(hashMap);

        processBuilder.redirectErrorStream(true);


        try {
            Process start = processBuilder.start();

            InputStream inputStream = start.getInputStream();

            int l = 0;

            byte[] b = new byte[1024];


            int temp = 0;

            while ((l = inputStream.read(b)) != -1) {

                String s = new String(b, "UTF-8");

                Log.e("XINHAO_HANCMMOD", "startInstallLinux: " + s);

                int i = way2(s, "\n");


                temp += i;


                logOut(s);
                int finalTemp = temp;
                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");

                        myDialog.getDialog_pro_prog().setProgress(finalTemp);
                        try {
                            if (s.lastIndexOf("\n") != -1) {
                                String substring = s.substring(s.lastIndexOf("\n"));
                                myDialog.getDialog_pro().setText(substring);
                            } else {
                                myDialog.getDialog_pro().setText("--");
                            }

                        } catch (Exception e) {
                            myDialog.getDialog_pro().setText("--");
                        }


                    }
                });
            }

            inputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.e("XINHAO_HAN", "startInstallLinux: " + "开始解压工具包!");
        writerFile("startSSHServer.sh", new File(mFileSupport, "/startSSHServer.sh"));

        try {
            Runtime.getRuntime().exec("chmod 777 " + new File(mFileSupport, "/startSSHServer.sh").getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        TermuxApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {


                // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");
                myDialog.getDialog_title().setText("安装完成!请再次点击启动linux\n如果过程很快，那就是失败了!");
                myDialog.getDialog_pro().setText("-");

                try {
                    mFileCom.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mFileRootfs.delete();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        TermuxApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {

                myDialog.dismiss();
            }
        });


        /*ZipUtils.unZip(new File(mFileSupport, "/assets.zip"), mFileSupport.getAbsolutePath(), new ZipUtils.ZipNameListener() {
            @Override
            public void zip(String FileName, int size, int position) {

            }

            @Override
            public void complete() {

                Log.e("XINHAO_HAN", "startInstallLinux: " + "安装完成!");

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");
                        myDialog.getDialog_title().setText("安装完成!请再次点击启动linux");
                        myDialog.getDialog_pro().setText("-");

                        try {
                            mFileCom.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                TermuxApplication.mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        myDialog.dismiss();
                    }
                });

            }

            @Override
            public void progress(long size, long position) {

            }
        });
*/

    }

    public int way2(String st, String M) {
        int count = (st.length() - st.replace(M, "").length()) / M.length();
        return count;
    }


    //执行命令

    private void execCommand(String cmd) {


        Runtime runtime = Runtime.getRuntime();

        try {
            Process exec = runtime.exec(cmd);

            InputStream inputStream = exec.getInputStream();

            int l = 0;

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            while ((l = inputStream.read()) != -1) {

                byteArrayOutputStream.write(l);
            }

            String s = byteArrayOutputStream.toString();

            Log.e("XINHAO_HAN_COMMD", "execCommand: " + s);
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //输出日志

    private void logOut(String s) {

        try {

            Log.e("XINHAO_HAN_LL", "logOut: " + s);

            if (s == null) {
                printWriter.print("null");
            } else {
                printWriter.print(s);
            }
            printWriter.flush();

        } catch (Exception e) {
            TermuxApplication.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(TestActivity.this, "出现了警告，但是这个警告可以忽略 \n" + e.toString(), Toast.LENGTH_SHORT).show();

                }
            });
        }
    }

    //写出文件
    private void writerFile(String name, File mFile) {

        try {
            Runtime.getRuntime().exec("chmod 777 " + mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream open = getAssets().open(name);

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


    private void shouDialog(String msg) {


        Log.e("XINHAO_HAN", "shouDialog: " + msg);
        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("下载失败!");
        ab.setMessage(msg);
        ab.show();

    }
}
