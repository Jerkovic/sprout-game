package com.binarybrains.sprout.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.binarybrains.sprout.SproutGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        if (true == true) { // todo args instead and create two runners
            config.width = 1440; //1440;
            config.height = 900;//900;
            config.fullscreen = true;

        }
        else {
            config.width = 1024; //1440;
            config.height = 768;//900;
            config.fullscreen = false;

        }

        // config.disableAudio = true;
        config.resizable = false;
        config.useGL30 = false;
		config.title = SproutGame.name;
        config.vSyncEnabled = true;
		new LwjglApplication(new SproutGame(), config);
	}
}
