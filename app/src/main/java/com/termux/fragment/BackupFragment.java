package main.java.com.termux.fragment;

import android.graphics.Color;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;

import org.apache.tools.tar.TarUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import main.java.com.termux.activity.BackNewActivity;
import main.java.com.termux.app.TermuxInstaller;
import main.java.com.termux.app.ZipUtils;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.utils.ExeCommand;


public class BackupFragment extends BaseFragment implements View.OnClickListener {


    private LinearLayout boom;
    private TextView title;
    private TextView mStartBackup;

    private File mFileHomeFiles = new File("/data/data/com.termux/files/");
    private File mFileHome = new File("/data/data/com.termux/busybox");
    private File mFileHomeStatic = new File("/data/data/com.termux/busybox_static");
    private File mFileSupport = new File("/data/data/com.termux/files/support");
    private File mFileSupportSh = new File("/data/data/com.termux/files/support/extractFilesystem.sh");
    private File mFileHomeFilesGz = new File(Environment.getExternalStorageDirectory() + "/xinhao/data/");
    private File mFileHomeFilesGzHome = new File(Environment.getExternalStorageDirectory() + "/xinhao/data/");

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

    @Override
    public View getFragmentView() {
        return View.inflate(getContext(), R.layout.fragment_backup_k, null);
    }

    @Override
    public void initFragmentView(View mView) {

        if (!mFileHomeFilesGzHome.exists()) {

            boolean mkdirs = mFileHomeFilesGzHome.mkdirs();

            if (!mkdirs) {
                Toast.makeText(getContext(), "你没有SD卡权限!", Toast.LENGTH_SHORT).show();
            }

        }

        boom = (LinearLayout) findViewById(R.id.boom);
        title = (TextView) findViewById(R.id.title);
        mStartBackup = (TextView) findViewById(R.id.start_backup);
        boom.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.boom:
                BackNewActivity.mIsRun = true;
                boom.setVisibility(View.GONE);
                mStartBackup.setText("开始备份....");

                if (!mFileHomeFilesGzHome.exists()) {

                    boolean mkdirs = mFileHomeFilesGzHome.mkdirs();

                    if (!mkdirs) {
                        Toast.makeText(getContext(), "你没有SD卡权限!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }


                if (!mFileHomeFilesGzHome.exists()) {

                    boolean mkdirs = mFileHomeFilesGzHome.mkdirs();

                    if (!mkdirs) {
                        Toast.makeText(getContext(), "你没有SD卡权限!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                Toast.makeText(getContext(), "开始", Toast.LENGTH_SHORT).show();

                String cpu = TermuxInstaller.determineTermuxArchName();

                switch (cpu) {
                    case "aarch64":
                        writerFile("arm_64/busybox", mFileHome, 1024);
                        writerFile("arm_64/busybox_static", mFileHomeStatic, 1024);
                        break;
                    case "arm":
                        writerFile("arm/busybox", mFileHome, 1024);
                        writerFile("arm_64/busybox_static", mFileHomeStatic, 1024);
                        break;
                    case "x86_64":
                        writerFile("x86/busybox", mFileHome, 1024);
                        writerFile("arm_64/busybox_static", mFileHomeStatic, 1024);
                        break;
                }

            /*    test();
                Toast.makeText(getContext(), "备份完成", Toast.LENGTH_SHORT).show();
                if (true) {
                    return;
                }
*/
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Runtime.getRuntime().exec("chmod 777 " + mFileHome.getAbsolutePath());
                            Runtime.getRuntime().exec("chmod 777 " + mFileHomeStatic.getAbsolutePath());

                            //tar -cpvzf /test/backup.tar.gz / --exclude=/test

                            Log.e("XINHAO_HAN", "run: " + mFileHome.getAbsolutePath() + "  tar -zcvf " + mFileHomeFilesGz.getAbsolutePath() + "/" + simpleDateFormat.format(new Date()) + ".tar.gz  " + mFileHomeFiles.getAbsolutePath() );
                            ExeCommand cmd = new ExeCommand(false).run(mFileHome.getAbsolutePath() + "  tar -zcvf " + mFileHomeFilesGz.getAbsolutePath() + "/" + simpleDateFormat.format(new Date()) + ".tar.gz  " + mFileHomeFiles.getAbsolutePath(), 60000);
                           // ExeCommand cmd = new ExeCommand(false).run(mFileHome.getAbsolutePath() , 60000);
                            while (cmd.isRunning()) {
                                try {
                                    Thread.sleep(50);
                                } catch (Exception e) {

                                }
                                String buf = cmd.getResult();
                                //do something

                                TermuxApplication.mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (!buf.equals("")) {
                                            SpannableString spannableString = new SpannableString("备份完成会自定退出!!!!!!!!不要说备份着闪退之类的!!备份完成自动退出  \n\n点击[开始备份]按钮一开始启动您的备份\n\n不要退出该页面,否则导致备份失败!\n\n备份文件在[sdcard -> xinhao/data/]目录下 \n\n 请等到备份完成,直至备份按钮再次出现!\n\n[正在备份...]\n\n" + buf);
                                            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                            title.setText(spannableString);

                                        }
                                    }
                                });

                                Log.e("XINHAO_CMD", "onClick: " + buf);

                            }


                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    boom.setVisibility(View.VISIBLE);
                                    mStartBackup.setText("备份完成!");
                                    Toast.makeText(getContext(), "备份完成!", Toast.LENGTH_SHORT).show();
                                    title.setText("点击[开始备份]按钮一开始启动您的备份\n\n不要退出该页面,否则导致备份失败!\n\n备份文件在[sdcard -> xinhao/data/]目录下 \n\n 请等到备份完成,直至备份按钮再次出现!\n\n[备份完成!]");
                                    getActivity().finish();
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


                break;


        }

    }


    private void test() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    Runtime.getRuntime().exec("chmod 777 " + mFileHome.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(!mFileSupport.exists()){
                    mFileSupport.mkdirs();
                }


                switch (TermuxInstaller.determineTermuxArchName()) {
                    case "arm64-v8a":
                        writerFile("sh.zip", new File(mFileSupport, "/sh.xh.tar"));
                        Log.e("XINHAO_HAN", "正在复制工具包[sh_arm64_v8a]...");
                        break;
                    case "armeabi-v7a":
                        writerFile("sh.zip", new File(mFileSupport, "/sh.xh.tar"));
                        Log.e("XINHAO_HAN", "正在复制工具包[sh_armeabi_v7a]...");
                        break;
                    default:
                        writerFile("sh.zip", new File(mFileSupport, "/sh.xh.tar"));
                        Log.e("XINHAO_HAN", "默认的没有!" + TermuxInstaller.determineTermuxArchName());
                        break;

                }


                ZipUtils.unZip(new File(mFileSupport, "/sh.xh.tar"), mFileSupport.getAbsolutePath(), new ZipUtils.ZipNameListener() {
                    @Override
                    public void zip(String FileName, int size, int position) {
                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");
                                // finalMyDialog.getDialog_title().setText("正在解压安装工具包[" + FileName + "]");

                                Log.e("XINHAO_HAN", "正在解压安装工具包[" + FileName + "]");
                            }
                        });
                    }

                    @Override
                    public void complete() {
                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                // finalMyDialog.getDialog_title().setText("正在解压安装程序包[arm64-assets.tar.gz]...");
                                // finalMyDialog.getDialog_title().setText("完成!");
                                Log.e("XINHAO_HAN", "完成!");
                            }
                        });

                        //  startInstallLinux(finalMyDialog);
                    }

                    @Override
                    public void progress(long size, long position) {

                    }
                });


                writerFile("extractFilesystem.sh", mFileSupportSh);

                try {
                    Runtime.getRuntime().exec("chmod 777 " + mFileSupportSh.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ArrayList<String> arrayList = new ArrayList<>();

                arrayList.add("/data/data/com.termux/files/support/busybox");
                arrayList.add("sh");
                arrayList.add("/data/data/com.termux/files/support/execInProot.sh");
                arrayList.add("/data/data/com.termux/files/support/extractFilesystem.sh");

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("INITIAL_USERNAME", "hanxinhao");
                hashMap.put("INITIAL_PASSWORD", "123456");
                hashMap.put("INITIAL_VNC_PASSWORD", "123456");
                hashMap.put("PROOT_DEBUG_LEVEL", "-1");
                hashMap.put("LD_LIBRARY_PATH", "/data/data/com.termux/files/support/");
                hashMap.put("ROOTFS_PATH", "/data/data/com.termux/files/support");
                hashMap.put("OS_VERSION", "4.4.153-perf+");
                hashMap.put("ROOT_PATH", "/data/data/com.termux/files");
                hashMap.put("EXTRA_BINDINGS", "-b /storage/emulated/0/xinhao/temp:/temp/internal");
                hashMap.put("LIB_PATH", "/data/data/com.termux/files/support/");
                hashMap.put("SD_PATH", Environment.getExternalStorageDirectory().getAbsolutePath() + "/xinhao/data/" + simpleDateFormat.format(new Date()) + ".tar.gz");


                ProcessBuilder processBuilder = new ProcessBuilder(arrayList);


                processBuilder.environment().putAll(hashMap);

                processBuilder.redirectErrorStream(true);


                try {
                    Process start = processBuilder.start();

                    InputStream inputStream = start.getInputStream();

                    int l = 0;

                    byte[] b = new byte[1024];

                    while ((l = inputStream.read(b)) != -1) {

                        String s = new String(b, "UTF-8");

                        Log.e("XINHAO_HANCMMOD", "startInstallLinux: " + s);
                    }

                    inputStream.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
        }).start();


    }

    //写出文件
    private void writerFile(String name, File mFile) {

        try {
            InputStream open = getContext().getAssets().open(name);

            int len = 0;

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read()) != -1) {
                fileOutputStream.write(len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }

    private void writerFile(String name, File mFile, int size) {

        try {
            InputStream open = getContext().getAssets().open(name);

            int len = 0;

            byte[] b = new byte[size];

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


}
