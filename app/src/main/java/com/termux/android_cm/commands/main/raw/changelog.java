package main.java.com.termux.android_cm.commands.main.raw;

import com.termux.R;

import main.java.com.termux.android_cm.commands.CommandAbstraction;
import main.java.com.termux.android_cm.commands.ExecutePack;
import main.java.com.termux.android_cm.commands.main.MainPack;
import main.java.com.termux.android_cm.managers.ChangelogManager;


/**
 * Created by francescoandreuzzi on 26/03/2018.
 */

public class changelog implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) throws Exception {
        ChangelogManager.printLog(pack.context, ((MainPack) pack).client, true);
        return null;
    }

    @Override
    public int[] argType() {
        return new int[0];
    }

    @Override
    public int priority() {
        return 2;
    }

    @Override
    public int helpRes() {
        return R.string.help_changelog;
    }

    @Override
    public String onArgNotFound(ExecutePack pack, int indexNotFound) {
        return null;
    }

    @Override
    public String onNotArgEnough(ExecutePack pack, int nArgs) {
        return null;
    }
}
