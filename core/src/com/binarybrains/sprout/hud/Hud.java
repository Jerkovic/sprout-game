package com.binarybrains.sprout.hud;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.experience.LevelRank;
import com.binarybrains.sprout.hud.inventory.CraftingWindow;
import com.binarybrains.sprout.hud.inventory.InventoryManagementWindow;
import com.binarybrains.sprout.hud.inventory.InventoryWindow;
import com.binarybrains.sprout.hud.tweens.ActorAccessor;
import com.binarybrains.sprout.hud.tweens.CameraAccessor;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.AmbienceSound;
import com.binarybrains.sprout.misc.BackgroundMusic;
import sun.rmi.server.InactiveGroupException;

import java.text.NumberFormat;
import java.util.Locale;


public class Hud {

    Stage stage;
    Label timeLabel, moneyLabel, xpLabel;
    Level level;
    Skin skin;
    Label fpsLabel;
    ProgressBar healthBar;
    ProgressBar levelBar;

    Image moneyIcon;

    private Actor fadeActor = new Actor();
    private ShapeRenderer fadeRenderer;

    TextureAtlas atlas;
    CraftingWindow craftingWindow;
    InventoryWindow inventoryWindow;
    InventoryManagementWindow inventoryManagementWindow;

    public int notificationsInHud = 0;
    public Image mouseItem;

    public Hud(Skin skin, Level level) {
        this.skin = skin;
        this.level = level;
        stage = new Stage(new ScreenViewport());
        atlas = SproutGame.assets.get("items2.txt");

        craftingWindow = new CraftingWindow(level.player,"Crafting", skin);
        craftingWindow.setVisible(false);
        craftingWindow.hide();
        stage.addActor(craftingWindow);


        craftingWindow.setPosition(Gdx.graphics.getWidth() / 2 - craftingWindow.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - craftingWindow.getHeight()/2);

        inventoryManagementWindow = new InventoryManagementWindow(level, skin);
        inventoryManagementWindow.setVisible(false);
        inventoryManagementWindow.hide();
        stage.addActor(inventoryManagementWindow);


        inventoryWindow = new InventoryWindow(level, skin);
        inventoryWindow.onInventoryChanged(level.player.getInventory());
        stage.addActor(inventoryWindow);

        fadeRenderer = new ShapeRenderer();
        fadeActor.clearActions();
        fadeActor.setColor(Color.CLEAR);

        gameTimeWindow();

        /*
        Dialog menuWindow = new TabbedMenuWindow(skin);
        menuWindow.setVisible(true);
        stage.addActor(menuWindow);
        */
    }

    public void setMouseItem(String regionId) {
        removeMouseItem();
        mouseItem = new Image(atlas.findRegion(regionId));
        getStage().addActor(mouseItem);
    }

    public void removeMouseItem() {
        if (mouseItem != null)
            mouseItem.remove();
            mouseItem = null;

    }

    public void fadeOutRunFadeInScreen(Runnable runnable) {
        fadeActor.clearActions();
        fadeActor.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(.6f, Interpolation.fade),
                Actions.run(runnable),
                Actions.fadeOut(.4f, Interpolation.fade)
        ));
    }

    public void teleportPlayer(final Player player, final int x, final int y) {
        player.releaseKeys();
        fadeActor.clearActions();
        fadeActor.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(.5f, Interpolation.fade),
                Actions.run(new Runnable() { public void run(){
                    player.setTilePos(x,y);
                    player.getLevel().getCamera().setPosition(new Vector3(player.getX(), player.getY(), 0));
                    SproutGame.playSound("door_close1"); // todo different sounds
                }}),
                Actions.fadeOut(.3f, Interpolation.fade)
        ));

    }

    public void playerPassedOut(final Player player) {
        player.releaseKeys();
        player.freezePlayerControl();
        fadeActor.clearActions();

        BackgroundMusic.stop();
        AmbienceSound.pause();

        SproutGame.playSound("heartbeat", 0.9f);

        fadeActor.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(.5f, Interpolation.fade),
                Actions.delay(3f),
                Actions.run(new Runnable() { public void run(){
                    player.setTilePos(9,2); // bed
                    player.getLevel().getCamera().setPosition(new Vector3(player.getX(), player.getY(), 0));
                    //SproutGame.playSound("door_close1"); //
                }}),
                Actions.fadeOut(.9f, Interpolation.fade),
                Actions.run(new Runnable() { public void run(){
                    player.setHealth(90);
                    player.getLevel().screen.hud.updateXP(player);
                    player.getLevel().screen.hud.addToasterMessage("Oh no!", "You passed out and woke up in your bed...");
                    player.unFreezePlayerControl();
                }})
        ));

    }

    public void hideMouseItem() {
        if (mouseItem == null) return;
        mouseItem.setVisible(false);
    }

    public void showMouseItem() {
        if (mouseItem == null) return;
        mouseItem.setVisible(true);
    }

    public void showCraftingWindow() {
        level.screen.game.pause();
        level.player.releaseKeys();
        craftingWindow.build(); // refresh content in window
        craftingWindow.setVisible(true);
        craftingWindow.show(getStage());
        craftingWindow.setScrollFocus(getStage());
        hideMouseItem();
    }

    public void showInventoryManagementWindow() {
        hideInventory();
        level.screen.game.pause();
        level.player.releaseKeys();
        // inventoryManagementWindow.build(); // refresh content in window
        inventoryManagementWindow.setVisible(true);
        inventoryManagementWindow.show(getStage());
        inventoryManagementWindow.onInventoryChanged(level.player.getInventory());
    }

    public void refreshInventory()  {
        inventoryWindow.onInventoryChanged(level.player.getInventory());
        removeMouseItem(); // ?
    }

    public void inventoryTop() {
        inventoryWindow.setWindowTop();
    }

    public void inventoryBottom() {
        inventoryWindow.setWindowBottom();
    }

    public void hideInventory() {
        inventoryWindow.setVisible(false);
    }

    public void showInventory() {
        inventoryWindow.setVisible(true);
    }

    public void addNotification(Item item) {
        // here we need a queue or something
        // action que from our Pinball game will do
        // the newest should be rendered from the top down
        // and a window should disappear after 4 secs
        buildNotificationsWindow(item);
    }

    // a test right now
    public void buildNotificationsWindow(Item item) {
        final Window window = new Window("YOU PICKED UP", skin);
        window.setRound(false);
        window.setKeepWithinStage(false);
        window.setPosition(20, -90);
        window.setMovable(false);
        window.row().fill().expandX();

        Image icon = new Image(atlas.findRegion(item.getRegionId()));

        Label notLabel = new Label(item.getNotificationText(), skin);
        notLabel.setWrap(false);
        notLabel.setWidth(150);
        notLabel.setEllipsis(true);
        notLabel.pack();

        window.add(icon);
        window.add(notLabel).pad(6f);
        window.row();
        window.pack();

        stage.addActor(window);

        // this should be grouped in some clever way.. no need to spawn 6 wood pickups
        int hudPosition = (notificationsInHud++ * 70) + 70;
        //Tween.set(window, ActorAccessor.ALPHA).target(0f).start(SproutGame.getTweenManager());
        Tween.to(window, ActorAccessor.POSITION_XY, 2.9f).target(20, hudPosition).ease(TweenEquations.easeOutElastic).delay(0.4f)
        .setCallback(new TweenCallback() {

            @Override
            public void onEvent(int type, BaseTween<?> source) {
                window.remove();
                notificationsInHud = -1;
            }

        }).start(SproutGame.getTweenManager());
        //Tween.to(window, ActorAccessor.ALPHA, 2).target(1f).start(SproutGame.getTweenManager());
        //.addAction(forever(sequence(fadeOut(5), fadeIn(5))));
    }

    public void moveCamera(float targetX, float targetY) {

        level.getCamera().disableFollow();

        Tween.to(level.getCamera(), CameraAccessor.POSITION_XY, 3f)
                .target(targetX, targetY)
                .ease(TweenEquations.easeOutSine).setCallback(new TweenCallback() {

                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        level.getCamera().enableFollow();
                    }

                }).start(SproutGame.getTweenManager());
    }

    // a test right now, we need some graphics
    public void addToasterMessage(String title, String text) {
        final Window window = new Window(title, skin);
        window.setRound(true);
        window.setKeepWithinStage(false);


        window.setMovable(false);
        window.row().fill().expandX();

        Label notLabel = new Label(text, skin);
        notLabel.setWrap(false);
        notLabel.setWidth(600);

        notLabel.setEllipsis(true);
        notLabel.pack();

        window.add(notLabel).pad(6f);
        window.row();
        window.pack();
        float startY = Gdx.graphics.getHeight() - window.getHeight() - 120;
        float endY = Gdx.graphics.getHeight() - window.getHeight() - 5;
        window.setPosition(Gdx.graphics.getWidth() / 2 - window.getWidth() / 2, startY);

        float moveBy = startY - endY;
        stage.addActor(window);

        window.addAction(Actions.sequence(
                Actions.alpha(0f),

                Actions.parallel(
                        Actions.alpha(1f, .8f, Interpolation.fade),
                        Actions.moveBy(0, -moveBy, 1.5f, Interpolation.fade)
                ),

                Actions.delay(5f),
                Actions.parallel(
                        Actions.alpha(0f, .5f, Interpolation.fade),
                        Actions.moveBy(0, -moveBy, 1.5f, Interpolation.fade)
                ),

                Actions.run(new Runnable() { public void run(){
                    window.remove();
                }})
        ));
    }

    public void speakDialog(String title, String say) {
        level.screen.game.pause();
        hideMouseItem();
        TypeWriterDialog dialog = new TypeWriterDialog(title, skin, "dialog") {
            public void result(Object obj) {
               // System.out.println("result "+obj);
                level.screen.game.resume();
                showMouseItem();
            }
        };

        dialog.text(say);
        dialog.button("  OK  ", true); //sends "true" as the result
        dialog.center();
        //dialog.key(Input.Keys.Enter, true); //sends "true" when the ENTER key is pressed
        dialog.show(stage);
    }

    public void gameTimeWindow() {

        timeLabel = new Label("Day 0 00:00", skin);
        fpsLabel = new Label("", skin);
        moneyLabel = new Label("0", skin);
        xpLabel = new Label("XP:", skin);


        Table table = new Table(skin);
        table.bottom();
        table.setFillParent(false);

        Image icon = new Image(atlas.findRegion("Clock")); // time clock icon
        table.add(timeLabel).left();
        table.row();
        table.add(moneyLabel).left();
        table.row();
        table.add(xpLabel).left();
        table.row();
        table.add(fpsLabel).left();
        table.row();
        stage.addActor(table);
        Window window = new Window(SproutGame.name, skin);
        window.setKeepWithinStage(false);
        window.setMovable(true);
        window.row().fill().expandX();
        timeLabel.setWrap(false);
        window.add(table).pad(4f);
        window.row();
        window.add(buildHealthMeters());
        window.row();

        window.add(buildLevelRankMeters()).pad(4f);
        window.pack();

        window.setPosition(10, Gdx.app.getGraphics().getHeight() - window.getHeight()-10);
        stage.addActor(window);
    }


    public Stage getStage() {
        return stage;
    }

    public Table buildLevelRankMeters() {
        Table hudTable = new Table(skin);

        Pixmap pixmap = new Pixmap(1, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("default-hud-texture", new Texture(pixmap));
        pixmap.dispose();

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(
                skin.newDrawable("default-hud-texture", Color.BLACK),
                skin.newDrawable("default-hud-texture", Color.YELLOW)
        );

        barStyle.knobBefore = barStyle.knob;

        barStyle.knobBefore.setLeftWidth(0f);
        barStyle.knobBefore.setRightWidth(0f);
        //barStyle.knobBefore = barStyle.knob;

        //barStyle.knob = barStyle.knobBefore;
        //barStyle.knobAfter = true;

        levelBar = new ProgressBar(0, 99f, 1f, false, barStyle);
        levelBar.setAnimateDuration(.25f);
        levelBar.setValue(0f);
        hudTable.add(levelBar);
        hudTable.row();

        return hudTable;

    }


    public Table buildHealthMeters() {
        Table hudTable = new Table(skin);

        Pixmap pixmap = new Pixmap(10, 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("default-hud-texture", new Texture(pixmap));
        pixmap.dispose();

        ProgressBar.ProgressBarStyle barStyle = new ProgressBar.ProgressBarStyle(
                skin.newDrawable("default-hud-texture", Color.BLACK),
                skin.newDrawable("default-hud-texture", Color.RED)
        );
        barStyle.knobBefore = barStyle.knob;

        healthBar = new ProgressBar(0, 100f, 1f, false, barStyle);
        healthBar.setAnimateDuration(1.5f);
        healthBar.setValue(100f);
        hudTable.add(healthBar);
        hudTable.row();

        return hudTable;

    }

    public void draw() {

        stage.draw();
        if (mouseItem != null)  mouseItem.setZIndex(9000);
        float alpha = fadeActor.getColor().a;

        if (alpha != 0){
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            fadeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            fadeRenderer.setColor(new Color(0f, 0f, 0.031f, alpha));
            fadeRenderer.rect(0, 0, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
            fadeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public void updateFunds(Player player) {
        /*
        if (moneyIcon.hasActions()) return;
        float oldW = moneyIcon.getImageWidth();
        float oldH = moneyIcon.getImageHeight();
        moneyIcon.addAction(Actions.sequence(
                Actions.sizeTo(moneyIcon.getImageWidth()+10,moneyIcon.getImageHeight()+10, .15f, Interpolation.pow2),
                Actions.sizeTo(oldW,oldH, .1f, Interpolation.fade)
        ));
        */
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        moneyLabel.setText("" + numberFormat.format(player.getStats().get("money")));
    }

    public void updateXP(Player player) {
        healthBar.setValue((float) player.getHealth());
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        int xp = player.getStats().get("xp");
        LevelRank rank = LevelRank.getLevelProgression(xp);
        xpLabel.setText("XP: " + numberFormat.format(xp) + " Level: " + rank.getCurrentLevel());

        levelBar.setValue(rank.getProgress());
    }

    public void act(float delta) {
        timeLabel.setText(level.gameTimer.toString());
        fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());

        // temp mouseItem
        if (mouseItem != null) {
            mouseItem.setPosition(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
        }

        stage.act(delta);
        fadeActor.act(delta);

    }

    public void dispose() {
        //super.dispose();
    }
}
