package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;

import java.io.File;
import java.util.ArrayList;

import main.java.com.termux.adapter.RestoreAdapter;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.service.BackService;

import static main.java.com.termux.service.BackService.BACK_FILES;
import static main.java.com.termux.service.BackService.RESTORE_FILES;

public class BackRestoreActivity extends AppCompatActivity {


    private ListView list_view;

    private File mSdFile = new File(Environment.getExternalStorageDirectory(), "/xinhao/data/");

    private TextView start_re;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_restore);

        list_view = findViewById(R.id.list_view);
        start_re = findViewById(R.id.start_re);

        ArrayList<File> files = new ArrayList<>();

        File[] files1 = mSdFile.listFiles();

        if (files1 == null) {
            Toast.makeText(this, "你没有SD卡权限!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        for (int i = 0; i < files1.length; i++) {

            files.add(files1[i]);

        }

        if (files.size() == 0) {
            start_re.setText("没有SD卡权限,或者sdcard->xinhao/data/目录下没有恢复文件!");
        }

        list_view.setAdapter(new RestoreAdapter(files));


        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File file = files.get(position);

                Intent intent = new Intent(BackRestoreActivity.this, BackService.class);

                intent.setAction(RESTORE_FILES);

                intent.putExtra("res_file_url",file.getAbsolutePath());

                startService(intent);

                Toast.makeText(BackRestoreActivity.this, "后台正在恢复,请耐心等待,恢复期间你无法使用termux的任何功能!", Toast.LENGTH_LONG).show();

                finish();




            }
        });




    }



}
