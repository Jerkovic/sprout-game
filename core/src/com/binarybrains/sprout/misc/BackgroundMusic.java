package com.binarybrains.sprout.misc;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class BackgroundMusic {

    static Music track1 = Gdx.audio.newMusic(Gdx.files.internal("music/track1.mp3"));
    static float FACTOR = .3f; // The bigger the factor, the faster the fade-out will be
    static float mVolume = 1f;
    static boolean mIsPlaying = false;
    static boolean isStopped = false;

    public static void start() {
        mIsPlaying = true;
        track1.setLooping(true);
        track1.play();
    }

    public static void setVolume(float volume) {
        mVolume = volume;
        track1.setVolume(mVolume);
    }

    public static void stop() {
        isStopped = true;
    }

    public static void dispose() {
        track1.dispose();
    }

    public static void update(float delta) {
        if (mIsPlaying && isStopped) {
            mVolume -= delta * FACTOR;
            if (mVolume > 0) {
                track1.setVolume(mVolume);
            } else {
                track1.stop();
                mIsPlaying = false;
            }
        }
    }
}
