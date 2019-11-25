package main.java.com.termux.fragment;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.termux.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import main.java.com.termux.app.TermuxInstaller;
import main.java.com.termux.utils.ExeCommand;
import main.java.com.termux.utils.SaveData;

public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private File mFileHome = new File("/data/data/com.termux/busybox");
    private File mFileHomeData = new File("/data/data/com.termux/files/home/run.xinhao");

    private LinearLayout mDefLinear;
    private LinearLayout mTermuxLinear;

    private CheckBox mDefCheckBox;

    private CheckBox mTermuxCheckBox;

    private ProgressBar mPro;

    @Override
    public View getFragmentView() {
        return View.inflate(getContext(), R.layout.fragment_setting_s, null);
    }

    @Override
    public void initFragmentView(View mView) {

        mDefLinear = (LinearLayout) findViewById(R.id.def_linear);
        mDefCheckBox = (CheckBox) findViewById(R.id.def_checkbox);

        mDefLinear.setOnClickListener(this);
        mDefCheckBox.setOnClickListener(this);


        mTermuxLinear = (LinearLayout) findViewById(R.id.termux_linear);
        mTermuxCheckBox = (CheckBox) findViewById(R.id.termux_checkbox);

        mTermuxLinear.setOnClickListener(this);
        mTermuxCheckBox.setOnClickListener(this);


        mPro = (ProgressBar) findViewById(R.id.pro);

        String res_files = SaveData.getData("res_files");

        if ("def".equals(res_files)) {
            mTermuxCheckBox.setChecked(false);
            mDefCheckBox.setChecked(true);
        } else {
            mTermuxCheckBox.setChecked(true);
            mDefCheckBox.setChecked(false);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.def_linear:
            case R.id.def_checkbox:

                mTermuxCheckBox.setChecked(false);
                mDefCheckBox.setChecked(true);

                SaveData.saveData("res_files", "def");
                break;
            case R.id.termux_linear:
            case R.id.termux_checkbox:

                if (true) {
                    Toast.makeText(getContext(), "因安全问题已被禁用!", Toast.LENGTH_SHORT).show();
                    return;
                }

                switchPath(1);
                mTermuxCheckBox.setChecked(true);
                mDefCheckBox.setChecked(false);
                SaveData.saveData("res_files", "1");
                break;
        }

    }


    private void switchPath(int index) {

        switch (index) {

            case 0:
                Toast.makeText(getActivity(), "您选择了默认方案[全平台]!", Toast.LENGTH_SHORT).show();
                break;
            case 1:

                String cpu = TermuxInstaller.determineTermuxArchName();

                switch (cpu) {
                    case "aarch64":
                        writerFile("arm_64/busybox", mFileHome, 1024);
                        break;
                    case "arm":
                        writerFile("arm/busybox", mFileHome, 1024);

                        break;
                    case "x86_64":
                        writerFile("x86/busybox", mFileHome, 1024);
                        break;
                }

                try {
                    Runtime.getRuntime().exec("chmod 777 " + mFileHome.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mPro.setVisibility(View.VISIBLE);
                                Toast.makeText(getContext(), "正在更新环境,再次期间不要做任何操作...", Toast.LENGTH_SHORT).show();
                            }
                        });
                        writerFile("xinhao.img", mFileHomeData);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "正在更新环境,再次期间不要做任何操作...", Toast.LENGTH_SHORT).show();
                            }
                        });

                        ExeCommand run = new ExeCommand(false).run(mFileHome.getAbsolutePath() + "  tar -xzvf " + mFileHomeData, 60000);


                        while (run.isRunning()) {
                            try {
                                Thread.sleep(5);
                            } catch (Exception e) {

                            }
                            String buf = run.getResult();

                            Log.e("XINHAO_HAN", "run: " + buf);
                            //do something}
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), "环境更新完成!", Toast.LENGTH_SHORT).show();
                                mPro.setVisibility(View.GONE);
                            }
                        });


                    }
                }).start();


                break;


        }

    }

    //写出文件
    private void writerFile(String name, File mFile) {

        try {
            InputStream open = getActivity().getAssets().open(name);

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

            Log.e("XINHAO_HAN", "写出错误!: " + e);
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
