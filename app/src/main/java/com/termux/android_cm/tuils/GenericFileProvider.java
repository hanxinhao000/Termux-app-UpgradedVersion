package main.java.com.termux.android_cm.tuils;


import androidx.core.content.FileProvider;

import com.termux.BuildConfig;

public class GenericFileProvider extends FileProvider {
    public static final String PROVIDER_NAME = BuildConfig.APPLICATION_ID + ".FILE_PROVIDER";
}
