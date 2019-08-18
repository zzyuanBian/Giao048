package com.example.bzy.my2048.utils;

import android.util.Log;

public class LogUtils {
    public static final boolean DBG = false;
    private static final String TAG = "[blog]";
    public static void log(String s) {
        Log.d(TAG, s);
    }
}
