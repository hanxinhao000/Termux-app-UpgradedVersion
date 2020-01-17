package main.java.com.termux.weblinux;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import main.java.com.termux.application.TermuxApplication;

import static com.max2idea.android.limbo.main.LimboActivity.getLocalIpAddress;

public class WebStartLinux {

    private static final String SHELL_IN_A_BOX = "shellinaboxd";
    private static String FILES_DIR;
    private static String PORT = "4201";
    private static Boolean LOCALHOST = false;
    private static Boolean ROOT = false;
    private static Boolean AUTOSTART = true;
    private static String SHELL;
    private static String USERNAME = "utermux";
    private static String PASSWORD = "123";
    private static Boolean ACTIVE = false;
    private static Boolean SCREEN_LOCK;


    public void start(boolean restart) {

        writerFile("shellinaboxd", new File("/data/data/com.termux/files/shellinaboxd"), 1024);
        writerFile("pkill", new File("/data/data/com.termux/files/pkill"), 1024);

        List<String> list = new ArrayList<String>();
        String cmd = /*FILES_DIR + File.separator + SHELL_IN_A_BOX +*/ "/data/data/com.termux/files/shellinaboxd -t -p "
            + PORT + " --shell=" + SHELL + ":" + USERNAME + ":" + PASSWORD;
        if (LOCALHOST) {
            cmd += " --localhost-only";
        }
        if (ROOT) {
            list.add("su");
            cmd += " -u 0 -g 0";
        } else {
            list.add("sh");
        }
        if (restart) {
            list.add(/*FILES_DIR + File.separator + */"/data/data/com.termux/files/pkill -9 " + SHELL_IN_A_BOX);
            list.add("sleep 1");
        }
        list.add(cmd);
        new Thread(new ExecCmd(list)).start();
        ACTIVE = true;
    }


    private void stop() {
        List<String> list = new ArrayList<String>();
        if (ROOT) {
            list.add("su");
        } else {
            list.add("sh");
        }
        ;
        String cmd =/* FILES_DIR + File.separator */ "/data/data/com.termux/files/pkill -9 " + SHELL_IN_A_BOX;
        list.add(cmd);
        new Thread(new ExecCmd(list)).start();
        ACTIVE = false;
    }

    private boolean isAlive() {
        boolean active = false;
        List<String> list = new ArrayList<String>();
        String cmd = "ps | grep " + SHELL_IN_A_BOX;
        list.add(cmd);
        ExecCmd r = new ExecCmd(list);
        r.run();
        if (r.getOutput().size() > 1) {
            active = true;
        }
        return active;
    }


    private void printStatus() {
        String address = "???";
        if (ACTIVE) {
            String ipaddress = "127.0.0.1";
            if (!LOCALHOST)
                ipaddress = getLocalIpAddress();
            address = ipaddress + ":" + PORT;
        }
    }

    private void writerFile(String name, File mFile, int size) {

        try {
            InputStream open = TermuxApplication.mContext.getAssets().open(name);

            int len = 0;

            byte[] b = new byte[size];

            if (!mFile.exists()) {
                mFile.createNewFile();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read(b)) != -1) {
                fileOutputStream.write(b, 0, len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {

        }

    }

}
