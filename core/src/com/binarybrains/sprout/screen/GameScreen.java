package com.binarybrains.sprout.screen;

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
    public Sound forestAmbienceSfx; // temporary
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

        BackgroundMusic.setVolume(.10f); // Preference.getSetting("music_volume")

        forestAmbienceSfx = SproutGame.assets.get("ambience/forest_morning_ambience.mp3");
        forestAmbienceSfx.loop(.25f);


        Timer.schedule(new Timer.Task(){
            @Override
            public void run(){
                if (!BackgroundMusic.isPlaying()) {
                    BackgroundMusic.start();
                    BackgroundMusic.setVolume(0.4f);
                }
            }
        }, 3.0f, 60);

    }

    @Override
    public void show() {

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
                gameState = GameState.PAUSE;
                level.gameTimer.paus();

                hud.showCraftingWindow();
            } else {
                gameState = GameState.RUN;
                level.gameTimer.resume();
            }
        }

        // Draw
        level.draw();
        hud.draw();
    }

    @Override
    public void resize(int width, int height) {
        // no resize available
    }

    @Override
    public void pause() {
        gameState = GameState.PAUSE;
    }

    @Override
    public void resume() {

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