package main.java.com.termux.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.termux.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import main.java.com.termux.app.EditTextActivity;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.app.TestActivity;
import main.java.com.termux.datat.TermuxData;
import main.java.com.termux.utils.SaveData;

public class FunctionActivity extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private RelativeLayout welcome_String;

    private RelativeLayout list_main;

    private RelativeLayout list_qinghua;

    private RelativeLayout list_String;
    private RelativeLayout install_userland;
    private RelativeLayout quanxian_xiufu;
    private RelativeLayout start_commd;

    private ImageView red;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_function);

        relativeLayout = findViewById(R.id.sms);
        welcome_String = findViewById(R.id.welcome_String);
        list_String = findViewById(R.id.list_String);
        list_main = findViewById(R.id.list_main);
        list_qinghua = findViewById(R.id.list_qinghua);
        install_userland = findViewById(R.id.install_userland);
        quanxian_xiufu = findViewById(R.id.quanxian_xiufu);

        start_commd = findViewById(R.id.start_commd);

        red = findViewById(R.id.red);

        String redD = SaveData.getData("redD");

        if (redD.equals("red")) {
            red.setVisibility(View.GONE);
        }

        start_commd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveData.saveData("redD", "red");
                red.setVisibility(View.GONE);

                TermuxData.getInstall().fileUrl = "/data/data/com.termux/files/home/BootCommand";

                startActivity(new Intent(FunctionActivity.this, EditTextActivity.class));

            }
        });

        quanxian_xiufu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FunctionActivity.this, RepairActivity.class));
            }
        });


        list_main.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SdCardPath")
            @Override
            public void onClick(View v) {

                //sources_main.list
                writerFile("sources_main.list", new File("/data/data/com.termux/files/usr/etc/apt/sources.list"));

                Toast.makeText(FunctionActivity.this, "切换到官方源成功!", Toast.LENGTH_SHORT).show();
            }
        });

        install_userland.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(FunctionActivity.this, TestActivity.class));

            }
        });

        list_qinghua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writerFile("sources_qh.list", new File("/data/data/com.termux/files/usr/etc/apt/sources.list"));
                Toast.makeText(FunctionActivity.this, "切换到清华源成功!", Toast.LENGTH_SHORT).show();
            }
        });

        list_String.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermuxData.getInstall().fileUrl = "/data/data/com.termux/files/usr/etc/apt/sources.list";
                startActivity(new Intent(FunctionActivity.this, EditTextActivity.class));
            }
        });

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FunctionActivity.this, "生成成功!你现在可以使用[XINHAO_HAN_Sms]命令来获取你的短信了", Toast.LENGTH_SHORT).show();
            }
        });

        welcome_String.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TermuxData.getInstall().fileUrl = "/data/data/com.termux/files/usr/etc/motd";
                startActivity(new Intent(FunctionActivity.this, EditTextActivity.class));
            }
        });
    }


    //写出文件
    private void writerFile(String name, File mFile) {

        try {
            InputStream open = getAssets().open(name);

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
