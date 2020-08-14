package main.java.com.termux.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;

import org.apache.tools.tar.TarUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import main.java.com.termux.activity.BackNewActivity;
import main.java.com.termux.adapter.RestoreAdapter;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.app.TermuxInstaller;
import main.java.com.termux.app.TermuxService;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.core.CoreLinux;
import main.java.com.termux.fragment.utils.QZUtils;
import main.java.com.termux.utils.ExeCommand;
import main.java.com.termux.utils.SaveData;
import main.java.com.termux.utils.UUtils;
import main.java.com.termux.utils.XHTarUtils;
import main.java.com.termux.view.MyDialog;
import main.java.com.termux.view.YesNoDialog;

public class RestoreFragment extends BaseFragment {

    private File mFile12 = new File("/data/data/com.termux/files/usr");
    private File mFile13 = new File("/data/data/com.termux/files/home");
    private File mSdFile = new File(Environment.getExternalStorageDirectory(), "/xinhao/data/");
    private File mFileHomeFiles = new File("/data/data/com.termux/files/");
    private File mFileHome = new File("/data/data/com.termux/busybox");
    private File mFileHomeStatic = new File("/data/data/com.termux/busybox_static");
    private File mFileHomeProot = new File("/data/data/com.termux/proot");
    private File mFileHomeMain = new File("/data/data/com.termux/files/usr/bin/tar");
    private File mFileHomeMainTar = new File("/data/data/com.termux/busybox_tar");
    private ListView mListView;
    private TextView mTitle;
    private TextView mStartRe;
    private Process mProcess;

    @Override
    public View getFragmentView() {
        return View.inflate(getContext(), R.layout.fragment_restore_k, null);
    }

    @Override
    public void initFragmentView(View mView) {

        mListView = (ListView) findViewById(R.id.list_view);
        mTitle = (TextView) findViewById(R.id.title);
        mStartRe = (TextView) findViewById(R.id.start_re);

        ArrayList<File> files = new ArrayList<>();

        File[] files1 = mSdFile.listFiles();

        if (files1 == null) {
            mStartRe.setText(UUtils.getString(R.string.没有SD卡权限rrr111));
            return;
        }

        for (int i = 0; i < files1.length; i++) {

            files.add(files1[i]);

        }

        if (files.size() == 0) {
            mStartRe.setText(UUtils.getString(R.string.没有SD卡权限rrr111));
        }

        mListView.setAdapter(new RestoreAdapter(files));


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //标记恢复处.....


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = files.get(position);


                YesNoDialog yesNoDialog = new YesNoDialog(getActivity());

                yesNoDialog.getTitleTv().setText(UUtils.getString(R.string.恢复方式rrr55));
                yesNoDialog.getMsgTv().setText(UUtils.getString(R.string.默认恢复555ggg));

                yesNoDialog.getYesTv().setText(UUtils.getString(R.string.默认恢复));

                yesNoDialog.getNoTv().setText(UUtils.getString(R.string.强制恢复));
                yesNoDialog.getNoTv().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {



                        if (!(new File("/data/data/com.termux/files/home/storage").exists())){

                            Toast.makeText(TermuxApplication.mContext, UUtils.getString(R.string.没有找到目录), Toast.LENGTH_SHORT).show();

                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {


                                    TermuxActivity.mTerminalView.sendTextToTerminal(UUtils.getString(R.string.这块直接输入回车即可hfrrr));

                                    TermuxActivity.mTerminalView.sendTextToTerminal("termux-setup-storage ");
                                    getActivity().finish();
                                }
                            });



                            return;
                        }



                        if(yesNoDialog.getInputSystemName().getText().toString().isEmpty()){
                            yesNoDialog.getInputSystemName().setVisibility(View.VISIBLE);

                            Toast.makeText(getContext(), UUtils.getString(R.string.请输入系统名称rrr), Toast.LENGTH_SHORT).show();
                            return;
                        }


                        yesNoDialog.dismiss();

                        MyDialog myDialog = new MyDialog(getActivity());

                        myDialog.getDialog_title().setText(UUtils.getString(R.string.请稍等此过程禁止切屏));

                        myDialog.getDialog_pro().setText(UUtils.getString(R.string.正在搜索多系统));

                        myDialog.getDialog_pro_prog().setMax(100);
                        myDialog.getDialog_pro_prog().setProgress(6);

                        myDialog.show();

                        new QZUtils().main(myDialog,yesNoDialog.getInputSystemName().getText().toString(),file,RestoreFragment.this);


                    }
                });

                yesNoDialog.show();

                yesNoDialog.getTitleTv().setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            yesNoDialog.dismiss();
                        }
                    }
                );


                yesNoDialog.getYesTv().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        yesNoDialog.dismiss();

                        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());

                        ab.setTitle(UUtils.getString(R.string.危险rrr));

                        ab.setMessage(UUtils.getString(R.string.恢复设备数据会擦除掉之前所有的数据));

                        ab.setPositiveButton(UUtils.getString(R.string.我清楚我在做什么), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mListView.setVisibility(View.GONE);
                                mTitle.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), UUtils.getString(R.string.开始), Toast.LENGTH_SHORT).show();
                                ab.create().dismiss();
                                BackNewActivity.mIsRun = true;
                                start(file);


                            }
                        });

                        ab.setNeutralButton(UUtils.getString(R.string.我在考虑考虑), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ab.create().dismiss();
                            }
                        });
                        ab.show();

                    }
                });


            }
        });


    }

    private void start(File file) {


        Toast.makeText(getContext(), UUtils.getString(R.string.开始清除数据), Toast.LENGTH_SHORT).show();
        Log.e("XINHAO_HAN_FILE", "start: " + "执行0");

        String cpu = TermuxInstaller.determineTermuxArchName();

        switch (cpu) {
            case "aarch64":
                writerFile("arm_64/busybox", mFileHome, 1024);
                writerFile("arm_64/busybox_static", mFileHomeStatic, 1024);
                // writerFile("arm_64/proot", mFileHomeProot, 1024);
                break;
            case "arm":
                writerFile("arm/busybox", mFileHome, 1024);
                // writerFile("arm/busybox_static", mFileHomeStatic, 1024);
                //   writerFile("arm/proot", mFileHomeProot, 1024);
                break;
            case "x86_64":
                writerFile("x86/busybox", mFileHome, 1024);
                //  writerFile("x86/busybox_static", mFileHomeStatic, 1024);
                //  writerFile("x86/proot", mFileHomeProot, 1024);
                break;
        }

        try {
            Runtime.getRuntime().exec("chmod 777 " + mFileHome.getAbsolutePath());
            Runtime.getRuntime().exec("chmod 777 " + mFileHomeStatic.getAbsolutePath());
            //Runtime.getRuntime().exec("chmod 777 " + mFileHomeProot.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        TermuxActivity.getTermux().getCurrentTermSession().finishIfRunning();

        Log.e("XINHAO_HAN_FILE", "start: " + "执行1");

      /*  test1(file);



        if (true) {
            return;
        }*/
        String res_files = SaveData.getData("res_files");

        if (res_files.equals("def")) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {


                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTitle.setText(UUtils.getString(R.string.恢复完成会自定退出rrvv));

                            }
                        });
                        ExeCommand cmd1 = new ExeCommand(false).run(mFileHome.getAbsolutePath() + " rm -rf " + mFile12.getAbsolutePath(), 60000);

                        while (cmd1.isRunning()) {
                            try {
                                Thread.sleep(5);
                            } catch (Exception e) {

                            }
                            String buf = cmd1.getResult();
                            //do something}

                            Log.e("XINHAO_HAN", "run: " + buf);
                        }

                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTitle.setText(UUtils.getString(R.string.恢复完成会自定退出擦除));

                            }
                        });

                        ExeCommand cmd2 = new ExeCommand(false).run(mFileHome.getAbsolutePath() + " rm -rf " + mFile13.getAbsolutePath(), 60000);

                        while (cmd2.isRunning()) {
                            try {
                                Thread.sleep(5);
                            } catch (Exception e) {

                            }
                            String buf = cmd2.getResult();
                            //do something}

                            Log.e("XINHAO_HAN", "run: " + buf);
                        }

                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTitle.setText(UUtils.getString(R.string.恢复完成会自定退出写入));

                            }
                        });


                        Runtime.getRuntime().exec("chmod 777 " + mFileHome.getAbsolutePath());


                        ExeCommand cmdd = new ExeCommand(false).run(mFileHome.getAbsolutePath() + "  gunzip -c " + file.getAbsolutePath() + " > ./data/data/com.termux/xinhao.tar", Integer.MAX_VALUE);
                        // cmd = new ExeCommand(false).run(mFileHome.getAbsolutePath() + "  tar -xvf " + file.getAbsolutePath(), 60000);

                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mTitle.setText(UUtils.getString(R.string.恢复完成会自定退出写展开));

                            }
                        });

                        while (cmdd.isRunning()) {
                            try {
                                Thread.sleep(5);
                            } catch (Exception e) {

                            }
                            String buf = cmdd.getResult();
                            //do something

                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (!buf.equals("")) {
                                        SpannableString spannableString = new SpannableString(UUtils.getString(R.string.恢复完成会自定退出写恢复) + buf);
                                        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                        mTitle.setText(spannableString);

                                    }
                                }
                            });

                            Log.e("XINHAO_CMD", "onClick: " + buf);

                        }


                  /*      if (true) {
                            return;
                        }*/

                        ExeCommand cmd;

                        cmd = new ExeCommand(false).run(mFileHome.getAbsolutePath() + "  tar -xvf /data/data/com.termux/xinhao.tar", Integer.MAX_VALUE);
                        // cmd = new ExeCommand(false).run(mFileHome.getAbsolutePath() + "  tar -xvf " + file.getAbsolutePath(), 60000);


                        while (cmd.isRunning()) {
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {

                            }
                            String buf = cmd.getResult();
                            //do something

                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (!buf.equals("")) {
                                        SpannableString spannableString = new SpannableString(UUtils.getString(R.string.恢复完成会自定退出写恢复112) + buf);
                                        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 64, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                        mTitle.setText(spannableString);

                                    }
                                }
                            });

                            Log.e("XINHAO_CMD", "onClick: " + buf);

                        }

                        ///data/data/com.termux/xinhao.tar

                        ExeCommand cmde = new ExeCommand(false).run(mFileHome.getAbsolutePath() + " rm -rf /data/data/com.termux/xinhao.tar", 60000);

                        while (cmde.isRunning()) {
                            try {
                                Thread.sleep(5);
                            } catch (Exception e) {

                            }
                            String buf = cmde.getResult();
                            //do something}

                            Log.e("XINHAO_HAN", "run: " + buf);
                        }


                        // mFileHomeTar.delete();
                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(TermuxApplication.mContext, UUtils.getString(R.string.恢复完成请重启APP), Toast.LENGTH_SHORT).show();

                                try {
                                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                                    ab.setTitle(UUtils.getString(R.string.恢复成功如果很短));
                                    ab.setMessage(UUtils.getString(R.string.你必须重启APP才能使用新的恢复系统));
                                    ab.setPositiveButton(UUtils.getString(R.string.我知道了), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = new Intent(getActivity(), TermuxService.class);
                                            intent.setAction(TermuxService.ACTION_STOP_SERVICE);
                                            getActivity().startService(intent);
                                            ab.create().dismiss();
                                            getActivity().finish();


                                        }
                                    });
                                    ab.show();
                                }catch (Exception e){
                                    Toast.makeText(TermuxApplication.mContext, UUtils.getString(R.string.出现了一个错误eed), Toast.LENGTH_SHORT).show();

                                }


                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("XINHAO_HAN", "run: " + e.toString());
                    }
                }
            }).start();

        } else {

            Toast.makeText(getContext(), "使用内置termux解压", Toast.LENGTH_SHORT).show();

            if (mFileHomeMain.exists()) {

                mTitle.setText("恢复完成会自定退出!!!!!!!!不要说恢复着闪退之类的!!恢复完成自动退出  \n\n选择一个恢复文件开始启动您的恢复\n\n不要退出该页面,否则导致恢复失败!\n\n恢复文件请放在[sdcard -> xinhao/data/]目录下 \n\n 请等到恢复完成,直至恢复退出!\n\n[正在检查环境...]\n\n");


            } else {
                SpannableString spannableString = new SpannableString("恢复完成会自定退出!!!!!!!!不要说恢复着闪退之类的!!备份完成自动退出  \n\n选择一个恢复文件开始启动您的备份\n\n不要退出该页面,否则导致备份失败!\n\n恢复文件请放在[sdcard -> xinhao/data/]目录下 \n\n 请等到备份完成,直至恢复退出!\n\n  [环境异常,请在主termux中,执行: pkg in tar]\n\n");
                spannableString.setSpan(new ForegroundColorSpan(Color.RED), spannableString.length() - 35, spannableString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mTitle.setText(spannableString);
                TermuxActivity.mTerminalView.sendTextToTerminal("pkg in tar \n");

                BackNewActivity.mIsRun = false;

                return;
            }

//            getActivity().startActivity(new Intent(getActivity(), TermuxCoreService.class));


            new Thread(new Runnable() {
                @Override
                public void run() {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CoreLinux.runCoreLinux();
                        }
                    });

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // CoreLinux.runCmd("termux-setup-storage \n");
                            TermuxActivity.mTerminalView.sendTextToTerminal("termux-setup-storage \n");
                        }
                    });


                    Log.e("XINHAO_HAN", "run: " + "执行1");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    while (CoreLinux.getIsRun()) {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                SpannableString spannableString = new SpannableString("恢复完成会自定退出!!!!!!!!不要说恢复着闪退之类的!!备份完成自动退出  \n\n选择一个恢复文件开始启动您的备份\n\n不要退出该页面,否则导致备份失败!\n\n恢复文件请放在[sdcard -> xinhao/data/]目录下 \n\n 请等到备份完成,直至恢复退出!\n\n[正在恢复...]\n\n" + CoreLinux.getText());
                                spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                mTitle.setText(spannableString);
                            }
                        });
                    }

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("XINHAO_HAN", "run: " + "执行2");

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ///data/data/com.termux/files/home/storage/shared/xinhao/data
                            CoreLinux.runCmd("tar xvf  " + new File("/data/data/com.termux/files/home/storage/shared/xinhao/data/" + file.getName()).getAbsolutePath() + " -C ../../ \n");
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Log.e("XINHAO_HAN", "run: " + "执行3");
                    while (CoreLinux.getIsRun()) {
                        try {
                            Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                SpannableString spannableString = new SpannableString("恢复完成会自定退出!!!!!!!!不要说恢复着闪退之类的!!备份完成自动退出  \n\n选择一个恢复文件开始启动您的备份\n\n不要退出该页面,否则导致备份失败!\n\n恢复文件请放在[sdcard -> xinhao/data/]目录下 \n\n 请等到备份完成,直至恢复退出!\n\n[正在恢复...]\n\n" + CoreLinux.getText());
                                spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                                mTitle.setText(spannableString);
                            }
                        });
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.e("XINHAO_HAN", "run: " + "执行4");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SpannableString spannableString = new SpannableString("恢复完成会自定退出!!!!!!!!不要说恢复着闪退之类的!!备份完成自动退出  \n\n选择一个恢复文件开始启动您的备份\n\n不要退出该页面,否则导致备份失败!\n\n恢复文件请放在[sdcard -> xinhao/data/]目录下 \n\n 请等到备份完成,直至恢复退出!\n\n[正在处理最后的工作,请稍等!]\n\n");
                            spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 38, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            mTitle.setText(spannableString);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    ExeCommand cmd2 = new ExeCommand(false).run(mFileHome.getAbsolutePath() + " rm -rf " + mFileHomeFiles.getAbsolutePath(), 60000);
                    Log.e("XINHAO_HAN", "run: " + "执行5");
                    while (cmd2.isRunning()) {
                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {

                        }
                        String buf = cmd2.getResult();
                        //do something}

                        Log.e("XINHAO_HAN", "run: " + buf);
                    }
                    ExeCommand cmd3 = new ExeCommand(false).run(mFileHome.getAbsolutePath() + "mv /data/data/com.termux/data/data/com.termux/*  /data/data/com.termux/ ", 60000);
                    Log.e("XINHAO_HAN", "run: " + "执行6");
                    while (cmd3.isRunning()) {
                        try {
                            Thread.sleep(5);
                        } catch (Exception e) {

                        }
                        String buf = cmd2.getResult();
                        //do something}

                        Log.e("XINHAO_HAN", "run: " + buf);

                    }

                }
            }).start();


            //  Log.e("XINHAO_HAN_TEXT", "start: " + text );


        }


    }


    private void test1(File files) {

        Log.e("XINHAO_HAN_FILE", "start: " + "执行2");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    XHTarUtils.unTarGZ(files.getAbsoluteFile(), mFileHomeFiles.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();


    }


    private void test(File files) {


        new Thread(new Runnable() {
            @Override
            public void run() {


                writerFile("arm_64/tar", mFileHomeMainTar);


                try {
                    Runtime.getRuntime().exec("chmod 777 " + mFileHomeMainTar.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.e("XINHAO_HAN", "run: " + mFileHomeMainTar.getAbsolutePath() + " -xvf " + files.getAbsolutePath());
                ExeCommand cmd1 = new ExeCommand(false).run(mFileHomeMainTar.getAbsolutePath() + " -xvf " + files.getAbsolutePath(), 60000);

                while (cmd1.isRunning()) {
                    try {
                        Thread.sleep(5);
                    } catch (Exception e) {

                    }
                    String buf = cmd1.getResult();
                    //do something}

                    Log.e("XINHAO_HAN", "run: " + buf);
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

            Log.e("XINHAO_HAN_FILE ", "writerFile: " + e.toString());
        }

    }


}
