package main.java.com.termux.core;

import android.annotation.SuppressLint;
import android.util.Log;

import com.termux.terminal.EmulatorDebug;
import com.termux.terminal.TerminalBuffer;
import com.termux.terminal.TerminalEmulator;
import com.termux.terminal.TerminalSession;
import com.termux.view.TerminalViewClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import main.java.com.termux.application.TermuxApplication;
import main.java.com.termux.floatwindows.TermuxFloatService;
import main.java.com.termux.utils.UUtils;

public class CoreLinux {

    @SuppressLint("SdCardPath")
    //public static final String FILES_PATH = "/data/data/com.termux/.xinhao";
    public static final String FILES_PATH = "/data/data/com.termux/files";
    public static final String PREFIX_PATH = FILES_PATH + "/usr";
    public static final String HOME_PATH = FILES_PATH + "/home";
    private static final boolean LOG_KEY_EVENTS = true;

    private static boolean IS_RUN = false;
    private static TerminalSession terminalSession;
    private static String transcriptTextBuilder;


    public static void runCoreLinux1() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                new File(HOME_PATH).mkdirs();

                ArrayList<String> arrayList = new ArrayList<>();

                arrayList.add(PREFIX_PATH + "/bin/login");
                arrayList.add("-login");

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("HOME", TermuxFloatService.HOME_PATH);
                hashMap.put("PREFIX", TermuxFloatService.PREFIX_PATH);
                hashMap.put("ANDROID_ROOT", System.getenv("ANDROID_ROOT"));
                hashMap.put("ANDROID_DATA", System.getenv("ANDROID_DATA"));
                hashMap.put("EXTERNAL_STORAGE", System.getenv("EXTERNAL_STORAGE"));
                hashMap.put("PS1", "$");
                hashMap.put("LD_LIBRARY_PATH", TermuxFloatService.PREFIX_PATH + "/lib");
                hashMap.put("LANG", "zh_CN.UTF-8");
                hashMap.put("PATH", TermuxFloatService.PREFIX_PATH + "/bin:" + TermuxFloatService.PREFIX_PATH + "/bin/applets");


                ProcessBuilder processBuilder = new ProcessBuilder(arrayList);


                processBuilder.environment().putAll(hashMap);

                processBuilder.redirectErrorStream(true);


                try {
                    Process start = processBuilder.start();

                    InputStream inputStream = start.getInputStream();

                    int l = 0;

                    byte[] b = new byte[1024];


                    int temp = 0;

                    while ((l = inputStream.read(b)) != -1) {

                        String s = new String(b, "UTF-8");

                        Log.e("XINHAO_HANCMMOD", "startInstallLinux: " + s);


                        inputStream.close();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    public static void runCoreLinux() {
        UUtils.showLog("运行状态:开始运行" );

        new File(HOME_PATH).mkdirs();

        final String termEnv = "TERM=xterm-256color";
        final String homeEnv = "HOME=" + TermuxFloatService.HOME_PATH;
        final String prefixEnv = "PREFIX=" + TermuxFloatService.PREFIX_PATH;
        final String androidRootEnv = "ANDROID_ROOT=" + System.getenv("ANDROID_ROOT");
        final String androidDataEnv = "ANDROID_DATA=" + System.getenv("ANDROID_DATA");
        // EXTERNAL_STORAGE is needed for /system/bin/am to work on at least
        // Samsung S7 - see https://plus.google.com/110070148244138185604/posts/gp8Lk3aCGp3.
        final String externalStorageEnv = "EXTERNAL_STORAGE=" + System.getenv("EXTERNAL_STORAGE");
        final String ps1Env = "PS1=$ ";
        final String ldEnv = "LD_LIBRARY_PATH1=" + TermuxFloatService.PREFIX_PATH + "/lib";
        final String langEnv = "LANG=en_US.UTF-8";
        final String pathEnv = "PATH=" + TermuxFloatService.PREFIX_PATH + "/bin:" + TermuxFloatService.PREFIX_PATH + "/bin/applets";
        String[] env = new String[]{termEnv, homeEnv, prefixEnv, ps1Env, ldEnv, langEnv, pathEnv, androidRootEnv, androidDataEnv, externalStorageEnv};

        String executablePath = null;
        String[] args;
        String shellName = null;

        for (String shellBinary : new String[]{"login", "bash", "zsh"}) {
            File shellFile = new File(PREFIX_PATH + "/bin/" + shellBinary);
            if (shellFile.canExecute()) {
                executablePath = shellFile.getAbsolutePath();
                shellName = "-" + shellBinary;
                break;
            }
        }

        if (executablePath == null) {
            // Fall back to system shell as last resort:
            executablePath = "/system/bin/sh";
            shellName = "-sh";
        }

        args = new String[]{shellName};

        mClient = new TermuxCoreViewClient();


        terminalSession = new TerminalSession(executablePath, HOME_PATH, args, env, new TerminalSession.SessionChangedCallback() {
            @Override
            public void onTitleChanged(TerminalSession changedSession) {
                // Ignore for now.
                Log.e("XINHAO_HAN", "onTitleChanged: " + changedSession);


            }

            @Override
            public void onTextChanged(TerminalSession changedSession) {

                transcriptTextBuilder = changedSession.getEmulator().getScreen().getTranscriptTextBuilder();


               // Log.e("XINHAO_HAN", "onTextChanged: " + transcriptTextBuilder);

                if (transcriptTextBuilder.equals("$")) {
                    IS_RUN = false;
                } else {
                    IS_RUN = true;
                }
                Log.e("XINHAO_HAN", "IS_RUN: " + IS_RUN);
            }

            @Override
            public void onSessionFinished(TerminalSession finishedSession) {
                Log.e("XINHAO_HAN", "onSessionFinished: " + finishedSession);
            }

            @Override
            public void onClipboardText(TerminalSession pastingSession, String text) {
                Log.e("XINHAO_HAN", "onClipboardText: " + text);
            }

            @Override
            public void onBell(TerminalSession riningSession) {
                Log.e("XINHAO_HAN", "onBell: " + riningSession);
            }

            @Override
            public void onColorsChanged(TerminalSession session) {
                Log.e("XINHAO_HAN", "onColorsChanged: " + session);
            }
        });

        IS_RUN = true;

        boolean running = terminalSession.isRunning();

        terminalSession.updateSize(180, 68);

        terminalSession.mSessionName = "xinhao_core";


        Log.e("XINHAO_HAN", "runCoreLinux: " + running);

    }


    public static boolean getIsRun() {
        if(terminalSession == null){
            return false;
        }
        return terminalSession.isRunning();
    }

    public static String getText() {

        return terminalSession.getEmulator().getScreen().getTranscriptText();
    }

    public static void runCmd(CharSequence cmd) {
        sendTextToTerminal(cmd);
    }

    private static void sendTextToTerminal(CharSequence text) {
        final int textLengthInChars = text.length();
        for (int i = 0; i < textLengthInChars; i++) {
            char firstChar = text.charAt(i);
            int codePoint;
            if (Character.isHighSurrogate(firstChar)) {
                if (++i < textLengthInChars) {
                    codePoint = Character.toCodePoint(firstChar, text.charAt(i));
                } else {
                    // At end of string, with no low surrogate following the high:
                    codePoint = TerminalEmulator.UNICODE_REPLACEMENT_CHAR;
                }
            } else {
                codePoint = firstChar;
            }

            boolean ctrlHeld = false;
            if (codePoint <= 31 && codePoint != 27) {
                if (codePoint == '\n') {
                    // The AOSP keyboard and descendants seems to send \n as text when the enter key is pressed,
                    // instead of a key event like most other keyboard apps. A terminal expects \r for the enter
                    // key (although when icrnl is enabled this doesn't make a difference - run 'stty -icrnl' to
                    // check the behaviour).
                    codePoint = '\r';
                }

                // E.g. penti keyboard for ctrl input.
                ctrlHeld = true;
                switch (codePoint) {
                    case 31:
                        codePoint = '_';
                        break;
                    case 30:
                        codePoint = '^';
                        break;
                    case 29:
                        codePoint = ']';
                        break;
                    case 28:
                        codePoint = '\\';
                        break;
                    default:
                        codePoint += 96;
                        break;
                }
            }

            inputCodePoint(codePoint, ctrlHeld, false);
        }
    }


    static TerminalViewClient mClient;

    static void inputCodePoint(int codePoint, boolean controlDownFromEvent, boolean leftAltDownFromEvent) {
        if (LOG_KEY_EVENTS) {
            Log.e("XINHAO_HAN", "inputCodePoint(codePoint=" + codePoint + ", controlDownFromEvent=" + controlDownFromEvent + ", leftAltDownFromEvent="
                + leftAltDownFromEvent + ")");
        }

        if (terminalSession == null) return;

        final boolean controlDown = controlDownFromEvent || mClient.readControlKey();
        final boolean altDown = leftAltDownFromEvent || mClient.readAltKey();

        if (mClient.onCodePoint(codePoint, controlDown, terminalSession)) return;

        if (controlDown) {
            if (codePoint >= 'a' && codePoint <= 'z') {
                codePoint = codePoint - 'a' + 1;
            } else if (codePoint >= 'A' && codePoint <= 'Z') {
                codePoint = codePoint - 'A' + 1;
            } else if (codePoint == ' ' || codePoint == '2') {
                codePoint = 0;
            } else if (codePoint == '[' || codePoint == '3') {
                codePoint = 27; // ^[ (Esc)
            } else if (codePoint == '\\' || codePoint == '4') {
                codePoint = 28;
            } else if (codePoint == ']' || codePoint == '5') {
                codePoint = 29;
            } else if (codePoint == '^' || codePoint == '6') {
                codePoint = 30; // control-^
            } else if (codePoint == '_' || codePoint == '7' || codePoint == '/') {
                // "Ctrl-/ sends 0x1f which is equivalent of Ctrl-_ since the days of VT102"
                // - http://apple.stackexchange.com/questions/24261/how-do-i-send-c-that-is-control-slash-to-the-terminal
                codePoint = 31;
            } else if (codePoint == '8') {
                codePoint = 127; // DEL
            }
        }


        if (codePoint > -1) {
            // Work around bluetooth keyboards sending funny unicode characters instead
            // of the more normal ones from ASCII that terminal programs expect - the
            // desire to input the original characters should be low.
            switch (codePoint) {
                case 0x02DC: // SMALL TILDE.
                    codePoint = 0x007E; // TILDE (~).
                    break;
                case 0x02CB: // MODIFIER LETTER GRAVE ACCENT.
                    codePoint = 0x0060; // GRAVE ACCENT (`).
                    break;
                case 0x02C6: // MODIFIER LETTER CIRCUMFLEX ACCENT.
                    codePoint = 0x005E; // CIRCUMFLEX ACCENT (^).
                    break;
            }

            Log.e("XINHAO_HAN", "codePoint: " + codePoint);
            Log.e("XINHAO_HAN", "altDown: " + altDown);

            // If left alt, send escape before the code point to make e.g. Alt+B and Alt+F work in readline:
            terminalSession.writeCodePoint(altDown, codePoint);
        }
    }

}
