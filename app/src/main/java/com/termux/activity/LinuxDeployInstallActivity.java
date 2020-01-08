package main.java.com.termux.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import main.java.com.termux.app.EditTextActivity;
import main.java.com.termux.app.TermuxInstaller;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.datat.TermuxData;
import main.java.com.termux.linux_deploy.EnvUtils;
import main.java.com.termux.linux_deploy.Logger;
import main.java.com.termux.linux_deploy.PrefStore;

public class LinuxDeployInstallActivity extends AppCompatActivity implements View.OnClickListener, Logger.MsgListener {


    private TextView title;
    private LinearLayout start_linux;
    private LinearLayout install_linux;
    private LinearLayout remove_linux;
    private File installFile = new File("/data/data/com.termux/files/config/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linux_deploy_install);
        title = findViewById(R.id.title);
        start_linux = findViewById(R.id.start_linux);
        install_linux = findViewById(R.id.install_linux);
        remove_linux = findViewById(R.id.remove_linux);

        String s = TermuxInstaller.determineTermuxArchName();

        if (!"aarch64".equals(s)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("错误");
            ab.setMessage("该模块是AARCH64架构专属,如果是其他架构，请手动下载LinuxDeploy");
            ab.setNeutralButton("我要强制安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();
                }
            });
            ab.show();
        }


        Logger.setMsgListener(this);


        install_linux.setOnClickListener(this);
        start_linux.setOnClickListener(this);
        remove_linux.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.install_linux:

                String linux_name = getIntent().getStringExtra("linux_name");

                if ("ubuntu".equals(linux_name)) {


                    writerFile("linux_ubuntu.conf", new File(installFile, "linux.conf"));
                    writerFile("cli.conf", new File("/data/data/com.termux/files/cli.conf"));

                    EnvUtils.execService(getApplicationContext(), "deploy", null);

                }

                if("kail".equals(linux_name)){

                    writerFile("linux_kail.conf", new File(installFile, "linux.conf"));
                    writerFile("cli.conf", new File("/data/data/com.termux/files/cli.conf"));

                    EnvUtils.execService(getApplicationContext(), "deploy", null);

                }
                if("arch".equals(linux_name)){

                    writerFile("linux_arch.conf", new File(installFile, "linux.conf"));
                    writerFile("cli.conf", new File("/data/data/com.termux/files/cli.conf"));

                    EnvUtils.execService(getApplicationContext(), "deploy", null);

                }

                break;

            case R.id.start_linux:

                Handler h = new Handler();
                if (PrefStore.isXserver(getApplicationContext())
                    && PrefStore.isXsdl(getApplicationContext())) {
                    PackageManager pm = getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage("x.org.server");
                    if (intent != null) startActivity(intent);
                    h.postDelayed(() -> EnvUtils.execService(getBaseContext(), "start", "-m"), PrefStore.getXsdlDelay(getApplicationContext()));
                } else if (PrefStore.isFramebuffer(getApplicationContext())) {
                    EnvUtils.execService(getBaseContext(), "start", "-m");
                    h.postDelayed(() -> {
                        /*Intent intent = new Intent(getApplicationContext(),
                            FullscreenActivity.class);
                        startActivity(intent);*/
                    }, 1500);
                } else {
                    EnvUtils.execService(getBaseContext(), "start", "-m");
                }


                break;

            case R.id.remove_linux:
                TermuxData.getInstall().fileUrl = new File(installFile, "linux.conf").getAbsolutePath();
                startActivity(new Intent(this, EditTextActivity.class));
                break;

        }
    }


    //写出文件
    private static void writerFile(String name, File mFile) {

        try {
            Runtime.getRuntime().exec("chmod 777 " + mFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream open = TermuxApplication.mContext.getAssets().open(name);

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


    @Override
    public void msg(String msg) {

        TermuxApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {
                title.setText(msg + "\n" + title.getText().toString());
            }
        });


    }
}
