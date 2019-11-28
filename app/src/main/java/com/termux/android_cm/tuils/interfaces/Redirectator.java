package main.java.com.termux.android_cm.tuils.interfaces;


import main.java.com.termux.android_cm.commands.main.specific.RedirectCommand;

/**
 * Created by francescoandreuzzi on 03/03/2017.
 */

public interface Redirectator {

    void prepareRedirection(RedirectCommand cmd);
    void cleanup();
}
