package com.binarybrains.sprout.util;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;


public class SheetGenerator extends ApplicationAdapter {

    PlayerSpriteGenerator player;
    ShapeRenderer shapeRenderer;
    SpriteBatch batch;

    @Override
    public void create () {

        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        player = new PlayerSpriteGenerator();


    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(0.4f, 0.45f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.app.getGraphics().getDeltaTime();

        player.update(delta);
        // rendering stuff
        batch.begin();
        player.draw(batch);
        batch.end();


    }

}
