package main.java.com.termux.filemanage.filemanager;

import android.app.Application;

import com.max2idea.android.limbo.main.LimboApplication;

import main.java.com.termux.filemanage.filemanager.util.CopyHelper;
import main.java.com.termux.filemanage.filemanager.util.MimeTypes;


public class FileManagerApplication extends LimboApplication {
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
