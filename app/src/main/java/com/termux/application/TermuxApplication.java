package main.java.com.termux.application;

import android.content.Context;
import android.os.Handler;
import android.widget.ImageView;


//import com.youdao.sdk.app.YouDaoApplication;

import main.java.com.termux.filemanage.filemanager.FileManagerApplication;


public class TermuxApplication extends FileManagerApplication {
    public static Context mContext;
    public static Handler mHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHandler = new Handler();
     //   YouDaoApplication.init(this, "53ccfce3d4dabd06");


    }

}
