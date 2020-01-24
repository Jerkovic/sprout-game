package com.binarybrains.sprout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.binarybrains.sprout.SproutGame;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private SproutGame game;

    public MainMenuScreen(SproutGame game) {
        this.game = game;
    }

    private void buildUI() {

        Skin skin = game.getSkin();


        skin.add("button-black", new Texture( Gdx.files.internal("new-design-button.png")));
        skin.add("main-menu-background", new Texture( Gdx.files.internal("menu_bg.png")));


        TextureRegion buttonRegion1 = new TextureRegion(skin.get("button-black", Texture.class));
        // TextureRegion buttonRegion2 = new TextureRegion(skin.get("button-black-hover", Texture.class));


        TextButton.TextButtonStyle button1 = new TextButton.TextButtonStyle(
                new TextureRegionDrawable(buttonRegion1),
                new TextureRegionDrawable(buttonRegion1),
                new TextureRegionDrawable(buttonRegion1),
                skin.getFont("default-font")
        );

        button1.fontColor = Color.LIGHT_GRAY;
        button1.overFontColor = Color.YELLOW;

        Button play = new TextButton("Play Game", button1);

        play.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SproutGame.playSound("button_click", 1f, MathUtils.random(1f, 1f), 1f);
                startGame(); // transition
            }
        });

        play.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
            }
        });

        // Options
        Button opt = new TextButton("Options", button1);

        opt.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SproutGame.playSound("button_click", 1f, MathUtils.random(1f, 1f), 1f);
            }
        });

        opt.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

            }
        });

        // Credits
        Button credits = new TextButton("Credits", button1);

        credits.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SproutGame.playSound("button_click", 1f, MathUtils.random(1f, 1f), 1f);
            }
        });

        credits.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

            }
        });

        // Exit
        Button exit = new TextButton("Exit Game", button1);

        exit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                SproutGame.playSound("button_click", 1f, MathUtils.random(1f, 1f), 1f);
                Gdx.app.exit();
            }
        });

        exit.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {

            }
        });


        Table buttonTable = new Table(skin);
        // buttonTable.setBackground("default-select-selection");
        buttonTable.add(play).pad(12);
        buttonTable.row();
        buttonTable.add(opt).pad(12);
        buttonTable.row();
        buttonTable.add(credits).pad(12);
        buttonTable.row();
        buttonTable.add(exit).pad(12);
        buttonTable.row();
        buttonTable.setWidth(200);
        buttonTable.setHeight(300);
        buttonTable.setPosition(
                Gdx.graphics.getWidth() / 2 - buttonTable.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - buttonTable.getHeight()/2
        );
        buttonTable.pack();

        stage = new Stage();
        TextureRegion bg = new TextureRegion(skin.get("main-menu-background", Texture.class));
        stage.addActor(new Image(bg));
        Gdx.input.setInputProcessor(stage);
        stage.addActor(buttonTable);
    }


    @Override
    public void show() {
        buildUI();
    }

    public void startGame() {
        game.setScreen(new GameScreen(game));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.01f, 0.01f, .03f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

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
        // unload shit
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

