package main.java.com.termux.pagerview;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.termux.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.application.TermuxApplication;

public class ScriptView extends BaseViewPagerView implements View.OnClickListener {

    private File mHomeFile = new File("/data/data/com.termux/files/home/");

    private LinearLayout mScriptAll;
    private LinearLayout mScriptCmatrix;
    private LinearLayout mScriptBlessed;
    private LinearLayout mScriptAafire;
    private LinearLayout mStrem;
    private LinearLayout mToilet;

    @Override
    public View getLayoutView() {
        return View.inflate(getActivity(), R.layout.view_script, null);
    }

    @Override
    public void initView(View mView) {

        mScriptAll = mView.findViewById(R.id.script_all);

        mScriptCmatrix = mView.findViewById(R.id.script_cmatrix);

        mScriptBlessed = mView.findViewById(R.id.script_blessed);

        mScriptAafire = mView.findViewById(R.id.script_aafire);

        mStrem = mView.findViewById(R.id.strem);

        mToilet = mView.findViewById(R.id.toilet);

        mScriptAll.setOnClickListener(this);
        mScriptCmatrix.setOnClickListener(this);
        mScriptBlessed.setOnClickListener(this);
        mScriptAafire.setOnClickListener(this);
        mStrem.setOnClickListener(this);
        mToilet.setOnClickListener(this);




        mView.findViewById(R.id.bastet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt install bastet -y && bastet \n");
                getActivity().finish();
            }
        });

        mView.findViewById(R.id.ninvaders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt-get install ninvaders -y && ninvaders \n");
                getActivity().finish();
            }
        });

        mView.findViewById(R.id.pacman4console).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt-get install pacman4console -y && pacman4console \n");
                getActivity().finish();
            }
        });

        mView.findViewById(R.id.nsnake).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt-get install nsnake -y && nsnake \n");
                getActivity().finish();
            }
        });

        mView.findViewById(R.id.ycm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TermuxApplication.mContext, "功能已出数据包,请下载数据包使用", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.script_all:

                writerFile("all_tool_msf.sh", new File(mHomeFile, "all_tool_msf.sh"));

                try {
                    Runtime.getRuntime().exec("chmod 777 " + mHomeFile.getAbsolutePath() + "all_tool_msf.sh");
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "执行失败!", Toast.LENGTH_SHORT).show();
                }

                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("chmod 777 all_tool_msf.sh \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("./all_tool_msf.sh \n");
                getActivity().finish();


                break;

            case R.id.script_cmatrix:
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt-get install cmatrix -y && cmatrix \n");
                getActivity().finish();

                break;

            case R.id.script_blessed:
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("" +
                    "cd ~ && " +
                    "apt-get update &&" +
                    "apt-get install npm && " +
                    "apt install nodejs-legacy && " +
                    "apt install git && " +
                    "git clone https://github.com/yaronn/blessed-contrib.git && " +
                    "cd blessed-contrib && npm install && " +
                    "node ./examples/dashboard.js \n");
                getActivity().finish();

                break;

            case R.id.script_aafire:
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt-get install aafire -y && aafire\n");
                getActivity().finish();
                break;
            case R.id.strem:
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt-get install sl -y && sl \n");
                getActivity().finish();
                break;

            case R.id.toilet:
                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt-get install toilet -y && toilet XINHAO_HAN \n");
                getActivity().finish();
                break;

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

}
