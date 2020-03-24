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
import android.telephony.TelephonyManager;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
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
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.madrapps.pikolo.ColorPicker;
import com.madrapps.pikolo.HSLColorPicker;
import com.madrapps.pikolo.listeners.OnColorSelectionListener;
import com.termux.R;
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
import java.lang.reflect.Array;
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
import main.java.com.termux.adapter.ItemSelectAdapter;
import main.java.com.termux.android_cm.LauncherActivity;
import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.bean.CreateSystemBean;
import main.java.com.termux.bean.UpDateBean;
import main.java.com.termux.datat.DataBean;
import main.java.com.termux.datat.ServiceDataBean;
import main.java.com.termux.datat.UrlDataHtml;
import main.java.com.termux.filemanage.filemanager.FileManagerActivity;
import main.java.com.termux.floatwindows.TermuxFloatService;
import main.java.com.termux.http.CheckUpDateCodeUtils;
import main.java.com.termux.http.UpDateHttpCode;
import main.java.com.termux.key.KeyData;
import main.java.com.termux.listener.SmsMsgListener;
import main.java.com.termux.service.BackService;
import main.java.com.termux.utils.SaveData;
import main.java.com.termux.utils.SmsUtils;
import main.java.com.termux.utils.SystemUtil;
import main.java.com.termux.utils.WindowUtils;
import main.java.com.termux.view.MyDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static main.java.com.termux.app.TermuxService.getEnvironmentPrefix;
import static main.java.com.termux.service.BackService.BACK_FILES;

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
public final class TermuxActivity extends Activity implements ServiceConnection, View.OnClickListener {

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
    private static final int INSTALL_PACKAGES_REQUEST_CODE = 8888;


    /**
     * The main view of the activity showing the terminal. Initialized in onCreate().
     */
    @SuppressWarnings("NullableProblems")
    @NonNull
    public static TerminalView mTerminalView;

    main.java.com.termux.app.ExtraKeysView mExtraKeysView;

    main.java.com.termux.app.TermuxPreferences mSettings;

    /**
     * The connection to the {@link TermuxService}. Requested in {@link #onCreate(Bundle)} with a call to
     * {@link #bindService(Intent, ServiceConnection, int)}, and obtained and stored in
     * {@link #onServiceConnected(ComponentName, IBinder)}.
     */
    main.java.com.termux.app.TermuxService mTermService;

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
                        main.java.com.termux.app.TermuxInstaller.setupStorageSymlinks(TermuxActivity.this);
                    return;
                }
                checkForFontAndColors();
                mSettings.reloadFromProperties(TermuxActivity.this);

                if (mExtraKeysView != null) {
                    mExtraKeysView.reload(mSettings.mExtraKeys, main.java.com.termux.app.ExtraKeysView.defaultCharDisplay);
                }
            }
        }
    };
    private AnimatorSet mRightOutSet;
    private AnimatorSet mLeftInSet;
    private ListView listView;
    private ListView lv;
    private ViewPager viewPager;
    private View viewById;
    private View fun_all_btn1;
    private View toggle_keyboard_button;

    private static TermuxActivity mTermuxActivity;
    private View mKeyBotView;
    private KeyData keyData;

    public static TermuxActivity getTermux() {

        return mTermuxActivity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        String redD = SaveData.getData("redD");

        if (redD.equals("red")) {
            red.setVisibility(View.GONE);
        }

        if (!video_view.isPlaying()) {

            video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.setVolume(0f, 0f);
                    mp.start();
//                        mVideoView.start();
                }
            });
        }

        try {
            File file3 = new File(Environment.getExternalStorageDirectory(), "/xinhao/check.js");

            if (file3.exists()) {

                if (file3.delete()) {
                    quanxian_123.setText("内存权限:   [已获取]");
                } else {
                    quanxian_123.setText("内存权限:   [没有权限]");
                }

            } else {

                try {
                    if (file3.createNewFile()) {
                        quanxian_123.setText("内存权限:   [已获取]");
                    } else {
                        quanxian_123.setText("内存权限:   [没有权限]");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    quanxian_123.setText("内存权限:   [没有权限]");
                }

            }

            TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);

            try {
                @SuppressLint("MissingPermission") String imei = telephonyManager.getDeviceId();
                if (imei == null || imei.isEmpty()) {
                    dianhua_123.setText("电话权限:   [没有权限]");
                } else {
                    dianhua_123.setText("电话权限:   [已获取]");
                }
            } catch (Exception e) {
                dianhua_123.setText("电话权限:   [没有权限]");
            }


            SmsUtils.getSmsInPhone2(new SmsMsgListener() {
                @Override
                public void getSms(String sms, String id) {

                }

                @Override
                public void getSmsEnd(int size, int sizeLeng, ArrayList<DataBean> dataBean) {

                }
            }, duanxin_123);

        } catch (Exception e) {

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

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTCODE_PERMISSION_STORAGE);
            return false;
        }

    }


    private TextView msg;

    private ProgressBar pro;

    private TextView position_text;

    private long mSize = 0;

    //写入目录
    private void writerFile() {
        mSize = 0;

        main.java.com.termux.app.FileWriterUtils.start(this, new main.java.com.termux.app.ZipUtils.ZipNameListener() {
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

        String start_end = SaveData.getData("start_end");

        if (start_end.equals("end")) {
            return;
        }

        if (main.java.com.termux.app.TermuxService.TAGRUN != null)
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


        mTerminalView = findViewById(R.id.terminal_view);
        mTerminalView.setOnKeyListener(new main.java.com.termux.app.TermuxViewClient(this));


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
                startActivity(new Intent(TermuxActivity.this, main.java.com.termux.app.SettingActivity.class));
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
                    layout = mExtraKeysView = (main.java.com.termux.app.ExtraKeysView) inflater.inflate(R.layout.extra_keys_main, collection, false);
                    mExtraKeysView.reload(mSettings.mExtraKeys, main.java.com.termux.app.ExtraKeysView.defaultCharDisplay);

                    String back_color_view = SaveData.getData("back_color_view");

                    if (!back_color_view.equals("def")) {

                        int back_color_view1 = Integer.parseInt(SaveData.getData("back_color_view"));
                        mExtraKeysView.setBackgroundColor(back_color_view1);
                    }


                    String image_back = SaveData.getData("image_back");

                    Log.e("XINHAO_HAN", "instantiateItem: " + image_back);
                    if (!image_back.equals("def")) {
                        mExtraKeysView.setBackgroundColor(Color.parseColor("#44000000"));
                    }

                    String video_back = SaveData.getData("video_back");

                    if (!video_back.equals("def")) {
                        mExtraKeysView.setBackgroundColor(Color.parseColor("#44000000"));
                    }


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
        other.add(newSessionButton);
        newSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewSession(false, null);
                qiehuan(0);
            }
        });
        newSessionButton.setOnLongClickListener(v -> {
            main.java.com.termux.app.DialogUtils.textInput(TermuxActivity.this, R.string.session_new_named_title, null, R.string.session_new_named_positive_button,
                text -> addNewSession(false, text), R.string.new_session_failsafe, text -> addNewSession(true, text)
                , -1, null, null);
            return true;
        });

        toggle_keyboard_button = findViewById(R.id.toggle_keyboard_button);
        other.add(toggle_keyboard_button);

        toggle_keyboard_button.setOnClickListener(v -> {
            String key_bot1 = SaveData.getData("key_bot");
            if (("def".equals(key_bot1))) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                getDrawer().closeDrawers();
            }

        });


        findViewById(R.id.toggle_keyboard_button).setOnLongClickListener(v -> {
            toggleShowExtraKeys();
            return true;
        });

        registerForContextMenu(mTerminalView);

        Intent serviceIntent = new Intent(this, main.java.com.termux.app.TermuxService.class);
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


        Log.e("返回的图片", "requestCode: " + requestCode);
        Log.e("返回的图片", "resultCode: " + resultCode);
        switch (requestCode) {


            case REQUEST_SELECT_IMAGES_CODE:


                List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

                if (selectList == null || selectList.size() == 0) {
                    return;
                }


                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外


                try {

                    String compressPath = selectList.get(0).getCompressPath();

                    Log.e("XINHAO_HAN", "onActivityResult: " + compressPath);


                    try {
                        video_view.setVisibility(View.GONE);
                        video_view.stopPlayback();
                        SaveData.saveData("video_back", "def");
                    } catch (Exception e) {

                    }

                    Bitmap bitmap = BitmapFactory.decodeFile(compressPath);

                    SaveData.saveData("image_back", compressPath);

                    termux_layout.setBackground(new BitmapDrawable(bitmap));

                    mTerminalView.setBackgroundColor(Color.parseColor("#44000000"));

                    mExtraKeysView.setBackgroundColor(Color.parseColor("#44000000"));

                    if (viewPager != null)
                        viewPager.setBackgroundColor(Color.parseColor("#44000000"));
                } catch (Exception e) {


                }

                break;

            case REQUEST_SELECT_IMAGES_CODE_VIDEO:


                List<LocalMedia> selectList1 = PictureSelector.obtainMultipleResult(data);

                if (selectList1 == null || selectList1.size() == 0) {
                    return;
                }


                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外


                try {

                    String compressPath = selectList1.get(0).getPath();

                    Log.e("XINHAO_HAN", "onActivityResult: " + compressPath);

                    SaveData.saveData("video_back", compressPath);


                    termux_layout.setBackgroundColor(Color.parseColor("#00000000"));

                    mTerminalView.setBackgroundColor(Color.parseColor("#44000000"));

                    mExtraKeysView.setBackgroundColor(Color.parseColor("#44000000"));

                    SaveData.saveData("image_back", "def");

                    //termux_layout.setBackground(new BitmapDrawable(bitmap));

                    video_view.setVisibility(View.VISIBLE);

                    video_view.setVideoPath(compressPath);

                    // video_view.start();

                    video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.setVolume(0f, 0f);
                            mp.start();
//                        mVideoView.start();
                        }
                    });

                    video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer mPlayer) {
                            mPlayer.start();
                            mPlayer.setLooping(true);
                        }
                    });
/*

                    video_view.pause();

                    video_view.resume();
*/


                    termux_layout.setBackgroundColor(Color.parseColor("#00000000"));

                    mTerminalView.setBackgroundColor(Color.parseColor("#44000000"));

                    mExtraKeysView.setBackgroundColor(Color.parseColor("#44000000"));

                    if (viewPager != null)
                        viewPager.setBackgroundColor(Color.parseColor("#44000000"));
                } catch (Exception e) {


                }
                break;

        }

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
    private LinearLayout start_end_command;
    private LinearLayout android_support;

    private TextView start_end_text;

    private boolean start_end;

    private ListView item_select;

    private ListView item_key;

    private Button item_key_linux;
    private Button item_key_user;


    private TextView text_jiagou;
    private TextView text_ip;


    private TextView wenzi;
    private TextView wenzijianpan;
    private TextView jianpan;
    private TextView beijin;


    boolean mIsUsingBlackUI;

    private ColorPicker colorPicker;
    private RelativeLayout color;

    private ImageView close;

    private HSLColorPicker rgb_color;


    private LinearLayout color_btn;
    private LinearLayout function_ll;
    private LinearLayout back_btn;
    private LinearLayout video_btn;
    private LinearLayout fun_all_btn;

    private VideoView video_view;

    private RelativeLayout termux_layout;

    private static final int REQUEST_SELECT_IMAGES_CODE = 199501;
    private static final int REQUEST_SELECT_IMAGES_CODE_VIDEO = 199403;

    private ArrayList<View> os = new ArrayList<>();
    private ArrayList<View> meihua = new ArrayList<>();
    private ArrayList<View> tool = new ArrayList<>();
    private ArrayList<View> other = new ArrayList<>();

    private LinearLayout ziyuan_group;
    private LinearLayout meihua_group;
    private LinearLayout gongju_group;
    private LinearLayout other_group;
    private LinearLayout linux_data_jisu;
    private LinearLayout linux_gui_list_btn;
    private LinearLayout linux_root_btn;
    private LinearLayout android_start_commd;

    private LinearLayout linux_data;
    private LinearLayout fun_core_btn;

    private TextView system_text;
    private TextView meihua_text;
    private TextView tool_text;
    private TextView other_text;
    private TextView visition;
    private TextView visition1;
    private TextView service_title;


    //http://45.205.175.163:29952/xinhao.json
    private void getVisition() {


        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().get().url(UrlDataHtml.IP + UrlDataHtml.VISITION).build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        service_title.setText("服务器离线,请到群:714730084获取最新版本!");
                        service_title.setTextColor(Color.YELLOW);
                        visition.setTextColor(Color.YELLOW);
                        visition1.setTextColor(Color.YELLOW);
                        visition.setText(visition.getText());
                        visition1.setText("最新版本:[-.--.--]");
                        visition4.setText("本地版本:[0.92.77]\n最新版本:[-.--.--]");
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String string = response.body().string();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        try {
                            ServiceDataBean serviceDataBean = new Gson().fromJson(string, ServiceDataBean.class);
                            String versionName = serviceDataBean.getVersionName();

                            // visition.setText(visition.getText() + "\n最新版本:[" + versionName + "]");
                            visition.setText(visition.getText());
                            if (!visition.getText().toString().equals(versionName)) {
                                visition1.setTextColor(Color.YELLOW);
                            } else {
                                visition1.setTextColor(Color.WHITE);
                            }
                            visition4.setText("本地版本:[0.92.77]\n最新版本:[" + versionName + "]");

                            visition1.setText("最新版本:[" + versionName + "]");

                            OkHttpClient okHttpClient1 = new OkHttpClient();

                            Request request1 = new Request.Builder().get().url("http://45.205.175.163:29954/xinhao/size").build();

                            Call call1 = okHttpClient1.newCall(request1);

                            call1.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            visition1.setText("最新版本:[" + versionName + "]");
                                            visition4.setText("本地版本:[0.92.77]\n最新版本:[" + versionName + "]");
                                        }
                                    });

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {

                                    String string1 = response.body().string();


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Log.e("XINHAO_HAN_M", "onResponse: " + string1);

                                            try {
                                                int i = Integer.parseInt(string1);
                                                visition1.setText("最新版本:[" + versionName + "]/装机量:" + string1);
                                            } catch (Exception e) {
                                                // visition1.setText("最新版本:[" + versionName + "]/装机量: -");
                                            }
                                        }
                                    });


                                }
                            });


                            service_title.setTextColor(Color.WHITE);
                            visition.setTextColor(Color.WHITE);
                            service_title.setText(serviceDataBean.getNote());
                        } catch (Exception e) {
                            service_title.setText("服务器离线,请到群:714730084获取最新版本!");
                            service_title.setTextColor(Color.YELLOW);
                            visition.setTextColor(Color.YELLOW);
                            visition1.setTextColor(Color.YELLOW);
                            visition.setText(visition.getText());
                            visition1.setText("最新版本:[-.--.--]");
                        }

                    }
                });


            }
        });

    }


    private LinearLayout linux_sea_btn;
    private LinearLayout linux_luntan_btn;
    private LinearLayout linux_data_zaixian;
    private LinearLayout windows_32_btn;

    //获取imei码
    private void getImei() {

        //8.0动态权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkPermission = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_PHONE_STATE}, 1); //后面的1为请求码
                // Toast.makeText(this, "获取imei码，来获取你的状态(数据包需提供)[必须]", Toast.LENGTH_SHORT).show();
                update(getIMEI(this));
                return;
            } else {
                update(getIMEI(this));
            }

        } else {
            update(getIMEI(this));
        }

    }

    public String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            String imei = SaveData.getData("imei");

            if (imei.equals("def")) {
                SaveData.saveData("imei", UUID.randomUUID().toString());
                imei = SaveData.getData("imei");
            }

            return imei;
        }
        String imei = telephonyManager.getDeviceId();

        return imei;
    }

    //上传
    public void update(String imei) {


        String updateImei = imei;


        Log.e("XINHAO_HAN_M", "update: " + updateImei);
        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder().get().url("http://45.205.175.163:29954/xinhao/login?iemi=" + updateImei + "&&name=" + SystemUtil.getLocalIpAddress() + "/" + android.os.Build.VERSION.RELEASE + "/" + android.os.Build.MODEL + "/" + android.os.Build.BRAND).build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.e("XINHAO_HAN", "onResponse: " + "成功!" + response.body().string());
            }
        });


    }


    private void checkIsAndroidO1() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                CheckUpDateCodeUtils.installApk(this, new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/dosPlugins.apk").getAbsolutePath());
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUEST_CODE);
            }
        } else {

        }
    }


    private void checkIsAndroidO2() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                CheckUpDateCodeUtils.installApk(this, new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/fdrPlugins.apk").getAbsolutePath());
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUEST_CODE);
            }
        } else {

        }
    }

    private void checkIsAndroidO3(String str) {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                CheckUpDateCodeUtils.installApk(this, new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/"+str+".apk").getAbsolutePath());
            } else {
                //请求安装未知应用来源的权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, INSTALL_PACKAGES_REQUEST_CODE);
            }
        } else {

        }
    }

    private void installAPK(String apkFile) {
        String file = apkFile;
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            //Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", new File(apkFile));
            intent.setDataAndType(CheckUpDateCodeUtils.getPathUri(this, apkFile), "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(new File(apkFile)), "application/vnd.android.package-archive");
        }


        /*intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(new File(file)),
            "application/vnp.android.package-archive");*/
        startActivity(intent);
    }

    protected void hideInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        View v = getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    private void installTermux(int index, String banben) {

        AlertDialog.Builder ad = new AlertDialog.Builder(TermuxActivity.this);
        ad.setTitle("你确定要换成官方版本吗？");
        ad.setMessage("当前安装:" + banben + "\n切换到官方版本您将失去一切UTermux的功能\n注意:不会!不会!不会!丢失数据的\n如果您想切换回来您可以覆盖安装");
        ad.setNegativeButton("切换", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/test.z");


                if(index == 0){


                    Intent intent = new Intent();
                    intent.setData(Uri.parse("https://pan.baidu.com/s/1Py6n_f6vJgy6S1w_oL4jBQ"));//Url 就是你要打开的网址
                    intent.setAction(Intent.ACTION_VIEW);
                    startActivity(intent); //启动浏览器

                    ad.create().dismiss();
                    return;

                }

                File file1 = new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/");
                if (file1.exists()) {
                    File file2 = new File(file1, banben + ".apk");
                    writerFile("termux_plugins" + (index), file2);

                    //installAPK(file2.getAbsolutePath());
                    checkIsAndroidO3(banben);
                } else {

                    file1.mkdirs();
                }


                ad.create().dismiss();

            }
        });
        ad.setPositiveButton("不切换", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.create().dismiss();
            }
        });
        ad.show();

    }


    private LinearLayout linux_vnc_btn;
    private LinearLayout key_bot_btn;
    private RelativeLayout mTermux_keybot;
    private LinearLayout web_linux_btn;

    private LinearLayout start_lan;

    private TextView start_lan_tv;

    private LinearLayout nemu_wo;
    private LinearLayout fun_all_ll;

    private TextView visition4;

    private TextView quanxian_123;
    private TextView dianhua_123;
    private TextView duanxin_123;

    private LinearLayout file_btn1;
    private LinearLayout lishi;

    private LinearLayout kongxia_linux;
    private LinearLayout font_termux;
    private LinearLayout switch_code;
    private LinearLayout termux_color;
    private LinearLayout utermux_downlod;
    private LinearLayout winlog_download;
    private LinearLayout switch_qinghua_new;
    private LinearLayout switch_main_new;
    private LinearLayout switch_code_vnc;
    private LinearLayout cof_vnc;
    private LinearLayout gaoji_vnc;


    @Override
    public void onCreate(Bundle bundle) {
        mSettings = new main.java.com.termux.app.TermuxPreferences(this);
        mIsUsingBlackUI = mSettings.isUsingBlackUI();
        if (mIsUsingBlackUI) {
            this.setTheme(R.style.Theme_Termux_Black);
        } else {
            this.setTheme(R.style.Theme_Termux);
        }

        getImei();

        super.onCreate(bundle);
        mTermuxActivity = this;
        //  CoreLinux.getInstall().startCoreLinux(this);
        //insFile();




        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.drawer_layout);
        mTermux_keybot = findViewById(R.id.termux_keybot);



        switch_qinghua_new = findViewById(R.id.switch_qinghua_new);

        kongxia_linux = findViewById(R.id.kongxia_linux);

        switch_code = findViewById(R.id.switch_code);

        utermux_downlod = findViewById(R.id.utermux_downlod);

        winlog_download = findViewById(R.id.winlog_download);

        font_termux = findViewById(R.id.font_termux);

        cof_vnc = findViewById(R.id.cof_vnc);

        fun_all_ll = findViewById(R.id.fun_all_ll);
        lishi = findViewById(R.id.lishi);
        visition = findViewById(R.id.visition);
        duanxin_123 = findViewById(R.id.duanxin_123);
        ziyuan_group = findViewById(R.id.ziyuan_group);
        file_btn1 = findViewById(R.id.file_btn1);
        meihua_group = findViewById(R.id.meihua_group);
        gongju_group = findViewById(R.id.gongju_group);
        other_group = findViewById(R.id.other_group);
        fun_core_btn = findViewById(R.id.fun_core_btn);
        visition4 = findViewById(R.id.visition4);
        android_start_commd = findViewById(R.id.android_start_commd);
        service_title = findViewById(R.id.service_title);
        visition1 = findViewById(R.id.visition1);
        linux_sea_btn = findViewById(R.id.linux_sea_btn);
        linux_luntan_btn = findViewById(R.id.linux_luntan_btn);
        linux_data_zaixian = findViewById(R.id.linux_data_zaixian);
        windows_32_btn = findViewById(R.id.windows_32_btn);
        key_bot_btn = findViewById(R.id.key_bot_btn);
        web_linux_btn = findViewById(R.id.web_linux_btn);
        nemu_wo = findViewById(R.id.nemu_wo);
        quanxian_123 = findViewById(R.id.quanxian_123);
        dianhua_123 = findViewById(R.id.dianhua_123);

        gaoji_vnc  = findViewById(R.id.gaoji_vnc);

        switch_main_new = findViewById(R.id.switch_main_new);

        gaoji_vnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent = new Intent();

                    intent.setAction("com.utermux.action.vnc");

                    intent.putExtra("utermux_as", "true");


                    startActivity(intent);
                }catch (Exception e){

                    e.printStackTrace();
                    AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                    ab.setTitle("您还未安装该插件!");

                    //链接: https://pan.baidu.com/s/17l6_bJ3EQN41I7Axs0USvQ 提取码: bxht

                    ab.setMessage("是否前往下载该插件?\n\n提取码:5m7a");

                    ab.setPositiveButton("前往", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ab.create().dismiss();

                            Intent intent = new Intent();
                            intent.setData(Uri.parse("https://pan.baidu.com/s/1qklFmSu53L83okqFC-S8JQ"));//Url 就是你要打开的网址
                            intent.setAction(Intent.ACTION_VIEW);
                            startActivity(intent); //启动浏览器

                        }
                    });

                    ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });

                    ab.show();

                }
            }

        });


        cof_vnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("输入信息");

                View inflate = View.inflate(TermuxApplication.mContext, R.layout.dialog_view_vnc, null);

                EditText address = inflate.findViewById(R.id.address);
                EditText port = inflate.findViewById(R.id.port);
                EditText password = inflate.findViewById(R.id.password);

                ab.setView(inflate);

                ab.setPositiveButton("连接", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ab.create().dismiss();

                        String help_vnc = SaveData.getData("help_vnc_1");

                        if(true){

                            AlertDialog.Builder abb = new AlertDialog.Builder(TermuxActivity.this);

                            abb.setTitle("如果鼠标不动");


                            abb.setCancelable(false);

                            abb.setView(R.layout.dialog_view_vnc_2);

                            abb.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    abb.create().dismiss();
                                    SaveData.saveData("help_vnc_1","xinhao");

                                    try {

                                        Intent intent = new Intent();

                                        intent.setAction("com.utermux.action.vnc");
                                        intent.putExtra("utermux_as", "false");
                                        intent.putExtra("address", address.getText().toString());
                                        intent.putExtra("port", port.getText().toString());
                                        intent.putExtra("password", password.getText().toString());

                                        startActivity(intent);
                                    }catch (Exception e){
                                        e.printStackTrace();
                                        AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                                        ab.setTitle("您还未安装该插件!");

                                        //链接: https://pan.baidu.com/s/17l6_bJ3EQN41I7Axs0USvQ 提取码: bxht

                                        ab.setMessage("是否前往下载该插件?\n\n提取码:5m7a");

                                        ab.setPositiveButton("前往", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                ab.create().dismiss();

                                                Intent intent = new Intent();
                                                intent.setData(Uri.parse("https://pan.baidu.com/s/1qklFmSu53L83okqFC-S8JQ"));//Url 就是你要打开的网址
                                                intent.setAction(Intent.ACTION_VIEW);
                                                startActivity(intent); //启动浏览器

                                            }
                                        });

                                        ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                ab.create().dismiss();
                                            }
                                        });

                                        ab.show();
                                    }
                                }
                            });

                            abb.show();
                        }else{

                            try {

                                Intent intent = new Intent();

                                intent.setAction("com.utermux.action.vnc");
                                intent.putExtra("utermux_as", "false");
                                intent.putExtra("address", address.getText().toString());
                                intent.putExtra("port", port.getText().toString());
                                intent.putExtra("password", password.getText().toString());

                                startActivity(intent);
                            }catch (Exception e){
                                e.printStackTrace();
                                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                                ab.setTitle("您还未安装该插件!");

                                //链接: https://pan.baidu.com/s/17l6_bJ3EQN41I7Axs0USvQ 提取码: bxht

                                ab.setMessage("是否前往下载该插件?\n\n提取码:5m7a");

                                ab.setPositiveButton("前往", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        ab.create().dismiss();

                                        Intent intent = new Intent();
                                        intent.setData(Uri.parse("https://pan.baidu.com/s/1qklFmSu53L83okqFC-S8JQ"));//Url 就是你要打开的网址
                                        intent.setAction(Intent.ACTION_VIEW);
                                        startActivity(intent); //启动浏览器

                                    }
                                });

                                ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ab.create().dismiss();
                                    }
                                });

                                ab.show();
                            }

                        }



                    }
                });

                ab.show();

            }
        });


        switch_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDrawer().closeDrawer(Gravity.LEFT);


                String help_vnc = SaveData.getData("help_vnc_1");

                if(true){

                    AlertDialog.Builder abb = new AlertDialog.Builder(TermuxActivity.this);

                    abb.setTitle("如果鼠标不动");


                    abb.setCancelable(false);

                    abb.setView(R.layout.dialog_view_vnc_2);

                    abb.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            abb.create().dismiss();
                            SaveData.saveData("help_vnc_1","xinhao");

                            try {

                                Intent intent = new Intent();

                                intent.setAction("com.utermux.action.vnc");
                                intent.putExtra("utermux_as", "false");
                                intent.putExtra("address", "127.0.0.1");
                                intent.putExtra("port", "5901");
                                intent.putExtra("password", "123456");

                                startActivity(intent);
                            }catch (Exception e){
                                e.printStackTrace();
                                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                                ab.setTitle("您还未安装该插件!");

                                //链接: https://pan.baidu.com/s/17l6_bJ3EQN41I7Axs0USvQ 提取码: bxht

                                ab.setMessage("是否前往下载该插件?\n\n提取码:5m7a");

                                ab.setPositiveButton("前往", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        ab.create().dismiss();

                                        Intent intent = new Intent();
                                        intent.setData(Uri.parse("https://pan.baidu.com/s/1qklFmSu53L83okqFC-S8JQ"));//Url 就是你要打开的网址
                                        intent.setAction(Intent.ACTION_VIEW);
                                        startActivity(intent); //启动浏览器

                                    }
                                });

                                ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ab.create().dismiss();
                                    }
                                });

                                ab.show();
                            }
                        }
                    });

                    abb.show();
                }else{
                    try {

                        Intent intent = new Intent();

                        intent.setAction("com.utermux.action.vnc");
                        intent.putExtra("utermux_as", "false");
                        intent.putExtra("address", "127.0.0.1");
                        intent.putExtra("port", "5901");
                        intent.putExtra("password", "123456");

                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                        AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                        ab.setTitle("您还未安装该插件!");

                        //链接: https://pan.baidu.com/s/17l6_bJ3EQN41I7Axs0USvQ 提取码: bxht

                        ab.setMessage("是否前往下载该插件?\n\n提取码:5m7a");

                        ab.setPositiveButton("前往", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ab.create().dismiss();

                                Intent intent = new Intent();
                                intent.setData(Uri.parse("https://pan.baidu.com/s/1qklFmSu53L83okqFC-S8JQ"));//Url 就是你要打开的网址
                                intent.setAction(Intent.ACTION_VIEW);
                                startActivity(intent); //启动浏览器

                            }
                        });

                        ab.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ab.create().dismiss();
                            }
                        });

                        ab.show();
                    }
                }








            }
        });


        switch_qinghua_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("提示");

                ab.setMessage("该操作会覆盖你原来的源文件,是否继续 ");

                ab.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                 /*       writerFile("qh_sources.list", new File("/data/data/com.termux/files/usr/etc/apt/sources.list"));
                        writerFile("qh_science.list", new File("/data/data/com.termux/files/usr/etc/apt/sources.list.d/science.list"));
                        writerFile("qh_game.list", new File("/data/data/com.termux/files/usr/etc/apt/sources.list.d/game.list"));
                        Toast.makeText(TermuxActivity.this, "切换到清华源成功!", Toast.LENGTH_SHORT).show();*/

                         mTerminalView.sendTextToTerminal("sed -i 's@^\\(deb.*stable main\\)$@#\\1\\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/termux-packages-24 stable main@' $PREFIX/etc/apt/sources.list && sed -i 's@^\\(deb.*games stable\\)$@#\\1\\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/game-packages-24 games stable@' $PREFIX/etc/apt/sources.list.d/game.list && sed -i 's@^\\(deb.*science stable\\)$@#\\1\\ndeb https://mirrors.tuna.tsinghua.edu.cn/termux/science-packages-24 science stable@' $PREFIX/etc/apt/sources.list.d/science.list && apt update && apt upgrade \n");

                        getDrawer().closeDrawer(Gravity.LEFT);
                    }
                });

                ab.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TermuxActivity.this, "未做任何改动!", Toast.LENGTH_SHORT).show();

                        getDrawer().closeDrawer(Gravity.LEFT);
                        ab.create().dismiss();
                    }
                });
                ab.show();


            }
        });

        switch_main_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("提示");

                ab.setMessage("该操作会覆盖你原来的源文件,是否继续 ");

                ab.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                        writerFile("main_sources.list", new File("/data/data/com.termux/files/usr/etc/apt/sources.list"));
                        writerFile("main_science.list", new File("/data/data/com.termux/files/usr/etc/apt/sources.list.d/science.list"));
                        writerFile("main_game.list", new File("/data/data/com.termux/files/usr/etc/apt/sources.list.d/game.list"));

                        Toast.makeText(TermuxActivity.this, "切换到官方源成功!", Toast.LENGTH_SHORT).show();
                        getDrawer().closeDrawer(Gravity.LEFT);
                    }
                });

                ab.setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TermuxActivity.this, "未做任何改动!", Toast.LENGTH_SHORT).show();

                        getDrawer().closeDrawer(Gravity.LEFT);
                        ab.create().dismiss();
                    }
                });
                ab.show();

            }
        });


        File file2 = new File("/data/data/com.termux/files/home/dostermux");

        if(!file2.exists()){
            file2.mkdirs();
        }


        utermux_downlod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);
                ab.setTitle("请注意");

                ab.setMessage("提取码：gzdp");

                ab.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setData(Uri.parse("https://pan.baidu.com/s/1iJj84b1o2ElXp_8TXmg8cw"));//Url 就是你要打开的网址
                        intent.setAction(Intent.ACTION_VIEW);
                        startActivity(intent); //启动浏览器
                    }
                });

                ab.show();



            }
        });

        winlog_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();
                intent.setData(Uri.parse("http://d.ixcmstudio.cn:21188/"));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent); //启动浏览器

            }
        });

        kongxia_linux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDrawer().closeDrawer(Gravity.LEFT);

                writerFile("ubuntu_oo1.sh",new File("/data/data/com.termux/files/home/ubuntu_oo1.sh"));


                TermuxActivity.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("chmod 777 ubuntu_oo1.sh \n");
                TermuxActivity.mTerminalView.sendTextToTerminal("./ubuntu_oo1.sh \n");
            }
        });


        termux_color = findViewById(R.id.termux_color);

        termux_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog
                    .Builder(TermuxActivity.this);
                builder.setTitle("选择您的配色文件");
                // builder.setMessage("这是个滚动列表，往下滑");
                builder.setItems(NameColor.name, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       //NameColor.name[which]
                        builder.create().dismiss();

                        File file1 = new File("/data/data/com.termux/files/home/.termux/");

                        if(!file1.exists()){
                            file1.mkdirs();
                        }

                        File file = new File("/data/data/com.termux/files/home/.termux/colors.properties");

                        if(file.exists()){
                            boolean delete = file.delete();

                            if(!delete){
                                Toast.makeText(TermuxActivity.this, "你没有SD卡权限1!", Toast.LENGTH_SHORT).show();
                                return;

                            }

                        }

                        try {
                            boolean newFile = file.createNewFile();

                            if(!newFile){

                                Toast.makeText(TermuxActivity.this, "你没有SD卡权限2!", Toast.LENGTH_SHORT).show();
                                return;

                            }

                            writerFile("colors/" + NameColor.name[which],file);

                            Intent intent = new Intent("com.termux.app.reload_style");
                            intent.putExtra("com.termux.app.reload_style","colors");

                            sendBroadcast(intent);


                    /*        AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);
                            ab.setTitle("配色设置成功!");
                            ab.setMessage("如果配色不成功，可能文件已损坏，请重选择!");
                            ab.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(TermuxActivity.this, TermuxService.class);
                                    intent.setAction(TermuxService.ACTION_STOP_SERVICE);
                                    TermuxActivity.this.startService(intent);
                                    ab.create().dismiss();
                                    TermuxActivity.this.finish();


                                }
                            });
                            ab.show();*/

                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(TermuxActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }




                    }
                });
                builder.setNegativeButton("现在不配色", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });


        font_termux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TermuxActivity.this, FontActivity.class));

            }
        });


        start_lan = findViewById(R.id.start_lan);

        start_lan_tv = findViewById(R.id.start_lan_tv);

        meihua.add(key_bot_btn);
        linux_vnc_btn = findViewById(R.id.linux_vnc_btn);
        mKeyBotView = findViewById(R.id.key_boy);

        fun_all_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);
                getDrawer().closeDrawer(Gravity.LEFT);
            }
        });

        File file1 = new File(Environment.getExternalStorageDirectory(), "/xinhao/font/");

        if(!(file1.exists())) {

            file1.mkdirs();


        }

        if(!(new File(Environment.getExternalStorageDirectory(),"/xinhao/font/termux_def.ttf").exists())){

            new Thread(new Runnable() {
                @Override
                public void run() {
                    writerFile("font_termux.ttf",new File(Environment.getExternalStorageDirectory(),"/xinhao/font/termux_def.ttf"));
                }
            }).start();

        }



        lishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String[] strings = {
                    "Termux其它版本 [百度网盘][pevy]",//0
                    "Termux0.75版本",//1
                    "Termux0.74版本",//2
                    "Termux0.73版本",//3
                    "Termux0.72版本",//4
                    "Termux0.71版本",//5
                    "Termux0.70版本",//6
                    "Termux0.69版本",//7
                    "Termux0.68版本",//8
                    "Termux0.67版本",//9
                    "Termux0.66版本",//10
                    "Termux0.65版本"//11


                };

                AlertDialog.Builder builder = new AlertDialog
                    .Builder(TermuxActivity.this);
                builder.setTitle("选择您的历史版本");
                // builder.setMessage("这是个滚动列表，往下滑");
                builder.setItems(strings, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(TermuxActivity.this, "选择了第" + which + "个", Toast.LENGTH_SHORT).show();


                        installTermux(which,strings[which]);
                    }
                });
                builder.setNegativeButton("现在不切换", new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialog, int which) {
                        builder.create().dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });
        other.add(start_lan);


        file_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    Intent intent2 = new Intent("com.xinhao.action.ENTER");
                    startActivity(intent2);
                } catch (Exception e) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(TermuxActivity.this);
                    ad.setTitle("未安装插件");
                    ad.setMessage("未安装插件，是否安装插件?\n如果自动安装插件失败\n请到Sdcard目录下找\nxinhao/apk/fdrPlugins.apk\n手动安装");
                    ad.setNegativeButton("安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            //File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/test.z");


                            File file1 = new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/");
                            if (file1.exists()) {
                                File file2 = new File(file1, "fdrPlugins.apk");
                                writerFile("fdr.plugins", file2, 1024);

                                //installAPK(file2.getAbsolutePath());
                                checkIsAndroidO2();
                            } else {

                                file1.mkdirs();
                            }


                            ad.create().dismiss();

                        }
                    });
                    ad.setPositiveButton("不安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ad.create().dismiss();
                        }
                    });
                    ad.show();
                }
            }
        });

        if (SaveData.getData("start_launcher").equals("def")) {
            // SaveData.saveData("start_launcher", "yes");
            // Toast.makeText(TermuxActivity.this, "已切换到旧版启动器", Toast.LENGTH_SHORT).show();
            start_lan_tv.setText("启动器[新]");
            start_lan_tv.setTextColor(Color.parseColor("#FFFFFF"));
        } else {
            // SaveData.saveData("start_launcher", "def");
            //Toast.makeText(TermuxActivity.this, "已切换到新版启动器", Toast.LENGTH_SHORT).show();
            start_lan_tv.setText("启动器[旧]");

            Toast.makeText(TermuxActivity.this, "当前为旧版启动器!", Toast.LENGTH_SHORT).show();

            start_lan_tv.setTextColor(Color.parseColor("#D02D33"));

        }

        start_lan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SaveData.getData("start_launcher").equals("def")) {
                    SaveData.saveData("start_launcher", "yes");
                    Toast.makeText(TermuxActivity.this, "已切换到旧版启动器", Toast.LENGTH_SHORT).show();
                    start_lan_tv.setText("启动器[旧]");
                    start_lan_tv.setTextColor(Color.parseColor("#D02D33"));

                } else {
                    SaveData.saveData("start_launcher", "def");
                    Toast.makeText(TermuxActivity.this, "已切换到新版启动器", Toast.LENGTH_SHORT).show();
                    start_lan_tv.setText("启动器[新]");
                    start_lan_tv.setTextColor(Color.parseColor("#FFFFFF"));

                }

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);
                ab.setTitle("提示");
                ab.setMessage("需要重启APP才能改变当前启动器\n是否重启?");
                ab.setNegativeButton("需要", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                        Intent intent = new Intent(TermuxActivity.this, TermuxService.class);
                        intent.setAction(TermuxService.ACTION_STOP_SERVICE);
                        TermuxActivity.this.startService(intent);
                    }
                });
                ab.setPositiveButton("稍后", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });
                ab.show();

            }
        });

        os.add(web_linux_btn);

        web_linux_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(TermuxActivity.this, MainActivity.class));

                //new WebStartLinux().start(false);

                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);

                getDrawer().closeDrawer(Gravity.LEFT);

                File file = new File("/data/data/com.termux/files/usr/bin/shellinaboxd");

                if (file.exists()) {

                    AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                    ab.setTitle("提示(prompt)");

                    ab.setMessage("即将开始运行网页版本的Utermux\n密码默认(password def):123\n是否开始运行(Whether to start running?)?\n端口(port):4200");

                    ab.setPositiveButton("运行(run)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ab.create().dismiss();
                            mTerminalView.sendTextToTerminal("shellinaboxd -s /:LOGIN -t -p 4200 & \n");

                            AlertDialog.Builder ad = new AlertDialog.Builder(TermuxActivity.this);
                            ad.setTitle("命令执行成功(ok)");
                            ad.setMessage("请在浏览器打开 [本机(phone)ip]:4200");
                            ad.setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ad.create().dismiss();
                                }
                            });
                            ad.show();
                        }
                    });
                    ab.setNegativeButton("稍后(later)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });

                    ab.show();

                } else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                    ab.setTitle("坏境错误(Bad mistake)");

                    ab.setMessage("你没有安装shellinabox(You didn't install shellinabox)\n离线安装只支持arm和aarch!!!!!\n点击立即安装(Click install now)\n如果提示找不到(If the prompt cannot be found) shellinabox,\n请查看您的源是否正确(Check to see if your source is correct)!\n\n注意!离线安装如果有报错,不用管!");

                    ab.setPositiveButton("安装(install)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            ab.create().dismiss();
                            mTerminalView.sendTextToTerminal("pkg install shellinabox \n");
                        }
                    });
                    ab.setNegativeButton("稍后(later)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ab.create().dismiss();
                        }
                    });

                    ab.setNeutralButton("离线安装(offline install)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            String s = TermuxInstaller.determineTermuxArchName();

                            switch (s) {


                                case "aarch64":
                                    writerFile("shelllliabox.zip", new File("/data/data/com.termux/files/home/shelllliabox.zip"), 1024);
                                    break;
                                case "arm":
                                    writerFile("shellliabox_arm.zip", new File("/data/data/com.termux/files/home/shelllliabox.zip"), 1024);
                                    break;

                            }


                            mTerminalView.sendTextToTerminal("cd ~ \n");
                            mTerminalView.sendTextToTerminal("mkdir shelllliabox \n");
                            mTerminalView.sendTextToTerminal("cd shelllliabox \n");

                            ZipUtils.unZip(new File("/data/data/com.termux/files/home/shelllliabox.zip"), "/data/data/com.termux/files/home/shelllliabox/", new ZipUtils.ZipNameListener() {
                                @Override
                                public void zip(String FileName, int size, int position) {

                                }

                                @Override
                                public void complete() {

                                    mTerminalView.sendTextToTerminal("dpkg -i * \n");
                                }

                                @Override
                                public void progress(long size, long position) {

                                }
                            });
                        }
                    });


                    ab.show();
                }


            }
        });

        linux_vnc_btn.findViewById(R.id.linux_vnc_btn);

        os.add(linux_vnc_btn);

        linux_vnc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, VNCMessageActivity.class));
            }
        });

        os.add(windows_32_btn);
        windows_32_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Intent intent2 = new Intent("com.jrmf360.action.ENTER2");
                    startActivity(intent2);
                } catch (Exception e) {
                    AlertDialog.Builder ad = new AlertDialog.Builder(TermuxActivity.this);
                    ad.setTitle("未安装插件");
                    ad.setMessage("未安装插件，是否安装插件?\n如果自动安装插件失败\n请到Sdcard目录下找\nxinhao/apk/DosBoxApplication.apk\n手动安装");
                    ad.setNegativeButton("安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/test.z");


                            File file1 = new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/");
                            if (file1.exists()) {
                                File file2 = new File(file1, "dosPlugins.apk");
                                writerFile("xdos.plugins", file2, 1024);

                                //installAPK(file2.getAbsolutePath());
                                checkIsAndroidO1();
                            } else {

                                file1.mkdirs();
                            }


                            ad.create().dismiss();

                        }
                    });
                    ad.setPositiveButton("不安装", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ad.create().dismiss();
                        }
                    });
                    ad.show();
                }

            }
        });
/*        linux_vnc_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                startActivity(new Intent(TermuxActivity.this, VNCMessageActivity.class));




                return true;
            }
        });*/


        //startvm(TermuxActivity.this, Config.UI_VNC);


        linux_data_zaixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, ListDataActivity.class));
            }
        });
        os.add(linux_data_zaixian);
        os.add(linux_sea_btn);
        getVisition();

        os.add(linux_luntan_btn);

        linux_luntan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, LunTanActivity.class));
            }
        });
        os.add(android_start_commd);

        linux_sea_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TermuxActivity.this, ALLRJActivity.class));
            }
        });

        android_start_commd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, LauncherActivity.class));
            }
        });


        other.add(fun_core_btn);

        fun_core_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setData(Uri.parse("https://github.com/hanxinhao000/Termux-app-UpgradedVersion"));//Url 就是你要打开的网址
                intent.setAction(Intent.ACTION_VIEW);
                startActivity(intent); //启动浏览器
            }
        });

        linux_root_btn = findViewById(R.id.linux_root_btn);

        os.add(linux_root_btn);

        linux_root_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              /*  final EditText et = new EditText(TermuxActivity.this);
                et.setHint("输入\"确定\"继续");
                et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                new androidx.appcompat.app.AlertDialog.Builder(TermuxActivity.this).setTitle("测试版本区域,分风险自行承担")
                    .setIcon(R.drawable.ic_launcher)
                    .setView(et)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (et.getText().toString().equals("确定")) {

                                startActivity(new Intent(TermuxActivity.this, RootActivity.class));
                            } else {
                                Toast.makeText(TermuxActivity.this, "你必须输入\"确定\"才能继续!" + et.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }).setNegativeButton("取消", null).show();*/


                Toast.makeText(TermuxActivity.this, "请在测试版本中打开!", Toast.LENGTH_SHORT).show();

            }
        });

        linux_gui_list_btn = findViewById(R.id.linux_gui_list_btn);
        os.add(linux_gui_list_btn);

        linux_gui_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TermuxActivity.this, UbuntuListActivity.class));
            }
        });

        ziyuan_group.setOnClickListener(this);
        meihua_group.setOnClickListener(this);
        gongju_group.setOnClickListener(this);
        other_group.setOnClickListener(this);

        system_text = findViewById(R.id.system_text);
        meihua_text = findViewById(R.id.meihua_text);
        tool_text = findViewById(R.id.tool_text);
        other_text = findViewById(R.id.other_text);
        linux_data_jisu = findViewById(R.id.linux_data_jisu);

        linux_data = findViewById(R.id.linux_data);

        linux_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, main.java.com.termux.app.RestoreActivity.class));
            }
        });


        os.add(linux_data);
        os.add(linux_data_jisu);
        tool.add(linux_data);
        tool.add(linux_data_jisu);

        linux_data_jisu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDrawer().closeDrawer(Gravity.LEFT);

                startActivity(new Intent(TermuxActivity.this, BackNewActivity.class));
                if (true) {
                    return;
                }
                Toast.makeText(TermuxActivity.this, "备份/恢复功能正在维护中...", Toast.LENGTH_SHORT).show();

                getDrawer().closeDrawer(Gravity.LEFT);

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("请选择");

                ab.setMessage("请选择你需要的操作\n\n备份/恢复 \n\n一定要加入白名单,或者保持在本程序页面!!!!否则系统可能会杀掉本程序,那么你的备份/恢复 又得重来!!!!\n\n\n请选择一项 \n备份将在后台运行,备份完成将会通过:\n1.弹窗\n2.通知栏\n方式提供给您\n\n备份目录在:[sdcard ->/xinhao/data/目录下\n\n恢复包,也请将放置到该目录下]");

                ab.setPositiveButton("备份", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (new File(Environment.getExternalStorageDirectory(), "/xinhao/data/").listFiles() == null) {

                            Toast.makeText(TermuxActivity.this, "你没有SD卡权限!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Intent intent = new Intent(TermuxActivity.this, BackService.class);

                        intent.setAction(BACK_FILES);

                        startService(intent);
                    }
                });
                ab.setNegativeButton("恢复", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (new File(Environment.getExternalStorageDirectory(), "/xinhao/data/").listFiles() == null) {

                            Toast.makeText(TermuxActivity.this, "你没有SD卡权限!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        startActivity(new Intent(TermuxActivity.this, BackRestoreActivity.class));
                        termux_run.setVisibility(View.VISIBLE);
                      /*  termux_run.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(TermuxActivity.this, "不允许，任何操作!切不能长时间离开,本程序页面", Toast.LENGTH_SHORT).show();
                            }
                        });*/
                    }
                });

                ab.show();

            }
        });

        rgb_color = findViewById(R.id.rgb_color);
        color = findViewById(R.id.color);
        wenzi = findViewById(R.id.wenzi);
        wenzijianpan = findViewById(R.id.wenzijianpan);
        jianpan = findViewById(R.id.jianpan);
        beijin = findViewById(R.id.beijin);
        function_ll = findViewById(R.id.function_ll);

        video_view = findViewById(R.id.video_view);

        fun_all_btn1 = findViewById(R.id.fun_all_btn);

        other.add(fun_all_btn1);

        fun_all_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(TermuxActivity.this, FunAddActivity.class));
            }
        });


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        video_view.setLayoutParams(layoutParams);


        video_btn = findViewById(R.id.video_btn);

        color_btn = findViewById(R.id.color_btn);

        back_btn = findViewById(R.id.back_btn);

        meihua.add(video_btn);
        meihua.add(color_btn);
        meihua.add(back_btn);

        termux_layout = findViewById(R.id.termux_layout);

        File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/img/");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!file.exists()) {
                    boolean mkdirs = file.mkdirs();

                    if (!mkdirs) {
                        Toast.makeText(TermuxActivity.this, "内存卡不可访问!", Toast.LENGTH_SHORT).show();
                    }
                }


                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(TermuxActivity.this)
                    .openGallery(PictureConfig.TYPE_IMAGE)//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                    .maxSelectNum(1)// 最大图片选择数量 int
                    .minSelectNum(0)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(true)// 是否可预览图片 true or false
                    .previewVideo(false)// 是否可预览视频 true or false
                    .enablePreviewAudio(false) // 是否可播放音频 true or false
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                    .enableCrop(false)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    // .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    // .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                    .isGif(true)// 是否显示gif图片 true or false
                    //.compressSavePath(file.getAbsolutePath())//压缩图片保存地址
                    // .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    // .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    // .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    // .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    // .openClickSound()// 是否开启点击声音 true or false
                    // .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                    //.previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    // .cropCompressQuality()// 裁剪压缩质量 默认90 int
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                    // .rotateEnabled() // 裁剪是否可旋转图片 true or false
                    // .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                    // .videoQuality()// 视频录制质量 0 or 1 int
                    .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                    .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                    // .recordVideoSecond()//视频秒数录制 默认60s int
                    .isDragFrame(false)// 是否可拖动裁剪框(固定)
                    .forResult(REQUEST_SELECT_IMAGES_CODE);//结果回调onActivityResult code


                Toast.makeText(TermuxActivity.this, "不要选择过大[过长]的背景,否则会造成卡顿", Toast.LENGTH_SHORT).show();

            }


        });


        video_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 进入相册 以下是例子：用不到的api可以不写
                PictureSelector.create(TermuxActivity.this)
                    .openGallery(PictureConfig.TYPE_VIDEO)//全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                    //.theme()//主题样式(不设置为默认样式) 也可参考demo values/styles下 例如：R.style.picture.white.style
                    .maxSelectNum(1)// 最大图片选择数量 int
                    .minSelectNum(0)// 最小选择数量 int
                    .imageSpanCount(4)// 每行显示个数 int
                    .selectionMode(PictureConfig.SINGLE)// 多选 or 单选 PictureConfig.MULTIPLE or PictureConfig.SINGLE
                    .previewImage(false)// 是否可预览图片 true or false
                    .previewVideo(true)// 是否可预览视频 true or false
                    .enablePreviewAudio(false) // 是否可播放音频 true or false
                    .isCamera(true)// 是否显示拍照按钮 true or false
                    .imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                    .sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                    .setOutputCameraPath("/CustomPath")// 自定义拍照保存路径,可不填
                    .enableCrop(false)// 是否裁剪 true or false
                    .compress(true)// 是否压缩 true or false
                    // .glideOverride()// int glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                    // .withAspectRatio()// int 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                    .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示 true or false
                    .isGif(true)// 是否显示gif图片 true or false
                    //.compressSavePath(file.getAbsolutePath())//压缩图片保存地址
                    // .freeStyleCropEnabled(true)// 裁剪框是否可拖拽 true or false
                    // .circleDimmedLayer(false)// 是否圆形裁剪 true or false
                    // .showCropFrame()// 是否显示裁剪矩形边框 圆形裁剪时建议设为false   true or false
                    // .showCropGrid()// 是否显示裁剪矩形网格 圆形裁剪时建议设为false    true or false
                    // .openClickSound()// 是否开启点击声音 true or false
                    // .selectionMedia()// 是否传入已选图片 List<LocalMedia> list
                    //.previewEggs()// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中) true or false
                    // .cropCompressQuality()// 裁剪压缩质量 默认90 int
                    .minimumCompressSize(100)// 小于100kb的图片不压缩
                    .synOrAsy(true)//同步true或异步false 压缩 默认同步
                    //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效 int
                    // .rotateEnabled() // 裁剪是否可旋转图片 true or false
                    // .scaleEnabled()// 裁剪是否可放大缩小图片 true or false
                    // .videoQuality()// 视频录制质量 0 or 1 int
                    .videoMaxSecond(15)// 显示多少秒以内的视频or音频也可适用 int
                    .videoMinSecond(10)// 显示多少秒以内的视频or音频也可适用 int
                    // .recordVideoSecond()//视频秒数录制 默认60s int
                    .isDragFrame(false)// 是否可拖动裁剪框(固定)
                    .forResult(REQUEST_SELECT_IMAGES_CODE_VIDEO);//结果回调onActivityResult code


            }
        });


        video_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                try {

                    video_view.setVisibility(View.GONE);
                    video_view.stopPlayback();
                    SaveData.saveData("video_back", "def");
                } catch (Exception e) {

                }


                return true;
            }
        });

        back_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                setTextColorView(Color.parseColor("#ffffff"));
                SaveData.saveData("text_color_view", "def");
                setKeyColorView(Color.parseColor("#ffffff"));
                SaveData.saveData("key_color_view", "def");
                setBackColorView(Color.parseColor("#000000"));
                SaveData.saveData("back_color_view", "def");
                fun_all.setBackgroundColor(Color.parseColor("#2b2b2b"));
                // function_ll.setBackgroundColor(Color.parseColor("#2b2b2b"));
                lv.setBackgroundColor(Color.parseColor("#2b2b2b"));

                SaveData.saveData("image_back", "def");

                termux_layout.setBackgroundColor(Color.parseColor("#00000000"));

                Toast.makeText(TermuxActivity.this, "已恢复默认", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        color_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);
                getDrawer().closeDrawer(Gravity.LEFT);
                color.setVisibility(View.VISIBLE);

                Toast.makeText(TermuxActivity.this, "长按此按钮恢复设置!", Toast.LENGTH_SHORT).show();
            }
        });

        color_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                setTextColorView(Color.parseColor("#ffffff"));
                SaveData.saveData("text_color_view", "def");
                setKeyColorView(Color.parseColor("#ffffff"));
                SaveData.saveData("key_color_view", "def");
                setBackColorView(Color.parseColor("#000000"));
                SaveData.saveData("back_color_view", "def");
                fun_all.setBackgroundColor(Color.parseColor("#2b2b2b"));
                // function_ll.setBackgroundColor(Color.parseColor("#2b2b2b"));
                lv.setBackgroundColor(Color.parseColor("#2b2b2b"));
                Toast.makeText(TermuxActivity.this, "已恢复默认", Toast.LENGTH_SHORT).show();

                return true;
            }
        });


        rgb_color.setColorSelectionListener(new OnColorSelectionListener() {
            @Override
            public void onColorSelected(int i) {

                switch (indexColor) {

                    case 0:
                        setTextColorView(i);
                        SaveData.saveData("text_color_view", i + "");
                        break;
                    case 1:
                        setTextColorView(i);
                        SaveData.saveData("text_color_view", i + "");

                        setKeyColorView(i);
                        SaveData.saveData("key_color_view", i + "");
                        break;
                    case 2:
                        setKeyColorView(i);
                        SaveData.saveData("key_color_view", i + "");

                        break;
                    case 3:
                        setBackColorView(i);
                        SaveData.saveData("back_color_view", i + "");
                        break;

                }

            }

            @Override
            public void onColorSelectionStart(int i) {

            }

            @Override
            public void onColorSelectionEnd(int i) {


            }
        });

        wenzi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorText(0);
            }
        });
        wenzijianpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorText(1);
            }
        });
        jianpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorText(2);
            }
        });

        beijin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setColorText(3);
            }
        });

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                color.setVisibility(View.GONE);
            }
        });


        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        termux_run = findViewById(R.id.termux_run);

        termux_run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TermuxActivity.this, "不允许，任何操作!切不能长时间离开,本程序页面", Toast.LENGTH_SHORT).show();
            }
        });

        if (BackService.RES_ISRUN) {
            termux_run.setVisibility(View.VISIBLE);
        }

        text_jiagou = findViewById(R.id.text_jiagou);

        text_ip = findViewById(R.id.text_ip);


        quanping = findViewById(R.id.quanping);

        item_key_linux = findViewById(R.id.item_key_linux);

        item_key_user = findViewById(R.id.item_key_user);

        item_select = findViewById(R.id.item_select);

        item_key = findViewById(R.id.item_key);

        if (main.java.com.termux.app.TermuxInstaller.determineTermuxArchName().equals("aarch64")) {
            text_jiagou.setTextColor(Color.parseColor("#ffffff"));
            text_jiagou.setText("[CPU架构:" + main.java.com.termux.app.TermuxInstaller.determineTermuxArchName().toUpperCase() + "]");

        }

        if (main.java.com.termux.app.TermuxInstaller.determineTermuxArchName().equals("arm")) {
            text_jiagou.setTextColor(Color.YELLOW);
            text_jiagou.setText("[CPU架构:" + main.java.com.termux.app.TermuxInstaller.determineTermuxArchName().toUpperCase() + "]\n[提醒:当前架构[可能存在](只是可能)兼容性问题]");

        }

        //x86_64

        if (main.java.com.termux.app.TermuxInstaller.determineTermuxArchName().equals("x86_64") || main.java.com.termux.app.TermuxInstaller.determineTermuxArchName().equals("i686")) {
            text_jiagou.setTextColor(Color.RED);
            text_jiagou.setText("[CPU架构:" + main.java.com.termux.app.TermuxInstaller.determineTermuxArchName().toUpperCase() + "]\n[警告:当前架构不支持群(所有)数据包]");

        }


        text_ip.setText("[IP地址:" + SystemUtil.getLocalIpAddress() + "]");

        android_support = findViewById(R.id.android_support);
        other.add(android_support);

        android_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("慎重警告");

                ab.setMessage("如果你的系统能正常运行，切勿点击!");

                ab.setNeutralButton("我考虑好了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                        writerFile("libandroid-support.so", new File("/data/data/com.termux/files/usr/lib/libandroid-support.so"), 2048);

                        try {
                            Runtime.getRuntime().exec("chmod 777 /data/data/com.termux/files/usr/lib/libandroid-support.so");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Toast.makeText(TermuxActivity.this, "修复成功,请重启后尝试", Toast.LENGTH_SHORT).show();
                    }
                });

                ab.setPositiveButton("我还没考虑好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ab.create().dismiss();
                    }
                });

                ab.show();


            }
        });


        getDrawer().addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                text_ip.setText("[IP地址:" + SystemUtil.getLocalIpAddress() + "]");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        initAdapter();

        start_end_text = findViewById(R.id.start_end_text);
        start_end_command = findViewById(R.id.start_end_command);
        tool.add(start_end_command);

        String start_end = SaveData.getData("start_end");

        if (start_end.equals("end")) {
            start_end_text.setText("开机命令[关]");
            TermuxActivity.this.start_end = true;
        } else {
            start_end_text.setText("开机命令[开]");
            TermuxActivity.this.start_end = false;
        }

        start_end_command.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TermuxActivity.this.start_end) {
                    start_end_text.setText("开机命令[开]");
                    SaveData.saveData("start_end", "start");
                } else {
                    start_end_text.setText("开机命令[关]");
                    SaveData.saveData("start_end", "end");
                }

                TermuxActivity.this.start_end = !TermuxActivity.this.start_end;

            }
        });


        mingxie = findViewById(R.id.mingxie);
        other.add(mingxie);
        key_ziding = findViewById(R.id.key_ziding);

        meihua.add(key_ziding);

        key_ziding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);
                startActivity(new Intent(TermuxActivity.this, CustomActivity.class));
            }
        });

        windows = findViewById(R.id.windows);
        os.add(windows);

        windows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);
                startActivity(new Intent(TermuxActivity.this, WindowsActivity.class));
            }
        });

        fedora_linux_gui_btn = findViewById(R.id.fedora_linux_gui_btn);
        unfedora_linux_gui_btn = findViewById(R.id.unfedora_linux_gui_btn);

        os.add(fedora_linux_gui_btn);
        os.add(unfedora_linux_gui_btn);

        quanping.findViewById(R.id.quanping);
        tool.add(quanping);

        quanping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);

                if (WindowUtils.isFullScreen) {
                    //退出全屏

                    WindowUtils.exitFullScreen(TermuxActivity.this);
                    toggleShowExtraKeys1(true);
                } else {

                    //打开全屏
                    WindowUtils.setFullScreen(TermuxActivity.this);
                    toggleShowExtraKeys1(false);
                }


            }
        });
        fedora_linux_gui_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);


                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);

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


                            File file = new File("/data/data/com.termux/files/home/fedora_full/");

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

                                        int t[] = {0};

                                        final boolean[] xianshi = {true};

                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                while (xianshi[0]) {

                                                    try {
                                                        Thread.sleep(10);
                                                    } catch (InterruptedException e) {
                                                        e.printStackTrace();
                                                    }

                                                    TermuxApplication.mHandler.post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            myDialog.getDialog_title().setText("正在复制文件到工作区域\n如果复制很慢请耐心等待\n页面刷新可能有延时\n可能一直卡在某个数,但是内部复制没停\n如果没有耐心，大退app重新执行[不建议!!!]");

                                                            myDialog.getDialog_pro().setText((t[0] / 1024 / 1024) + "MB/" + (fileF.length() / 1024 / 1024) + "MB");

                                                            myDialog.getDialog_pro_prog().setProgress(t[0]);

                                                            Log.e("XINHAO_HAN", "run: " + "我还在运行");
                                                        }
                                                    });
                                                }
                                            }
                                        }).start();

                                        while ((l = fileInputStream.read(bytes)) != -1) {
                                            t[0] += l;
                                            fileOutputStream.write(bytes, 0, l);
                                        }

                                        xianshi[0] = false;
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

                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);

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
        os.add(x86_64);

        xuanfu = findViewById(R.id.xuanfu);

        tool.add(xuanfu);

        xuanfu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDrawer().closeDrawer(Gravity.LEFT);

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);
                ab.setTitle("进入提醒");
                ab.setMessage("请选择打开方式?\n打开选项:\n1.带小图标打开[图标打开]\n2.不带小图标打开");
                ab.setPositiveButton("图标打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TermuxFloatService.isShow = true;
                        startService(new Intent(TermuxActivity.this, TermuxFloatService.class));
                    }
                });
                ab.setNegativeButton("无标打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TermuxFloatService.isShow = false;
                        startService(new Intent(TermuxActivity.this, TermuxFloatService.class));
                    }
                });
                ab.show();


            }
        });
        int[] i = new int[1];

        xuanfu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {


                // stopService(new Intent(TermuxActivity.this, TermuxFloatService.class));

                i[0]++;

                if (i[0] > 3) {
                    AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);
                    ab.setTitle("关于");
                    ab.setMessage("软件制作:XINHAO_HAN\n作者姓名:韩**\n软件类型:termux功能集合\n项目开始时间:2019年7月11日\n集合外部资源:github.com/谷歌\n开发地点:西安雁塔区");
                    ab.setPositiveButton("进入后台管理", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            startActivity(new Intent(TermuxActivity.this, XINHAO_HANActivity.class));
                            ab.create().dismiss();
                        }
                    });
                    ab.show();
                    i[0] = 0;
                }


                return true;
            }
        });

        linux_quanxian_btn = findViewById(R.id.linux_quanxian_btn);

        other.add(linux_quanxian_btn);

        x86_64.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);

                setX86_64();
            }
        });
        linux_gui_btn = findViewById(R.id.linux_gui_btn);
        os.add(linux_gui_btn);

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
                startActivity(new Intent(TermuxActivity.this, main.java.com.termux.app.TestActivity.class));
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

        sess_btn.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                qiehuan(0);
                addNewSession(false, null);
                return true;
            }
        });

        fun_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qiehuan(1);
                //  ViewUtils.xuanzhuanView(mTerminalView,nemu_wo);
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




       /* case 0:
        setTextColorView(i);
        SaveData.saveData("text_color_view", i + "");
        break;
        case 1:
        setTextColorView(i);
        SaveData.saveData("text_color_view", i + "");
        setKeyColorView(i);
        SaveData.saveData("key_color_view", i + "");
        break;
        case 2:
        setKeyColorView(i);
        SaveData.saveData("key_color_view", i + "");

        break;
        case 3:
        setBackColorView(i);
        SaveData.saveData("back_color_view", i + "");
        break;*/

        String text_color_view = SaveData.getData("text_color_view");
        String key_color_view = SaveData.getData("key_color_view");
        String back_color_view = SaveData.getData("back_color_view");
        String video_back = SaveData.getData("video_back");

        try {
            if (!text_color_view.equals("def")) {
                TerminalRenderer.COLOR_TEXT = Integer.parseInt(SaveData.getData("text_color_view"));
                mTerminalView.invalidate();

            }

            if (!key_color_view.equals("def")) {
                main.java.com.termux.app.ExtraKeysView.TEXT_COLOR = Integer.parseInt(SaveData.getData("key_color_view"));
                //   mExtraKeysView.setColorButton();
            }

            if (!back_color_view.equals("def")) {
                int back_color_view1 = Integer.parseInt(SaveData.getData("back_color_view"));
                mTerminalView.setBackgroundColor(back_color_view1);
                fun_all.setBackgroundColor(back_color_view1);
                // function_ll.setBackgroundColor(back_color_view1);

            }


            String image_back = SaveData.getData("image_back");
            if (!image_back.equals("def")) {
                Bitmap bitmap = BitmapFactory.decodeFile(image_back);
                termux_layout.setBackground(new BitmapDrawable(bitmap));
                mTerminalView.setBackgroundColor(Color.parseColor("#44000000"));
                //  fun_all.setBackgroundColor(Color.parseColor("#22000000"));
                //   function_ll.setBackgroundColor(Color.parseColor("#22000000"));
                //   getDrawer().setBackgroundColor(Color.parseColor("#22000000"));
            }

            if (!video_back.equals("def")) {
                mTerminalView.setBackgroundColor(Color.parseColor("#44000000"));

                video_view.setVisibility(View.VISIBLE);
                video_view.setVideoPath(video_back);


                video_view.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setVolume(0f, 0f);
                        mp.start();
//                        mVideoView.start();
                    }
                });


                //video_view.start();

                video_view.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mPlayer) {
                        mPlayer.start();
                        mPlayer.setLooping(true);
                    }
                });


            }

            Log.e("XINHAO_HAN_VIDEO", "video_back: " + video_back);

        } catch (Exception e) {

        }

        index_group(0);

        keyData = new KeyData();
        keyData.setKeyView(mKeyBotView, mTerminalView);


        String key_bot = SaveData.getData("key_bot");
        if ("def".equals(key_bot)) {
            mKeyBotView.setVisibility(View.GONE);
            mTermux_keybot.setVisibility(View.GONE);
            toggleShowExtraKeys1(true);
        } else {
            mKeyBotView.setVisibility(View.VISIBLE);
            mTermux_keybot.setVisibility(View.VISIBLE);
            mTermux_keybot.requestFocus();
            // mExtraKeysView.setVisibility(View.GONE);
            toggleShowExtraKeys1(false);
        }


        key_bot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);

                String key_bot = SaveData.getData("key_bot");
                if ("def".equals(key_bot)) {
                    SaveData.saveData("key_bot", "yes");
                    mKeyBotView.setVisibility(View.VISIBLE);
                    mTermux_keybot.setVisibility(View.VISIBLE);
                    toggleShowExtraKeys1(false);
                } else {
                    SaveData.saveData("key_bot", "def");
                    mKeyBotView.setVisibility(View.GONE);
                    mTermux_keybot.setVisibility(View.GONE);
                    toggleShowExtraKeys1(true);
                }


            }
        });


        String key_bot1 = SaveData.getData("key_bot");
        if (!("def".equals(key_bot1))) {
            hideInput();
        }
    }


    //设置文字颜色
    private void setTextColorView(int color) {

        TerminalRenderer.COLOR_TEXT = color;

        mTerminalView.invalidate();


    }

    //设置背景颜色
    private void setBackColorView(int color) {


        String image_back = SaveData.getData("image_back");
        String video_back = SaveData.getData("video_back");
        if (image_back.equals("def") && video_back.equals("def")) {
            mTerminalView.setBackgroundColor(color);
            mExtraKeysView.setBackgroundColor(color);
        } else {
            //  Toast.makeText(this, "你已设置背景图,无法再次设置背景颜色!", Toast.LENGTH_SHORT).show();
        }


        fun_all.setBackgroundColor(color);
        //  function_ll.setBackgroundColor(color);
        lv.setBackgroundColor(color);

    }

    //设置键盘
    private void setKeyColorView(int color) {


        String key_bot = SaveData.getData("key_bot");
        if ("def".equals(key_bot)) {
            main.java.com.termux.app.ExtraKeysView.TEXT_COLOR = color;
            mExtraKeysView.setColorButton();
            mExtraKeysView.invalidate();
        } else {
            keyData.setTextKeyColor(color);
        }

    }

    private int indexColor = 1;

    private void setColorText(int index) {

        indexColor = index;
        wenzi.setTextColor(Color.parseColor("#ffffff"));
        wenzijianpan.setTextColor(Color.parseColor("#ffffff"));
        jianpan.setTextColor(Color.parseColor("#ffffff"));
        beijin.setTextColor(Color.parseColor("#ffffff"));

        switch (index) {

            case 0:
                wenzi.setTextColor(Color.parseColor("#CC0099"));
                break;
            case 1:
                wenzijianpan.setTextColor(Color.parseColor("#CC0099"));
                break;
            case 2:
                jianpan.setTextColor(Color.parseColor("#CC0099"));
                break;
            case 3:
                beijin.setTextColor(Color.parseColor("#CC0099"));
                break;


        }

    }

    private void initAdapter() {


       /* private ListView item_select;

        private ListView item_key;
        */



       /* private Button item_key_linux;
        private Button item_key_user;*/

        String key_box_r = SaveData.getData("key_box_r");
        ArrayList<String> arrayList = new ArrayList<>();
        if ("def".equals(key_box_r)) {
            // arrayList.add("TAB");
            // arrayList.add("ESC");
            //  arrayList.add("CTRL");
            //  arrayList.add("ALT");
            arrayList.add("pkg update");
            arrayList.add("pkg install ");
            arrayList.add("pkg uninstall ");
            arrayList.add("apt-get install ");
            arrayList.add("apt-get remove  ");
            arrayList.add("apt-get update  ");
            arrayList.add("clang");
            arrayList.add("%d");
            arrayList.add("include");
            arrayList.add("%s");
            arrayList.add("intmain");
            arrayList.add("%f");
            arrayList.add("printf(\"\")");
            arrayList.add("#");
            arrayList.add("!");
            arrayList.add("return 0;");
            arrayList.add("void");
            arrayList.add("main");
            arrayList.add("int");
            arrayList.add("float");
            arrayList.add("double");
            arrayList.add("long");
            arrayList.add("char");
            arrayList.add("String");
            arrayList.add("");

            arrayList.add(" -y \n");
            arrayList.add("vim");
            arrayList.add(":wq!");
            arrayList.add(":q!");

            arrayList.add("|");
            arrayList.add("[");
            arrayList.add("]");
            //  arrayList.add("HOME");
            //  arrayList.add("UP");
            //  arrayList.add("END");
            arrayList.add(":w");
            arrayList.add("^");
            arrayList.add("(");
            arrayList.add(")");
            //  arrayList.add("LEFT");
            //  arrayList.add("DOWN");
            //  arrayList.add("RIGHT");
            arrayList.add("=");
            arrayList.add("/");
            arrayList.add("-");
            arrayList.add("-");
            arrayList.add("+");
            arrayList.add("\"");
            arrayList.add("{");
            arrayList.add("}");
            arrayList.add("<");
            arrayList.add("*");
            arrayList.add(">");
            arrayList.add(",");
            arrayList.add(";");
            arrayList.add(":");
            arrayList.add("%");
            arrayList.add("/**/");
            arrayList.add("./");
            arrayList.add("$");
            arrayList.add("&&");
            arrayList.add("||");
            arrayList.add("\n");

        } else {


            try {
                JSONArray arr = new JSONArray(key_box_r);
                String[][] mExtraKeys = new String[arr.length()][];

                for (int i = 0; i < arr.length(); i++) {
                    JSONArray line = arr.getJSONArray(i);
                    mExtraKeys[i] = new String[line.length()];
                    for (int j = 0; j < line.length(); j++) {
                        mExtraKeys[i][j] = line.getString(j);

                        arrayList.add(mExtraKeys[i][j]);
                    }
                }


            } catch (JSONException e) {
                e.printStackTrace();
                arrayList.add("pkg update");
                arrayList.add("pkg install ");
                arrayList.add("pkg uninstall ");
                arrayList.add("apt-get install ");
                arrayList.add("apt-get remove  ");
                arrayList.add("apt-get update  ");
                arrayList.add("clang");
                arrayList.add("%d");
                arrayList.add("include");
                arrayList.add("%s");
                arrayList.add("intmain");
                arrayList.add("%f");
                arrayList.add("printf(\"\")");
                arrayList.add("#");
                arrayList.add("!");
                arrayList.add("return 0;");
                arrayList.add("void");
                arrayList.add("main");
                arrayList.add("int");
                arrayList.add("float");
                arrayList.add("double");
                arrayList.add("long");
                arrayList.add("char");
                arrayList.add("String");
                arrayList.add("");

                arrayList.add(" -y \n");
                arrayList.add("vim");
                arrayList.add(":wq!");
                arrayList.add(":q!");

                arrayList.add("|");
                arrayList.add("[");
                arrayList.add("]");
                //  arrayList.add("HOME");
                //  arrayList.add("UP");
                //  arrayList.add("END");
                arrayList.add(":w");
                arrayList.add("^");
                arrayList.add("(");
                arrayList.add(")");
                //  arrayList.add("LEFT");
                //  arrayList.add("DOWN");
                //  arrayList.add("RIGHT");
                arrayList.add("=");
                arrayList.add("/");
                arrayList.add("-");
                arrayList.add("-");
                arrayList.add("+");
                arrayList.add("\"");
                arrayList.add("{");
                arrayList.add("}");
                arrayList.add("<");
                arrayList.add("*");
                arrayList.add(">");
                arrayList.add(",");
                arrayList.add(";");
                arrayList.add(":");
                arrayList.add("%");
                arrayList.add("/**/");
                arrayList.add("./");
                arrayList.add("$");
                arrayList.add("&&");
                arrayList.add("||");
                arrayList.add("\n");
                Toast.makeText(this, "出现错误!使用默认键盘", Toast.LENGTH_SHORT).show();
            }

        }


        ItemSelectAdapter itemSelectAdapter = new ItemSelectAdapter(arrayList);

        item_key.setAdapter(itemSelectAdapter);


        item_key.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mTerminalView.sendTextToTerminal(arrayList.get(position));

            }
        });


        item_key_linux.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        item_key_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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

            ab.setMessage("请将安装包放置到 内部存储/xinhao/iso 下");

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

                                    main.java.com.termux.app.ZipUtils.unZip(file2, "/data/data/com.termux/files", new main.java.com.termux.app.ZipUtils.ZipNameListener() {
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
        tool.add(repair_bl);
        os.add(repair);
        os.add(repair_bl);
        function_btn = findViewById(R.id.function_btn);
        tool.add(function_btn);
        linux_system_btn = findViewById(R.id.linux_system_btn);
        os.add(linux_system_btn);

        linux_system_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);
                getDrawer().closeDrawer(Gravity.LEFT);
                String[] strings = {"" +
                    "[ubuntu]乌班图发行版[19.10][./start.sh]",
                    "debian  乌班图子系统[安装约30分钟][./bin/enter_deb]",
                    "fedora  [安装后谨慎卸载,未root用户,某些文件无法删除!!]",
                    "Kali [此文件下载1.2GB 安装后 3+GB 请思考后在安装]",
                    "Centos  [需要root,只不过目前该版本用不了,别点]",
                    "给我一键安装JAVA[JDK8]",
                    "安装其他发行版 [ubuntu、Centos、Kali...]",
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
        tool.add(switch_btn);

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
        apkFilePath = new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/termux.apk").getAbsolutePath();

        //检查版本升级

        CheckUpDateCodeUtils.updateCode(new CheckUpDateCodeUtils.HttpCode() {
            @Override
            public void onRes(String msg) {

                UpDateBean upDateBean = new Gson().fromJson(msg, UpDateBean.class);
                int anInt = 0;
                try {
                    anInt = Integer.parseInt(upDateBean.getNote3());
                } catch (Exception e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(TermuxActivity.this, "服务器不可用!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    return;
                }

                if (UpDateHttpCode.CODE < anInt) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                            ab.setTitle("升级提醒");

                            ab.setMessage(upDateBean.getNote());

                            ab.setCancelable(false);

                            ab.setNegativeButton("立即升级", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    download_text.setVisibility(View.VISIBLE);
                                    CheckUpDateCodeUtils.update(upDateBean.getNote4(), new CheckUpDateCodeUtils.Pro() {
                                        @Override
                                        public void size(int size) {


                                        }

                                        @Override
                                        public void thisSize(int postion, int size) {
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //  ab.setMessage("正在下载[总大小]:" + size + "/" + postion);

                                                    String ss = String.format("%1.2f", ((float) size / 1024 / 1024));
                                                    String ss1 = String.format("%1.2f", ((float) postion / 1024 / 1024));

                                                    download_text.setText("正在下载[总大小]:" + ss + " MB/" + ss1 + " MB");
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
        switch_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, SwitchActivity.class));
            }
        });


        repair_bl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);

                AlertDialog.Builder ab = new AlertDialog.Builder(TermuxActivity.this);

                ab.setTitle("提示");

                ab.setMessage("修复各种错误 \n\n1.dpkg 无法使用\n2.xxx.so not fount\n3.pkg无法使用\n4.更多错误");

                ab.setNegativeButton("开始进行", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                      /*  TermuxInstaller.setupIfNeeded(TermuxActivity.this, new Runnable() {
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
                        });*/

                        getDrawer().openDrawer(Gravity.LEFT);

                        main.java.com.termux.app.TermuxInstaller.setupIfNeeded3("正在修复...", TermuxActivity.this, new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(TermuxActivity.this, "修复成功!请重启APP", Toast.LENGTH_SHORT).show();
                            }
                        });
                        ab.create().dismiss();
                    }
                });

                ab.setPositiveButton("我想稍后修复", new DialogInterface.OnClickListener() {
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

                ViewUtils.xuanzhuanView(mTerminalView, nemu_wo);

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

        tool.add(file_btn);
        file_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermuxActivity.this, FileManagerActivity.class);
                Uri build = new Uri.Builder().path("/data/data/com.termux/files").build();
                intent.setData(build);
                startActivity(intent);
            }
        });

        tool.add(setting);
        os.add(setting);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TermuxActivity.this, main.java.com.termux.app.SettingActivity.class));
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

                mTerminalView.sendTextToTerminal("pkg install curl && curl -fsSL https://its-pointless.github.io/setup-pointless-repo.sh | bash && pkg install qemu-system-x86_64-headless\n");
                break;
            case 2:
                mTerminalView.sendTextToTerminal("pkg install curl && curl -fsSL https://its-pointless.github.io/setup-pointless-repo.sh | bash && pkg install qemu-system-x86_64-headless\n");
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
        viewPager = findViewById(R.id.viewpager);
        final boolean showNow = mSettings.toggleShowExtraKeys(TermuxActivity.this);
        viewPager.setVisibility(showNow ? View.VISIBLE : View.GONE);
        if (showNow && viewPager.getCurrentItem() == 1) {
            // Focus the text input view if just revealed.
            findViewById(R.id.text_input).requestFocus();
        }

        try {
            String image_back = SaveData.getData("image_back");
            if (!image_back.equals("def")) {

                viewPager.setBackgroundColor(Color.parseColor("#44000000"));
                //  fun_all.setBackgroundColor(Color.parseColor("#22000000"));
                //   function_ll.setBackgroundColor(Color.parseColor("#22000000"));
                //   getDrawer().setBackgroundColor(Color.parseColor("#22000000"));
            }
        } catch (Exception e) {

        }
    }

    void toggleShowExtraKeys1(boolean isShow) {
        viewPager = findViewById(R.id.viewpager);
        final boolean showNow = mSettings.toggleShowExtraKeys(TermuxActivity.this);
        viewPager.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (showNow && viewPager.getCurrentItem() == 1) {
            // Focus the text input view if just revealed.
            findViewById(R.id.text_input).requestFocus();
        }

        try {
            String image_back = SaveData.getData("image_back");
            if (!image_back.equals("def")) {

                viewPager.setBackgroundColor(Color.parseColor("#44000000"));
                //  fun_all.setBackgroundColor(Color.parseColor("#22000000"));
                //   function_ll.setBackgroundColor(Color.parseColor("#22000000"));
                //   getDrawer().setBackgroundColor(Color.parseColor("#22000000"));
            }
        } catch (Exception e) {

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
    public TerminalSession getCurrentTermSession() {
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
            //TerminalSession newSession = mTermService.createTermSession(null, null, null, failSafe);
            TerminalSession currentSession = getCurrentTermSession();
            String workingDirectory = (currentSession == null) ? null : currentSession.getCwd();
            //标记2345
            TerminalSession newSession = mTermService.createTermSession(null, null, workingDirectory, failSafe);
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
       // TerminalSession newSession = mTermService.createTermSession2(null, null, null, true);

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
        lv = findViewById(R.id.left_drawer_list);
        lv.setItemChecked(indexOfSession, true);
        lv.smoothScrollToPosition(indexOfSession);
        String back_color_view = SaveData.getData("back_color_view");
        if (!back_color_view.equals("def")) {
            int back_color_view1 = Integer.parseInt(SaveData.getData("back_color_view"));
            lv.setBackgroundColor(back_color_view1);


        }
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
              /*  Intent stylingIntent = new Intent();
                stylingIntent.setClassName("com.termux.styling", "com.termux.styling.TermuxStyleActivity");
                try {
                    startActivity(stylingIntent);
                } catch (ActivityNotFoundException | IllegalArgumentException e) {
                    // The startActivity() call is not documented to throw IllegalArgumentException.
                    // However, crash reporting shows that it sometimes does, so catch it here.
                    new AlertDialog.Builder(this).setMessage(R.string.styling_not_installed)
                        .setPositiveButton(R.string.styling_install, (dialog, which) -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.termux.styling")))).setNegativeButton(android.R.string.cancel, null).show();
                }*/

                new AlertDialog.Builder(this).setMessage(R.string.styling_not_installed)
                    .setPositiveButton(R.string.styling_install, (dialog, which) ->  getDrawer().openDrawer(Gravity.LEFT)).setNegativeButton(android.R.string.cancel, null).show();

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

            case INSTALL_PACKAGES_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CheckUpDateCodeUtils.installApk(this, new File(Environment.getExternalStorageDirectory(), "/xinhao/apk/dosPlugins.apk").getAbsolutePath());

                } else {
                    //  引导用户手动开启安装权限
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
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

        if(index == -1){
            return;
        }
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
                    //  String s = mTerminalView.getText().toString();


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

                    //String s = mTerminalView.getText().toString();


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


    @Override
    protected void onPause() {
        super.onPause();
        if (video_view.isPlaying()) {
            video_view.pause();
        }

    }

    /*
    *
    * (R.id.ziyuan_group)
(R.id.meihua_group)
(R.id.gongju_group)
R.id.other_group);
    *
    *
    * */

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.ziyuan_group:
                index_group(0);
                break;
            case R.id.meihua_group:
                index_group(1);
                break;
            case R.id.gongju_group:
                index_group(2);
                break;
            case R.id.other_group:
                index_group(3);
                break;

        }

    }


    /*
    *
    *
    * system_text;
meihua_text;
tool_text;
other_text;
    *
    * */

    private void index_group(int index) {


        if (true) {

            for (int i = 0; i < meihua.size(); i++) {
                meihua.get(i).setVisibility(View.VISIBLE);
            }

            return;
        }

        system_text.setTextColor(Color.parseColor("#ffffff"));
        meihua_text.setTextColor(Color.parseColor("#ffffff"));
        tool_text.setTextColor(Color.parseColor("#ffffff"));
        other_text.setTextColor(Color.parseColor("#ffffff"));

        for (int i = 0; i < os.size(); i++) {

            os.get(i).setVisibility(View.GONE);
        }

        for (int i = 0; i < meihua.size(); i++) {

            meihua.get(i).setVisibility(View.GONE);
        }

        for (int i = 0; i < tool.size(); i++) {

            tool.get(i).setVisibility(View.GONE);
        }

        for (int i = 0; i < other.size(); i++) {

            other.get(i).setVisibility(View.GONE);
        }

        switch (index) {


            case 0:
                system_text.setTextColor(Color.parseColor("#FF6EC7"));
                for (int i = 0; i < os.size(); i++) {

                    os.get(i).setVisibility(View.VISIBLE);
                }
                break;
            case 1:
                meihua_text.setTextColor(Color.parseColor("#FF6EC7"));
                for (int i = 0; i < meihua.size(); i++) {

                    meihua.get(i).setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                tool_text.setTextColor(Color.parseColor("#FF6EC7"));
                for (int i = 0; i < tool.size(); i++) {

                    tool.get(i).setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                for (int i = 0; i < other.size(); i++) {

                    other.get(i).setVisibility(View.VISIBLE);
                }
                other_text.setTextColor(Color.parseColor("#FF6EC7"));
                break;

        }

    }


}
