package main.java.com.termux.android_cm.commands.main.raw;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;

import com.termux.R;

import main.java.com.termux.android_cm.commands.CommandAbstraction;
import main.java.com.termux.android_cm.commands.ExecutePack;
import main.java.com.termux.android_cm.commands.main.MainPack;
import main.java.com.termux.android_cm.commands.main.specific.APICommand;


/**
 * Created by andre on 03/12/15.
 */
public class airplane implements APICommand, CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            boolean isEnabled = isEnabled(info.context);
            Settings.System.putInt(info.context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, isEnabled ? 0 : 1);

            Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
            intent.putExtra("state", !isEnabled);
            info.context.sendBroadcast(intent);

            return info.res.getString(R.string.output_airplane) + !isEnabled;
        }
        return null;
    }

    private boolean isEnabled(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int helpRes() {
        return R.string.help_airplane;
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }

    @Override
    public boolean willWorkOn(int api) {
        return api < Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
