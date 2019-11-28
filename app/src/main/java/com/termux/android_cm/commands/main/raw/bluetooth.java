package main.java.com.termux.android_cm.commands.main.raw;

import android.bluetooth.BluetoothAdapter;

import com.termux.R;

import main.java.com.termux.android_cm.commands.CommandAbstraction;
import main.java.com.termux.android_cm.commands.ExecutePack;
import main.java.com.termux.android_cm.commands.main.MainPack;


public class bluetooth implements CommandAbstraction {

    @Override
    public String exec(ExecutePack pack) {
        MainPack info = (MainPack) pack;

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

        if(adapter == null) return info.context.getString(R.string.output_bluetooth_unavailable);

        if(adapter.isEnabled()) {
            adapter.disable();
            return info.context.getString(R.string.output_bluetooth) + " false";
        } else {
            adapter.enable();
            return info.context.getString(R.string.output_bluetooth) + " true";
        }
    }

    @Override
    public int helpRes() {
        return R.string.help_bluetooth;
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
    public String onNotArgEnough(ExecutePack info, int nArgs) {
        return null;
    }

    @Override
    public String onArgNotFound(ExecutePack info, int index) {
        return null;
    }

}
