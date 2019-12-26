package main.java.com.termux.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.termux.R;
import com.termux.view.TerminalView;

import main.java.com.termux.utils.VNCActivityUtils;

public class XINHAO_HANActivity extends AppCompatActivity {


    private TerminalView termux_view;

    private TextView back_res;

    private Button start_sdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xinhao__han);
        //termux_view = findViewById(R.id.termux_view);

        //BackNewActivity
        back_res = findViewById(R.id.back_res);
        back_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(XINHAO_HANActivity.this, BackNewActivity.class));
            }
        });

        start_sdl = findViewById(R.id.start_sdl);

        start_sdl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sdlIntent = VNCActivityUtils.getSDLIntent(XINHAO_HANActivity.this);

                startActivity(sdlIntent);
            }
        });
    }
}
