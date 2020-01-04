package com.binarybrains.sprout.misc;

import com.badlogic.gdx.audio.Sound;
import com.binarybrains.sprout.SproutGame;

public class AmbienceSound {
    static Sound currentAmbience;

    static float FACTOR = .2f; // The bigger the factor, the faster the fade-out will be
    static float mVolume = 1.2f;
    static boolean mIsPlaying = false;
    static boolean isStopped = false;
    static long soundId;

    public static void start() {
        if (currentAmbience != null) AmbienceSound.soundId = currentAmbience.loop(.15f);
    }

    public static void setSoundAndStart(String soundName) {
        if (currentAmbience != null) {
            currentAmbience.stop(AmbienceSound.soundId);
            currentAmbience.dispose();
        }
        currentAmbience = SproutGame.assets.get("ambience/" + soundName+ ".mp3", Sound.class);
        AmbienceSound.start();
    }

    public static void setVolume(float volume) {

    }

    public static void pause() {
        currentAmbience.pause(AmbienceSound.soundId);
    }

    public static void resume()
    {
        currentAmbience.resume(AmbienceSound.soundId);
    }

    public static void dispose() {
        if (currentAmbience != null) currentAmbience.dispose();
    }

    public static void update(float delta) {
    }
}
