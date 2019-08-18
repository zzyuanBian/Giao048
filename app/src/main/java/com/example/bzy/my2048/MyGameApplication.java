package com.example.bzy.my2048;

import android.app.Application;
import android.content.Context;

import com.example.bzy.my2048.sounds.GlobalSoundManager;

public class MyGameApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        GlobalSoundManager.getInstance();
    }


    public static Context getContext() {
        return mContext;
    }
}
