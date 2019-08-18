package com.example.bzy.my2048.sounds;

import android.media.SoundPool;
import android.util.Log;

import com.example.bzy.my2048.R;
import com.example.bzy.my2048.utils.LogUtils;

import java.util.HashMap;

public class GlobalSoundManager {
    private static final String TAG = "GlobalSoundManager";
    private static GlobalSoundManager sInstance = new GlobalSoundManager();
    private SoundUtils mSoundUtils;
    private SoundInfo mStartSound;
    private SoundInfo mGameOverSound;
    private SoundInfo mMoveSound;
    private HashMap<Integer,Integer> moveSoundIds = new HashMap<>(6);

    private GlobalSoundManager() {
        initSounds();
    }

    public static GlobalSoundManager getInstance() {
        return sInstance;
    }

    private void initSounds() {
        mSoundUtils = SoundUtils.getInstance();

        if (mSoundUtils != null) {
//            try {

                mStartSound = new StartSound(mSoundUtils.load(R.raw.giao_begin));
                mGameOverSound = new GameOverSound(mSoundUtils.load(R.raw.giao_over));

                moveSoundIds.put(0,mSoundUtils.load(R.raw.giao_move_01));
                moveSoundIds.put(1,mSoundUtils.load(R.raw.giao_move_02));
                moveSoundIds.put(2,mSoundUtils.load(R.raw.giao_move_03));
                moveSoundIds.put(3,mSoundUtils.load(R.raw.giao_move_04));
                moveSoundIds.put(4,mSoundUtils.load(R.raw.giao_move_05));
                moveSoundIds.put(5,mSoundUtils.load(R.raw.giao_move_06));
                mMoveSound = new MoveSound(moveSoundIds);

                mSoundUtils.getSoundPool().setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                    @Override
                    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                        LogUtils.log("onLoadComplete id = " + sampleId);
                        mStartSound.onPrepared(sampleId);
                    }
                });
//            } catch (Exception e) {
//                Log.w(TAG, "Exception in initSounds : " + e);
//            }
        }
    }

    public void playSound(SoundType type) {
        mStartSound.playSound(type);
    }

    public SoundInfo getNext(SoundType type) {
        SoundInfo next = null;
        switch (type) {
            case START:
                next = mStartSound;
                break;
            case MOVE:
                next = mMoveSound;
                break;
            case GAME_OVER:
                next = mGameOverSound;
                break;
        }
        return next;
    }
}


