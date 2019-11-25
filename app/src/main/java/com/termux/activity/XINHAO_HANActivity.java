package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.termux.R;
import com.termux.view.TerminalView;

import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.app.TermuxViewClient;

public class XINHAO_HANActivity extends AppCompatActivity {


    private TerminalView termux_view;

    private TextView back_res;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xinhao__han);
        termux_view = findViewById(R.id.termux_view);

        //BackNewActivity
        back_res = findViewById(R.id.back_res);
        back_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(XINHAO_HANActivity.this, BackNewActivity.class));
            }
        });
    }
}
