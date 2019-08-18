package com.example.bzy.my2048.sounds;

import android.app.Application;
import android.media.SoundPool;

import com.example.bzy.my2048.MyGameApplication;
import com.example.bzy.my2048.utils.LogUtils;


public class SoundUtils {

    private static SoundUtils sInstance = new SoundUtils();
    private SoundPool mSoundPool;
    private int id;

    private SoundUtils() {
        mSoundPool = new SoundPool.Builder()
                .setMaxStreams(1).build();
    }

    public static SoundUtils getInstance() {
        return sInstance;
    }

    public int play(int soundID, float leftVolume, float rightVolume,
                    int priority, int loop, float rate) {
        return mSoundPool.play(soundID, leftVolume, rightVolume,
                priority, loop, rate);
    }

    public int load(final String path) {
        return mSoundPool.load(path, 100);
    }

    public int load(int resId ){
        LogUtils.log("context = " + MyGameApplication.getContext());
        return mSoundPool.load(MyGameApplication.getContext(),resId,100);
    }

    public SoundPool getSoundPool() {
        return mSoundPool;
    }
}
