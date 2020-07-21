package com.binarybrains.sprout.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.util.SheetGenerator;

import java.awt.*;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

        Graphics.DisplayMode[] modes = LwjglApplicationConfiguration.getDisplayModes();
        System.out.println("Operating System: " + System.getProperty("os.name"));
        System.out.println("======================Graphic Modes=========================");
        for (Graphics.DisplayMode mode: modes) {
            System.out.println(mode);
        }
        System.out.println("============================================================");
        Graphics.DisplayMode displayMode = LwjglApplicationConfiguration.getDesktopDisplayMode();
        config.setFromDisplayMode(displayMode);
        config.width = 1920;
        config.height = 1080;
        config.fullscreen = true;
        config.resizable = false;
        config.useGL30 = true;
		config.title = SproutGame.name;
        config.vSyncEnabled = true;
        config.forceExit = true;
        // config.useHDPI = true // High Density Pixels on Mac?
        // Window icons: 128x128(Mac), 32x32 (for Win and Linux), and 16x16 (for Win).
        // config.addIcon("some icon", Files.FileType.Local);
        new LwjglApplication(new SproutGame(), config) {
                @Override
                public void exit()
                {
                        System.out.println("Overridden exit method Lwjgl app. should ask for exiting");
                        super.exit();
                }
        };

        // new LwjglApplication(new SheetGenerator(), config);
	}
}
