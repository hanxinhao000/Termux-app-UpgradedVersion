package main.java.com.termux.activity;

import android.os.Bundle;
import android.system.Os;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.termux.R;

import java.io.File;

public class RepairActivity extends AppCompatActivity {

    private ImageView start;
    private ImageView ico;
    private TextView name;
    private ProgressBar pro;

    private TextView msg;

    private File mFile12 = new File("/data/data/com.termux/files/usr");

    //
    private int size = 0;
    private int temp = 0;
    private int error = 0;

    private EditText edit_chmod;
    private String edit_chmodString;
    private Runtime runtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);
        start = findViewById(R.id.start);
        ico = findViewById(R.id.ico);
        pro = findViewById(R.id.pro);
        msg = findViewById(R.id.msg);
        name = findViewById(R.id.name);
        edit_chmod = findViewById(R.id.edit_chmod);

        runtime = Runtime.getRuntime();
        main();


    }

    //开始修复
    private void main() {

        //启动动画
        startAnim();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                edit_chmodString = edit_chmod.getText().toString();

                if (edit_chmodString == null || edit_chmodString.isEmpty()) {
                    Toast.makeText(RepairActivity.this, "权限不能为空!", Toast.LENGTH_SHORT).show();
                    return;
                }

                edit_chmod.setVisibility(View.GONE);

                start.setVisibility(View.GONE);
                name.setText("正在修复,如果修复不成功，请尝试暴力修复");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        size = 0;
                        error = 0;
                        temp = 0;
                        recursiveFiles(mFile12.getAbsolutePath());
                        chmodRecursiveFiles(mFile12.getAbsolutePath());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(RepairActivity.this, "修复完成!必须杀掉APP，重启，才能看见效果，如果还是没好，那就尝试暴力修复", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        });
                    }
                }).start();
                //先遍历一遍

            }
        });


    }


    //遍历文件


    //动画
    private void startAnim() {

        final int[] i = {0};
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {


                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    i[0]++;
                    if (i[0] > 360) {
                        i[0] = 0;
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ico.setRotationY(i[0]);

                        }
                    });
                }


            }
        }).start();

    }


    //递归获取目录文件
    public void recursiveFiles(String path) {

        // 创建 File对象
        File file = new File(path);


        // 取 文件/文件夹
        File[] files = file.listFiles();


        // 对象为空 直接返回
        if (files == null) {

            return;
        }

        // 目录下文件
        if (files.length == 0) {
            System.out.println(path + "该文件夹下没有文件");
        }

        // 存在文件 遍历 判断
        for (File f : files) {
            size++;
            // 判断是否为 文件夹
            if (f.isDirectory()) {
                // 为 文件夹继续遍历
                recursiveFiles(f.getAbsolutePath());
                // 判断是否为 文件
            } else if (f.isFile()) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg.setText("正在扫描[" + size + "]个文件");
                    }
                });

            } else {

            }

        }

    }


    //递归获取目录文件
    public void chmodRecursiveFiles(String path) {

        // 创建 File对象
        File file = new File(path);


        // 取 文件/文件夹
        File[] files = file.listFiles();


        // 对象为空 直接返回
        if (files == null) {

            return;
        }

        // 目录下文件
        if (files.length == 0) {
            System.out.println(path + "该文件夹下没有文件");
        }

        // 存在文件 遍历 判断
        for (File f : files) {
            temp++;
            // 判断是否为 文件夹
            if (f.isDirectory()) {
                // 为 文件夹继续遍历
                chmodRecursiveFiles(f.getAbsolutePath());
                // 判断是否为 文件
            } else if (f.isFile()) {

                try {


                      //Os.chmod(f.getAbsolutePath(), Integer.parseInt(edit_chmodString));
                    runtime.exec("chmod " + edit_chmodString + " " + f.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                    error++;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        msg.setText("已修复[" + temp + "/" + size + "]个文件\n修复错误[" + error + "]\n权限为:[" + edit_chmodString + "]");
                        pro.setMax(size);
                        pro.setProgress(temp);
                    }
                });


            } else {

            }

        }

    }


}

