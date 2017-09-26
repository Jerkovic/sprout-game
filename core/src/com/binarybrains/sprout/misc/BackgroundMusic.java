package com.binarybrains.sprout.misc;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.binarybrains.sprout.SproutGame;

public class BackgroundMusic {

    static Music currentTrack;

    static float FACTOR = .2f; // The bigger the factor, the faster the fade-out will be
    static float mVolume = 1.2f;
    static boolean mIsPlaying = false;
    static boolean isStopped = false;

    public static void start() {

        changeTrack(MathUtils.random(1, 3)); // random select a track in our library
        mIsPlaying = true;
        isStopped = false;
        currentTrack.setLooping(true);
        currentTrack.play();
    }

    public static void changeTrack(int track) {
        changeTrack("track" + track);
    }

    public static void setVolume(float volume) {
        mVolume = volume;
        currentTrack.setVolume(mVolume);
    }

    public static void changeTrack(String newTrackName) {
        // load it here?
        currentTrack = SproutGame.assets.get("music/" + newTrackName + ".mp3");
    }

    public static boolean isPlaying() {
        return mIsPlaying && !isStopped;
    }

    public static void stop() {
        isStopped = true;
    }

    public static void dispose() {
        // dispose tracks
        currentTrack.dispose();
    }

    public static void update(float delta) {
        if (mIsPlaying && isStopped) {
            mVolume -= delta * FACTOR;
            if (mVolume > 0) {
                currentTrack.setVolume(mVolume);
            } else {
                currentTrack.stop();
                mIsPlaying = false;
            }
        }
    }
}
