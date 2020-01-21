package com.binarybrains.sprout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;

public class LoadingScreen implements Screen {

    public static String LOGO_IMAGE = "game_logo.png";
    private Stage stage;
    private float percent;
    private SproutGame game;
    private ProgressBar loadingBar;
    private StringBuilder stringBuilder;

    public LoadingScreen(SproutGame game) {
        this.game = game;
        this.stringBuilder = new StringBuilder();
        buildProgressBar();
    }

    private void buildProgressBar() {
        Skin skin = game.getSkin();
        Pixmap pixmap = new Pixmap(1, 20, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("default-hud-texture", new Texture(pixmap));
        pixmap.dispose();

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(
                skin.newDrawable("default-hud-texture", Color.DARK_GRAY),
                skin.newDrawable("default-hud-texture", Color.RED)
        );
        barStyle.knobBefore = barStyle.knob;

        loadingBar = new ProgressBar(0, 100f, .5f, false, barStyle);
        loadingBar.setAnimateDuration(1f);
        loadingBar.setVisualInterpolation(Interpolation.smooth);
        loadingBar.setValue(0f);
        loadingBar.setWidth(300f);

        loadingBar.setPosition(
                (Gdx.graphics.getWidth() - loadingBar.getWidth()) / 2f,
                (Gdx.graphics.getHeight() - loadingBar.getHeight()) / 2f - 100
        );
    }


    @Override
    public void show() {

        SproutGame.assets.load(LoadingScreen.LOGO_IMAGE, Texture.class);
        SproutGame.assets.load("skin/uiskin.atlas", TextureAtlas.class);
        SproutGame.assets.load("skin/uiskin.json", Skin.class,
                new SkinLoader.SkinParameter("skin/uiskin.atlas"));
        // Wait until they are finished loading
        SproutGame.assets.finishLoading();

        stage = new Stage();

        Image logo = new Image((Texture) SproutGame.assets.get(LOGO_IMAGE));
        logo.setOrigin(Align.center);
        logo.setPosition(
                (Gdx.app.getGraphics().getWidth() - logo.getWidth()) / 2f,
                (Gdx.app.getGraphics().getHeight() - logo.getHeight()) / 2f
        );
        stage.addActor(logo);
        stage.addActor(loadingBar);
        game.loadAssets(); // Call everything else be loaded
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.01f, 0.01f, .1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (SproutGame.assets.update()) {
            // Transition to MainMenuScreen here
            game.setScreen(new GameScreen(game));
        }

        percent = Interpolation.linear.apply(percent, SproutGame.assets.getProgress(), 0.5f);
        stringBuilder.delete(0, stringBuilder.length());
        stringBuilder.append(String.format("%.0f", percent * 100));
        stringBuilder.append("%");
        //progressLabel.setText(stringBuilder.toString());
        loadingBar.setValue(percent * 100f);

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
        SproutGame.assets.unload(LOGO_IMAGE);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
