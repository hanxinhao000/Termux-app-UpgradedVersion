package main.java.com.termux.app;

import androidx.appcompat.app.AppCompatActivity;

import main.java.com.termux.datat.TermuxData;

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
            textStartCommd.setText("如何使用开机命令[不可用于耗时操作的启动命令例如:pkg update]?\n 命令之间用 && 隔开,如果一定要使用耗时操作:命令 &结尾\n比如我开机想执行ls cd .. ssh mysqld(启动mysql的) \n命令就是：ls && cd .. && ssh && mysqld \n切记最后不要加&&\n正确示例: ls && cd && ssh && mysqld\n错误示例：ls && cd && ssh && mysqld &&\n当只有一条命令时:直接写那行命令就行，不用写&&\n启动命令如果输入错误，系统将会忽略掉所有的启动命令");
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
