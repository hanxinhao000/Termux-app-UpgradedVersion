package main.java.com.termux.activity;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.termux.R;

import java.io.File;
import java.io.IOException;


public class DownloadActivity extends AppCompatActivity {


    File file = new File(Environment.getExternalStorageDirectory() + "/xinhao/maoxian.zip");

    private static final String TAG = "XINHAO_HAN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }


}
