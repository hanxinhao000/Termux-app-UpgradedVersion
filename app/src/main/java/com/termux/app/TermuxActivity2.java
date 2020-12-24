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
    private View server_zhengmian;
    private View server_fanmian;
    private ImageView zhengmian_viewcvcv;
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

        onClickTermux();
    }



    //按钮的点击事件
    private void onClickTermux(){

        File file = new File(Environment.getExternalStorageDirectory(), "/xinhao/server");

        if(!file.exists()){
            file.mkdirs();
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
                popupWindow.setContentView(new BoomWindow().getView(new BoomMinLAdapter.CloseLiftListener() {
                    @Override
                    public void close() {
                        popupWindow.dismiss();
                    }
                }));
                popupWindow.setOutsideTouchable(true);
              //  popupWindow.setAnimationStyle(R.style.Animation);
                popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                popupWindow.showAsDropDown(mTerminalView);

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
}
