package main.java.com.termux.linux_deploy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.termux.R;

import java.io.File;

public class LinuxdeployActivity extends AppCompatActivity {


    private TextView title;

    private LinearLayout start_linux;

    private LinearLayout install_linux;

    private LinearLayout uninstall_remove;

    private File mFile = new File("/data/data/com.termux/files/include/bootstrap");
    private File mFileConfig = new File("/data/data/com.termux/files/config");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linuxdeploy);

        title = findViewById(R.id.title);
        start_linux = findViewById(R.id.start_linux);
        install_linux = findViewById(R.id.install_linux);
        uninstall_remove = findViewById(R.id.remove_linux);

        if(mFile.exists()){

            title.setText("环境已经存在,不需要更新 \n" + title.getText().toString());

        }else{
            title.setText("正在更新环境 \n" + title.getText().toString());

            new UpdateEnvTask(this).execute();

            title.setText("环境更新完成! \n" + title.getText().toString());
        }



        uninstall_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LinuxdeployActivity.this, "经过考虑,不自带卸载本系统脚本,安全考虑,可能会影响其他系统", Toast.LENGTH_LONG).show();
            }
        });

        start_linux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab = new AlertDialog.Builder(LinuxdeployActivity.this);

                ab.setTitle("确定开始?");

                ab.setMessage("你确定要开始安装ubuntu图形系统吗？");

                ab.setPositiveButton("我确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ab.setNegativeButton("我不确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                ab.show();

                //Redmi
                //12345678

               // EnvUtils.execService(getApplicationContext(), "deploy", null);
            }
        });
    }
}
