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
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.termux.R;
import com.termux.terminal.EmulatorDebug;
import com.termux.terminal.TerminalColors;
import com.termux.terminal.TerminalSession;
import com.termux.terminal.TerminalSession.SessionChangedCallback;
import com.termux.terminal.TextStyle;
import com.termux.view.TerminalView;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.java.com.termux.activity.CustomActivity;
import main.java.com.termux.activity.FunctionActivity;
import main.java.com.termux.activity.RepairActivity;
import main.java.com.termux.activity.SwitchActivity;
import main.java.com.termux.activity.ThanksActivity;
import main.java.com.termux.activity.WindowsActivity;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.bean.CreateSystemBean;
import main.java.com.termux.datat.DataBean;
import main.java.com.termux.filemanage.filemanager.FileManagerActivity;
import main.java.com.termux.floatwindows.TermuxFloatService;
import main.java.com.termux.http.CheckUpDateCodeUtils;
import main.java.com.termux.listener.SmsMsgListener;
import main.java.com.termux.utils.SaveData;
import main.java.com.termux.utils.SmsUtils;
import main.java.com.termux.utils.WindowUtils;
import main.java.com.termux.view.MyDialog;

import static main.java.com.termux.app.TermuxService.getEnvironmentPrefix;

/**
 * A terminal emulator activity.
 * <p/>
 * See
 * <ul>
 * <li>http://www.mongrel-phones.com.au/default/how_to_make_a_local_service_and_bind_to_it_in_android</li>
 * <li>https://code.google.com/p/android/issues/detail?id=6426</li>
 * </ul>
 * about memory leaks.
 */
public final class TermuxActivity extends Activity implements ServiceConnection {

    public static final String TERMUX_FAILSAFE_SESSION_ACTION = "com.termux.app.failsafe_session";

    private static final int CONTEXTMENU_SELECT_URL_ID = 0;
    private static final int CONTEXTMENU_SHARE_TRANSCRIPT_ID = 1;
    private static final int CONTEXTMENU_PASTE_ID = 3;
    private static final int CONTEXTMENU_KILL_PROCESS_ID = 4;
    private static final int CONTEXTMENU_RESET_TERMINAL_ID = 5;
    private static final int CONTEXTMENU_STYLING_ID = 6;
    private static final int CONTEXTMENU_HELP_ID = 8;
    private static final int CONTEXTMENU_TOGGLE_KEEP_SCREEN_ON = 9;

    private static final int MAX_SESSIONS = 8;

    private static final int REQUESTCODE_PERMISSION_STORAGE = 1234;

    private static final String RELOAD_STYLE_ACTION = "com.termux.app.reload_style";
    private static final int INSTALL_PACKAGES_REQUESTCODE = 20000;
    private static final int GET_UNKNOWN_APP_SOURCES = 20001;
    private static final int REQUEST_WRITE = 33333;

    /**
     * The main view of the activity showing the terminal. Initialized in onCreate().
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    public static TerminalView mTerminalView;

    ExtraKeysView mExtraKeysView;

    TermuxPreferences mSettings;

    /**
     * The connection to the {@link TermuxService}. Requested in {@link #onCreate(Bundle)} with a call to
     * {@link #bindService(Intent, ServiceConnection, int)}, and obtained and stored in
     * {@link #onServiceConnected(ComponentName, IBinder)}.
     */
    TermuxService mTermService;

    /**
     * Initialized in {@link #onServiceConnected(ComponentName, IBinder)}.
     */
    ArrayAdapter<TerminalSession> mListViewAdapter;

    Toast mLastToast;

    /**
     * If between onResume() and onStop(). Note that only one session is in the foreground of the terminal view at the
     * time, so if the session causing a change is not in the foreground it should probably be treated as background.
     */
    boolean mIsVisible;

    final SoundPool mBellSoundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(
        new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION).build()).build();
    int mBellSoundId;

    private final BroadcastReceiver mBroadcastReceiever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mIsVisible) {
                String whatToReload = intent.getStringExtra(RELOAD_STYLE_ACTION);
                if ("storage".equals(whatToReload)) {
                    if (ensureStoragePermissionGranted())
                        TermuxInstaller.setupStorageSymlinks(TermuxActivity.this);
                    return;
                }
                checkForFontAndColors();
                mSettings.reloadFromProperties(TermuxActivity.this);

                if (mExtraKeysView != null) {
                    mExtraKeysView.reload(mSettings.mExtraKeys, ExtraKeysView.defaultCharDisplay);
                }
            }
        }
    };
    private AnimatorSet mRightOutSet;
    private AnimatorSet mLeftInSet;
    private ListView listView;

    @Override
    protected void onResume() {
        super.onResume();
        String redD = SaveData.getData("redD");
        if (redD.equals("red")) {
            red.setVisibility(View.GONE);
        }
    }

    void checkForFontAndColors() {
        try {
            @SuppressLint("SdCardPath") File fontFile = new File("/data/data/com.termux/files/home/.termux/font.ttf");
            @SuppressLint("SdCardPath") File colorsFile = new File("/data/data/com.termux/files/home/.termux/colors.properties");

            final Properties props = new Properties();
            if (colorsFile.isFile()) {
                try (InputStream in = new FileInputStream(colorsFile)) {
                    props.load(in);
                }
            }

            TerminalColors.COLOR_SCHEME.updateWith(props);
            TerminalSession session = getCurrentTermSession();
            if (session != null && session.getEmulator() != null) {
                session.getEmulator().mColors.reset();
            }
            updateBackgroundColor();

            final Typeface newTypeface = (fontFile.exists() && fontFile.length() > 0) ? Typeface.createFromFile(fontFile) : Typeface.MONOSPACE;
            mTerminalView.setTypeface(newTypeface);
        } catch (Exception e) {
            Log.e(EmulatorDebug.LOG_TAG, "Error in checkForFontAndColors()", e);
        }
    }

    void updateBackgroundColor() {
        TerminalSession session = getCurrentTermSession();
        if (session != null && session.getEmulator() != null) {
            getWindow().getDecorView().setBackgroundColor(session.getEmulator().mColors.mCurrentColors[TextStyle.COLOR_INDEX_BACKGROUND]);
        }
    }

    /**
     * For processes to access shared internal storage (/sdcard) we need this permission.
     */
    @TargetApi(Build.VERSION_CODES.M)
    public boolean ensureStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_PERMISSION_STORAGE);
                return false;
            }
        } else {
            // Always granted before Android 6.0.
            return true;
        }
    }


    private TextView msg;

    private ProgressBar pro;

    private TextView position_text;

    private long mSize = 0;

    //写入目录
    private void writerFile() {
        mSize = 0;

        FileWriterUtils.start(this, new ZipUtils.ZipNameListener() {
            @Override
            public void zip(String fileName, int size, int sizeC) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (size == 0 && sizeC == 0) {
                            msg.setText(fileName);
                        } else {
                            msg.setText(fileName);
                        }
                        //  msg.setText(fileName + "(" + sizeC + "/" + size + ")");
                    }
                });
            }

            @Override
            public void complete() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                        ab.setTitle("注意");

                        ab.setMessage("为了使你的配置生效，请稍后重启APP[大退,不要按返回键退出]\n不重启无法正常使用.");

                        ab.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ab.create().dismiss();
                            }
                        });


                       /* ab.setNegativeButton("稍后重启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                temp(true);
                                ab.create().dismiss();
                            }
                        });*/
                        ab.show();

                    }
                });
            }

            @Override
            public void progress(long size, long position) {
                mSize += size;
                pro.setMax((int) size);
                // pro.setMin(0);
                pro.setProgress((int) position);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pro_file

                        position_text.setText("文件数量:(" + position + "/" + size + ")\n  解压前:不知道 \n  解压后:" + (mSize / 1024 / 1024) + "MB");

                    }
                });

            }
        });


    }


    //启动命令操作系统
    public static void startCmmd(TextView msg) {

        ///data/data/com.termux/files/home/jails/ubuntu/ubuntu.sh

        if (TermuxService.TAGRUN != null)
            return;


        String commandTemp = "";
        String command = "";
        File mFile = new File("/data/data/com.termux/files/home/BootCommand");
        if (mFile.exists()) {

            if (mFile.length() > 0) {


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(mFile)));

                    while ((commandTemp = bufferedReader.readLine()) != null) {


                        command += commandTemp;

                    }

                    bufferedReader.close();


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String finalCommand = command;


                try {
                    if (finalCommand != null && finalCommand.length() > 0) {

                        String[] split = finalCommand.split("&&");

                        for (int i = 0; i < split.length; i++) {

                            int finalI = i;
                            int finalI1 = i;
                            TermuxApplication.mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mTerminalView.sendTextToTerminal(split[finalI] + " \n");
                                    msg.setText("正在启动开机命令[命令目录/home/BootCommand]命令之间用'&&'隔开" + "[" + finalI1 + "/" + split.length + "]");
                                }
                            });


                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }


                        }




                   /* try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTerminalView.sendTextToTerminal("cd .. \n");
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

/*                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTerminalView.sendTextToTerminal("chmod -R 777 home \n");
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    TermuxApplication.mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mTerminalView.sendTextToTerminal("chmod -R 777 usr \n");
                        }
                    });*/


                    }
                } catch (Exception e) {

                }


            }


        }


    }


    private RelativeLayout lay_r;

    private void temp(boolean istrue) {


        msg.setText("开始启动mysql");
        mSettings = new TermuxPreferences(this);

        mTerminalView = findViewById(R.id.terminal_view);
        mTerminalView.setOnKeyListener(new TermuxViewClient(this));

        lay_r = findViewById(R.id.lay_r);
        msg.setText("mysql 已启动");
        mTerminalView.setTextSize(mSettings.getFontSize());
        mTerminalView.setKeepScreenOn(mSettings.isScreenAlwaysOn());
        mTerminalView.requestFocus();
        if (istrue) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            start_copy.setVisibility(View.GONE);
                            try {
                                Runtime.getRuntime().exec("chmod 0700 /data/data/com.termux/files/usr/bin/myisam_ftdump");
                                Runtime.getRuntime().exec("chmod 0700 /data/data/com.termux/files/usr/bin/mysqld");
                                Runtime.getRuntime().exec("chmod 0700 /data/data/com.termux/files/usr/bin/node");
                                Runtime.getRuntime().exec("chmod 0700 /data/data/com.termux/files/usr/bin/php");
                                Runtime.getRuntime().exec("chmod 0700 /data/data/com.termux/files/usr/bin/php-cgi");
                                Runtime.getRuntime().exec("chmod 0700 /data/data/com.termux/files/usr/bin/php-fpm");
                                Runtime.getRuntime().exec("chmod 0700 /data/data/com.termux/files/usr/bin/phpdbg");
                                Runtime.getRuntime().exec("chmod 0700 /data/data/com.termux/files/usr/bin/test-connect-t");

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            mTerminalView.sendTextToTerminal("termux-chroot \n");

                            mTerminalView.sendTextToTerminal("mysqld & \n");

                            //启动apache2   httpd

                            //启动nginx   nginx

                            //模拟root termux-chroot

                            //启动php termux-chroot php-fpm  nginx
                            // lay_r.setVisibility(View.GONE);

                            msg.setText("正在启动 php环境");
                        }
                    });


                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //mTerminalView.sendTextToTerminal("export DISPLAY=:1 \n");
                            // mTerminalView.sendTextToTerminal("vncserver :1 -geometry 1920x1080 \n");

                            //启动apache2   httpd

                            //启动nginx   nginx

                            //模拟root termux-chroot

                            //启动php termux-chroot php-fpm  nginx
                            //   lay_r.setVisibility(View.GONE);

                            msg.setText("启动VNC!");
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
                            mTerminalView.sendTextToTerminal("nginx & \n");

                            //启动apache2   httpd

                            //启动nginx   nginx

                            //模拟root termux-chroot

                            //启动php termux-chroot php-fpm  nginx
                            // lay_r.setVisibility(View.GONE);

                            msg.setText("正在启动 nginx 环境");
                        }
                    });


                    try {
                        Thread.sleep(6000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTerminalView.sendTextToTerminal("termux-chroot php-fpm & \n");

                            //启动apache2   httpd

                            //启动nginx   nginx

                            //模拟root termux-chroot

                            //启动php termux-chroot php-fpm  nginx
                            //   lay_r.setVisibility(View.GONE);

                            msg.setText("启动完成!右下角的+号可以点啦~");
                        }
                    });


                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            msg.setText("正在启动开机命令[命令目录/home/BootCommand]命令之间用'&&'隔开");
                        }
                    });
                    startCmmd(msg);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            /// mTerminalView.sendTextToTerminal("php termux-chroot php-fpm & \n");

                            //启动apache2   httpd

                            //启动nginx   nginx

                            //模拟root termux-chroot

                            //启动php termux-chroot php-fpm  nginx
                            lay_r.setVisibility(View.GONE);

                            //msg.setText("启动完成!");
                        }
                    });


                }
            }).start();

        } else {

            new Thread(new Runnable() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            msg.setText("正在启动开机命令[命令目录/home/BootCommand]命令之间用'&&'隔开");
                        }
                    });
                    startCmmd(msg);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lay_r.setVisibility(View.GONE);
                        }
                    });
                }
            }).start();


        }

        start_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, SettingActivity.class));
            }
        });


        final ViewPager viewPager = findViewById(R.id.viewpager);
        if (mSettings.mShowExtraKeys) viewPager.setVisibility(View.VISIBLE);


        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        layoutParams.height = layoutParams.height * mSettings.mExtraKeys.length;
        viewPager.setLayoutParams(layoutParams);

        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup collection, int position) {
                LayoutInflater inflater = LayoutInflater.from(TermuxActivity.this);
                View layout;
                if (position == 0) {
                    layout = mExtraKeysView = (ExtraKeysView) inflater.inflate(R.layout.extra_keys_main, collection, false);
                    mExtraKeysView.reload(mSettings.mExtraKeys, ExtraKeysView.defaultCharDisplay);
                } else {
                    layout = inflater.inflate(R.layout.extra_keys_right, collection, false);
                    final EditText editText = layout.findViewById(R.id.text_input);
                    editText.setOnEditorActionListener((v, actionId, event) -> {
                        TerminalSession session = getCurrentTermSession();
                        if (session != null) {
                            if (session.isRunning()) {
                                String textToSend = editText.getText().toString();
                                if (textToSend.length() == 0) textToSend = "\r";
                                session.write(textToSend);
                            } else {
                                removeFinishedSession(session);
                            }
                            editText.setText("");
                        }
                        return true;
                    });
                }
                collection.addView(layout);
                return layout;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
                collection.removeView((View) view);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    mTerminalView.requestFocus();
                } else {
                    final EditText editText = viewPager.findViewById(R.id.text_input);
                    if (editText != null) editText.requestFocus();
                }
            }
        });

        View newSessionButton = findViewById(R.id.new_session_button);
        newSessionButton.setOnClickListener(v -> addNewSession(false, null));
        newSessionButton.setOnLongClickListener(v -> {
            DialogUtils.textInput(TermuxActivity.this, R.string.session_new_named_title, null, R.string.session_new_named_positive_button,
                text -> addNewSession(false, text), R.string.new_session_failsafe, text -> addNewSession(true, text)
                , -1, null, null);
            return true;
        });

        findViewById(R.id.toggle_keyboard_button).setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
            getDrawer().closeDrawers();
        });

        findViewById(R.id.toggle_keyboard_button).setOnLongClickListener(v -> {
            toggleShowExtraKeys();
            return true;
        });

        registerForContextMenu(mTerminalView);

        Intent serviceIntent = new Intent(this, TermuxService.class);
        // Start the service and make it run regardless of who is bound to it:
        startService(serviceIntent);
        if (!bindService(serviceIntent, this, 0))
            throw new RuntimeException("bindService() failed");

        checkForFontAndColors();

        mBellSoundId = mBellSoundPool.load(this, R.raw.bell, 1);
        // https://pan.baidu.com/s/1jEY-iAWYV4UdBvNelBbpyg

        start_copy.setVisibility(View.GONE);
    }


    private AnimatorSet mRightOutAnimatorSet, mLeftInAnimatorSet;

    private boolean mIsShowBack = false;  //是否显示背面

    //动画

    private void animImage() {


        new Thread(new Runnable() {
            @Override
            public void run() {

                int i = -100;
                while (true) {


                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (i > 200000) {
                        i = -100;
                    }

                    int finalI = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            image.setRotationY(finalI);
                        }
                    });

                    i++;
                }

            }
        }).start();


    }


    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                //installApk();//安装应用的逻辑(写自己的就可以)
                CheckUpDateCodeUtils.installApk(this, apkFilePath);
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            //installApk();
            CheckUpDateCodeUtils.installApk(this, apkFilePath);
        }

    }

    private ImageView image;

    private ProgressBar pro_file;

    private ImageView start_copy;

    private LinearLayout file_btn;

    private LinearLayout setting;

    private LinearLayout repair;

    public LinearLayout repair_bl;

    private LinearLayout switch_btn;

    private TextView download_text;

    private RelativeLayout function_btn;

    private LinearLayout linux_system_btn;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    private File mFileSystem = new File(Environment.getExternalStorageDirectory(), "/xinhao/system");
    private File mFileIso = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso");
    private File mFileMysql = new File(Environment.getExternalStorageDirectory(), "/xinhao/mysql");

    //创建文件夹
    private void mkdirFilePermission() {

//判断是否6.0以上的手机 不是就不用
        if (Build.VERSION.SDK_INT >= 23) {
            //判断是否有这个权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //第一请求权限被取消显示的判断，一般可以不写
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Log.i("readTosdCard", "我们需要这个权限给你提供存储服务");
                    Toast.makeText(this, "你已拒绝，备份恢复功能将无法使用!", Toast.LENGTH_SHORT).show();
                    startOk();
                } else {
                    //2、申请权限: 参数二：权限的数组；参数三：请求码

                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE);
                }
            } else {
                mkdirFile();
                startOk();
            }
        } else {
            mkdirFile();
            startOk();
        }


    }


    private void mkdirFile() {


        if (!mFileSystem.exists()) {
            mFileSystem.mkdirs();
        }

        if (!mFileIso.exists()) {
            mFileIso.mkdirs();
        }

        if (!mFileMysql.exists()) {
            mFileMysql.mkdirs();
        }

    }

    private ImageView red;

    private LinearLayout x86_64;


    private Button sess_btn;
    private Button fun_btn;

    private LinearLayout linux_gui_btn;
    private LinearLayout linux_quanxian_btn;

    private LinearLayout fun_all;

    private LinearLayout xuanfu;

    private LinearLayout mingxie;

    private LinearLayout fedora_linux_gui_btn;
    private LinearLayout quanping;
    private LinearLayout unfedora_linux_gui_btn;
    private LinearLayout windows;
    private LinearLayout key_ziding;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView(R.layout.drawer_layout);

        termux_run = findViewById(R.id.termux_run);

        quanping = findViewById(R.id.quanping);

        mingxie = findViewById(R.id.mingxie);
        key_ziding = findViewById(R.id.key_ziding);

        key_ziding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);
                startActivity(new Intent(TermuxActivity.this, CustomActivity.class));
            }
        });

        windows = findViewById(R.id.windows);

        windows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);
                startActivity(new Intent(TermuxActivity.this, WindowsActivity.class));
            }
        });

        fedora_linux_gui_btn = findViewById(R.id.fedora_linux_gui_btn);
        unfedora_linux_gui_btn = findViewById(R.id.unfedora_linux_gui_btn);

        quanping.findViewById(R.id.quanping);

        quanping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (WindowUtils.isFullScreen) {
                    //退出全屏

                    WindowUtils.exitFullScreen(TermuxActivity.this);
                } else {

                    //打开全屏
                    WindowUtils.setFullScreen(TermuxActivity.this);
                }

                toggleShowExtraKeys();

            }
        });
        fedora_linux_gui_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("请确认");

                ab.setMessage("你需要离线还是在线\n在线需要:vpn以及20KB~200KB的捉急网速\n离线：直接在群文件下载放到[sdcared ->xinhao/iso/]目录下");

                ab.setPositiveButton("我选择离线", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();

                        if (fileF.exists()) {

                            MyDialog myDialog = new MyDialog(TermuxActivity.this);

                            myDialog.getDialog_title().setText("正在复制文件到工作区域");

                            myDialog.getDialog_pro().setText("请耐心等待,如果一直卡这\n请去看看你的sd卡权限\n有没有打开");

                            myDialog.show();


                            File file = new File("/data/data/com.termux/files/home/xinhao_fedora/");

                            if (!file.exists()) {
                                file.mkdirs();
                            }

                            File file1 = new File(file, "fedora.7z");

                            myDialog.getDialog_pro_prog().setMax((int) file1.length());


                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        FileInputStream fileInputStream = new FileInputStream(fileF);

                                        FileOutputStream fileOutputStream = new FileOutputStream(file1);

                                        byte[] bytes = new byte[8192];

                                        int l = 0;

                                        int t = 0;

                                        while ((l = fileInputStream.read(bytes)) != -1) {
                                            t += l;
                                            fileOutputStream.write(bytes, 0, l);

                                            int finalT = t;
                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    myDialog.getDialog_title().setText("正在复制文件到工作区域\n如果复制很慢请耐心等待\n页面刷新可能有延时\n可能一直卡在某个数,但是内部复制没停\n如果没有耐心，大退app重新执行[不建议!!!]");

                                                    myDialog.getDialog_pro().setText((finalT / 1024 / 1024) + "MB/" + (fileF.length() / 1024 / 1024) + "MB");

                                                    myDialog.getDialog_pro_prog().setProgress(finalT);
                                                }
                                            });
                                        }

                                        fileInputStream.close();
                                        fileOutputStream.flush();
                                        fileOutputStream.close();


                                        TermuxApplication.mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                myDialog.dismiss();

                                                writerFile("Install_cn_li.sh", new File("/data/data/com.termux/files/home/Install_cn_li.sh"));

                                                mTerminalView.sendTextToTerminal("cd ~ \n");
                                                mTerminalView.sendTextToTerminal("chmod 777 Install_cn_li.sh \n");
                                                mTerminalView.sendTextToTerminal("./Install_cn_li.sh \n");
                                                Toast.makeText(TermuxApplication.mContext, "如果一直卡在(正在安装 wget proot...),请ctrl + c 然后再按 ↑建 重新执行一下", Toast.LENGTH_LONG).show();


                                            }
                                        });

                                        Thread.sleep(4000);

                                        TermuxApplication.mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);
                                                ab.setCancelable(false);
                                                ab.setTitle("请注意");
                                                ab.setMessage("如果一直卡在(正在安装 wget proot...)\n\n请ctrl + c 然后再按 ↑建 重新执行一下\n\n或者 重新执行脚本[./Install_cn_li.sh]");
                                                ab.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ab.create().dismiss();


                                                    }
                                                });
                                                ab.show();

                                            }
                                        });


                                    } catch (Exception e) {

                                        Log.e("XINHAO_HAN", "run: " + e.toString());

                                    }


                                }
                            }).start();


                        } else {
                            Toast.makeText(TermuxActivity.this, "在 sdcard ->xinhao/iso 未发现 fedora.7z 文件", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ab.setNegativeButton("我选择在线", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                        mTerminalView.sendTextToTerminal("cd ~ \n");
                        writerFile("Install_cn.sh", new File("/data/data/com.termux/files/home/Install_cn.sh"));
                        mTerminalView.sendTextToTerminal("chmod 777 Install_cn.sh \n");
                        mTerminalView.sendTextToTerminal("./Install_cn.sh \n");
                    }
                });

                ab.show();


            }
        });

        unfedora_linux_gui_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("您确定?");
                ab.setMessage("你确定要卸载fedora系统吗？");
                ab.setPositiveButton("我要卸载!!!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                        mTerminalView.sendTextToTerminal("cd ~ \n");
                        writerFile("uninstall_cn.sh", new File("/data/data/com.termux/files/home/uninstall_cn.sh"));

                        mTerminalView.sendTextToTerminal("chmod 777 uninstall_cn.sh \n");
                        mTerminalView.sendTextToTerminal("./uninstall_cn.sh \n");
                    }
                });

                ab.setNegativeButton("我不卸载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });

                ab.show();


            }
        });


        mingxie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TermuxActivity.this, ThanksActivity.class));
            }
        });

        x86_64 = findViewById(R.id.x86_64);

        xuanfu = findViewById(R.id.xuanfu);

        xuanfu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);
                startService(new Intent(TermuxActivity.this, TermuxFloatService.class));
            }
        });

        linux_quanxian_btn = findViewById(R.id.linux_quanxian_btn);

        x86_64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setX86_64();
            }
        });
        linux_gui_btn = findViewById(R.id.linux_gui_btn);

        linux_quanxian_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);
                startActivity(new Intent(TermuxActivity.this, RepairActivity.class));
            }
        });

        linux_gui_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);
                startActivity(new Intent(TermuxActivity.this, TestActivity.class));
            }
        });

        sess_btn = findViewById(R.id.sess_btn);

        fun_all = findViewById(R.id.fun_all);

        fun_btn = findViewById(R.id.fun_btn);

        red = findViewById(R.id.red);

        listView = findViewById(R.id.left_drawer_list);

        sess_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qiehuan(0);
            }
        });


        fun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qiehuan(1);
            }
        });

        String redD = SaveData.getData("redD");

        if (redD.equals("red")) {
            red.setVisibility(View.GONE);
        }


        if (mFileSystem.exists()) {
            mkdirFile();
            startOk();
        } else {
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle("注意!");
            ab.setMessage("我们需要你的sd卡权限,用于以下需求:\n1.扩展包的使用\n2.图形包的使用\n3.缓存配置\n如果你拒绝!以上功能全部无法使用!");
            ab.setCancelable(false);
            ab.setNegativeButton("好的", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mkdirFilePermission();
                }
            });
            ab.setPositiveButton("可以", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mkdirFilePermission();
                }
            });
            ab.show();
        }


        // startActivity(new Intent(this,TestActivity.class));

    }


    private File fileF = new File(Environment.getExternalStorageDirectory(), "/xinhao/iso/fedora.7z");

    //设置x86_64
    private void setX86_64() {


        File file = new File("/data/data/com.termux/files/environment/os_cdrom.iso");


        if (file.exists()) {
            qiehuan(0);
            addQemuSession();

        } else {

            getDrawer().closeDrawer(Gravity.LEFT);


            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("提示");

            ab.setMessage("已有安装包被发现,是否立即开始安装？");

            ab.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();
                }
            });
            ab.setNegativeButton("给我安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();

                    File file1 = new File(mFileIso, "/alpine.zip");

                    if (file1.exists()) {

                        MyDialog myDialog = new MyDialog(TermuxActivity.this);

                        myDialog.getDialog_title().setText("正在复制到工作区域");

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {


                                    FileInputStream fileInputStream = new FileInputStream(file1);
                                    File file2 = new File("/data/data/com.termux/files/environment.zip");

                                    if (!file2.exists()) {
                                        file2.createNewFile();
                                    }
                                    FileOutputStream fileOutputStream = new FileOutputStream(file2);

                                    int l = 0;

                                    byte[] b = new byte[4096];

                                    int temp = 0;

                                    while ((l = fileInputStream.read(b)) != -1) {

                                        temp += l;
                                        fileOutputStream.write(b, 0, l);
                                        int finalTemp = temp;
                                        TermuxApplication.mHandler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                myDialog.getDialog_pro().setText((finalTemp / 1024 / 1024 * 1.0) + "MB/" + (file1.length() / 1024 / 1024 * 1.0) + "MB]");
                                            }
                                        });

                                    }
                                    fileOutputStream.flush();
                                    fileInputStream.close();
                                    fileOutputStream.close();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myDialog.getDialog_title().setText("复制完成,开始解压");
                                        }
                                    });
                                    Thread.sleep(2000);

                                    ZipUtils.unZip(file2, "/data/data/com.termux/files", new ZipUtils.ZipNameListener() {
                                        @Override
                                        public void zip(String FileName, int size, int position) {

                                            TermuxApplication.mHandler.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    myDialog.getDialog_pro().setText(FileName);
                                                    myDialog.getDialog_pro_prog().setMax(size);
                                                    myDialog.getDialog_pro_prog().setProgress(position);
                                                }
                                            });

                                        }

                                        @Override
                                        public void complete() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    myDialog.getDialog_title().setText("完成!请再次点击一下侧边栏菜单，进入linux");
                                                }
                                            });
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    myDialog.dismiss();
                                                }
                                            });

                                        }

                                        @Override
                                        public void progress(long size, long position) {

                                        }
                                    });


                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myDialog.dismiss();
                                            Toast.makeText(TermuxActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myDialog.dismiss();
                                            Toast.makeText(TermuxActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                } catch (InterruptedException e) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myDialog.dismiss();
                                            Toast.makeText(TermuxActivity.this, e.toString(), Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                        myDialog.setCancelable(false);
                        myDialog.show();


                    } else {
                        AlertDialog.Builder ab1 = new AlertDialog.Builder(TermuxActivity.this);

                        ab1.setTitle("提示");

                        ab1.setMessage("缺少数据包,请放置数据包到\n放置到:sdcard/xinhao/iso\n文件名:alpine.zip\n请在群文件中下载!\n群号[1]:714730084\n群号[2]:463233836\n如果没有该文件夹,请允许termux获取存储及空间权限，再次进入termux，会自动创建相关文件夹!也可以自己创建");

                        ab1.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ab1.create().dismiss();
                            }
                        });
                        ab1.show();
                    }


                }
            });

            ab.show();


        }

    }


    //来回切换

    private void qiehuan(int index) {

        //0会话
        //1功能

        fun_all.setVisibility(View.GONE);
        listView.setVisibility(View.GONE);


        if (index == 0) {
            sess_btn.setText("[*]会话");
            fun_btn.setText("功能");
            listView.setVisibility(View.VISIBLE);
            fun_all.setVisibility(View.GONE);

        } else {
            sess_btn.setText("会话");
            fun_btn.setText("[*]功能");
            listView.setVisibility(View.GONE);
            fun_all.setVisibility(View.VISIBLE);

        }


    }

    private void startOk() {


        start_copy = findViewById(R.id.start_copy);
        start_copy.requestFocus();
        image = findViewById(R.id.image);
        msg = findViewById(R.id.msg);
        pro = findViewById(R.id.pro);
        pro_file = findViewById(R.id.pro_file);
        position_text = findViewById(R.id.position_text);
        file_btn = findViewById(R.id.file_btn);
        setting = findViewById(R.id.setting);
        repair = findViewById(R.id.repair);
        repair_bl = findViewById(R.id.repair_bl);
        function_btn = findViewById(R.id.function_btn);
        linux_system_btn = findViewById(R.id.linux_system_btn);

        linux_system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);
                String[] strings = {"" +
                    "[ubuntu]乌班图发行版[19.04][./start.sh]",
                    "debian  乌班图子系统[安装约30分钟][./bin/enter_deb]",
                    "fedora  [安装后谨慎卸载,未root用户,某些文件无法删除!!]",
                    "Kali [此文件下载1.2GB 安装后 3+GB 请思考后在安装]",
                    "Centos  [需要root,只不过目前该版本用不了,别点]",
                    "给我一键安装JAVA[JDK8]",
                    "安装其他linux [安装备用其他linux]",
                    "安装桌面 [在各个版本linux 中安装各种界面]",
                    "给我复制[ apktool.jar(APK反编译)] 到home目录",
                    "给我复制[ atilo(linux安装工具)] 到home目录",
                    "给我安装Ddos[ ddos 工具] ",
                    "** 卸载安装的linux [卸载备用其他linux]",
                    "** 执行美化终端脚本",
                    "[ubuntu]乌班图发行版[14.04][./start.sh]",
                    "[ubuntu]乌班图发行版[16.04][./start.sh]",
                    "[ubuntu]乌班图发行版[18.04][./start.sh]"
                };

                AlertDialog.Builder builder = new AlertDialog
                    .Builder(TermuxActivity.this);
                builder.setTitle("滚动列表往下滑[需要VPN否则很慢]");
                // builder.setMessage("这是个滚动列表，往下滑");
                builder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(TermuxActivity.this, "选择了第" + which + "个", Toast.LENGTH_SHORT).show();

                        installSystem(which);
                    }
                });
                builder.setNegativeButton("我还没想好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });


        download_text = findViewById(R.id.download_text);

        switch_btn = findViewById(R.id.switch_btn);

        function_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, FunctionActivity.class));
            }
        });

        if (!smsFileSms.exists()) {

            if (new File("/data/data/com.termux/files/usr/bin/pkg").exists()) {

                if (!smsFileSms1.exists()) {
                    smsFileSms1.mkdirs();
                }

                try {
                    InputStream xinhao_han_sms = getAssets().open("XINHAO_HAN_Sms");

                    int len = 0;
                    smsFileSms.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(smsFileSms);
                    while ((len = xinhao_han_sms.read()) != -1) {
                        fileOutputStream.write(len);
                    }

                    fileOutputStream.flush();
                    fileOutputStream.close();

                    Runtime.getRuntime().exec("chmod 777 " + smsFileSms.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();

                    Log.e("XINHAO_HAN", "onCreate: " + e.toString());
                }
            }


        }


        // startActivity(new Intent(this, DownloadActivity.class));

        try {
            File file = new File("/data/data/com.termux/files/xinhao_system.infoJson");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            String temp = "";
            String tempStr = "";

            while ((temp = bufferedReader.readLine()) != null) {
                tempStr += temp;
            }
            bufferedReader.close();

            CreateSystemBean createSystemBean = new Gson().fromJson(tempStr, CreateSystemBean.class);

            // Log.e("XINHAO_HAN", "onCreate: " +   tempStr);
/*
            Toast.makeText(this, "当前进入系统:" + createSystemBean.systemName, Toast.LENGTH_LONG).show();

            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("目前运行的系统");

            ab.setMessage("目前运行的系统:" + createSystemBean.systemName + "\n如果需要添加linux操作系统\n请在主页面左边缘\n由左往右滑,点击\"切换linux系统\"");

            ab.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();
                }
            });

            ab.show();*/


            //  Log.e("XINHAO_HAN", "onCreate: " +   createSystemBean.systemName);

        } catch (FileNotFoundException e) {
            // Log.e("XINHAO_HAN", "onCreate: " +   e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            // Log.e("XINHAO_HAN", "onCreate: " +   e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            // Log.e("XINHAO_HAN", "onCreate: " +   e.toString());

        }
      /*  apkFilePath = new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/termux.apk").getAbsolutePath();

        //检查版本升级

        CheckUpDateCodeUtils.updateCode(new CheckUpDateCodeUtils.HttpCode() {
            @Override
            public void onRes(String msg) {

                UpDateBean upDateBean = new Gson().fromJson(msg, UpDateBean.class);

                if (UpDateHttpCode.CODE < upDateBean.code) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                            ab.setTitle("升级提醒");

                            ab.setMessage(upDateBean.msg);

                            ab.setCancelable(false);

                            ab.setNegativeButton("立即升级", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    download_text.setVisibility(View.VISIBLE);
                                    CheckUpDateCodeUtils.update(upDateBean.msg1, new CheckUpDateCodeUtils.Pro() {
                                        @Override
                                        public void size(int size) {


                                        }

                                        @Override
                                        public void thisSize(int postion, int size) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                  //  ab.setMessage("正在下载[总大小]:" + size + "/" + postion);

                                                    download_text.setText("正在下载[总大小]:" + size + "/" + postion);
                                                }
                                            });
                                        }

                                        @Override
                                        public void error(String msg) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    download_text.setVisibility(View.GONE);
                                                   // Toast.makeText(TermuxActivity.this, "下载失败!" + msg, Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                        @Override
                                        public void com() {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                   // Toast.makeText(mTermService, "下载完成!正在安装", Toast.LENGTH_SHORT).show();
                                                    download_text.setVisibility(View.GONE);
                                                    checkIsAndroidO();
                                                   // CheckUpDateCodeUtils.installApk(TermuxActivity.this, apkFilePath);
                                                }
                                            });
                                        }
                                    });


                                }
                            });

                            ab.setPositiveButton("取消升级", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ab.create().dismiss();
                                }
                            });
                            ab.show();


                        }
                    });


                }


            }

            @Override
            public void onError(String errorMsg) {

            }
        });
*/
        switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, SwitchActivity.class));
            }
        });


        repair_bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("请注意");

                ab.setMessage("如果你都开始尝试暴力修复了，\n成功率 30%\n如果暴力修复还是不能解决，那就下载恢复包吧\n但是不保证你装的某些东西会正常工作 \n这个是你在无法进入系统的\n时候使用的\n没事，别手痒点这个，系统\n能正常进入，不要点这个试着玩\n是否开始？");

                ab.setNegativeButton("开始进行", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        TermuxInstaller.setupIfNeededbl(TermuxActivity.this, new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(mTermService, "暴力修复完成,请重启APP", Toast.LENGTH_SHORT).show();
                                        ab.create().dismiss();
                                    }
                                });
                            }
                        });
                    }
                });

                ab.setPositiveButton("不要进行暴力恢复", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });

                ab.show();

            }
        });

        repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDrawer().closeDrawer(Gravity.LEFT);
                String[] strings = {
                    "[*]一键安装msf   [./msfvenom.sh][有 linux 基础的装]",
                    "[*]一键安装qemu ↓[关联][不可用]",
                    "[*]一键安装unstable-repo ↑[关联][不可用]",
                    "[*]一键安装 x11-repo",
                    "[*]一键安装 tigervnc fluxbox [VNC]",
                    "[*]一键安装msf    [直接命令启动 常用 ]",
                    "[*]一键安装msf    [执行:msfconsole ] [备用安装  ]",
                    "[*]一键安装msf    [执行:msfconsole ] [备用安装2 ]",
                    "[*]一键安装msf5   [执行:msfconsole ] [Metasploit Framework 5]",
                    "[*]一键安装JDK8   [执行:   java    ] [java]"/*,
                    "[*]一键安装xfce4  [执行:需要配合vnc ] [xfce4]"*/
                };
                AlertDialog.Builder builder = new AlertDialog
                    .Builder(TermuxActivity.this);
                builder.setTitle("请选中一个[需要VPN否则很慢]");
                // builder.setMessage("这是个滚动列表，往下滑");
                builder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(TermuxActivity.this, "选择了第" + which + "个", Toast.LENGTH_SHORT).show();

                        installGui(which);
                    }
                });
                builder.setNegativeButton("我还没想好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermuxActivity.this, FileManagerActivity.class);
                Uri build = new Uri.Builder().path("/data/data/com.termux/files").build();
                intent.setData(build);
                startActivity(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, SettingActivity.class));
            }
        });
        animImage();
        temp(false);
        //isUser();
        //判断文件是否存在
        //writerFile();
        getSms();
    }


    private void installGui(int index) {


        switch (index) {


            case 0:
                File file = new File("/data/data/com.termux/files/home/apk.rb");
                File file1 = new File("/data/data/com.termux/files/home/database.yml");
                File file2 = new File("/data/data/com.termux/files/home/metasploit.sh");
                writerFile("apk.rb", file);
                writerFile("database.yml", file1);
                writerFile("metasploit.sh", file2);

                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 apk.rb \n");
                mTerminalView.sendTextToTerminal("chmod 777 database.yml \n");
                mTerminalView.sendTextToTerminal("chmod 777 metasploit.sh \n");
                mTerminalView.sendTextToTerminal("./metasploit.sh \n");

                break;
            case 1:

                mTerminalView.sendTextToTerminal("pkg install curl && curl -fsSL https://its-pointless.github.io/setup-pointless-repo.sh | bash && pkg install qemu-system-x86_64\n");
                break;
            case 2:
                mTerminalView.sendTextToTerminal("pkg install curl && curl -fsSL https://its-pointless.github.io/setup-pointless-repo.sh | bash && pkg install qemu-system-x86_64\n");
                break;
            case 3:
                mTerminalView.sendTextToTerminal("pkg install x11-repo \n");
                break;
            case 4:
                mTerminalView.sendTextToTerminal("pkg install tigervnc fluxbox \n");
                break;
            case 5:
                writerFile("metasploit_cy.sh", new File("/data/data/com.termux/files/home/metasploit.sh"));
                writerFile("x32", new File("/data/data/com.termux/files/home/x32"));
                writerFile("x64", new File("/data/data/com.termux/files/home/x64"));
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 metasploit.sh \n");
                mTerminalView.sendTextToTerminal("chmod 777 x32 \n");
                mTerminalView.sendTextToTerminal("chmod 777 x64 \n");
                mTerminalView.sendTextToTerminal("./metasploit.sh \n");
                break;
            case 6:
                writerFile("metasploit_cy2.sh", new File("/data/data/com.termux/files/home/metasploit.sh"));
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 metasploit.sh \n");
                mTerminalView.sendTextToTerminal("./metasploit.sh \n");
                break;
            case 7:
                writerFile("metasploit_cy3.sh", new File("/data/data/com.termux/files/home/metasploit.sh"));
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 metasploit.sh \n");
                mTerminalView.sendTextToTerminal("./metasploit.sh \n");
                break;
            //
            case 8:
                writerFile("metasploit_cy4.sh", new File("/data/data/com.termux/files/home/metasploit.sh"));
                writerFile("postgresql_ctl.sh", new File("/data/data/com.termux/files/home/postgresql_ctl.sh"));
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 metasploit.sh \n");
                mTerminalView.sendTextToTerminal("chmod 777 postgresql_ctl.sh \n");
                mTerminalView.sendTextToTerminal("./metasploit.sh \n");
                break;
            case 9:
                AlertDialog.Builder ab = new AlertDialog.Builder(this);

                ab.setTitle("提示");

                ab.setMessage("执行 java 前 先执行 :termux-chroot\n否则会出现:bad call");

                ab.setNeutralButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nethunter.sh
                        File fileArch = new File("/data/data/com.termux/files/home/installjava");
                        writerFile("installjava", fileArch);
                        execFile(new File("/data/data/com.termux/files/home/installjava"), "bash installjava");

                        ab.create().dismiss();
                    }
                });

                ab.show();
                break;

            case 10:
                mTerminalView.sendTextToTerminal("printf \"deb https://hax4us.github.io/termux-x/x11-stable main\" > $PREFIX/etc/apt/sources.list && apt install x11-repo && wget https://hax4us.github.io/termux-x/hax4us.key && apt-key add hax4us.key && apt update && apt install xfce4 \n");
                break;

        }


    }


    private void installSystem(int index) {


        File fileProot = new File("/data/data/com.termux/files/usr/bin/termux-chroot");
        File fileWget = new File("/data/data/com.termux/files/usr/bin/wget");

        if (!fileProot.exists() || !fileWget.exists()) {


            AlertDialog.Builder ab = new AlertDialog.Builder(this);

            ab.setTitle("环境不达要求");

            ab.setMessage("你没有安装 wget 或者 proot \n是否立即安装");

            ab.setNegativeButton("给我安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mTerminalView.sendTextToTerminal("pkg in wget proot -y" + "\n");
                    ab.create().dismiss();
                }
            });

            ab.setPositiveButton("不安装", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ab.create().dismiss();
                }
            });

            ab.show();


            return;
        }


        switch (index) {

            //ubuntu
            case 0:


                File file = new File("/data/data/com.termux/files/home/resolv.conf");
                writerFile("resolv.conf", file);
                File file1 = new File("/data/data/com.termux/files/home/ubuntu.sh");

                if (file1.exists()) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(this);

                    ab.setTitle("提示");

                    ab.setMessage("你貌似已安装过 ubuntu 系统，无需再次安装 \n如果需要重新安装,请点击'强制重装'\n会删除你以前的所有数据，请慎重!");

                    ab.setPositiveButton("强制重装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mTerminalView.sendTextToTerminal("cd ~" + "\n");
                            mTerminalView.sendTextToTerminal("rm ubuntu.sh" + "\n");
                            mTerminalView.sendTextToTerminal("rm resolv.conf" + "\n");
                            mTerminalView.sendTextToTerminal("rm start.sh" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf ubuntu-binds" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf ubuntu-fs" + "\n");
                            Toast.makeText(TermuxActivity.this, "数据清除成功!请重新进入,系统安装窗口", Toast.LENGTH_LONG).show();
                            ab.create().dismiss();

                        }
                    });

                    ab.setNegativeButton("不需要重装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });

                    ab.show();

                    return;
                }


                writerFile("ubuntu.sh", file1);
                execFile(new File("/data/data/com.termux/files/home/ubuntu.sh"));
                break;
            case 1:


                File file2 = new File("/data/data/com.termux/files/home/deboot_debian");

                if (file2.exists()) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(this);

                    ab.setTitle("提示");

                    ab.setMessage("你貌似已安装过 debian 系统，无需再次安装 \n如果需要重新安装,请点击'强制重装'\n会删除你以前的所有数据，请慎重!");

                    ab.setPositiveButton("强制重装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mTerminalView.sendTextToTerminal("cd ~" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf deboot_debian" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf debootstrap" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf debootstrap-1.0.115" + "\n");
                            mTerminalView.sendTextToTerminal("rm debian_on_termux.sh" + "\n");
                            Toast.makeText(TermuxActivity.this, "数据清除成功!请重新进入,系统安装窗口", Toast.LENGTH_LONG).show();
                            ab.create().dismiss();

                        }
                    });

                    ab.setNegativeButton("不需要重装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });

                    ab.setNeutralButton("忽略警告继续安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File fileDebian = new File("/data/data/com.termux/files/home/debian_on_termux.sh");
                            writerFile("debian_on_termux.sh", fileDebian);
                            execFile(new File("/data/data/com.termux/files/home/debian_on_termux.sh"));


                            ab.create().dismiss();
                        }
                    });

                    ab.show();

                    return;
                }

                File fileDebian = new File("/data/data/com.termux/files/home/debian_on_termux.sh");
                writerFile("debian_on_termux.sh", fileDebian);
                execFile(new File("/data/data/com.termux/files/home/debian_on_termux.sh"));
                break;
            case 2:

                File file3 = new File("/data/data/com.termux/files/home/fedora");

                if (file3.exists()) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(this);

                    ab.setTitle("提示");

                    ab.setMessage("你貌似已安装过 fedora 系统，无需再次安装 \n如果需要重新安装,请点击'强制重装'\n会删除你以前的所有数据，请慎重!");

                    ab.setPositiveButton("强制重装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mTerminalView.sendTextToTerminal("cd ~" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf fedora" + "\n");
                            mTerminalView.sendTextToTerminal("rm termux-fedora.sh" + "\n");
                            Toast.makeText(TermuxActivity.this, "数据清除成功!请重新进入,系统安装窗口", Toast.LENGTH_LONG).show();
                            ab.create().dismiss();

                        }
                    });

                    ab.setNegativeButton("不需要重装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });

                    ab.setNeutralButton("忽略警告继续安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File fileFedora = new File("/data/data/com.termux/files/home/termux-fedora.sh");
                            writerFile("termux-fedora.sh", fileFedora);
                            execFile(new File("/data/data/com.termux/files/home/termux-fedora.sh"), "./termux-fedora.sh f30_arm64");

                            ab.create().dismiss();
                        }
                    });


                    ab.show();

                    return;
                }

                //termux-fedora.sh
                File fileFedora = new File("/data/data/com.termux/files/home/termux-fedora.sh");
                writerFile("termux-fedora.sh", fileFedora);
                execFile(new File("/data/data/com.termux/files/home/termux-fedora.sh"), "./termux-fedora.sh f30_arm64");

                break;
            case 3:

                File file4 = new File("/data/data/com.termux/files/home/kali-arm64");
                File file5 = new File("/data/data/com.termux/files/home/nethunter-fs");

                if (file4.exists() || file5.exists()) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(this);

                    ab.setTitle("提示");

                    ab.setMessage("你貌似已安装过 kali 系统，无需再次安装 \n如果需要重新安装,请点击'强制重装'\n会删除你以前的所有数据，请慎重!");

                    ab.setPositiveButton("强制重装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            mTerminalView.sendTextToTerminal("cd ~" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf kali-arm64" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf nethunter-binds" + "\n");
                            mTerminalView.sendTextToTerminal("rm -rf nethunter-fs" + "\n");
                            mTerminalView.sendTextToTerminal("rm nethunter.sh" + "\n");
                            Toast.makeText(TermuxActivity.this, "数据清除成功!请重新进入,系统安装窗口", Toast.LENGTH_LONG).show();
                            ab.create().dismiss();

                        }
                    });

                    ab.setNegativeButton("不需要重装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });

                    ab.setNeutralButton("忽略警告继续安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File fileKail = new File("/data/data/com.termux/files/home/nethunter.sh");
                            writerFile("nethunter.sh", fileKail);
                            execFile(new File("/data/data/com.termux/files/home/nethunter.sh"));


                            ab.create().dismiss();
                        }
                    });


                    ab.show();

                    return;
                }

                //nethunter.sh
                File fileKail = new File("/data/data/com.termux/files/home/nethunter.sh");
                writerFile("nethunter.sh", fileKail);
                execFile(new File("/data/data/com.termux/files/home/nethunter.sh"));


                break;
            case 4:

                //nethunter.sh
                File fileContos = new File("/data/data/com.termux/files/home/atilo");
                writerFile("atilo", fileContos);
                execFile(new File("/data/data/com.termux/files/home/atilo"), "./atilo install centos");


                break;
            case 5:

                AlertDialog.Builder ab = new AlertDialog.Builder(this);

                ab.setTitle("提示");

                ab.setMessage("执行 java 前 先执行 :termux-chroot\n否则会出现:bad call");

                ab.setNeutralButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //nethunter.sh
                        File fileArch = new File("/data/data/com.termux/files/home/installjava");
                        writerFile("installjava", fileArch);
                        execFile(new File("/data/data/com.termux/files/home/installjava"), "bash installjava");

                        ab.create().dismiss();
                    }
                });

                ab.show();


                break;
            case 6:
                notifyUserToChooseDistro();
                Toast.makeText(this, "原项目作者: exalab999\n特此感谢!", Toast.LENGTH_SHORT).show();
                break;

            case 7:
                Toast.makeText(this, "原项目作者: exalab999\n特此感谢!", Toast.LENGTH_SHORT).show();
                notifyUserToChooseDistro1();
                break;

            case 8:
                File fileAPK = new File("/data/data/com.termux/files/home/apktool-2.2.0.jar");
                writerFile("apktool-2.2.0.jar", fileAPK, 2048);
                mTerminalView.sendTextToTerminal("cd ~");
                mTerminalView.sendTextToTerminal("chmod 777 apktool-2.2.0.jar");
                break;
            case 9:
                File fileAtilo = new File("/data/data/com.termux/files/home/atilo");
                writerFile("atilo", fileAtilo);
                mTerminalView.sendTextToTerminal("cd ~");
                mTerminalView.sendTextToTerminal("chmod 777 atilo");
                break;
            case 10:
                File fileDDOS = new File("/data/data/com.termux/files/usr/bin/ddos");
                File fileDdosBrute = new File("/data/data/com.termux/files/usr/bin/ddosbrute");
                File fileDDOSer = new File("/data/data/com.termux/files/usr/bin/ddoser");
                File headers = new File("/data/data/com.termux/files/usr/bin/headers.txt");
                File showfile = new File("/data/data/com.termux/files/usr/bin/showfile.sh");
                File version = new File("/data/data/com.termux/files/usr/bin/version");
                writerFile("ddos", fileDDOS);
                writerFile("ddosbrute", fileDdosBrute);
                writerFile("ddoser", fileDDOSer);
                writerFile("headers.txt", headers);
                writerFile("showfile.sh", showfile);
                writerFile("version", version);

                try {
                    Runtime.getRuntime().exec("chmod 777 " + fileDDOS.getAbsolutePath());
                    Runtime.getRuntime().exec("chmod 777 " + fileDdosBrute.getAbsolutePath());
                    Runtime.getRuntime().exec("chmod 777 " + fileDDOSer.getAbsolutePath());
                    Runtime.getRuntime().exec("chmod 777 " + headers.getAbsolutePath());
                    Runtime.getRuntime().exec("chmod 777 " + showfile.getAbsolutePath());
                    Runtime.getRuntime().exec("chmod 777 " + version.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }


                Toast.makeText(this, "安装成功!您可以运行[ddos]命令来验证", Toast.LENGTH_SHORT).show();

                break;

            case 11:


                String[] msg = {
                    "Ubuntu",
                    "Debian",
                    "Kali",
                    "Nethunter",
                    "Parrot",
                    "BackBox",
                    "Fedora",
                    "CentOS",
                    "Leap",
                    "Tumbleweed",
                    "openSUSE",
                    "Arch",
                    "Alpine"
                };

                AlertDialog.Builder builder = new AlertDialog
                    .Builder(TermuxActivity.this);
                builder.setTitle("选择一个要卸载的系统");
                // builder.setMessage("这是个滚动列表，往下滑");
                builder.setItems(msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        switch (which) {


                            case 0:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/Ubuntu/UNI-ubuntu.sh && bash UNI-ubuntu.sh" + "\n");
                                break;
                            case 1:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/Debian/UNI-debian.sh && bash UNI-debian.sh" + "\n");
                                break;
                            case 2:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/Kali/UNI-kali.sh && bash UNI-kali.sh" + "\n");
                                break;
                            case 3:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/Nethunter/UNI-nethunter.sh && bash UNI-nethunter.sh" + "\n");
                                break;
                            case 4:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/Parrot/UNI-parrot.sh && bash UNI-parrot.sh" + "\n");
                                break;
                            case 5:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/BackBox/UNI-backbox.sh && bash UNI-backbox.sh" + "\n");
                                break;
                            case 6:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/Fedora/UNI-fedora.sh && bash UNI-fedora.sh" + "\n");
                                break;
                            case 7:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/CentOS/UNI-centos.sh && bash UNI-centos.sh" + "\n");
                                break;
                            case 8:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/openSUSE/Leap/UNI-opensuse-leap.sh && bash UNI-opensuse-leap.sh" + "\n");
                                break;
                            case 9:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/openSUSE/Tumbleweed/UNI-opensuse-tumbleweed.sh && bash UNI-opensuse-tumbleweed.sh" + "\n");
                                break;
                            case 10:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/openSUSE/armhf/UNI-opensuse.sh && bash UNI-opensuse.sh" + "\n");
                                break;
                            case 11:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/Arch/UNI-arch.sh && bash UNI-arch.sh" + "\n");
                                break;
                            case 12:
                                mTerminalView.sendTextToTerminal("wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Uninstaller/Alpine/UNI-alpine.sh && bash UNI-alpine.sh" + "\n");
                                break;

                        }

                    }
                });
                builder.setNegativeButton("我还没想好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                break;
            case 12:
                File file6 = new File("/data/data/com.termux/files/home/install_gui.sh");
                writerFile("install_gui.sh", file6);
                execFile(file6);

                break;

            /**
             *  "[ubuntu]乌班图发行版[14.04][./start.sh]",
             *  "[ubuntu]乌班图发行版[16.04][./start.sh]",
             *  "[ubuntu]乌班图发行版[18.04][./start.sh]"
             *
             */
            case 13:
                writerFile("ubuntu_14.sh", new File("/data/data/com.termux/files/home/ubuntu_14.sh"));
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 ubuntu_14.sh \n");
                mTerminalView.sendTextToTerminal("./ubuntu_14.sh \n");
                break;
            case 14:
                writerFile("ubuntu_16.sh", new File("/data/data/com.termux/files/home/ubuntu_16.sh"));
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 ubuntu_16.sh \n");
                mTerminalView.sendTextToTerminal("./ubuntu_16.sh \n");
                break;
            case 15:
                writerFile("ubuntu_18.sh", new File("/data/data/com.termux/files/home/ubuntu_18.sh"));
                mTerminalView.sendTextToTerminal("cd ~ \n");
                mTerminalView.sendTextToTerminal("chmod 777 ubuntu_18.sh \n");
                mTerminalView.sendTextToTerminal("./ubuntu_18.sh \n");
                /////////////////////////////////////////////////////////////////////////////////////////////////////////////
                break;

        }

    }

    String desktop = "Nothing";

    public void notifyUserToChooseDistro1() {
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.desktop_environment_chooser, nullParent);
        final CheckBox checkBox = view.findViewById(R.id.checkBox);
        final CheckBox checkBox2 = view.findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = view.findViewById(R.id.checkBox3);
        final CheckBox checkBox4 = view.findViewById(R.id.checkBox4);
        final CheckBox checkBox5 = view.findViewById(R.id.checkBox5);
        final CheckBox checkBox6 = view.findViewById(R.id.checkBox6);
        final CheckBox checkBox7 = view.findViewById(R.id.checkBox7);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        s = Build.SUPPORTED_ABIS[0];
        if (distro.equals("Ubuntu")) {
            checkBox.setChecked(true);
        } else if (distro.equals("Debian")) {
            checkBox2.setChecked(true);
        } else if (distro.equals("Kali")) {
            checkBox3.setChecked(true);
        } else if (distro.equals("Parrot")) {
            checkBox4.setChecked(true);
        } else if (distro.equals("BackVox")) {
            checkBox5.setChecked(true);
        } else if (distro.equals("Fedora")) {
            checkBox6.setChecked(true);
        } else if (distro.equals("Arch")) {
            checkBox7.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
            }
        });
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
            }
        });
        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
            }
        });
        checkBox6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox7.setChecked(false);
            }
        });
        checkBox7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
            }
        });
        if (s.equals("i386")) {
            checkBox6.setEnabled(false);
            checkBox6.setText(R.string.not_Supported);
            checkBox7.setEnabled(false);
            checkBox7.setText(R.string.not_Supported);
        }
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (checkBox.isChecked()) {
                    if (!distro.equals("Ubuntu")) {
                        shouldShowAds = true;
                        distro = "Ubuntu";
                    }
                } else if (checkBox2.isChecked()) {
                    if (!distro.equals("Debian")) {
                        shouldShowAds = true;
                        distro = "Debian";
                    }
                } else if (checkBox3.isChecked()) {
                    if (!distro.equals("Kali")) {
                        shouldShowAds = true;
                        distro = "Kali";
                    }
                } else if (checkBox4.isChecked()) {
                    if (!distro.equals("Parrot")) {
                        shouldShowAds = true;
                        distro = "Parrot";
                    }
                } else if (checkBox5.isChecked()) {
                    if (!distro.equals("BackBox")) {
                        shouldShowAds = true;
                        distro = "BackBox";
                    }
                } else if (checkBox6.isChecked()) {
                    if (!distro.equals("Fedora")) {
                        shouldShowAds = true;
                        distro = "Fedora";
                    }
                } else if (checkBox7.isChecked()) {
                    if (!distro.equals("Arch")) {
                        shouldShowAds = true;
                        distro = "Arch";
                    }
                }

                desktop = "Nothing";


                dialog.dismiss();
                notifyUserToChooseDesktop();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    public void notifyUserToChooseDesktop() {
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.desktop_environment_chooser2, nullParent);
        final CheckBox checkBox = view.findViewById(R.id.checkBox);
        final CheckBox checkBox2 = view.findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = view.findViewById(R.id.checkBox3);
        final CheckBox checkBox4 = view.findViewById(R.id.checkBox4);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        if (desktop.equals("Xfce4")) {
            if (!distro.equals("Arch")) {
                checkBox.setChecked(true);
            }
        } else if (desktop.equals("Mate")) {
            if (!distro.equals("Arch")) {
                checkBox2.setChecked(true);
            }
        } else if (desktop.equals("LXQt")) {
            if (!distro.equals("Arch")) {
                checkBox3.setChecked(true);
            }
        } else if (desktop.equals("LXDE")) {
            checkBox4.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox4.setChecked(false);
            }
        });
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
            }
        });
        if (distro.equals("Arch")) {
            checkBox.setText(R.string.not_available);
            checkBox2.setText(R.string.not_available);
            checkBox3.setText(R.string.not_available);
            checkBox.setEnabled(false);
            checkBox2.setEnabled(false);
            checkBox3.setEnabled(false);
        }
        alertDialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (checkBox.isChecked()) {
                    if (!desktop.equals("Xfce4")) {
                        shouldShowAds = true;
                        desktop = "Xfce4";
                    }
                } else if (checkBox2.isChecked()) {
                    if (!desktop.equals("Mate")) {
                        shouldShowAds = true;
                        desktop = "Mate";
                    }
                } else if (checkBox3.isChecked()) {
                    if (!desktop.equals("LXQt")) {
                        shouldShowAds = true;
                        desktop = "LXQt";
                    }
                } else if (checkBox4.isChecked()) {
                    if (!desktop.equals("LXDE")) {
                        shouldShowAds = true;
                        desktop = "LXDE";
                    }
                }
                if (distro.equals("Ubuntu")) {
                    if (desktop.equals("Xfce4")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/Xfce4/de-apt-xfce4.sh && bash de-apt-xfce4.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-ubuntu.sh"));
                    } else if (desktop.equals("Mate")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/Mate/de-apt-mate.sh && bash de-apt-mate.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-ubuntu.sh"));
                    } else if (desktop.equals("LXQt")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/LXQt/de-apt-lxqt.sh && bash de-apt-lxqt.sh" + "\n");
                        //textView4.setText(getString(R.string.de_step3, "./start-ubuntu.sh"));
                    } else if (desktop.equals("LXDE")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/LXDE/de-apt-lxde.sh && bash de-apt-lxde.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-ubuntu.sh"));
                    }
                } else if (distro.equals("Debian")) {
                    if (desktop.equals("Xfce4")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/Xfce4/de-apt-xfce4.sh && bash de-apt-xfce4.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-debian.sh"));
                    } else if (desktop.equals("Mate")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/Mate/de-apt-mate.sh && bash de-apt-mate.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-debian.sh"));
                    } else if (desktop.equals("LXQt")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/LXQt/de-apt-lxqt.sh && bash de-apt-lxqt.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-debian.sh"));
                    } else if (desktop.equals("LXDE")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/LXDE/de-apt-lxde.sh && bash de-apt-lxde.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-debian.sh"));
                    }
                } else if (distro.equals("Kali")) {
                    if (desktop.equals("Xfce4")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/Xfce4/de-apt-xfce4.sh && bash de-apt-xfce4.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-kali.sh"));
                    } else if (desktop.equals("Mate")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/Mate/de-apt-mate.sh && bash de-apt-mate.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-kali.sh"));
                    } else if (desktop.equals("LXQt")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/LXQt/de-apt-lxqt.sh && bash de-apt-lxqt.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-kali.sh"));
                    } else if (desktop.equals("LXDE")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/LXDE/de-apt-lxde.sh && bash de-apt-lxde.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-kali.sh"));
                    }
                } else if (distro.equals("Parrot")) {
                    if (desktop.equals("Xfce4")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/Xfce4/de-apt-xfce4.sh && bash de-apt-xfce4.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-parrot.sh"));
                    } else if (desktop.equals("Mate")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/Mate/de-apt-mate.sh && bash de-apt-mate.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-parrot.sh"));
                    } else if (desktop.equals("LXQt")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/LXQt/de-apt-lxqt.sh && bash de-apt-lxqt.sh" + "\n");
                        // textView4.setText(getString(R.string.de_step3, "./start-parrot.sh"));
                    } else if (desktop.equals("LXDE")) {
                        mTerminalView.sendTextToTerminal("apt-get update && apt-get install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Apt/LXDE/de-apt-lxde.sh && bash de-apt-lxde.sh" + "\n");
                        //textView4.setText(getString(R.string.de_step3, "./start-parrot.sh"));
                    }
                } else if (distro.equals("Fedora")) {
                    if (s.contains("arm") && !s.equals("arm64-v8a")) {
                        if (desktop.equals("Xfce4")) {
                            mTerminalView.sendTextToTerminal("yum install wget --forcearch=armv7hl -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Yum/Fedora/arm/Xfce4/de-yum-xfce4.sh && bash de-yum-xfce4.sh" + "\n");
                            //  textView4.setText(getString(R.string.de_step3, "./start-fedora.sh"));
                        } else if (desktop.equals("Mate")) {
                            mTerminalView.sendTextToTerminal("yum install wget --forcearch=armv7hl -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Yum/Fedora/arm/Mate/de-yum-mate.sh && bash de-yum-mate.sh" + "\n");
                            // textView4.setText(getString(R.string.de_step3, "./start-fedora.sh"));
                        } else if (desktop.equals("LXQt")) {
                            mTerminalView.sendTextToTerminal("yum install wget --forcearch=armv7hl -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Yum/Fedora/arm/LXQt/de-yum-lxqt.sh && bash de-yum-lxqt.sh" + "\n");
                            //  textView4.setText(getString(R.string.de_step3, "./start-fedora.sh"));
                        } else if (desktop.equals("LXDE")) {
                            mTerminalView.sendTextToTerminal("yum install wget --forcearch=armv7hl -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Yum/Fedora/arm/LXDE/de-yum-lxde.sh && bash de-yum-lxde.sh" + "\n");
                            // textView4.setText(getString(R.string.de_step3, "./start-fedora.sh"));
                        }
                    } else {
                        if (desktop.equals("Xfce4")) {
                            mTerminalView.sendTextToTerminal("yum install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Yum/Fedora/Xfce4/de-yum-xfce4.sh && bash de-yum-xfce4.sh" + "\n");
                            //  textView4.setText(getString(R.string.de_step3, "./start-fedora.sh"));
                        } else if (desktop.equals("Mate")) {
                            mTerminalView.sendTextToTerminal("yum install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Yum/Fedora/Mate/de-yum-mate.sh && bash de-yum-mate.sh" + "\n");
                            // textView4.setText(getString(R.string.de_step3, "./start-fedora.sh"));
                        } else if (desktop.equals("LXQt")) {
                            mTerminalView.sendTextToTerminal("yum install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Yum/Fedora/LXQt/de-yum-lxqt.sh && bash de-yum-lxqt.sh" + "\n");
                            //textView4.setText(getString(R.string.de_step3, "./start-fedora.sh"));
                        } else if (desktop.equals("LXDE")) {
                            mTerminalView.sendTextToTerminal("yum install wget -y && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Yum/Fedora/LXDE/de-yum-lxde.sh && bash de-yum-lxde.sh" + "\n");
                            // textView4.setText(getString(R.string.de_step3, "./start-fedora.sh"));
                        }
                    }
                } else if (distro.equals("Arch")) {
                    if (s.contains("arm")) {
                        mTerminalView.sendTextToTerminal("pacman -Sy --noconfirm wget && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Pacman/de-pac.sh && bash de-pac.sh" + "\n");
                        //textView4.setText(getString(R.string.de_step3, "./start-arch.sh"));
                    } else {
                        mTerminalView.sendTextToTerminal("pacman -Sy --noconfirm wget && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/DesktopEnvironment/Pacman/de-pac.sh && bash de-pac.sh" + "\n");
                        //textView4.setText(getString(R.string.de_step3, "./start-arch.sh"));
                    }
                }

                dialog.dismiss();
            }
        });
        alertDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    String distro = "Nothing";
    String s;
    boolean shouldShowAds;
    boolean isOreoNotified;
    boolean isNethunterNotified;
    SharedPreferences sharedPreferences;

    public void notifyUserToChooseDistro() {
        final ViewGroup nullParent = null;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.distro_chooser, nullParent);
        final CheckBox checkBox = view.findViewById(R.id.checkBox);
        final CheckBox checkBox2 = view.findViewById(R.id.checkBox2);
        final CheckBox checkBox3 = view.findViewById(R.id.checkBox3);
        final CheckBox checkBox4 = view.findViewById(R.id.checkBox4);
        final CheckBox checkBox5 = view.findViewById(R.id.checkBox5);
        final CheckBox checkBox6 = view.findViewById(R.id.checkBox6);
        final CheckBox checkBox7 = view.findViewById(R.id.checkBox7);
        final CheckBox checkBox8 = view.findViewById(R.id.checkBox8);
        final CheckBox checkBox9 = view.findViewById(R.id.checkBox9);
        final CheckBox checkBox10 = view.findViewById(R.id.checkBox10);
        final CheckBox checkBox11 = view.findViewById(R.id.checkBox11);
        final CheckBox checkBox12 = view.findViewById(R.id.checkBox12);
        final CheckBox checkBox13 = view.findViewById(R.id.checkBox13);

        alertDialog.setView(view);
        alertDialog.setCancelable(false);

        if (distro.equals("Ubuntu")) {
            checkBox.setChecked(true);
        } else if (distro.equals("Debian")) {
            checkBox2.setChecked(true);
        } else if (distro.equals("Kali")) {
            checkBox3.setChecked(true);
        } else if (distro.equals("Nethunter")) {
            checkBox4.setChecked(true);
        } else if (distro.equals("Parrot")) {
            checkBox5.setChecked(true);
        } else if (distro.equals("BackBox")) {
            checkBox6.setChecked(true);
        } else if (distro.equals("Fedora")) {
            checkBox7.setChecked(true);
        } else if (distro.equals("CentOS")) {
            checkBox8.setChecked(true);
        } else if (distro.equals("Leap")) {
            checkBox9.setChecked(true);
        } else if (distro.equals("Tumbleweed")) {
            checkBox10.setChecked(true);
        } else if (distro.equals("Arch")) {
            checkBox11.setChecked(true);
        } else if (distro.equals("BlackArch")) {
            checkBox12.setChecked(true);
        } else if (distro.equals("Alpine")) {
            checkBox13.setChecked(true);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox12.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox13.setChecked(false);
            }
        });
        checkBox13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
                checkBox5.setChecked(false);
                checkBox6.setChecked(false);
                checkBox7.setChecked(false);
                checkBox8.setChecked(false);
                checkBox9.setChecked(false);
                checkBox10.setChecked(false);
                checkBox11.setChecked(false);
                checkBox12.setChecked(false);
            }
        });

        s = Build.SUPPORTED_ABIS[0];
        alertDialog.setPositiveButton("我想好了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (checkBox.isChecked()) {
                    if (!distro.equals("Ubuntu")) {
                        shouldShowAds = true;
                        distro = "Ubuntu";
                    }
                } else if (checkBox2.isChecked()) {
                    if (!distro.equals("Debian")) {
                        shouldShowAds = true;
                        distro = "Debian";
                    }
                } else if (checkBox3.isChecked()) {
                    if (!distro.equals("Kali")) {
                        shouldShowAds = true;
                        distro = "Kali";
                    }
                } else if (checkBox4.isChecked()) {
                    if (!distro.equals("Nethunter")) {
                        shouldShowAds = true;
                        distro = "Nethunter";
                        if (!isNethunterNotified) {
                            // notifyUserForNethunter();
                            Toast.makeText(TermuxActivity.this, "不存在安装选项", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (checkBox5.isChecked()) {
                    if (!distro.equals("Parrot")) {
                        shouldShowAds = true;
                        distro = "Parrot";
                    }
                } else if (checkBox6.isChecked()) {
                    if (!distro.equals("BackBox")) {
                        shouldShowAds = true;
                        distro = "BackBox";
                    }
                } else if (checkBox7.isChecked()) {
                    if (!distro.equals("Fedora")) {
                        shouldShowAds = true;
                        distro = "Fedora";
                    }
                } else if (checkBox8.isChecked()) {
                    if (!distro.equals("CentOS")) {
                        shouldShowAds = true;
                        distro = "CentOS";
                    }
                } else if (checkBox9.isChecked()) {
                    if (!distro.equals("Leap")) {
                        shouldShowAds = true;
                        distro = "Leap";
                    }
                } else if (checkBox10.isChecked()) {
                    if (!distro.equals("Tumbleweed")) {
                        shouldShowAds = true;
                        distro = "Tumbleweed";
                    }
                } else if (checkBox11.isChecked()) {
                    if (!distro.equals("Arch")) {
                        shouldShowAds = true;
                        distro = "Arch";
                    }
                } else if (checkBox12.isChecked()) {
                    if (!distro.equals("BlackArch")) {
                        shouldShowAds = true;
                        distro = "BlackArch";
                    }
                } else if (checkBox13.isChecked()) {
                    if (!distro.equals("Alpine")) {
                        shouldShowAds = true;
                        distro = "Alpine";
                    }
                }

                if (distro.equals("Ubuntu")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Ubuntu/ubuntu.sh && bash ubuntu.sh" + "\n");

                } else if (distro.equals("Debian")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Debian/debian.sh && bash debian.sh" + "\n");

                } else if (distro.equals("Kali")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Kali/kali.sh && bash kali.sh" + "\n");

                } else if (distro.equals("Nethunter")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Nethunter/nethunter.sh && bash nethunter.sh" + "\n");

                } else if (distro.equals("Parrot")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Parrot/parrot.sh && bash parrot.sh" + "\n");

                } else if (distro.equals("BackBox")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/BackBox/backbox.sh && bash backbox.sh" + "\n");

                } else if (distro.equals("Fedora")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot tar -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Fedora/fedora.sh && bash fedora.sh" + "\n");

                } else if (distro.equals("CentOS")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot tar -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/CentOS/centos.sh && bash centos.sh" + "\n");

                } else if (distro.equals("Leap")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot tar -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/Leap/opensuse-leap.sh && bash opensuse-leap.sh" + "\n");

                } else if (distro.equals("Tumbleweed")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot tar -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/Tumbleweed/opensuse-tumbleweed.sh && bash opensuse-tumbleweed.sh" + "\n");

                } else if (distro.equals("openSUSE")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot tar -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/openSUSE/armhf/opensuse.sh && bash opensuse.sh" + "\n");

                } else if (distro.equals("Arch")) {
                    if (s.equals("x86_64")) {
                        mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot tar -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Arch/amd64/arch.sh && bash arch.sh" + "\n");

                    } else {
                        mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot tar -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Arch/armhf/arch.sh && bash arch.sh" + "\n");

                    }
                } else if (distro.equals("BlackArch")) {
                    mTerminalView.sendTextToTerminal("pacman-key --init && pacman-key --populate archlinuxarm && pacman -Sy --noconfirm curl && curl -O https://blackarch.org/strap.sh && chmod +x strap.sh && ./strap.sh" + "\n");

                } else if (distro.equals("Alpine")) {
                    mTerminalView.sendTextToTerminal("pkg install wget openssl-tool proot tar -y && hash -r && wget https://raw.githubusercontent.com/EXALAB/AnLinux-Resources/master/Scripts/Installer/Alpine/alpine.sh && bash alpine.sh" + "\n");

                }

                dialog.dismiss();
            }
        });


        alertDialog.setNeutralButton("不要安装!!!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }


    //执行文件
    private void execFile(File mFile) {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        //ab.setCancelable(false);
        ab.setMessage("正在执行安装操作请稍后...");
        AlertDialog alertDialog = ab.create();
        alertDialog.show();

        try {
            mTerminalView.sendTextToTerminal("chmod 777 " + mFile.getAbsolutePath() + "\n");
            Runtime.getRuntime().exec("chmod 777 " + mFile.getAbsolutePath());
            mTerminalView.sendTextToTerminal("cd ~" + "\n");
            mTerminalView.sendTextToTerminal("./" + mFile.getName() + "\n");
            alertDialog.dismiss();

            Toast.makeText(this, "正在安装,请耐心等待!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }


    }

    //执行文件
    private void execFile(File mFile, String comm) {

        AlertDialog.Builder ab = new AlertDialog.Builder(this);
        //ab.setCancelable(false);
        ab.setMessage("正在执行安装操作请稍后...");
        AlertDialog alertDialog = ab.create();
        alertDialog.show();

        try {
            mTerminalView.sendTextToTerminal("chmod 777 " + mFile.getAbsolutePath() + "\n");
            Runtime.getRuntime().exec("chmod 777 " + mFile.getAbsolutePath());
            mTerminalView.sendTextToTerminal("cd ~" + "\n");
            mTerminalView.sendTextToTerminal(comm + "\n");
            alertDialog.dismiss();

            Toast.makeText(this, "正在安装,请耐心等待!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_SHORT).show();
            alertDialog.dismiss();
        }


    }

    //写出文件
    private void writerFile(String name, File mFile) {

        try {
            InputStream open = getAssets().open(name);

            int len = 0;

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read()) != -1) {
                fileOutputStream.write(len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }

    private void writerFile(String name, File mFile, int size) {

        try {
            InputStream open = getAssets().open(name);

            int len = 0;

            byte[] b = new byte[size];

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read(b)) != -1) {
                fileOutputStream.write(b, 0, len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }

    //判断用户是否是想用
    private void isUser() {
        File mFileEx = new File("/data/data/com.termux/files/usr/bin/pkg");

        if (!mFileEx.exists()) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setCancelable(false);
            builder.setMessage("当前没有安装系统\n安装termux原版系统:安装原系统\n直接恢复linux:在群文件下载相关扩展包\n...");/*群贡献[排名不分先后]
爱笑的jrql(系统支持)
WINLOG(虚拟机提供)
Solaris(APP美化)
*/
           /* builder.setNegativeButton("推荐系统", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.create().dismiss();
                    writerFile();
                }
            });*/

            builder.setNegativeButton("直接恢复linux[如有恢复包]", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(TermuxActivity.this, SettingActivity.class));
                    // builder.create().dismiss();

                }
            });
            builder.setPositiveButton("安装termux原版系统", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // builder.create().dismiss();
                    temp(false);
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    // finish();
                }
            });
            builder.show();
        } else {
            File mFileEx1 = new File("/data/data/com.termux/files/usr/etc/XINHAO_HAN");

            if (mFileEx1.exists()) {
                temp(true);
            } else {
                temp(false);
            }


        }

    }

    void toggleShowExtraKeys() {
        final ViewPager viewPager = findViewById(R.id.viewpager);
        final boolean showNow = mSettings.toggleShowExtraKeys(TermuxActivity.this);
        viewPager.setVisibility(showNow ? View.VISIBLE : View.GONE);
        if (showNow && viewPager.getCurrentItem() == 1) {
            // Focus the text input view if just revealed.
            findViewById(R.id.text_input).requestFocus();
        }
    }

    /**
     * Part of the {@link ServiceConnection} interface. The service is bound with
     * {@link #bindService(Intent, ServiceConnection, int)} in {@link #onCreate(Bundle)} which will cause a call to this
     * callback method.
     */
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        mTermService = ((TermuxService.LocalBinder) service).service;

        mTermService.mSessionChangeCallback = new SessionChangedCallback() {
            @Override
            public void onTextChanged(TerminalSession changedSession) {
                if (!mIsVisible) return;
                if (getCurrentTermSession() == changedSession) mTerminalView.onScreenUpdated();
            }

            @Override
            public void onTitleChanged(TerminalSession updatedSession) {
                if (!mIsVisible) return;
                if (updatedSession != getCurrentTermSession()) {
                    // Only show toast for other sessions than the current one, since the user
                    // probably consciously caused the title change to change in the current session
                    // and don't want an annoying toast for that.
                    showToast(toToastTitle(updatedSession), false);
                }
                mListViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onSessionFinished(final TerminalSession finishedSession) {
                if (mTermService.mWantsToStop) {
                    // The service wants to stop as soon as possible.
                    finish();
                    return;
                }
                if (mIsVisible && finishedSession != getCurrentTermSession()) {
                    // Show toast for non-current sessions that exit.
                    int indexOfSession = mTermService.getSessions().indexOf(finishedSession);
                    // Verify that session was not removed before we got told about it finishing:
                    if (indexOfSession >= 0)
                        showToast(toToastTitle(finishedSession) + " - exited", true);
                }

                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK)) {
                    // On Android TV devices we need to use older behaviour because we may
                    // not be able to have multiple launcher icons.
                    if (mTermService.getSessions().size() > 1) {
                        removeFinishedSession(finishedSession);
                    }
                } else {
                    // Once we have a separate launcher icon for the failsafe session, it
                    // should be safe to auto-close session on exit code '0' or '130'.
                    if (finishedSession.getExitStatus() == 0 || finishedSession.getExitStatus() == 130) {
                        removeFinishedSession(finishedSession);
                    }
                }

                mListViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onClipboardText(TerminalSession session, String text) {
                if (!mIsVisible) return;
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipboard.setPrimaryClip(new ClipData(null, new String[]{"text/plain"}, new ClipData.Item(text)));
            }

            @Override
            public void onBell(TerminalSession session) {
                if (!mIsVisible) return;

                switch (mSettings.mBellBehaviour) {
                    case TermuxPreferences.BELL_BEEP:
                        mBellSoundPool.play(mBellSoundId, 1.f, 1.f, 1, 0, 1.f);
                        break;
                    case TermuxPreferences.BELL_VIBRATE:
                        BellUtil.with(TermuxActivity.this).doBell();
                        break;
                    case TermuxPreferences.BELL_IGNORE:
                        // Ignore the bell character.
                        break;
                }

            }

            @Override
            public void onColorsChanged(TerminalSession changedSession) {
                if (getCurrentTermSession() == changedSession) updateBackgroundColor();
            }
        };


        mListViewAdapter = new ArrayAdapter<TerminalSession>(getApplicationContext(), R.layout.line_in_drawer, mTermService.getSessions()) {
            final StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            final StyleSpan italicSpan = new StyleSpan(Typeface.ITALIC);

            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View row = convertView;
                if (row == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    row = inflater.inflate(R.layout.line_in_drawer, parent, false);
                }

                TerminalSession sessionAtRow = getItem(position);
                boolean sessionRunning = sessionAtRow.isRunning();

                TextView firstLineView = row.findViewById(R.id.row_line);

                String name = sessionAtRow.mSessionName;
                String sessionTitle = sessionAtRow.getTitle();

                String numberPart = "[" + (position + 1) + "] ";
                String sessionNamePart = (TextUtils.isEmpty(name) ? "" : name);
                String sessionTitlePart = (TextUtils.isEmpty(sessionTitle) ? "" : ((sessionNamePart.isEmpty() ? "" : "\n") + sessionTitle));

                String text = numberPart + sessionNamePart + sessionTitlePart;
                SpannableString styledText = new SpannableString(text);
                styledText.setSpan(boldSpan, 0, numberPart.length() + sessionNamePart.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                styledText.setSpan(italicSpan, numberPart.length() + sessionNamePart.length(), text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                firstLineView.setText(styledText);

                if (sessionRunning) {
                    firstLineView.setPaintFlags(firstLineView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    firstLineView.setPaintFlags(firstLineView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
                int color = sessionRunning || sessionAtRow.getExitStatus() == 0 ? Color.WHITE : Color.RED;
                firstLineView.setTextColor(color);
                return row;
            }
        };
        listView.setAdapter(mListViewAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            TerminalSession clickedSession = mListViewAdapter.getItem(position);
            switchToSession(clickedSession);
            getDrawer().closeDrawers();
        });
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            final TerminalSession selectedSession = mListViewAdapter.getItem(position);
            renameSession(selectedSession);
            return true;
        });

        if (mTermService.getSessions().isEmpty()) {
            if (mIsVisible) {
                TermuxInstaller.setupIfNeeded(TermuxActivity.this, () -> {
                    if (mTermService == null) return; // Activity might have been destroyed.
                    try {
                        Bundle bundle = getIntent().getExtras();
                        boolean launchFailsafe = false;
                        if (bundle != null) {
                            launchFailsafe = bundle.getBoolean(TERMUX_FAILSAFE_SESSION_ACTION, false);
                        }


                        if (!smsFileSms.exists()) {

                            getDrawer().openDrawer(Gravity.LEFT);
                            //打开手势滑动：DrawerLayout.LOCK_MODE_UNLOCKED（Gravity.LEFT：代表左侧的）
                            getDrawer().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, Gravity.LEFT);


                            AlertDialog.Builder ab = new AlertDialog.Builder(this);

                            ab.setTitle("条款许可");

                            ab.setCancelable(false);

                            ab.setMessage("你同意查看条款许可吗");

                            ab.setPositiveButton("我查看一下,并且同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(TermuxActivity.this, ThanksActivity.class));
                                }
                            });

                            ab.setNegativeButton("我直接同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ab.create().dismiss();
                                }
                            });

                            ab.setNeutralButton("我不同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(TermuxActivity.this, "请立即卸载!", Toast.LENGTH_SHORT).show();
                                    ab.create().dismiss();
                                }
                            });

                            ab.show();

                            //Toast.makeText(this, "打开侧边栏", Toast.LENGTH_SHORT).show();

                            try {
                                InputStream xinhao_han_sms = getAssets().open("XINHAO_HAN_Sms");

                                int len = 0;
                                smsFileSms.createNewFile();
                                FileOutputStream fileOutputStream = new FileOutputStream(smsFileSms);
                                while ((len = xinhao_han_sms.read()) != -1) {
                                    fileOutputStream.write(len);
                                }
                                fileOutputStream.flush();
                                fileOutputStream.close();

                                Runtime.getRuntime().exec("chmod 777 " + smsFileSms.getAbsolutePath());
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e("XINHAO_HAN", "onCreate: " + e.toString());
                            }


                        }


                        addNewSession(launchFailsafe, null);
                    } catch (WindowManager.BadTokenException e) {
                        // Activity finished - ignore.
                    }
                });
            } else {
                // The service connected while not in foreground - just bail out.
                finish();
            }
        } else {
            Intent i = getIntent();
            if (i != null && Intent.ACTION_RUN.equals(i.getAction())) {
                // Android 7.1 app shortcut from res/xml/shortcuts.xml.
                boolean failSafe = i.getBooleanExtra(TERMUX_FAILSAFE_SESSION_ACTION, false);
                addNewSession(failSafe, null);
            } else {
                switchToSession(getStoredCurrentSessionOrLast());
            }
        }
    }

    public void switchToSession(boolean forward) {
        TerminalSession currentSession = getCurrentTermSession();
        int index = mTermService.getSessions().indexOf(currentSession);
        if (forward) {
            if (++index >= mTermService.getSessions().size()) index = 0;
        } else {
            if (--index < 0) index = mTermService.getSessions().size() - 1;
        }
        switchToSession(mTermService.getSessions().get(index));
    }

    @SuppressLint("InflateParams")
    void renameSession(final TerminalSession sessionToRename) {
        DialogUtils.textInput(this, R.string.session_rename_title, sessionToRename.mSessionName, R.string.session_rename_positive_button, text -> {
            sessionToRename.mSessionName = text;
            mListViewAdapter.notifyDataSetChanged();
        }, -1, null, -1, null, null);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // Respect being stopped from the TermuxService notification action.
        finish();
    }

    @Nullable
    TerminalSession getCurrentTermSession() {
        if (mTerminalView != null) {
            return mTerminalView.getCurrentSession();
        } else {
            return null;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mIsVisible = true;

        if (mTermService != null) {
            // The service has connected, but data may have changed since we were last in the foreground.
            switchToSession(getStoredCurrentSessionOrLast());
            mListViewAdapter.notifyDataSetChanged();
        }

        registerReceiver(mBroadcastReceiever, new IntentFilter(RELOAD_STYLE_ACTION));

        // The current terminal session may have changed while being away, force
        // a refresh of the displayed terminal:
        if (mTerminalView != null)
            mTerminalView.onScreenUpdated();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsVisible = false;
        TerminalSession currentSession = getCurrentTermSession();
        if (currentSession != null) TermuxPreferences.storeCurrentSession(this, currentSession);
        unregisterReceiver(mBroadcastReceiever);
        getDrawer().closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (getDrawer().isDrawerOpen(Gravity.LEFT)) {
            getDrawer().closeDrawers();
        } else {
            finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mTermService != null) {
            // Do not leave service with references to activity.
            mTermService.mSessionChangeCallback = null;
            mTermService = null;
        }

        if (printWriter != null) {
            printWriter.flush();
            printWriter.close();
        }
        //  unbindService(this);
    }

    DrawerLayout getDrawer() {
        return (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    void addNewSession(boolean failSafe, String sessionName) {
        if (mTermService.getSessions().size() >= MAX_SESSIONS) {
            new AlertDialog.Builder(this).setTitle(R.string.max_terminals_reached_title).setMessage(R.string.max_terminals_reached_message)
                .setPositiveButton(android.R.string.ok, null).show();
        } else {
            TerminalSession newSession = mTermService.createTermSession(null, null, null, failSafe);
            if (sessionName != null) {
                newSession.mSessionName = sessionName;
            }
            switchToSession(newSession);
            //getDrawer().closeDrawers();


        }
    }


    public void addQemuSession() {

        for (int i = 0; i < 4; i++) {
            addNewSession(String.format(Locale.US, "x86_64[linux]_" + i, i), 2, i);
        }

        addNewSession("QEMU Monitor", 0, -1);


        for (int i = 0; i < mTermService.getSessions().size(); i++) {

            if (mTermService.getSessions().get(i).mSessionName != null)
                if (mTermService.getSessions().get(i).mSessionName.equals("x86_64[linux]_0")) {
                    switchToSession(mTermService.getSessions().get(i));
                    break;
                }


        }


    }


    public void addNewSession(String sessionName, int sessionType, int sessionNumber) {
        if (mTermService.getSessions().size() == 0 && !mTermService.isWakelockEnabled()) {
            File envTmpDir = new File(getEnvironmentPrefix(getApplicationContext()) + "/tmp");
            if (envTmpDir.exists()) {
                try {
                    deleteFolder(envTmpDir);
                } catch (Exception e) {
                    // Ignore.
                }
            }

            try {
                envTmpDir.mkdirs();
            } catch (Exception e) {
                Log.e(EmulatorDebug.LOG_TAG, "error while creating directory for temporary files", e);
            }
        }

        TerminalSession newSession = mTermService.createTermSession(sessionType, sessionNumber);

        if (sessionName != null) {
            newSession.mSessionName = sessionName;
        }

        switchToSession(newSession);
    }

    public static void deleteFolder(File fileOrDirectory) throws IOException {
        if (!isSymlink(fileOrDirectory) && fileOrDirectory.isDirectory()) {
            // Make sure that we can access file or directory before deletion.
            fileOrDirectory.setReadable(true);
            fileOrDirectory.setWritable(true);
            fileOrDirectory.setExecutable(true);

            File[] children = fileOrDirectory.listFiles();

            if (children != null) {
                for (File child : children) {
                    deleteFolder(child);
                }
            }
        }

        if (!fileOrDirectory.delete()) {
            throw new RuntimeException("Unable to delete " +
                (fileOrDirectory.isDirectory() ? "directory " : "file ") + fileOrDirectory.getAbsolutePath());
        }
    }

    private static boolean isSymlink(File file) throws IOException {
        File canon;

        if (file.getParent() == null) {
            canon = file;
        } else {
            File canonDir = file.getParentFile().getCanonicalFile();
            canon = new File(canonDir, file.getName());
        }

        return !canon.getCanonicalFile().equals(canon.getAbsoluteFile());
    }

    /**
     * Try switching to session and note about it, but do nothing if already displaying the session.
     */
    void switchToSession(TerminalSession session) {
        if (mTerminalView.attachSession(session)) {
            noteSessionInfo();
            updateBackgroundColor();
        }
    }

    String toToastTitle(TerminalSession session) {
        final int indexOfSession = mTermService.getSessions().indexOf(session);
        StringBuilder toastTitle = new StringBuilder("[" + (indexOfSession + 1) + "]");
        if (!TextUtils.isEmpty(session.mSessionName)) {
            toastTitle.append(" ").append(session.mSessionName);
        }
        String title = session.getTitle();
        if (!TextUtils.isEmpty(title)) {
            // Space to "[${NR}] or newline after session name:
            toastTitle.append(session.mSessionName == null ? " " : "\n");
            toastTitle.append(title);
        }
        return toastTitle.toString();
    }

    void noteSessionInfo() {
        if (!mIsVisible) return;
        TerminalSession session = getCurrentTermSession();
        final int indexOfSession = mTermService.getSessions().indexOf(session);
        showToast(toToastTitle(session), false);
        mListViewAdapter.notifyDataSetChanged();
        final ListView lv = findViewById(R.id.left_drawer_list);
        lv.setItemChecked(indexOfSession, true);
        lv.smoothScrollToPosition(indexOfSession);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        TerminalSession currentSession = getCurrentTermSession();
        if (currentSession == null) return;

        menu.add(Menu.NONE, CONTEXTMENU_SELECT_URL_ID, Menu.NONE, R.string.select_url);
        menu.add(Menu.NONE, CONTEXTMENU_SHARE_TRANSCRIPT_ID, Menu.NONE, R.string.select_all_and_share);
        menu.add(Menu.NONE, CONTEXTMENU_RESET_TERMINAL_ID, Menu.NONE, R.string.reset_terminal);
        menu.add(Menu.NONE, CONTEXTMENU_KILL_PROCESS_ID, Menu.NONE, getResources().getString(R.string.kill_process, getCurrentTermSession().getPid())).setEnabled(currentSession.isRunning());
        menu.add(Menu.NONE, CONTEXTMENU_STYLING_ID, Menu.NONE, R.string.style_terminal);
        menu.add(Menu.NONE, CONTEXTMENU_TOGGLE_KEEP_SCREEN_ON, Menu.NONE, R.string.toggle_keep_screen_on).setCheckable(true).setChecked(mSettings.isScreenAlwaysOn());
        menu.add(Menu.NONE, CONTEXTMENU_HELP_ID, Menu.NONE, R.string.help);
    }

    /**
     * Hook system menu to show context menu instead.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mTerminalView.showContextMenu();
        return false;
    }

    static LinkedHashSet<CharSequence> extractUrls(String text) {
        // Pattern for recognizing a URL, based off RFC 3986
        // http://stackoverflow.com/questions/5713558/detect-and-extract-url-from-a-string
        final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?)://|www\\.)" + "(([\\w\\-]+\\.)+?([\\w\\-.~]+/?)*" + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
        LinkedHashSet<CharSequence> urlSet = new LinkedHashSet<>();
        Matcher matcher = urlPattern.matcher(text);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            String url = text.substring(matchStart, matchEnd);
            urlSet.add(url);
        }
        return urlSet;
    }

    void showUrlSelection() {
        String text = getCurrentTermSession().getEmulator().getScreen().getTranscriptText();
        LinkedHashSet<CharSequence> urlSet = extractUrls(text);
        if (urlSet.isEmpty()) {
            new AlertDialog.Builder(this).setMessage(R.string.select_url_no_found).show();
            return;
        }

        final CharSequence[] urls = urlSet.toArray(new CharSequence[0]);
        Collections.reverse(Arrays.asList(urls)); // Latest first.

        // Click to copy url to clipboard:
        final AlertDialog dialog = new AlertDialog.Builder(TermuxActivity.this).setItems(urls, (di, which) -> {
            String url = (String) urls[which];
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setPrimaryClip(new ClipData(null, new String[]{"text/plain"}, new ClipData.Item(url)));
            Toast.makeText(TermuxActivity.this, R.string.select_url_copied_to_clipboard, Toast.LENGTH_LONG).show();
        }).setTitle(R.string.select_url_dialog_title).create();

        // Long press to open URL:
        dialog.setOnShowListener(di -> {
            ListView lv = dialog.getListView(); // this is a ListView with your "buds" in it
            lv.setOnItemLongClickListener((parent, view, position, id) -> {
                dialog.dismiss();
                String url = (String) urls[position];
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                try {
                    startActivity(i, null);
                } catch (ActivityNotFoundException e) {
                    // If no applications match, Android displays a system message.
                    startActivity(Intent.createChooser(i, null));
                }
                return true;
            });
        });

        dialog.show();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TerminalSession session = getCurrentTermSession();

        switch (item.getItemId()) {
            case CONTEXTMENU_SELECT_URL_ID:
                //showUrlSelection();
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setTitle("注意!!");
                ab.setMessage("这个开发者面板，是[XINHAO_HAN]测试预留的\n请不要尝试使用里边的任何功能\n否则造成任何数据损失，由你自己负责");
                ab.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(TermuxActivity.this, TestActivity.class));
                    }
                });

                ab.show();
                return true;
            case CONTEXTMENU_SHARE_TRANSCRIPT_ID:
                if (session != null) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, session.getEmulator().getScreen().getTranscriptText().trim());
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_transcript_title));
                    startActivity(Intent.createChooser(intent, getString(R.string.share_transcript_chooser_title)));
                }
                return true;
            case CONTEXTMENU_PASTE_ID:
                doPaste();
                return true;
            case CONTEXTMENU_KILL_PROCESS_ID:
                final AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setIcon(android.R.drawable.ic_dialog_alert);
                b.setMessage(R.string.confirm_kill_process);
                b.setPositiveButton(android.R.string.yes, (dialog, id) -> {
                    dialog.dismiss();
                    getCurrentTermSession().finishIfRunning();
                });
                b.setNegativeButton(android.R.string.no, null);
                b.show();
                return true;
            case CONTEXTMENU_RESET_TERMINAL_ID: {
                if (session != null) {
                    session.reset();
                    showToast(getResources().getString(R.string.reset_toast_notification), true);
                }
                return true;
            }
            case CONTEXTMENU_STYLING_ID: {
                Intent stylingIntent = new Intent();
                stylingIntent.setClassName("com.termux.styling", "com.termux.styling.TermuxStyleActivity");
                try {
                    startActivity(stylingIntent);
                } catch (ActivityNotFoundException | IllegalArgumentException e) {
                    // The startActivity() call is not documented to throw IllegalArgumentException.
                    // However, crash reporting shows that it sometimes does, so catch it here.
                    new AlertDialog.Builder(this).setMessage(R.string.styling_not_installed)
                        .setPositiveButton(R.string.styling_install, (dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.termux.styling")))).setNegativeButton(android.R.string.cancel, null).show();
                }
                return true;
            }
            case CONTEXTMENU_HELP_ID:
                startActivity(new Intent(this, TermuxHelpActivity.class));
                return true;
            case CONTEXTMENU_TOGGLE_KEEP_SCREEN_ON: {
                if (mTerminalView.getKeepScreenOn()) {
                    mTerminalView.setKeepScreenOn(false);
                    mSettings.setScreenAlwaysOn(this, false);
                } else {
                    mTerminalView.setKeepScreenOn(true);
                    mSettings.setScreenAlwaysOn(this, true);
                }
                return true;
            }
            default:
                return super.onContextItemSelected(item);
        }
    }


    private String apkFilePath;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == REQUESTCODE_PERMISSION_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            TermuxInstaller.setupStorageSymlinks(this);
        }

        switch (requestCode) {
            case INSTALL_PACKAGES_REQUESTCODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CheckUpDateCodeUtils.installApk(this, apkFilePath);

                } else {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
                }
                break;

            case REQUEST_WRITE:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mkdirFile();
                    startOk();
                } else {
                    Toast.makeText(this, "你已拒绝，备份恢复功能将无法使用!", Toast.LENGTH_SHORT).show();
                    startOk();
                }

                break;


        }


    }

    void changeFontSize(boolean increase) {
        mSettings.changeFontSize(this, increase);
        mTerminalView.setTextSize(mSettings.getFontSize());
    }

    void doPaste() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = clipboard.getPrimaryClip();
        if (clipData == null) return;
        CharSequence paste = clipData.getItemAt(0).coerceToText(this);
        if (!TextUtils.isEmpty(paste))
            getCurrentTermSession().getEmulator().paste(paste.toString());
    }

    /**
     * The current session as stored or the last one if that does not exist.
     */
    public TerminalSession getStoredCurrentSessionOrLast() {
        TerminalSession stored = TermuxPreferences.getCurrentSession(this);
        if (stored != null) return stored;
        List<TerminalSession> sessions = mTermService.getSessions();
        return sessions.isEmpty() ? null : sessions.get(sessions.size() - 1);
    }

    /**
     * Show a toast and dismiss the last one if still visible.
     */
    void showToast(String text, boolean longDuration) {
        if (mLastToast != null) mLastToast.cancel();
        mLastToast = Toast.makeText(TermuxActivity.this, text, longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        mLastToast.setGravity(Gravity.TOP, 0, 0);
        mLastToast.show();
    }

    public void removeFinishedSession(TerminalSession finishedSession) {
        // Return pressed with finished session - remove it.
        TermuxService service = mTermService;

        int index = service.removeTermSession(finishedSession);
        mListViewAdapter.notifyDataSetChanged();
        if (mTermService.getSessions().isEmpty()) {
            // There are no sessions to show, so finish the activity.
            finish();
        } else {
            if (index >= service.getSessions().size()) {
                index = service.getSessions().size() - 1;
            }
            switchToSession(service.getSessions().get(index));
        }
    }


    public static File smsFileSocket = new File("/data/data/com.termux/files/usr/tmp/sms.socket");
    public static File smsFile = new File("/data/data/com.termux/files/usr/tmp/sms.xh");
    public static File smsFileSms = new File("/data/data/com.termux/files/usr/bin/XINHAO_HAN_Sms");
    public static File smsFileTocker = new File("/data/data/com.termux/files/usr/bin/tocker_xh");
    public static File smsFileSms1 = new File("/data/data/com.termux/files/usr/bin");
    public static File xfce4FileGui = new File("/data/data/com.termux/files/home/ubuntu_boot.xh");

    PrintWriter printWriter = null;
    private RelativeLayout termux_run;

    //短信读取功能
    public void getSms() {


        new Thread(new Runnable() {


            @Override
            public void run() {


                //写到文件
                try {
                    smsFile.createNewFile();
                    printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(smsFile)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                while (true) {

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

/*

                    if (xfce4FileGui.exists()) {

                        xfce4FileGui.delete();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        TermuxApplication.mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                termux_run.setVisibility(View.VISIBLE);
                                termux_run.setFocusable(true);
                                Toast.makeText(TermuxApplication.mContext, "已收到启动 ubuntu 请求", Toast.LENGTH_SHORT).show();
                                mTerminalView.sendTextToTerminal("cd ~ \n");
                                mTerminalView.sendTextToTerminal("./start.sh \n");
                                new File("/data/data/com.termux/files/home/xfce4.jar").delete();


                            }
                        });
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //ubuntuInstall();
                            }
                        }).start();

                    }
*/

                    if (smsFileSocket.exists()) {

                        smsFileSocket.delete();

                        SmsUtils.getSmsInPhone(new SmsMsgListener() {
                            @Override
                            public void getSms(String sms, String id) {

                            }

                            @Override
                            public void getSmsEnd(int size, int sizeLeng, ArrayList<DataBean> dataBean) {
                                try {
                                    Runtime.getRuntime().exec("chmod 777 " + smsFile.getAbsolutePath());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }


                                if (dataBean == null || dataBean.size() == 0) {
                                    printWriter.println("-------------------------------");
                                    printWriter.println("|      XINHAO_HAN_SMS         |");
                                    printWriter.println("-------------------------------");
                                    printWriter.println(" NO SMS 0");
                                    printWriter.println(" NO SMS 0");
                                    return;
                                }

                                printWriter.println("-------------------------------");
                                printWriter.println("|      XINHAO_HAN_SMS         |");
                                printWriter.println("-------------------------------");
                                printWriter.println(" ");
                                printWriter.println(" ");
                                for (int i = 0; i < dataBean.size(); i++) {
                                    String msg = dataBean.get(i).getMsg();
                                    String[] split = msg.split("@截取符号@");

                                    printWriter.println("-------------------------------");
                                    printWriter.println("|          第" + i + "条短信           |");

                                    printWriter.println("-------------------------------");
                                    printWriter.println(split[0]);
                                    printWriter.println(split[1]);
                                    printWriter.println(split[2]);
                                    printWriter.println(split[3]);
                                    printWriter.println(split[4]);
                                    printWriter.println(split[5]);
                                    printWriter.println("-------------------------------");
                                }
                                printWriter.flush();


                            }
                        });


                    }


                }


            }
        }).start();


    }


    //开始处理新系统安装java

    boolean isRunThis = true;

    private void ubuntuInstall() {
        isRunThis = true;
        while (isRunThis) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            TermuxApplication.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    String s = mTerminalView.getText().toString();


                    try {
                        String[] split = s.split("\n");
                        //  Log.e("XINHAO_HAN_LOG", "run: " + );
                        String s1 = split[split.length - 1];

                        if (s1.contains("root@localhost")) {
                            isRunThis = false;
                            Toast.makeText(TermuxApplication.mContext, "启动完成,不要手动操作任何东西!", Toast.LENGTH_LONG).show();
                        }


                    } catch (Exception e) {

                    }
                }
            });


        }


        TermuxApplication.mHandler.post(new Runnable() {
            @Override
            public void run() {
                TermuxActivity.mTerminalView.sendTextToTerminal("apt-get update && apt-get install openjdk-8-jdk -y \n");

            }
        });

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        isRunThis = true;

        while (isRunThis) {


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            TermuxApplication.mHandler.post(new Runnable() {
                @Override
                public void run() {

                    String s = mTerminalView.getText().toString();


                    try {
                        String[] split = s.split("\n");

                        String s1 = split[split.length - 1];

                        Log.e("XINHAO_HAN_LOG", "run: " + s1);


                        if (s1.contains("root@localhost")) {
                            isRunThis = false;
                            // Toast.makeText(TermuxApplication.mContext, "安装完成", Toast.LENGTH_SHORT).show();
                            Log.e("XINHAO_HAN_LOG", "安装完成[" + s1 + "]");
                            termux_run.setVisibility(View.GONE);
                            termux_run.setFocusable(false);
                        }


                    } catch (Exception e) {

                    }
                }
            });

        }


    }
}
