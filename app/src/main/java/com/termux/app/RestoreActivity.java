package main.java.com.termux.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.termux.R;



import java.io.File;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import main.java.com.termux.adapter.RestoreAdapter;
import main.java.com.termux.app.BackupActivity;
import main.java.com.termux.datat.TermuxData;

public class RestoreActivity extends Activity {

    private ListView list;

    private File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/system/");


    private File dFile = new File("/data/data/com.termux/");
    private ArrayList<File> arrayList;

    private TextView no_file;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore);
        list = findViewById(R.id.list);
        arrayList = new ArrayList<>();
        no_file = findViewById(R.id.no_file);
        getFile();
    }

    //获取目录
    private void getFile() {

        if (file.listFiles() != null) {
            no_file.setVisibility(View.GONE);
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {

                arrayList.add(files[i]);


            }
        } else {
            no_file.setVisibility(View.VISIBLE);
        }


        list.setAdapter(new RestoreAdapter(arrayList));


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File file = arrayList.get(position);

                //解压zip
                // ZipUtils.unZip(file.getAbsoluteFile(),);

                TermuxData.getInstall().isB_R = 1;

                TermuxData.getInstall().mFile = file;

                startActivity(new Intent(RestoreActivity.this, BackupActivity.class));

            }
        });
    }

}
