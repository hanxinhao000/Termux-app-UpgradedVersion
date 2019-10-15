package main.java.com.termux.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.termux.R;

import androidx.annotation.Nullable;

import main.java.com.termux.activity.SwitchActivity;
import main.java.com.termux.datat.TermuxData;
import main.java.com.termux.filemanage.filemanager.FileManagerActivity;

public class SettingActivity extends Activity {

    private LinearLayout backup;

    private LinearLayout restore;

    private LinearLayout file_text;

    private LinearLayout switch_text;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        backup = findViewById(R.id.backup);

        restore = findViewById(R.id.restore);

        switch_text = findViewById(R.id.switch_text);

        file_text = findViewById(R.id.file_text);
        backup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TermuxData.getInstall().isB_R = 0;
                startActivity(new Intent(SettingActivity.this, BackupActivity.class));
            }
        });

        restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(SettingActivity.this, RestoreActivity.class));

            }
        });

        file_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SettingActivity.this, FileManagerActivity.class);
                Uri build = new Uri.Builder().path("/data/data/com.termux/files").build();
                intent.setData(build);
                startActivity(intent);
            }
        });

        switch_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingActivity.this, SwitchActivity.class));
            }
        });
    }
}
