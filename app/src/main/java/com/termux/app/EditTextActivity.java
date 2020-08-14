package main.java.com.termux.app;

import androidx.appcompat.app.AppCompatActivity;

import main.java.com.termux.datat.TermuxData;
import main.java.com.termux.utils.UUtils;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class EditTextActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText text;
    private ImageView save;
    private PrintWriter printWriter;
    private String textString;

    private TextView textStartCommd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);
        text = findViewById(R.id.text);
        save = findViewById(R.id.save);
        textStartCommd = findViewById(R.id.textStartCommd);
        save.setOnClickListener(this);
        String fileUrl = TermuxData.getInstall().fileUrl;

        if (fileUrl.contains("BootCommand")) {
            textStartCommd.setVisibility(View.VISIBLE);
            textStartCommd.setText(UUtils.getString(R.string.不可用于耗时操作的启动命令例如));
        }

        String txt = "";

        String temp = "";

        File file = new File(fileUrl);
        if (!file.exists()) {
            try {


                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));


            while ((temp = bufferedReader.readLine()) != null) {
                txt = txt + temp + "\n";
            }


            Log.e("XINHAO_HAN", "onCreate: " + txt);


            text.setText(txt);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            text.setText("文件加载失败!" + e.toString());
        } catch (IOException e) {
            text.setText("文件加载失败!" + e.toString());
            e.printStackTrace();
        }

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {


                textString = s.toString();

                Log.e("XINHAO_HAN", "afterTextChanged: " + textString);
            }
        });

        showKeyboard();

    }

    @Override
    public void onClick(View v) {


        if (textString == null) {
            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(new File(TermuxData.getInstall().fileUrl))));
            printWriter.print(textString);
            printWriter.flush();
            Toast.makeText(this, "成功保存!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "保存失败!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


    }

    private void showKeyboard() {
        text.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(text, 0);
    }

    @Override
    public void finish() {
        if (printWriter != null)
            printWriter.close();
        super.finish();
    }
}
