package main.java.com.termux.core;

public interface CoreGuiInstallListener {

    void position(String name, int position, int size);

    void error(String msg);

    void complete();
}
