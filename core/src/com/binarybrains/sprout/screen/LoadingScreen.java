package com.binarybrains.sprout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.binarybrains.sprout.SproutGame;

public class LoadingScreen implements Screen {
    private Stage stage;
    private float percent;
    private SproutGame game;
    private Label progressLabel;
    private StringBuilder stringBuilder;

    public LoadingScreen(SproutGame game) {
        this.game = game;
        this.stringBuilder = new StringBuilder();
    }

    @Override
    public void show() {

        SproutGame.assets.load("game_logo2.png", Texture.class);
        // Wait until they are finished loading
        SproutGame.assets.finishLoading();

        stage = new Stage();

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        progressLabel = new Label("", skin);
        progressLabel.setPosition(300, 300);
        progressLabel.setColor(Color.WHITE);

        stage.addActor(new Image((Texture) SproutGame.assets.get("game_logo2.png")));
        stage.addActor(progressLabel);
        game.loadAssets(); // Call everything else be loaded
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (SproutGame.assets.update()) {
            // Transition to MainMenuScreen here
            game.setScreen(new GameScreen(game));
        }

        percent = Interpolation.linear.apply(percent, SproutGame.assets.getProgress(), 0.5f);
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(String.format("%.0f", percent * 100));
        stringBuilder.append("%");
        progressLabel.setText(stringBuilder.toString());

        // Update and Show the loading screen
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        // we can unload loading screen related stuff here
        // assets.unload("some asset texture", Texture.class);
        SproutGame.assets.unload("game_logo2.png");

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
