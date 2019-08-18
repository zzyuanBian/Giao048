package com.example.bzy.my2048.utils;

public class DeviceInfo {
    private float mWidth = 0;
    private static DeviceInfo sInstance = new DeviceInfo();

    private DeviceInfo() {

    }

    public static DeviceInfo getsInstance() {
        return sInstance;
    }

    public float getWidth() {
        return mWidth;
    }

    public void setWidth(float width) {
        this.mWidth = width;
    }

    public boolean isGlobalSoundOn() {
        return true;
    }
}
