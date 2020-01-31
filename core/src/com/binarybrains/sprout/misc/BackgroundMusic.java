package com.binarybrains.sprout.misc;


import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.binarybrains.sprout.SproutGame;

public class BackgroundMusic {

    private static Music currentTrack; // make a map out of it?

    private static float FACTOR = .2f; // The bigger the factor, the faster the fade-out will be
    private static float mVolume = .8f;
    private static boolean mIsPlaying = false;
    private static boolean isStopped = false;

    public static void start() {
        stop();
        
        if (currentTrack != null && currentTrack.isPlaying()) currentTrack.stop();

        mIsPlaying = true;
        isStopped = false;
        try {
            currentTrack.setLooping(true);
            currentTrack.play();
        } catch (GdxRuntimeException e) {
            System.out.println("============================================");
            System.out.println("!Error! :" + e);
            System.out.println("============================================");
        }
    }

    public static void changeTrack(int track) {
        changeTrack("track" + track);
    }

    public static void setVolume(float volume) {
        mVolume = volume;
        currentTrack.setVolume(mVolume);
    }

    public static void changeTrack(String newTrackName) {
        // is this causing the crash
        if (currentTrack != null) currentTrack.stop();
        stop();

        currentTrack = SproutGame.assets.get("music/" + newTrackName + ".mp3");
        mIsPlaying = true;
        isStopped = false;
        currentTrack.setLooping(true);
        currentTrack.play();

    }

    public static boolean isPlaying() {
        return mIsPlaying && !isStopped;
    }

    public static void stop() {
        isStopped = true;
    }

    public static void dispose() {
        // dispose tracks
        if (currentTrack != null) currentTrack.dispose();
    }

    // setOnCompletionListener <- should look into this.

    // todo We are missing a fade up
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
