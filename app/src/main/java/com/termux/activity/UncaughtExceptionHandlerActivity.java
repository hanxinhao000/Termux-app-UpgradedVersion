package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.termux.R;

public class UncaughtExceptionHandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncaught_exception_handler);


        String error = getIntent().getStringExtra("error");


        EditText viewById = findViewById(R.id.error_text);

        viewById.setText(viewById.getText().toString() + "\n\n" + error);

    }
}
