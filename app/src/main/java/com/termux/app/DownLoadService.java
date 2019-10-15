package main.java.com.termux.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class DownLoadService extends Service {
    public DownLoadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


    }


}
