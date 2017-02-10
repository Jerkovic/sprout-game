package com.binarybrains.sprout.screen;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.ashley.components.MovementComponent;
import com.binarybrains.sprout.entity.ashley.components.PositionComponent;
import com.binarybrains.sprout.entity.ashley.systems.MovementSystem;
import com.binarybrains.sprout.hud.Hud;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;

public class GameScreen implements Screen {

    public SproutGame game;
    public Level level;
    public SpriteBatch batch;
    public Float width;
    public Float height;

    public enum GameState
    {
        PAUSE,
        RUN,
    }

    public GameState gameState = GameState.RUN;
    public Hud hud;

    Skin skin;

    public GameScreen(SproutGame game) {
        this.game = game;
        width = (float) Gdx.graphics.getWidth();
        height = (float) Gdx.graphics.getHeight();

        batch = new SpriteBatch();
        level = new Level(this, 1);
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        hud = new Hud(skin, level);

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());
        multiplexer.addProcessor(level.player);
        Gdx.input.setInputProcessor(multiplexer);

        // custom mouse pointer test
        Pixmap pm = new Pixmap(Gdx.files.internal("mouse_pointer.png"));
        Gdx.graphics.setCursor(Gdx.graphics.newCursor(pm, 0, 0));
        pm.dispose();

        BackgroundMusic.start();

        // ashley test
        Array<Entity> entities = new Array<Entity>();

        PooledEngine engine = new PooledEngine();
        engine.addSystem(new MovementSystem());


        for (int i = 0; i < 10; i++) {
            Entity entity = engine.createEntity();

            entity.add(new MovementComponent(10, 10));
            entity.add(new PositionComponent(0, 0));

            engine.addEntity(entity);

            entities.add(entity);
        }

        engine.update(0f); // deltaTime

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
                hud.act(Math.min(delta, 1 / 60f));
                SproutGame.getTweenManager().update(Math.min(delta, 1 / 60f)); // really here???
                break;
            case PAUSE:
                //System.out.println("paused");
                break;
            default:
                break;
        }

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
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        level.draw();
        hud.draw();

    }

    @Override
    public void resize(int width, int height) {
        // the windowed mode is not resizable
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
        batch.dispose();

    }
}