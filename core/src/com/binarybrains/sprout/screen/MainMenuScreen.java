package com.binarybrains.sprout.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;

public class MainMenuScreen implements Screen {

    private Stage stage;
    private SproutGame game;

    public MainMenuScreen(SproutGame game) {
        this.game = game;
    }

    private void buildWindow() {
        Skin skin = game.getSkin();
        extendSkinStyles(skin);

        TextureAtlas atlas = SproutGame.assets.get("new_ui_experiment/ui.atlas");
        TextureAtlas.AtlasRegion winTemplate = atlas.findRegion("window");
        // Slots button
        TextureAtlas.AtlasRegion buttonTemplate = atlas.findRegion("button");
        TextureAtlas.AtlasRegion buttonHoverTemplate = atlas.findRegion("button-hover");

        // Action buttons
        TextureAtlas.AtlasRegion bigButtonTemplate = atlas.findRegion("big-button-normal");
        TextureAtlas.AtlasRegion bigButtonHoverTemplate = atlas.findRegion("big-button-hover");

        NinePatch ninePatch = new NinePatch(winTemplate, 16, 16, 41, 41);
        NinePatch buttonPatch = new NinePatch(buttonTemplate, 8, 8, 8, 8);
        NinePatch buttonHoverPatch = new NinePatch(buttonHoverTemplate, 8, 8, 8, 8);

        NinePatch bigButtonPatch = new NinePatch(bigButtonTemplate, 16, 16, 8, 8);
        NinePatch bugButtonHoverPatch = new NinePatch(bigButtonHoverTemplate, 16, 16, 8, 8);

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.down = new NinePatchDrawable(bigButtonPatch);
        textButtonStyle.up = new NinePatchDrawable(bigButtonPatch);
        textButtonStyle.over = new NinePatchDrawable(bugButtonHoverPatch);
        textButtonStyle.font = skin.getFont("ruin-font");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.YELLOW;
        skin.add("text-button-default", textButtonStyle);


        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.background = new NinePatchDrawable(ninePatch);
        windowStyle.titleFont = skin.getFont("ruin-font");
        windowStyle.titleFontColor = Color.WHITE;


        skin.addRegions(atlas);

        Window window = new Window("Change log", windowStyle);
        window.setResizable(true);
        window.setPosition(0, 0);
        window.setVisible(true);
        window.setMovable(true);
        window.setSize(600, 200);

        // Image button style used for slots
        ImageButton.ImageButtonStyle ibs = new ImageButton.ImageButtonStyle();
        ibs.down = new NinePatchDrawable(buttonPatch);
        ibs.up = new NinePatchDrawable(buttonPatch);
        ibs.over = new NinePatchDrawable(buttonHoverPatch);
        ibs.disabled = new NinePatchDrawable(buttonPatch);
        ibs.checked = new NinePatchDrawable(buttonHoverPatch);
        skin.add("inventory-slot-btn", ibs);

        ImageButton btn = new ImageButton(ibs);
        btn.setSize(48, 48);

        stage.addActor(window);
    }

    /**
     * Todo Move this!
     * @return
     */
    public void extendSkinStyles(Skin skin) {
        TextureAtlas atlas = SproutGame.assets.get("new_ui_experiment/ui.atlas");

        TextureAtlas.AtlasRegion winTemplate = atlas.findRegion("window");
        TextureAtlas.AtlasRegion winBorderlessTemplate = atlas.findRegion("label");

        NinePatch window9Patch = new NinePatch(winTemplate, 16, 16, 41, 16);
        NinePatch widget9Patch = new NinePatch(winBorderlessTemplate, 16, 16, 16, 16);

        // Dim dialog dialogDim
        Skin.TintedDrawable tintedDrawable = new Skin.TintedDrawable();
        tintedDrawable.color = new Color(0,0,0, .8f);
        tintedDrawable.name = "dialogDim";
        skin.add("dialogDim", tintedDrawable);

        // Create Window with title bar
        Window.WindowStyle windowStyle = new Window.WindowStyle();
        windowStyle.background = new NinePatchDrawable(window9Patch);
        windowStyle.titleFont = skin.getFont("ruin-font");
        windowStyle.titleFontColor = Color.WHITE;
        windowStyle.stageBackground = null; // we need a style for dimmed win.
        skin.add("new-ui-win", windowStyle);

        // Window dialog with stage dimmer
        Window.WindowStyle dialogStyle = new Window.WindowStyle();
        dialogStyle.background = new NinePatchDrawable(window9Patch);
        dialogStyle.titleFont = skin.getFont("ruin-font");
        dialogStyle.titleFontColor = Color.WHITE;
        dialogStyle.stageBackground = skin.getDrawable("dialogDim");
        skin.add("new-ui-dialog", dialogStyle);

        // without title bar no need for dm
        skin.add("test-draw", new NinePatchDrawable(widget9Patch));

    }

    private void buildUI() {

        Skin skin = game.getSkin();

        // dont load like this!!!!!!!!!!!
        skin.add("button-black", new Texture( Gdx.files.internal("new-design-button.png")));
        skin.add("main-menu-background", new Texture( Gdx.files.internal("menu_bg2.png")));



        TextureRegion buttonRegion1 = new TextureRegion(skin.get("button-black", Texture.class));

        Texture logotext = new Texture(Gdx.files.internal("farmbound.png"));
        Image logoImg = new Image(logotext);

        // TextureRegion buttonRegion2 = new TextureRegion(skin.get("button-black-hover", Texture.class));

        TextButton.TextButtonStyle button1 = new TextButton.TextButtonStyle(
                new TextureRegionDrawable(buttonRegion1),
                new TextureRegionDrawable(buttonRegion1),
                new TextureRegionDrawable(buttonRegion1),
                skin.getFont("ruin-font")
        );


        button1.fontColor = Color.LIGHT_GRAY;
        button1.overFontColor = Color.YELLOW;

        // Label with "ver 0.23a"

        TextButton play = new TextButton("START GAME", button1);

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
        Button opt = new TextButton("OPTIONS", button1);

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
        Button credits = new TextButton("CREDITS", button1);

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
        Button exit = new TextButton("EXIT TO DESKTOP", button1);

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
        buttonTable.add(logoImg).pad(20);
        buttonTable.row();
        buttonTable.add(play).pad(10);
        buttonTable.row();
        buttonTable.add(opt).pad(10);
        buttonTable.row();
        buttonTable.add(credits).pad(10);
        buttonTable.row();
        buttonTable.add(exit).pad(10);
        buttonTable.row();
        buttonTable.pack();
        buttonTable.setPosition(
                Gdx.graphics.getWidth() / 2 - buttonTable.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - buttonTable.getHeight()/2
        );


        stage = new Stage();
        TextureRegion bg = new TextureRegion(skin.get("main-menu-background", Texture.class));
        Image backdrop = new Image(bg);
        backdrop.setFillParent(true);
        backdrop.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backdrop.setAlign(Align.center);
        stage.addActor(backdrop);
        Gdx.input.setInputProcessor(stage);
        stage.addActor(buttonTable);

        buildWindow();
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

