package com.example.bzy.my2048.sounds;

 public abstract class SoundInfo {
     abstract void onPrepared(int id);
     abstract void playSound(SoundType type);
     abstract void _playSound();
}
