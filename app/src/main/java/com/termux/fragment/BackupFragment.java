package main.java.com.termux.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.tools.tar.TarUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import main.java.com.termux.activity.BackNewActivity;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.app.TermuxInstaller;
import main.java.com.termux.app.ZipUtils;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.fragment.utils.QZHFUtils;
import main.java.com.termux.utils.ExeCommand;
import main.java.com.termux.utils.UUtils;
import main.java.com.termux.view.MyDialog;
import main.java.com.termux.view.YesNoDialog;


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
                Toast.makeText(getContext(), UUtils.getString(R.string.你没有SD卡权限), Toast.LENGTH_SHORT).show();
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
                mStartBackup.setText(UUtils.getString(R.string.开始备份));

                if (!mFileHomeFilesGzHome.exists()) {

                    boolean mkdirs = mFileHomeFilesGzHome.mkdirs();

                    if (!mkdirs) {
                        Toast.makeText(getContext(), UUtils.getString(R.string.你没有SD卡权限), Toast.LENGTH_SHORT).show();
                        return;
                    }

                }


                if (!mFileHomeFilesGzHome.exists()) {

                    boolean mkdirs = mFileHomeFilesGzHome.mkdirs();

                    if (!mkdirs) {
                        Toast.makeText(getContext(), UUtils.getString(R.string.你没有SD卡权限), Toast.LENGTH_SHORT).show();
                        return;
                    }

                }


                YesNoDialog yesNoDialog = new YesNoDialog(getActivity());

                yesNoDialog.getTitleTv().setText(UUtils.getString(R.string.开始备份1));

                yesNoDialog.getMsgTv().setText(UUtils.getString(R.string.选择您的备份方式));

                yesNoDialog.getYesTv().setText(UUtils.getString(R.string.默认备份56));

                yesNoDialog.getNoTv().setText(UUtils.getString(R.string.强制备份));

                yesNoDialog.getNoTv().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        yesNoDialog.dismiss();


                        if (!(new File("/data/data/com.termux/files/home/storage").exists())){

                            Toast.makeText(TermuxApplication.mContext, UUtils.getString(R.string.没有找到目录), Toast.LENGTH_SHORT).show();

                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    TermuxActivity.mTerminalView.sendTextToTerminal(UUtils.getString(R.string.这块直接输入回车即可));

                                    TermuxActivity.mTerminalView.sendTextToTerminal("termux-setup-storage ");

                                    getActivity().finish();
                                }
                            });

                            return;
                        }

                        MyDialog myDialog = new MyDialog(getActivity());

                        myDialog.getDialog_title().setText(UUtils.getString(R.string.强制备份));

                        myDialog.getDialog_pro_prog().setMax(100);

                        myDialog.show();




                        new QZHFUtils().main(myDialog,simpleDateFormat.format(new Date()) + ".tar.gz",BackupFragment.this);
                    }
                });

                yesNoDialog.show();

                yesNoDialog.getYesTv().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), UUtils.getString(R.string.开始), Toast.LENGTH_SHORT).show();

                        yesNoDialog.dismiss();
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

               /* test();
                Toast.makeText(getContext(), "备份完成", Toast.LENGTH_SHORT).show();
                if (true) {
                    return;
                }*/
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Runtime.getRuntime().exec("chmod 777 " + mFileHome.getAbsolutePath());
                                    Runtime.getRuntime().exec("chmod 777 " + mFileHomeStatic.getAbsolutePath());

                                    //tar -cpvzf /test/backup.tar.gz / --exclude=/test

                                    Log.e("XINHAO_HAN", "run: " + mFileHome.getAbsolutePath() + "  tar -zcvf " + mFileHomeFilesGz.getAbsolutePath() + "/" + simpleDateFormat.format(new Date()) + ".tar.gz  " + mFileHomeFiles.getAbsolutePath());
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
                                                    SpannableString spannableString = new SpannableString(UUtils.getString(R.string.不要说备份着闪退之类的) + buf);
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
                                            try {
                                                if (mStartBackup != null) {
                                                    mStartBackup.setText(UUtils.getString(R.string.备份完成));
                                                }
                                            }catch (Exception e){
                                                Toast.makeText(getContext(), UUtils.getString(R.string.备份完成), Toast.LENGTH_SHORT).show();
                                            }
                                            AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                                            ab.setTitle(UUtils.getString(R.string.备份完成));
                                            ab.setCancelable(false);
                                            ab.setMessage(UUtils.getString(R.string.已成功备份已成功备份));
                                            ab.setNegativeButton(UUtils.getString(R.string.我知道了), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    getActivity().finish();
                                                }
                                            });
                                            //Toast.makeText(getContext(), "备份完成!", Toast.LENGTH_SHORT).show();
                                            title.setText(UUtils.getString(R.string.按钮一开始启动您的备份));
                                            ab.show();
                                        }
                                    });

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();


                    }
                });





                break;


        }

    }


    private void test() {

        try {
            tarFile(mFileHomeFiles, new File(mFileHomeFilesGz.getAbsolutePath() + "/" + simpleDateFormat.format(new Date()) + ".tar.gz"));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static void tarFile(File inFile, File outFile) throws Exception {

        TarArchiveOutputStream taos = new TarArchiveOutputStream(new FileOutputStream(outFile));
        TarArchiveEntry tae = new TarArchiveEntry(inFile);
        tae.setSize(inFile.length());//大小
        tae.setName(new String(inFile.getName().getBytes("gbk"), "ISO-8859-1"));//不设置会默认全路径
        taos.putArchiveEntry(tae);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFile));
        int count;
        byte data[] = new byte[1024];
        while ((count = bis.read(data, 0, 1024)) != -1) {
            taos.write(data, 0, count);
        }
        bis.close();

        taos.closeArchiveEntry();

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
