package main.java.com.termux.android_cm.commands.main.raw;

import android.app.Activity;
import android.content.Intent;

import com.termux.R;

import java.io.File;
import java.io.IOException;

import main.java.com.termux.android_cm.LauncherActivity;
import main.java.com.termux.android_cm.commands.CommandAbstraction;
import main.java.com.termux.android_cm.commands.ExecutePack;
import main.java.com.termux.android_cm.commands.main.MainPack;
import main.java.com.termux.android_cm.commands.tuixt.TuixtActivity;
import main.java.com.termux.android_cm.managers.FileManager;
import main.java.com.termux.android_cm.tuils.Tuils;


/**
 * Created by francescoandreuzzi on 18/01/2017.
 */

public class tuixt implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;
        File file = info.get(File.class);
        if(file.isDirectory()) {
            return info.res.getString(R.string.output_isdirectory);
        }

        Intent intent = new Intent(info.context, TuixtActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(TuixtActivity.PATH, file.getAbsolutePath());
        ((Activity) info.context).startActivityForResult(intent, LauncherActivity.TUIXT_REQUEST);

        return Tuils.EMPTYSTRING;
    }

    @Override
    public int[] argType() {
        return new int[] {CommandAbstraction.FILE};
    }

    @Override
    public int priority() {
        return 3;
    }

    @Override
    public int helpRes() {
        return R.string.help_tuixt;
    }

    @Override
    public String onArgNotFound(ExecutePack pack, int index) {
        MainPack info = (MainPack) pack;

        String path = info.getString();
        if(path == null || path.length() == 0) {
            return onNotArgEnough(info, info.args.length);
        }

        FileManager.DirInfo dirInfo = FileManager.cd(info.currentDirectory, path);

        File file = new File(dirInfo.getCompletePath());
        if(!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
            return info.res.getString(R.string.output_error);
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            return e.toString();
        }

        Intent intent = new Intent(info.context, TuixtActivity.class);
        intent.putExtra(TuixtActivity.PATH, file.getAbsolutePath());
        ((Activity) info.context).startActivityForResult(intent, LauncherActivity.TUIXT_REQUEST);

        return Tuils.EMPTYSTRING;
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        MainPack info = (MainPack) pack;
        return info.res.getString(R.string.help_tuixt);
    }
}
