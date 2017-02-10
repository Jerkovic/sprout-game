package com.binarybrains.sprout.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

/**
 * Created by erikl on 08/02/17.
 */
public class Display {

    public static Graphics.DisplayMode[] getSupportedFullscreenModes() {
        return Gdx.graphics.getDisplayModes();

    }

}
