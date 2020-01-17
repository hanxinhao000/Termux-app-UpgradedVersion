package main.java.com.termux.activity.exceptionactivity;

import android.content.Intent;

import main.java.com.termux.activity.UncaughtExceptionHandlerActivity;
import main.java.com.termux.application.TermuxApplication;

public class ErrorUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable ex) {


        Intent intent = new Intent(TermuxApplication.mContext, UncaughtExceptionHandlerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        TermuxApplication.mContext.startActivity(intent);
        System.exit(1);//关闭已奔溃的app进程

    }




}
