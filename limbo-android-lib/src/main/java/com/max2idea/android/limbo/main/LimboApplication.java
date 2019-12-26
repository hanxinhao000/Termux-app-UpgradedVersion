package com.max2idea.android.limbo.main;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class LimboApplication extends Application {


    public static Context limboApplication;
    public static Handler mHandlerY;
    @Override
	public void onCreate() {
        super.onCreate();
		try {
			Class.forName("android.os.AsyncTask");
		} catch (Throwable ignore) {
			// ignored
		}
        limboApplication = this;
        mHandlerY = new Handler();

	}

}
