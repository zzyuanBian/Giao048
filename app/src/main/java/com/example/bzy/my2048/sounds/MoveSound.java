package com.example.bzy.my2048.sounds;


import android.util.Log;

import com.example.bzy.my2048.utils.DeviceInfo;
import com.example.bzy.my2048.utils.LogUtils;

import java.util.HashMap;

import static com.example.bzy.my2048.sounds.SoundType.*;

public class MoveSound extends SoundInfo {

    private static final String TAG = "SoundInfo";
    private final SoundType mType = MOVE;
    private HashMap<Integer, Integer> mId;
    private boolean mPrepared = false;
    private int mPreparedCounts = 0;
    private int mLoopIndex = 0;

    public MoveSound(HashMap<Integer, Integer> id) {
        mId = id;
    }

    @Override
    void onPrepared(int id) {
        if (mId.containsValue(id)) {
            mPreparedCounts++;
            if (mPreparedCounts == 6) {
                mPrepared = true;
            }
            LogUtils.log("MoveSound onPrepared");
        } else {
            Log.d(TAG, "onPrepared: not handled");
        }
    }

    @Override
    public void playSound(SoundType type) {
        if (mType == type) {
            _playSound();
        } else {
            Log.d(TAG, "playSound: not handled");
        }
    }

    @Override
    void _playSound() {
        boolean on = DeviceInfo.getsInstance().isGlobalSoundOn();

        if (!on) {
            return;
        }

        SoundUtils utils = SoundUtils.getInstance();

        if ((mId.get(mLoopIndex) > 0) && mPrepared) {
            try {
                LogUtils.log("move play " + mLoopIndex);
                utils.play(mId.get(mLoopIndex), 0.5f, 0.5f, 0, 0, 1);
            } catch (Exception e) {
                Log.w(TAG, "Exception in _handleSound");
            }
        }
        mLoopIndex++;

        if (mLoopIndex == 6) {
            mLoopIndex = 0;
        }
    }
}
