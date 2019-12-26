package main.java.com.termux.utils;

import android.app.Activity;
import android.content.Intent;

import com.max2idea.android.limbo.main.LimboSDLActivity;
import com.max2idea.android.limbo.main.LimboVNCActivity;

import main.java.com.termux.activity.VNCMessageActivity;
import main.java.com.termux.datat.TermuxData;
import okhttp3.OkHttpClient;

public class VNCActivityUtils {

    public static Intent getVNCIntent(Activity activity) {
        return new Intent(activity, VNCMessageActivity.class);

    }

    public static Intent getVNCIntent(Activity activity, String password) {

        // TermuxData.getInstall().vncPassword = password;
        Intent intent = new Intent(activity, LimboVNCActivity.class);

        intent.putExtra("vncPassword", password);

        return intent;

    }

    public static Intent getVNCIntent(Activity activity, String port, String adress, String passwordD1) {


        Intent intent = new Intent(activity, LimboVNCActivity.class);

        intent.putExtra("passwordD1", passwordD1);
        intent.putExtra("port", port);
        intent.putExtra("adress", adress);

        return intent;

    }

    //
    public static Intent getSDLIntent(Activity activity) {


        Intent intent = new Intent(activity, LimboSDLActivity.class);

        return intent;

    }


}
