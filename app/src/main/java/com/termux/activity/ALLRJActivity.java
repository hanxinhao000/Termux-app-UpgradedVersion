package main.java.com.termux.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.termux.R;

import java.util.ArrayList;
import java.util.Arrays;

import main.java.com.termux.adapter.YDFY_Adapter;
import main.java.com.termux.app.TermuxActivity;
import main.java.com.termux.datat.FYChecaBean;
import main.java.com.termux.utils.SaveData;
import main.java.com.termux.utils.YDFY_Utils;
import main.java.com.termux.view.MyDialog;

public class ALLRJActivity extends AppCompatActivity {


    private ListView sech_list;
    private String text;
    private MyDialog myDialog;

    private ArrayList<String> arrayList;
    private ArrayList<String> arrayListFY;
    private ArrayList<String> arrayListFYCatch;


    private ImageView sousuo;

    private ImageView gengxin;

    private EditText edit;

    private boolean is_cache = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allrj);

        sech_list = findViewById(R.id.sech_list);

        arrayListFYCatch = new ArrayList<>();

        gengxin = findViewById(R.id.gengxin);
        sousuo = findViewById(R.id.sousuo);
        edit = findViewById(R.id.edit);

        gengxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ab = new AlertDialog.Builder(ALLRJActivity.this);

                ab.setTitle("更新");

                ab.setMessage("你确定要更新所有包目录吗?");

                ab.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                        start();
                    }
                });

                ab.setNeutralButton("稍后", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });
                ab.show();

            }
        });

        arrayListFY = new ArrayList<>();

        myDialog = new MyDialog(this);

        String cache = SaveData.getData("cache");


        if (cache.equals("def")) {
            start();
        } else {

            try {
                FYChecaBean fyChecaBean = new Gson().fromJson(cache, FYChecaBean.class);
                arrayListFY = fyChecaBean.arrayListFY;
                setAdapter();
            } catch (Exception e) {
                start();
            }
        }


        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                arrayListFYCatch.clear();
                if (s == null || s.toString().isEmpty()) {

                    setAdapter();

                } else {

                    for (int i = 0; i < arrayListFY.size(); i++) {

                        if (arrayListFY.get(i).contains(s.toString())) {
                            arrayListFYCatch.add(arrayListFY.get(i));
                        }

                    }


                    setAdapter1();

                }


            }
        });


        sech_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (is_cache) {

                    TermuxActivity.mTerminalView.sendTextToTerminal("apt-get install " + arrayListFYCatch.get(position).split(" - ")[0] + " -y \n");
                    finish();
                } else {

                    TermuxActivity.mTerminalView.sendTextToTerminal("apt-get install " + arrayListFY.get(position).split(" - ")[0] + " -y \n");
                    finish();
                }

            }
        });

    }

    //开始查找

    private void start() {

        arrayListFYCatch.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myDialog.show();
                        myDialog.getDialog_title().setText("正在查询软件包请稍后....");
                        myDialog.getDialog_pro_prog().setMax(100);
                        myDialog.getDialog_pro_prog().setProgress(20);
                        TermuxActivity.mTerminalView.sendTextToTerminal("clear \n");
                        TermuxActivity.mTerminalView.sendTextToTerminal("apt-cache search a \n");
                    }
                });

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text = TermuxActivity.mTerminalView.getText1().toString();
                        Log.e("XINHAO_HAN_TEXT", "run: " + text);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (text == null || text.isEmpty()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myDialog.dismiss();
                            Toast.makeText(ALLRJActivity.this, "没有查询到任何数据!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    return;
                }

                String[] split = text.split("\n");

                Log.e("XINHAO_HAN_TEXT", "run: " + Arrays.toString(split));

                arrayList = new ArrayList<String>(Arrays.asList(split));


                startFY(0, arrayList.size() - 1);
                //startFY1(0, arrayList.size() - 1);
                //startFY2(position, arrayList.size() - 1);
               // startFY3(position, arrayList.size() - 1);
               // startFY4(position, arrayList.size() - 1);

            }
        }).start();


    }


    private int position = 0;

    private void startFY(int position, int size) {



        if (position <= size) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myDialog.getDialog_title().setText("当前数据量可能很大,请耐心等待... ");
                    myDialog.getDialog_pro().setText("正在处理第:" + position + "/" + size);
                    myDialog.getDialog_pro_prog().setMax(size);
                    myDialog.getDialog_pro_prog().setProgress(position);
                }
            });

            try {

                String s = arrayList.get(position);

                String[] split = s.split(" - ");

                YDFY_Utils.eTozh(split[1], new YDFY_Utils.FYListener() {
                    @Override
                    public void onResponse(String s) {
                        arrayListFY.add(split[0] + " - " + s);
                        startFY(position + 1, size);
                    }

                    @Override
                    public void onFailure(String s) {
                        arrayListFY.add(split[0] + " - " + s);
                        startFY(position + 1, size);
                    }
                });


            } catch (Exception e) {
                arrayListFY.add(arrayList.get(position));
                startFY(position + 1, size);
            }


        } else {

            arrayListFY.remove(0);
            arrayListFY.remove(arrayListFY.size() - 1);
            Log.e("XINHAO_HAN_TEXT", "startFY: " + arrayListFY.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setAdapter();
                }
            });
        }


    }


    private void startFY2(int position, int size) {
        if (size >= 1000) {
            if (position >= 1000) {
                this.position = position;
                return;
            }
        }

        if (position <= size) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myDialog.getDialog_title().setText("当前数据量可能很大,请耐心等待... ");
                    myDialog.getDialog_pro().setText("正在处理第:" + position + "/" + size);
                    myDialog.getDialog_pro_prog().setMax(size);
                    myDialog.getDialog_pro_prog().setProgress(position);
                }
            });

            try {

                String s = arrayList.get(position);

                String[] split = s.split(" - ");

                YDFY_Utils.eTozh(split[1], new YDFY_Utils.FYListener() {
                    @Override
                    public void onResponse(String s) {
                        arrayListFY.add(split[0] + " - " + s);
                        startFY(position + 1, size);
                    }

                    @Override
                    public void onFailure(String s) {
                        arrayListFY.add(split[0] + " - " + s);
                        startFY(position + 1, size);
                    }
                });


            } catch (Exception e) {
                arrayListFY.add(arrayList.get(position));
                startFY(position + 1, size);
            }


        } else {

            arrayListFY.remove(0);
            arrayListFY.remove(arrayListFY.size() - 1);
            Log.e("XINHAO_HAN_TEXT", "startFY: " + arrayListFY.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setAdapter();
                }
            });
        }

    }


    private void startFY3(int position, int size) {
        if (size >= 1500) {
            if (position >= 1500) {
                this.position = position;
                return;
            }
        }

        if (position <= size) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myDialog.getDialog_title().setText("当前数据量可能很大,请耐心等待... ");
                    myDialog.getDialog_pro().setText("正在处理第:" + position + "/" + size);
                    myDialog.getDialog_pro_prog().setMax(size);
                    myDialog.getDialog_pro_prog().setProgress(position);
                }
            });

            try {

                String s = arrayList.get(position);

                String[] split = s.split(" - ");

                YDFY_Utils.eTozh(split[1], new YDFY_Utils.FYListener() {
                    @Override
                    public void onResponse(String s) {
                        arrayListFY.add(split[0] + " - " + s);
                        startFY(position + 1, size);
                    }

                    @Override
                    public void onFailure(String s) {
                        arrayListFY.add(split[0] + " - " + s);
                        startFY(position + 1, size);
                    }
                });


            } catch (Exception e) {
                arrayListFY.add(arrayList.get(position));
                startFY(position + 1, size);
            }


        } else {

            arrayListFY.remove(0);
            arrayListFY.remove(arrayListFY.size() - 1);
            Log.e("XINHAO_HAN_TEXT", "startFY: " + arrayListFY.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setAdapter();
                }
            });
        }

    }




    private void startFY4(int position, int size) {
        if (size >= 2000) {
            if (position >= 2000) {
                this.position = position;
                return;
            }
        }

        if (position <= size) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myDialog.getDialog_title().setText("当前数据量可能很大,请耐心等待... ");
                    myDialog.getDialog_pro().setText("正在处理第:" + position + "/" + size);
                    myDialog.getDialog_pro_prog().setMax(size);
                    myDialog.getDialog_pro_prog().setProgress(position);
                }
            });

            try {

                String s = arrayList.get(position);

                String[] split = s.split(" - ");

                YDFY_Utils.eTozh(split[1], new YDFY_Utils.FYListener() {
                    @Override
                    public void onResponse(String s) {
                        arrayListFY.add(split[0] + " - " + s);
                        startFY(position + 1, size);
                    }

                    @Override
                    public void onFailure(String s) {
                        arrayListFY.add(split[0] + " - " + s);
                        startFY(position + 1, size);
                    }
                });


            } catch (Exception e) {
                arrayListFY.add(arrayList.get(position));
                startFY(position + 1, size);
            }


        } else {

            arrayListFY.remove(0);
            arrayListFY.remove(arrayListFY.size() - 1);
            Log.e("XINHAO_HAN_TEXT", "startFY: " + arrayListFY.toString());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setAdapter();
                }
            });
        }

    }


    private void setAdapter() {

        myDialog.dismiss();
        is_cache = false;

        TermuxActivity.mTerminalView.sendTextToTerminal("clear \n");

        sech_list.setAdapter(new YDFY_Adapter(arrayListFY));


        FYChecaBean fyChecaBean = new FYChecaBean();
        fyChecaBean.arrayListFY = arrayListFY;

        fyChecaBean.title = "缓存1";

        SaveData.saveData("cache", new Gson().toJson(fyChecaBean));

    }


    private void setAdapter1() {

        // myDialog.dismiss();

        is_cache = true;
        sech_list.setAdapter(new YDFY_Adapter(arrayListFYCatch));


    }

}
