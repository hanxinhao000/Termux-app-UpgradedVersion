package main.java.com.termux.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.max2idea.android.limbo.main.Config;
import com.termux.R;

import java.io.File;

import main.java.com.termux.utils.SaveData;

public class VNCMessageActivity extends AppCompatActivity {

    private EditText adress;

    private EditText port;

    private EditText password;

    private CardView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vncmessage);

        adress = findViewById(R.id.adress);

        port = findViewById(R.id.port);

        password = findViewById(R.id.password);

        save = findViewById(R.id.save);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*SaveData.saveData("adress", adress.getText().toString());
                SaveData.saveData("port", port.getText().toString());
                SaveData.saveData("password", password.getText().toString());*/
                // Toast.makeText(VNCMessageActivity.this, "保存成功!", Toast.LENGTH_SHORT).show();
                Intent vncIntent = getVNCIntent(VNCMessageActivity.this);

                Config.cacheDir = "/data/data/com.termux";
                Config.storagedir = Environment.getExternalStorageDirectory().toString();

                File folder = new File(Config.getTmpFolder());
                if (!folder.exists())
                    folder.mkdirs();

                String s = port.getText().toString();
                if(s.isEmpty()){
                    s = "1";
                }
                int i = Integer.parseInt(s);


                if (i < 100) {
                    i += 5900;
                    vncIntent.putExtra("port", i + "");
                } else {
                    vncIntent.putExtra("port", port.getText().toString());
                }

                vncIntent.putExtra("adress", adress.getText().toString());

                vncIntent.putExtra("passwordD1", password.getText().toString());
                Bundle b = new Bundle();
                b.putInt("ui", 0);
                vncIntent.putExtras(b);
                startActivity(vncIntent);

                finish();
            }
        });
    }


    public static Intent getVNCIntent(Activity activity) {
        return new Intent(activity, com.max2idea.android.limbo.main.LimboVNCActivity.class);

    }
}
