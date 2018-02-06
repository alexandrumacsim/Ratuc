package com.ratuc.alex.ratuc;

import android.app.Application;


public class RatucApp extends Application {           //clasa care ne da contextul aplicatiei , folosit in dbhelper pt initializare realm
    private static RatucApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static RatucApp getInstance() {
        return sInstance;
    }
}
