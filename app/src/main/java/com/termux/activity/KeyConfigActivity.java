package main.java.com.termux.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.termux.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import main.java.com.termux.adapter.WindowAdapter;
import main.java.com.termux.utils.SaveData;

public class KeyConfigActivity extends AppCompatActivity {

    private File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/config/");
    private ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_config);
        list_view = findViewById(R.id.list_view);

        ArrayList<File> files = new ArrayList<File>(Arrays.asList(file.listFiles()));

        list_view.setAdapter(new WindowAdapter(files));

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //JSONArray arr = new JSONArray(props.getProperty("extra-keys", key_box));

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(files.get(position))));

                    String temp = "";
                    String content = "";

                    while ((temp = bufferedReader.readLine()) != null) {

                        content += temp;

                    }

                    new JSONArray(content);

                    SaveData.saveData("key_box", content);

                    Toast.makeText(KeyConfigActivity.this, "更改配置文件布局成功!等下次启动之后就会更改布局了", Toast.LENGTH_SHORT).show();


                } catch (Exception e) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(KeyConfigActivity.this);
                    ab.setTitle("错误!");
                    ab.setMessage(e.toString());
                    ab.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });

                    ab.show();
                }
            }
        });
    }
}
