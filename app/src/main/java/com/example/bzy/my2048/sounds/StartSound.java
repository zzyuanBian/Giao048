package com.example.bzy.my2048.sounds;


import android.util.Log;

import com.example.bzy.my2048.utils.DeviceInfo;
import com.example.bzy.my2048.utils.LogUtils;

import static com.example.bzy.my2048.sounds.SoundType.*;

public class StartSound extends SoundInfo{

    private static final String TAG = "SoundInfo";
    private final SoundType mType = START;
    private int mId = 0;
    private boolean mPrepared = false;
    private SoundInfo next = null;


    public StartSound(int id) {
        mId = id;
        LogUtils.log("StartSound create");
    }

    @Override
    void onPrepared(int id) {
        if (mId == id) {
            mPrepared = true;
            LogUtils.log("StartSound onPrepared");
        } else {
            next = GlobalSoundManager.getInstance().getNext(GAME_OVER);
            if (next != null) {
                next.onPrepared(id);
            }
        }
    }

    @Override
    public void playSound(SoundType type){
        if (mType == type){
            _playSound();
        } else {
            SoundInfo next = GlobalSoundManager.getInstance().getNext(type);
            if (next != null) {
                next.playSound(type);
            }
        }
    }

    @Override
    void _playSound() {
        boolean on = DeviceInfo.getsInstance().isGlobalSoundOn();

        if (!on) {
            return;
        }

        SoundUtils utils = SoundUtils.getInstance();

        if ((mId > 0) && mPrepared) {
            try {
                LogUtils.log("play success");
                utils.play(mId, 0.5f, 0.5f, 0, 0, 1);
            } catch (Exception e) {
                Log.w(TAG, "Exception in _handleSound");
            }
        }
    }
}
