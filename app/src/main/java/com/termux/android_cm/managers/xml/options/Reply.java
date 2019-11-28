package main.java.com.termux.android_cm.managers.xml.options;

import android.os.Build;

import main.java.com.termux.android_cm.managers.notifications.reply.ReplyManager;
import main.java.com.termux.android_cm.managers.xml.classes.XMLPrefsElement;
import main.java.com.termux.android_cm.managers.xml.classes.XMLPrefsSave;


/**
 * Created by francescoandreuzzi on 17/01/2018.
 */

public enum Reply implements XMLPrefsSave {

    reply_enabled {
        @Override
        public String defaultValue() {
            return "true";
        }

        @Override
        public String type() {
            return XMLPrefsSave.BOOLEAN;
        }

        @Override
        public String info() {
            return "If false, notification reply will be disabled";
        }
    };

    @Override
    public XMLPrefsElement parent() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) return ReplyManager.instance;
        else return null;
    }

    @Override
    public String label() {
        return name();
    }

    @Override
    public String[] invalidValues() {
        return null;
    }

    @Override
    public String getLowercaseString() {
        return label();
    }

    @Override
    public String getString() {
        return label();
    }
}
