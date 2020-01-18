package com.binarybrains.sprout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Timer;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.hud.Hud;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.AmbienceSound;
import com.binarybrains.sprout.misc.BackgroundMusic;
import com.binarybrains.sprout.misc.EnviroManager;

public class GameScreen implements Screen {

    public SproutGame game;
    public Level level;
    public Float width;
    public Float height;
    public Hud hud;
    public Skin skin;

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

        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        level = new Level(this, 1);
        hud = new Hud(skin, level);

        EnviroManager.getInstance().init(level);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(level.player);
        Gdx.input.setInputProcessor(multiplexer);

        Pixmap pm = new Pixmap(Gdx.files.internal("mouse_pointer.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        // AmbienceSound.setSoundAndStart("forest_night_ambience");

        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                if (!BackgroundMusic.isPlaying()) {
                    //BackgroundMusic.start();
                    //BackgroundMusic.setVolume(0.12f);
                }
            }
        }, 20.0f, 60);
    }

    @Override
    public void show() {
        hud.fadeIn();
        // SproutGame.playSound("god_morning", .5f);
    }

    @Override
    public void render(float delta) {

        switch (gameState)
        {
            case RUN:
                BackgroundMusic.update(delta);
                level.update(Math.min(delta, 1 / 60f));
                break;
            case PAUSE: // ESC
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
                // level.player.inventory.renderDebug();
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
        AmbienceSound.pause();
        // BackgroundMusic.stop();
        level.cameraFix();

        gameState = GameState.PAUSE;
        level.gameTimer.pause();
    }

    @Override
    public void resume() {
        level.player.releaseKeys();
        AmbienceSound.resume();
        gameState = GameState.RUN;
        level.gameTimer.resume();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        hud.dispose(); // just killing listeners at the moment
        level.dispose();
        skin.dispose();
        BackgroundMusic.dispose();
        AmbienceSound.dispose();
    }
}