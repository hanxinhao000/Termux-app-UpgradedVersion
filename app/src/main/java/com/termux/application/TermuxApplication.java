package main.java.com.termux.application;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.Toast;

import com.termux.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;

import main.java.com.termux.activity.UncaughtExceptionHandlerActivity;
import main.java.com.termux.filemanage.filemanager.FileManagerApplication;
import main.java.com.termux.utils.UUtils;


//import com.youdao.sdk.app.YouDaoApplication;


public class TermuxApplication extends FileManagerApplication {
    public static Context mContext;
    public static Handler mHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHandler = new Handler();
        UUtils.initUUtils(mContext,mHandler);
        //   YouDaoApplication.init(this, "53ccfce3d4dabd06");
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this, "602f78b2425ec25f10f82077", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);
    /*    try {
            Typeface fromFile = Typeface.createFromFile("/data/data/com.termux/files/home/.termux/font.ttf");
            Field field = Typeface.class.getDeclaredField("SERIF");
            field.setAccessible(true);
            field.set(null, fromFile);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
*/

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {


                Intent intent = new Intent(TermuxApplication.mContext, UncaughtExceptionHandlerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("error", collectExceptionInfo((Exception) e));
                TermuxApplication.mContext.startActivity(intent);
                System.exit(1);//关闭已奔溃的app进程

            }
        });

       /* CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
            .setDefaultFontPath("/data/data/com.termux/files/home/.termux/font.ttf")
            .setFontAttrId(R.attr.fontPath)
            .build()
        );*/
    }

    private String collectExceptionInfo(Exception extra) {


        ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutput);
        extra.printStackTrace(printStream);
        try {
            String s = byteArrayOutput.toString("utf-8");
            Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
            return s;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return "are.you.kidding.me.NoExceptionFoundException: This is a bug, please contact developers!";
    }

}
