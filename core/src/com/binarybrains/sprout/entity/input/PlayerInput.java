package com.binarybrains.sprout.entity.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.binarybrains.sprout.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class PlayerInput implements InputProcessor {

    public static enum Keys {
        W, A, S, D
    }
    private static Map<Keys, Boolean> keys = new HashMap<>();

    static {
        keys.put(Keys.W, false);
        keys.put(Keys.A, false);
        keys.put(Keys.S, false);
        keys.put(Keys.D, false);
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                keys.get(keys.put(Keys.A, true));
                break;
            case Input.Keys.D:
                keys.get(keys.put(Keys.D, true));
                break;
            case Input.Keys.W:
                keys.get(keys.put(Keys.W, true));
                break;
            case Input.Keys.S:
                keys.get(keys.put(Keys.S, true));
                break;
        }
        return true;
    }
    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                keys.get(keys.put(Keys.A, false));
                break;
            case Input.Keys.D:
                keys.get(keys.put(Keys.D, false));
                break;
            case Input.Keys.W:
                keys.get(keys.put(Keys.W, false));
                break;
            case Input.Keys.S:
                keys.get(keys.put(Keys.S, false));
                break;
        }
        return false;
    }

    /**
     *
     */
    public void releaseKeys() {
        keys.put(Keys.A, false);
        keys.put(Keys.D, false);
        keys.put(Keys.W, false);
        keys.put(Keys.S, false);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
