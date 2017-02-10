package com.binarybrains.sprout.misc;

import com.badlogic.gdx.audio.Music;
import com.binarybrains.sprout.SproutGame;


public class MusicPlayer {

    static public Music music = SproutGame.assets.get("music/track1.mp3");

    public static void start() {

        music.play();
        music.setLooping(true);
    }

    public static void update(float delta) {

    }

    public static void stop() {
        music.stop();
    }
}
