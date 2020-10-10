package main.java.com.termux.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.termux.R;
import com.termux.view.PromptMsg;

public class PromptSettingActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PromptMsg mPromptMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prompt_setting);
        recyclerView = findViewById(R.id.recyclerView);
        mPromptMsg = PromptMsg.getInstall();
    }



}
