package main.java.com.termux.utils;

import android.content.Context;
import android.content.SharedPreferences;

import main.java.com.termux.application.TermuxApplication;

public class SaveData {


    public static void saveData(String key, String values) {


        SharedPreferences xinhao = TermuxApplication.mContext.getSharedPreferences("xinhao", Context.MODE_PRIVATE);

        xinhao.edit().putString(key, values).apply();


    }


    public static String getData(String key) {


        SharedPreferences xinhao = TermuxApplication.mContext.getSharedPreferences("xinhao", Context.MODE_PRIVATE);

        String def = xinhao.getString(key, "def");

        return def;

    }

}
