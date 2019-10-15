package main.java.com.termux.filemanage.filemanager;

import android.app.Application;

import main.java.com.termux.filemanage.filemanager.util.CopyHelper;
import main.java.com.termux.filemanage.filemanager.util.MimeTypes;


public class FileManagerApplication extends Application {
    private CopyHelper mCopyHelper;

    @Override
    public void onCreate() {
        super.onCreate();

        mCopyHelper = new CopyHelper(this);
        MimeTypes.initInstance(this);
    }

    public CopyHelper getCopyHelper() {
        return mCopyHelper;
    }
}
