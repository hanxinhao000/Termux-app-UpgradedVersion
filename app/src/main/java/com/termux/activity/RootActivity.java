package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.termux.R;

import java.math.BigInteger;

public class RootActivity extends AppCompatActivity {


    private LinearLayout ubuntu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        ubuntu = findViewById(R.id.ubuntu);

        ubuntu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RootActivity.this, "功能正在测试中,请耐心等待后几个版本...", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
