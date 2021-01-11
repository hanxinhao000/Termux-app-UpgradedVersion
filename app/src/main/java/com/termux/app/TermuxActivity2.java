package main.java.com.termux.app;

import android.Manifest;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.system.ErrnoException;
import android.system.Os;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.autofill.AutofillManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.madrapps.pikolo.ColorPicker;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;
import com.suke.widget.SwitchButton;
import com.termux.R;
import com.termux.api.TermuxApiReceiver;
import com.termux.terminal.EmulatorDebug;
import com.termux.terminal.TerminalColors;
import com.termux.terminal.TerminalSession;
import com.termux.terminal.TerminalSession.SessionChangedCallback;
import com.termux.terminal.TextStyle;
import com.termux.view.TerminalRenderer;
import com.termux.view.TerminalView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import main.java.com.termux.activity.ALLRJActivity;
import main.java.com.termux.activity.BackNewActivity;
import main.java.com.termux.activity.BackRestoreActivity;
import main.java.com.termux.activity.CustomActivity;
import main.java.com.termux.activity.FontActivity;
import main.java.com.termux.activity.FunAddActivity;
import main.java.com.termux.activity.FunctionActivity;
import main.java.com.termux.activity.ListDataActivity;
import main.java.com.termux.activity.LunTanActivity;
import main.java.com.termux.activity.RepairActivity;
import main.java.com.termux.activity.SwitchActivity;
import main.java.com.termux.activity.ThanksActivity;
import main.java.com.termux.activity.UbuntuListActivity;
import main.java.com.termux.activity.VNCMessageActivity;
import main.java.com.termux.activity.WindowsActivity;
import main.java.com.termux.activity.XINHAO_HANActivity;
import main.java.com.termux.adapter.BoomMinLAdapter;
import main.java.com.termux.adapter.ItemSelectAdapter;
import main.java.com.termux.adapter.MinLAdapter;
import main.java.com.termux.adapter.databean.MinLBean;
import main.java.com.termux.android_cm.LauncherActivity;
import main.java.com.termux.app.dialog.BoomListDialog;
import main.java.com.termux.app.dialog.BoomWindow;
import main.java.com.termux.app.dialog.FileInstallServerListDialog;
import main.java.com.termux.app.dialog.FileListQemuDialog;
import main.java.com.termux.app.dialog.LoadingDialog;
import main.java.com.termux.app.dialog.MingLShowDialog;
import main.java.com.termux.app.dialog.MinglingDaoruDaoChuDialog;
import main.java.com.termux.app.dialog.RootfsDialog;
import main.java.com.termux.app.dialog.TextJZShowDialog;
import main.java.com.termux.app.dialog.TextShowDialog;
import main.java.com.termux.app.web.Constants;
import main.java.com.termux.app.web.WebZip;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.bean.CreateSystemBean;
import main.java.com.termux.bean.UpDateBean;
import main.java.com.termux.core.CoreLinux;
import main.java.com.termux.datat.DataBean;
import main.java.com.termux.datat.ServiceDataBean;
import main.java.com.termux.datat.TermuxData;
import main.java.com.termux.datat.UrlDataHtml;
import main.java.com.termux.filemanage.filemanager.FileManagerActivity;
import main.java.com.termux.floatwindows.TermuxFloatService;
import main.java.com.termux.http.CheckUpDateCodeUtils;
import main.java.com.termux.http.UpDateHttpCode;
import main.java.com.termux.key.KeyData;
import main.java.com.termux.listener.SmsMsgListener;
import main.java.com.termux.service.BackService;
import main.java.com.termux.utils.CustomTextView;
import main.java.com.termux.utils.SaveData;
import main.java.com.termux.utils.SmsUtils;
import main.java.com.termux.utils.SystemUtil;
import main.java.com.termux.utils.UUtils;
import main.java.com.termux.utils.WindowUtils;
import main.java.com.termux.view.MyDialog;
import main.java.com.termux.view.MyDrawerLayout;
import main.java.com.termux.view.MyHorizontalScrollView;
import main.java.com.termux.windows.RunWindowActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static main.java.com.termux.app.TermuxService.getEnvironmentPrefix;
import static main.java.com.termux.service.BackService.BACK_FILES;


public class TermuxActivity2 extends TermuxActivity {





    private CustomTextView open_server_url;
    private LinearLayout start_fanmian;
    private LinearLayout xiezai_server_df;
    private LinearLayout install_server_fwq;
    private LinearLayout download_server_fdgdfg;
    private LinearLayout moren_qemu_ll;
    private LinearLayout qemu_files_install;
    private LinearLayout un_install_qemu;
    private LinearLayout download_qemu_bd;
    private View server_zhengmian;
    private View server_fanmian;
    private View qemu_huanjing;
    private ImageView zhengmian_viewcvcv;
    private RelativeLayout dakai_qemu_huanj;
    private RelativeLayout qemu_install_jiaoben;
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        open_server_url = findViewById(R.id.open_server_url);
        start_fanmian = findViewById(R.id.start_fanmian);
        server_zhengmian = findViewById(R.id.server_zhengmian);
        server_fanmian = findViewById(R.id.server_fanmian);
        zhengmian_viewcvcv = findViewById(R.id.zhengmian_viewcvcv);
        install_server_fwq = findViewById(R.id.install_server_fwq);
        xiezai_server_df = findViewById(R.id.xiezai_server_df);
        download_server_fdgdfg = findViewById(R.id.download_server_fdgdfg);
        qemu_huanjing = findViewById(R.id.qemu_huanjing);
        dakai_qemu_huanj = findViewById(R.id.dakai_qemu_huanj);
        moren_qemu_ll = findViewById(R.id.moren_qemu_ll);
        qemu_files_install = findViewById(R.id.qemu_files_install);
        un_install_qemu = findViewById(R.id.un_install_qemu);
        download_qemu_bd = findViewById(R.id.download_qemu_bd);
        qemu_install_jiaoben = findViewById(R.id.qemu_install_jiaoben);

        onClickTermux();
    }



    //按钮的点击事件
    private void onClickTermux(){

        File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/server");
        File file1 = new File(Environment.getExternalStorageDirectory(), "/xinhao/qemu");
        File file2 = new File(Environment.getExternalStorageDirectory(), "/xinhao/share");

        if(!file.exists()){
            file.mkdirs();
        }

        if(!file1.exists()){
            file1.mkdirs();
        }

        if(!file2.exists()){
            file2.mkdirs();
        }
        qemu_install_jiaoben.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDrawer().closeDrawer(Gravity.LEFT);
                writerFile("qemu.sh",new File("/data/data/com.termux/files/home/qemu.sh"));

                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 qemu.sh \n");
                mTerminalView.sendTextToTerminal("./qemu.sh \n");

            }
        });

        download_qemu_bd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity2.this);

                ab.setTitle(UUtils.getString(R.string.提示));

                /**
                 *
                 * 链接: https://pan.baidu.com/s/1_0vhcTNGoCpo-J_y2lWS2g 提取码: 7hm2 复制这段内容后打开百度网盘手机App，操作更方便哦
                 * --来自百度网盘超级会员v3的分享
                 *
                 */

                ab.setMessage(UUtils.getString(R.string.提取码dsf8520520));

                ab.setPositiveButton(UUtils.getString(R.string.前往), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ab.create().dismiss();

                        Intent intent = new Intent();
                        intent.setData(Uri.parse("https://pan.baidu.com/s/1_0vhcTNGoCpo-J_y2lWS2g"));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        startActivity(intent); //启动浏览器

                    }
                });

                ab.setNegativeButton(UUtils.getString(R.string.取消), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });

                ab.show();
            }
        });


        un_install_qemu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TextShowDialog textShowDialog = new TextShowDialog(TermuxActivity2.this);
                textShowDialog.show();
                textShowDialog.commit_ll.setVisibility(View.VISIBLE);
                textShowDialog.edit_text.setText(UUtils.getString(R.string.你确定要卸载qemu以及所有插件));
                textShowDialog.start.setText(UUtils.getString(R.string.卸载));
                textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textShowDialog.dismiss();
                        File file2 = new File("/data/data/com.termux/files/usr/.unInstallQemu.ini");

                        if(!file2.exists()){

                            UUtils.showMsg( UUtils.getString(R.string.没有找到qemu的卸载配置文件));
                            return;
                        }

                        String fileString = UUtils.getFileString(file2);


                        deleteFile1(fileString,file2);

                    }
                });

                textShowDialog.commit.setText(UUtils.getString(R.string.取消));
                textShowDialog.commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textShowDialog.dismiss();
                    }
                });




            }
        });

        qemu_files_install.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                TextShowDialog textShowDialog1 = new TextShowDialog(TermuxActivity2.this);
                textShowDialog1.show();
                textShowDialog1.commit_ll.setVisibility(View.VISIBLE);
                textShowDialog1.edit_text.setText(UUtils.getString(R.string.你确定要提示sdf以及所有插件));
                textShowDialog1.start.setText(UUtils.getString(R.string.好的));
                textShowDialog1.start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textShowDialog1.dismiss();


                        //pkg install x11-repo unstable-repo qemu-system-x86* -y
                        if(new File("/data/data/com.termux/files/usr/bin/qemu-system-x86_64").exists() && !(new File("/data/data/com.termux/files/usr/.unInstallQemu.ini").exists())){

                            TextShowDialog textShowDialog = new TextShowDialog(TermuxActivity2.this);
                            textShowDialog.show();
                            textShowDialog.edit_text.setText(UUtils.getString(R.string.检测到你安装termux默认的));
                            textShowDialog.start.setText(UUtils.getString(R.string.卸载));
                            textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textShowDialog.dismiss();
                                    mTerminalView.sendTextToTerminal("pkg uninstall x11-repo unstable-repo qemu-system-x86* -y \n");
                                }
                            });
                            textShowDialog.commit_ll.setVisibility(View.VISIBLE);
                            textShowDialog.commit.setText(UUtils.getString(R.string.强制安装));
                            textShowDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {

                                    textShowDialog.dismiss();

                                    FileListQemuDialog fileListQemuDialog = new FileListQemuDialog(TermuxActivity2.this);
                                    fileListQemuDialog.show();
                                    fileListQemuDialog.setTitleText(UUtils.getString(R.string.请选择一个qemu安装文件));
                                    fileListQemuDialog.setOnItemFileClickListener(new FileListQemuDialog.OnItemFileClickListener() {
                                        @Override
                                        public void onItemClick(File file) {
                                            // UUtils.showMsg("选中的文件:" + file.getAbsolutePath());
                                            fileListQemuDialog.dismiss();
                                            StringBuilder stringBuilder = new StringBuilder();
                                            LoadingDialog loadingDialog = new LoadingDialog(TermuxActivity2.this);
                                            loadingDialog.show();

                                            new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    ZipUtils.unZip(file, "/data/data/com.termux/files/usr/", new ZipUtils.ZipNameListener() {
                                                        @Override
                                                        public void zip(String FileName, int size, int position) {

                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    stringBuilder.append(FileName).append(",");

                                                                    UUtils.showLog("解压:" + FileName);
                                                                    loadingDialog.msg_dialog.setText(FileName);
                                                                }
                                                            });

                                                        }

                                                        @Override
                                                        public void complete() {


                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    String string = stringBuilder.toString();
                                                                    UUtils.showLog("卸载的文件:" + string);
                                                                    UUtils.showMsg(UUtils.getString(R.string.安装qemu完成sdfsd));




                                                                    UUtils.setFileString(new File("/data/data/com.termux/files/usr/.unInstallQemu.ini"),string);
                                                                    loadingDialog.dismiss();
                                                                }
                                                            });
                                                            File file2 = new File(("/data/data/com.termux/files/usr/bin/qemu-system-ppc"));
                                                            if(!(file2.exists())){
                                                                writerFilePpc(file2);
                                                            }

                                                            Runtime runtime = Runtime.getRuntime();

                                                            try {
                                                                runtime.exec("chmod 0755 " + file2.getAbsolutePath());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }


                                                        }

                                                        @Override
                                                        public void progress(long size, long position) {

                                                        }
                                                    });
                                                }
                                            }).start();

                                        }
                                    });

                                }
                            });
                            textShowDialog.cancel.setText(UUtils.getString(R.string.取消));
                            textShowDialog.cancel.setVisibility(View.VISIBLE);
                            textShowDialog.setCancelable(false);
                            textShowDialog.diyige_ll.setVisibility(View.VISIBLE);
                            textShowDialog.cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    textShowDialog.dismiss();
                                }
                            });


                        }else{

                            File file3 = new File("/data/data/com.termux/files/usr/.unInstallQemu.ini");
                            if(file3.exists()){
                                UUtils.showMsg(UUtils.getString(R.string.请先卸载之前的qemu));
                                return;
                            }

                            FileListQemuDialog fileListQemuDialog = new FileListQemuDialog(TermuxActivity2.this);
                            fileListQemuDialog.show();
                            fileListQemuDialog.setTitleText(UUtils.getString(R.string.请选择一个qemu安装文件));
                            fileListQemuDialog.setOnItemFileClickListener(new FileListQemuDialog.OnItemFileClickListener() {
                                @Override
                                public void onItemClick(File file) {
                                    // UUtils.showMsg("选中的文件:" + file.getAbsolutePath());
                                    fileListQemuDialog.dismiss();
                                    StringBuilder stringBuilder = new StringBuilder();
                                    LoadingDialog loadingDialog = new LoadingDialog(TermuxActivity2.this);
                                    loadingDialog.show();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ZipUtils.unZip(file, "/data/data/com.termux/files/usr/", new ZipUtils.ZipNameListener() {
                                                @Override
                                                public void zip(String FileName, int size, int position) {

                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            stringBuilder.append(FileName).append(",");

                                                            UUtils.showLog("解压:" + FileName);
                                                            loadingDialog.msg_dialog.setText(FileName);
                                                        }
                                                    });

                                                }

                                                @Override
                                                public void complete() {


                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            String string = stringBuilder.toString();
                                                            UUtils.showLog("卸载的文件:" + string);
                                                            UUtils.showMsg(UUtils.getString(R.string.安装qemu完成sdfsd));




                                                            UUtils.setFileString(new File("/data/data/com.termux/files/usr/.unInstallQemu.ini"),string);
                                                            loadingDialog.dismiss();
                                                        }
                                                    });
                                                    File file2 = new File(("/data/data/com.termux/files/usr/bin/qemu-system-ppc"));
                                                    if(!(file2.exists())){
                                                        writerFilePpc(file2);
                                                    }

                                                    Runtime runtime = Runtime.getRuntime();

                                                    try {
                                                        runtime.exec("chmod 0755 " + file2.getAbsolutePath());
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                    }


                                                }

                                                @Override
                                                public void progress(long size, long position) {

                                                }
                                            });
                                        }
                                    }).start();

                                }
                            });

                        }


                    }
                });

                textShowDialog1.commit.setText(UUtils.getString(R.string.取消));
                textShowDialog1.commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textShowDialog1.dismiss();
                    }
                });





            }
        });

        moren_qemu_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File file2 = new File("/data/data/com.termux/files/usr/.unInstallQemu.ini");
                if(file2.exists()){
                    UUtils.showMsg(UUtils.getString(R.string.请先卸载之前的qemu));
                    return;
                }

                LoadingDialog loadingDialog = new LoadingDialog(TermuxActivity2.this);
                loadingDialog.show();
                loadingDialog.setCancelable(false);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        writerFile(new File(("/data/data/com.termux/files/usr/bin/qemu-system-ppc")));

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismiss();
                                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ && cd .. && cd usr && cd bin && chmod 777 qemu-system-ppc && cd ~\n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("pkg update -y && pkg install x11-repo unstable-repo -y && pkg install qemu-utils qemu-system-x86_64-headless  qemu-system-i386-headless -y &&  termux-setup-storage\n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("y\n");

                                TermuxActivity.mTerminalView.sendTextToTerminal("y\n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("y\n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("y\n");
                                TermuxActivity.mTerminalView.sendTextToTerminal("y\n");
                                Toast.makeText(TermuxActivity2.this, UUtils.getString(R.string.默认qemu正在安装dsf), Toast.LENGTH_SHORT).show();

                                getDrawer().closeDrawer(Gravity.LEFT);

                            }
                        });

                    }
                }).start();



            }
        });
        dakai_qemu_huanj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(qemu_huanjing.getVisibility() == View.GONE){
                    qemu_huanjing.setVisibility(View.VISIBLE);
                }else{
                    qemu_huanjing.setVisibility(View.GONE);
                }
            }
        });




        try {
            Runtime.getRuntime().exec("chmod 777 /data/data/com.termux/files/usr/bin/files_mulu");
        } catch (IOException e) {
            e.printStackTrace();
        }

        jinru_mysql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cmd = Constants.MYSQL_MONITOR_SBIN_LOCATION + " -h 127.0.0.1 -T -f -r -t -E --disable-pager -n --port 19914 --user=root --password= --default-character-set=utf8 -L";


                mTerminalView.sendTextToTerminal(cmd + "\n");

                getDrawer().closeDrawer(Gravity.LEFT);
            }
        });

        download_server_fdgdfg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity2.this);

                ab.setTitle(UUtils.getString(R.string.提示));

                /**
                 *
                 * 链接: https://pan.baidu.com/s/1IpgqOJjpVH3Ut34mMrwWng 提取码: wpq2 复制这段内容后打开百度网盘手机App，操作更方便哦
                 * --来自百度网盘超级会员v3的分享
                 *
                 */

                ab.setMessage(UUtils.getString(R.string.提取码sdfsdfcxvn));

                ab.setPositiveButton(UUtils.getString(R.string.前往), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ab.create().dismiss();

                        Intent intent = new Intent();
                        intent.setData(Uri.parse("https://pan.baidu.com/s/1IpgqOJjpVH3Ut34mMrwWng"));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        startActivity(intent); //启动浏览器

                    }
                });

                ab.setNegativeButton(UUtils.getString(R.string.取消), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });

                ab.show();

            }
        });
        open_server_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("https://github.com/DroidPHP/DroidPHP"));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent); //
            }
        });

        install_server_fwq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FileInstallServerListDialog fileInstallServerListDialog = new FileInstallServerListDialog(TermuxActivity2.this);
                fileInstallServerListDialog.show();
                fileInstallServerListDialog.setOnItemFileClickListener(new FileInstallServerListDialog.OnItemFileClickListener() {
                    @Override
                    public void onItemClick(File file) {

                        fileInstallServerListDialog.dismiss();
                        try {
                            WebZip.installQZ(null,new FileInputStream(file),TermuxActivity2.this);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
        xiezai_server_df.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                TextShowDialog textShowDialog = new TextShowDialog(TermuxActivity2.this);

                textShowDialog.show();

                textShowDialog.edit_text.setText(UUtils.getString(R.string.你确定要删除dfgd));

                textShowDialog.commit_ll.setVisibility(View.VISIBLE);
                textShowDialog.commit.setText(UUtils.getString(R.string.取消));
                textShowDialog.commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textShowDialog.dismiss();
                    }
                });
                textShowDialog.start.setText(UUtils.getString(R.string.卸载));
                textShowDialog.start.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        File web_config = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_config");
                        File web_php_html = new File(Environment.getExternalStorageDirectory(),"/xinhao/web_php_html");

                        File root = new File("/data/data/com.termux/home/.components");

                        mTerminalView.sendTextToTerminal("rm -rf ~/.components \n");

                        deleteDir(web_config.getAbsolutePath());
                        deleteDir(web_php_html.getAbsolutePath());


                        textShowDialog.dismiss();

                        UUtils.showMsg(UUtils.getString(R.string.卸载完成));
                    }
                });




            }
        });

        start_fanmian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(WebZip.isInstall())){

                    UUtils.showMsg(UUtils.getString(R.string.请先安装环境后));



                    return;
                }
                server_zhengmian.setVisibility(View.GONE);
                server_fanmian.setVisibility(View.VISIBLE);
            }
        });

        zhengmian_viewcvcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server_zhengmian.setVisibility(View.VISIBLE);
                server_fanmian.setVisibility(View.GONE);
            }
        });

        mTerminalView.setDoubleClickListener(new TerminalView.DoubleClickListener() {
            @Override
            public void doubleClicke() {




                PopupWindow popupWindow = new PopupWindow();
                final BoomWindow[] boomWindow = {new BoomWindow()};



                popupWindow.setContentView(boomWindow[0].getView(new BoomMinLAdapter.CloseLiftListener() {
                    @Override
                    public void close() {
                        popupWindow.dismiss();
                    }
                },TermuxActivity2.this,popupWindow));


                popupWindow.setOutsideTouchable(true);
              //  popupWindow.setAnimationStyle(R.style.Animation);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.showAsDropDown(mTerminalView,0,- boomWindow[0].getHigh());
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        boomWindow[0] = null;
                    }
                });


                boomWindow[0].popu_windows_huihua.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addNewSession(false, null);
                        popupWindow.dismiss();

                    }
                });
                boomWindow[0].popu_windows_jianpan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                        getDrawer().closeDrawers();

                        //popupWindow.dismiss();


                    }
                });
            }
        });

    }


    public static void deleteDir(String dirPath)
    {
        File file = new File(dirPath);
        if(file.isFile())
        {
            file.delete();
        }else
        {
            File[] files = file.listFiles();
            if(files == null)
            {
                file.delete();
            }else
            {
                for (int i = 0; i < files.length; i++)
                {
                    deleteDir(files[i].getAbsolutePath());
                }
                file.delete();
            }
        }
    }


    //写出文件
    private void writerFile(File mFile) {

        try {
            InputStream open = getResources().openRawResource(R.raw.qemu_system_ppc);

            int len = 0;
            byte[] lll = new byte[1024];

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read(lll)) != -1) {
                fileOutputStream.write(lll,0,len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }


    private void deleteFile1(String s,File file){


        LoadingDialog loadingDialog = new LoadingDialog(TermuxActivity2.this);
        loadingDialog.show();
        try {
            String[] split = s.split(",");


            for (int i = 0; i < split.length; i++) {


                boolean delete = new File("/data/data/com.termux/files/usr/" + split[i]).delete();

                loadingDialog.msg_dialog.setText(UUtils.getString(R.string.删除dfgdfg) +" "+ split[i] +delete);
                UUtils.showLog(UUtils.getString(R.string.删除dfgdfg) +" "+ split[i] + delete);

            }

            //

            UUtils.showMsg(UUtils.getString(R.string.卸载完成sdf43420));

            loadingDialog.dismiss();

            file.delete();

        }catch (Exception e){
            e.printStackTrace();
            file.delete();
            UUtils.showMsg(UUtils.getString(R.string.错误的qemu卸载配置文件));
            return;
        }


    }

    //写出文件
    private void writerFilePpc(File mFile) {

        try {
            InputStream open = getResources().openRawResource(R.raw.qemu_system_ppc);

            int len = 0;
            byte[] lll = new byte[1024];

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read(lll)) != -1) {
                fileOutputStream.write(lll,0,len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }
}
