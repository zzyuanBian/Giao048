package com.example.bzy.my2048.sounds;


import android.util.Log;

import com.example.bzy.my2048.utils.DeviceInfo;
import com.example.bzy.my2048.utils.LogUtils;

import static com.example.bzy.my2048.sounds.SoundType.*;

public class GameOverSound extends SoundInfo{

    private static final String TAG = "SoundInfo";
    private final SoundType mType = GAME_OVER;
    private int mId = 0;
    private boolean mPrepared = false;
    private SoundInfo next = null;


    public GameOverSound(int id) {
        mId = id;
    }

    @Override
    void onPrepared(int id) {
        LogUtils.log("GameOverSound onPrepared mId = " + mId);
        if (mId == id) {
            mPrepared = true;
        } else {
            next = GlobalSoundManager.getInstance().getNext(MOVE);
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
            next = GlobalSoundManager.getInstance().getNext(MOVE);
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
                utils.play(mId, 0.5f, 0.5f, 0, 0, 1);
            } catch (Exception e) {
                Log.w(TAG, "Exception in _handleSound");
            }
        }
    }
}
