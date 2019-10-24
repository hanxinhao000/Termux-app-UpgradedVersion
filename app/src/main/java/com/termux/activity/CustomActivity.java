package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.termux.R;

import java.io.File;

import main.java.com.termux.datat.TermuxData;
import main.java.com.termux.utils.SaveData;

public class CustomActivity extends AppCompatActivity {


    private RelativeLayout guanfang_key;
    private RelativeLayout termux_key;
    private RelativeLayout ziding_key;
    private RelativeLayout ziding_key_you;
    private RelativeLayout ziding_key_def;


    private File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/config/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        guanfang_key = findViewById(R.id.guanfang_key);
        termux_key = findViewById(R.id.termux_key);
        ziding_key = findViewById(R.id.ziding_key);
        ziding_key_you = findViewById(R.id.ziding_key_you);
        ziding_key_def = findViewById(R.id.ziding_key_def);

        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();

            if (!mkdirs) {
                Toast.makeText(this, "请检查你的sd卡权限", Toast.LENGTH_SHORT).show();
            }
        }

        guanfang_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData.saveData("key_box", "[['ESC', 'TAB', 'CTRL', 'ALT', '-', 'DOWN', 'UP']]");
                Toast.makeText(CustomActivity.this, "更改官方布局成功!等下次启动之后就会更改布局了", Toast.LENGTH_SHORT).show();

            }
        });
        termux_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData.saveData("key_box", "[['ESC', 'TAB', 'CTRL', 'ALT', '-', 'UP', 'ENTER'],['INS', 'END','SHIFT',':', 'LEFT', 'DOWN', 'RIGHT']]");
                Toast.makeText(CustomActivity.this, "更改魔改布局成功!等下次启动之后就会更改布局了", Toast.LENGTH_SHORT).show();

            }
        });
        ziding_key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (file.exists()) {
                    TermuxData.getInstall().config = 0;
                    startActivity(new Intent(CustomActivity.this, KeyConfigActivity.class));
                } else {
                    Toast.makeText(CustomActivity.this, "目录[/xinhao/config]不存在!", Toast.LENGTH_SHORT).show();
                }

            }
        });


        ziding_key_you.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                if (file.exists()) {
                    TermuxData.getInstall().config = 1;
                    startActivity(new Intent(CustomActivity.this, KeyConfigActivity.class));
                } else {
                    Toast.makeText(CustomActivity.this, "目录[/xinhao/config]不存在!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        ziding_key_def.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveData.saveData("key_box_r", "def");
                Toast.makeText(CustomActivity.this, "更改配置文件布局成功!等下次启动之后就会更改布局了", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
