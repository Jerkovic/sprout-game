package com.binarybrains.sprout.screen;

import aurelienribon.tweenengine.equations.Back;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.hud.Hud;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;

public class GameScreen implements Screen {

    public SproutGame game;
    public Level level;
    public Float width;
    public Float height;
    public Hud hud;
    public Sound forestAmbienceSfx; // temporary move to AmbienceSound handler class
    Skin skin;

    public enum GameState
    {
        PAUSE,
        RUN,
    }

    public GameState gameState = GameState.RUN;

    public GameScreen(SproutGame game) {
        this.game = game;
        width = (float) Gdx.graphics.getWidth();
        height = (float) Gdx.graphics.getHeight();

        level = new Level(this, 1);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        hud = new Hud(skin, level);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(level.player);
        Gdx.input.setInputProcessor(multiplexer);

        Pixmap pm = new Pixmap(Gdx.files.internal("mouse_pointer.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        // todo we need a handling class for Ambience
        forestAmbienceSfx = SproutGame.assets.get("ambience/forest_morning_ambience.mp3");


        /*
        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                if (!BackgroundMusic.isPlaying()) {
                    BackgroundMusic.start();
                    BackgroundMusic.setVolume(0.18f);
                }
            }
        }, 2.0f, 10);
        */
    }

    private long soundID;

    @Override
    public void show() {
        soundID = forestAmbienceSfx.loop(.15f);
    }

    public void pauseAmbience () {
        forestAmbienceSfx.pause(soundID);
    }

    public void resumeAmbience () {
        forestAmbienceSfx.resume(soundID);
    }


    @Override
    public void render(float delta) {

        switch (gameState)
        {
            case RUN:
                BackgroundMusic.update(delta);
                level.update(Math.min(delta, 1 / 60f));
                break;
            case PAUSE:
                // System.out.println("paused");
                // todo indicate in hud that the game is paused
                break;
            default:
                break;
        }

        // Yes the tween manager has  to be here because the hud utils it
        SproutGame.getTweenManager().update(Math.min(delta, 1 / 60f));
        hud.act(Math.min(delta, 1 / 60f));

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (gameState == GameState.RUN) {
                pause();
                hud.showCraftingWindow();
            } else {
                resume();
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            if (gameState == GameState.RUN) {
                pause();
                hud.showInventoryManagementWindow();
                level.player.inventory.renderDebug();
            } else {
                resume();
            }
        }

        level.draw();
        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        // no resize available (yet)
    }

    @Override
    public void pause() {
        level.player.releaseKeys();
        forestAmbienceSfx.pause();
        //BackgroundMusic.stop();
        gameState = GameState.PAUSE;
        level.gameTimer.paus();
    }

    @Override
    public void resume() {
        level.player.releaseKeys();
        forestAmbienceSfx.resume();
        gameState = GameState.RUN;
        level.gameTimer.resume();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        level.dispose();
        skin.dispose();
        BackgroundMusic.dispose();
    }
}