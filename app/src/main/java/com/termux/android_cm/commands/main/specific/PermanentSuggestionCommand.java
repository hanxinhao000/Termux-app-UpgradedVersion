package main.java.com.termux.android_cm.commands.main.specific;


import main.java.com.termux.android_cm.commands.CommandAbstraction;

/**
 * Created by francescoandreuzzi on 29/01/2017.
 */

public abstract class PermanentSuggestionCommand implements CommandAbstraction {

    public abstract String[] permanentSuggestions();
}
