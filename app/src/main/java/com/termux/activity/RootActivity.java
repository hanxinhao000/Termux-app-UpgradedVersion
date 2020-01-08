package main.java.com.termux.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.termux.R;

import java.io.File;

import main.java.com.termux.linux_deploy.UpdateEnvTask;

public class RootActivity extends AppCompatActivity {

    private File mFile = new File("/data/data/com.termux/files/include/bootstrap");
    private LinearLayout ubuntu;
    private TextView mTitle;
    private LinearLayout ubuntu_system;
    private LinearLayout kail_system;
    private LinearLayout archlinux_system;
    private LinearLayout deploy_iso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ubuntu = findViewById(R.id.ubuntu);

        mTitle = findViewById(R.id.title);
        ubuntu_system = findViewById(R.id.ubuntu_system);
        kail_system = findViewById(R.id.kail_system);
        archlinux_system = findViewById(R.id.archlinux_system);
        deploy_iso = findViewById(R.id.deploy_iso);


        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        ab.setTitle("注意");
        ab.setMessage("该模块现在是测试模块，某些功能可能无法正确工作，请各位积极提交BUG【截图即可】，感谢各位!!");
        ab.setNeutralButton("好滴", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ab.create().dismiss();
            }
        });
        ab.show();


        if (mFile.exists()) {

            mTitle.setText("[root功能页面]本页面需要你手机root\n[环境监测:已安装]\n部分手机可能无法正确安装");

        } else {
            mTitle.setText("[root功能页面]本页面需要你手机root\n[环境监测:正在安装]\n部分手机可能无法正确安装");

            new UpdateEnvTask(this).execute();

            mTitle.setText("[root功能页面]本页面需要你手机root\n[环境监测:安装完成!]\n部分手机可能无法正确安装");
        }

        ubuntu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RootActivity.this, "该环境自动安装,如果出现错误请再次点击", Toast.LENGTH_SHORT).show();

                if (mFile.exists()) {

                    mTitle.setText("[root功能页面]本页面需要你手机root\n[环境监测:已安装]\n部分手机可能无法正确安装");

                } else {
                    mTitle.setText("[root功能页面]本页面需要你手机root\n[环境监测:正在安装]\n部分手机可能无法正确安装");

                    new UpdateEnvTask(RootActivity.this).execute();

                    mTitle.setText("[root功能页面]本页面需要你手机root\n[环境监测:安装完成!]\n部分手机可能无法正确安装");
                }

            }
        });

        ubuntu_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RootActivity.this, LinuxDeployInstallActivity.class);

                intent.putExtra("linux_name","ubuntu");

                startActivity(intent);
            }
        });

        kail_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootActivity.this, LinuxDeployInstallActivity.class);

                intent.putExtra("linux_name","kail");

                startActivity(intent);
            }
        });

        archlinux_system.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RootActivity.this, LinuxDeployInstallActivity.class);

                intent.putExtra("linux_name","arch");

                startActivity(intent);
            }
        });

        deploy_iso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RootActivity.this, "正在测试中..", Toast.LENGTH_SHORT).show();
            }
        });

        //


    }
}
