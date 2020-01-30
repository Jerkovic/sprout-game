package com.binarybrains.sprout.hud;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.achievement.Achievement;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.furniture.Chest;
import com.binarybrains.sprout.events.*;
import com.binarybrains.sprout.experience.LevelRank;
import com.binarybrains.sprout.hud.components.ItemToaster;
import com.binarybrains.sprout.hud.components.LetterBoxing;
import com.binarybrains.sprout.hud.components.LocationLabel;
import com.binarybrains.sprout.hud.inventory.ChestWindow;
import com.binarybrains.sprout.hud.inventory.CraftingWindow;
import com.binarybrains.sprout.hud.inventory.InventoryManagementWindow;
import com.binarybrains.sprout.hud.inventory.InventoryWindow;
import com.binarybrains.sprout.hud.tweens.CameraAccessor;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.locations.Location;
import com.binarybrains.sprout.misc.AmbienceSound;
import com.binarybrains.sprout.misc.BackgroundMusic;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class Hud implements Telegraph {

    Stage stage;
    Label timeLabel, moneyLabel, xpLabel, lc;
    Level level;
    Skin skin;
    Label fpsLabel;
    ProgressBar healthBar;
    ProgressBar levelBar;

    private Actor fadeActor = new Actor();
    private ShapeRenderer fadeRenderer;
    public Boolean cutSceneMode = false;

    TextureAtlas atlas;

    // Hud gui major actors.
    Window gameTimeWindow;
    CraftingWindow craftingWindow;
    LocationLabel locationLabel; // location area indicator label
    InventoryWindow inventoryWindow; // our Inventory fast reachable slots
    InventoryManagementWindow inventoryManagementWindow;
    ChestWindow chestManagementWindow;

    public int notificationsInHud = 0;
    public Image mouseItem;

    public LetterBoxing letterboxing;

    public Hud(Skin skin, Level level) {
        this.skin = skin;
        this.level = level;

        stage = new Stage(new ScreenViewport());
        atlas = SproutGame.assets.get("items2.txt");

        locationLabel = new LocationLabel("Welcome to Bearshade Creek", skin, "default");
        locationLabel.setVisible(true);

        stage.addActor(locationLabel);

        // mouse
        lc = new Label("", skin);

        lc.setAlignment(Align.bottomRight);
        lc.setColor(250, 250, 250, 1f);
        stage.addActor(lc);

        craftingWindow = new CraftingWindow(level.player,"CRAFTING", skin);
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

        createGameTimeWindow();

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                inventoryWindow.activateSlotByShortcutKey(keycode);
                return false;
            }
        });

        chestManagementWindow = new ChestWindow(level.player, skin);
        chestManagementWindow.setVisible(false);
        chestManagementWindow.hide();
        stage.addActor(chestManagementWindow);

        // Letter boxing Cinema Mode actor
        float barHeight = Gdx.app.getGraphics().getHeight() * .1f;
        letterboxing = new LetterBoxing(stage, new Color(0f, 0f, 0.031f, 1), barHeight, .95f);
        stage.addActor(letterboxing);

        // Subscribe to events here
        MessageManager.getInstance().addListeners(this,
                TelegramType.PLAYER_STATS_XP_INCREASED,
                TelegramType.PLAYER_STATS_RANK_INCREASED,
                TelegramType.PLAYER_PASSED_OUT,

                TelegramType.PLAYER_STATS_HEALTH_DECREASED,
                TelegramType.PLAYER_STATS_HEALTH_INCREASED,

                TelegramType.PLAYER_LOCATION_REACHED,
                TelegramType.PLAYER_LOCATION_LEAVES,

                TelegramType.PLAYER_ACHIEVEMENT_UNLOCKED,
                TelegramType.PLAYER_CRAFTING_SUCCESS,
                TelegramType.PLAYER_CRAFTING_FAILURE,

                TelegramType.PLAYER_INVENTORY_UPDATED,
                TelegramType.PLAYER_INVENTORY_ADD_ITEM,

                TelegramType.TIME_MINUTE_INC
        );
    }

    /**
     *
     * @param text
     */
    public void setMousePointerLabel(String text) {
        lc.setText(text);
        lc.setVisible(true);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        // System.out.println("HUD got msg " + msg.message);
        switch(msg.message) {
            case TelegramType.PLAYER_STATS_RANK_INCREASED:
                rankedUp((int) msg.extraInfo);
                break;
            case TelegramType.PLAYER_STATS_XP_INCREASED:
                updateXP((Player) msg.sender);
                break;
            case TelegramType.PLAYER_STATS_HEALTH_DECREASED:
            case TelegramType.PLAYER_STATS_HEALTH_INCREASED:
                updateHealth((Player) msg.sender);
                break;
            case TelegramType.PLAYER_ACHIEVEMENT_UNLOCKED:
                Achievement achievement = ((Achievement) msg.extraInfo);
                SproutGame.playSound("magic_swish", .6f);
                addToasterMessage("New Achievement" , achievement.getName());
                // Add awards ...etc to ui?
                break;
            case TelegramType.PLAYER_PASSED_OUT:
                playerPassedOut((Player) msg.sender);
                break;
            case TelegramType.PLAYER_INVENTORY_UPDATED:
                refreshInventory();
                break;
            case TelegramType.PLAYER_INVENTORY_ADD_ITEM:
                 Item item = ((Item) msg.extraInfo);
                 addNotification(item);
                 break;
            case TelegramType.PLAYER_STATS_MONEY_UPDATED:
                updateFunds((int) msg.extraInfo);
                break;
            case TelegramType.TIME_MINUTE_INC:
                timeLabel.setText(msg.extraInfo.toString());
                fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
                break;
            case TelegramType.PLAYER_LOCATION_REACHED:
                Location location = (Location) msg.extraInfo;
                locationLabel.setText(location.name);
                locationLabel.setVisible(true);
                // speakDialog(location.name, location.description);
                // level.player.releaseKeys();
                // BackgroundMusic.stop();
                break;
             case TelegramType.PLAYER_LOCATION_LEAVES:
                 locationLabel.setText("...");
                 // locationLabel.setVisible(false);
                 break;
            default:
                // code block
        }
        return false;
    }

    public void setMouseItem(String regionId, String mpLabel) {
        removeMouseItem();
        mouseItem = new Image(atlas.findRegion(regionId));
        setMousePointerLabel(mpLabel);
        getStage().addActor(mouseItem);
    }

    public void removeMouseItem() {
        if (mouseItem != null) {
            lc.setVisible(false);
            mouseItem.remove();
            mouseItem = null;
        }
    }

    public void flash() {
        fadeActor.clearActions();
        fadeActor.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(.01f, Interpolation.fade),
                Actions.fadeOut(.8f, Interpolation.fade)

        ));
    }

    public void fadeIn() {
        fadeActor.clearActions();
        fadeActor.addAction(Actions.sequence(
                Actions.alpha(1f),
                Actions.fadeOut(2f, Interpolation.fade)
        ));
    }

    public void fadeOutRun(Runnable runnable) {
        fadeActor.clearActions();
        fadeActor.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(.6f, Interpolation.fade),
                Actions.run(runnable)
        ));
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
                Actions.run(() -> {
                    player.setTilePos(x,y);
                    player.getLevel().getCamera().setPosition(new Vector3(player.getX(), player.getY(), 0));
                    SproutGame.playSound("door_close1"); // todo different sounds
                }),
                Actions.fadeOut(.3f, Interpolation.fade)
        ));

    }

    public void playerPassedOut(final Player player) {
        player.releaseKeys();
        player.freezePlayerControl();
        fadeActor.clearActions();

        BackgroundMusic.stop();
        AmbienceSound.pause();

        SproutGame.playSound("heartbeat", 1.1f);

        fadeActor.addAction(Actions.sequence(
                Actions.alpha(0),
                Actions.fadeIn(1f, Interpolation.fade),
                Actions.delay(3.5f),
                Actions.run(() -> {
                    player.setTilePos(9,2); // bed hardcoded
                    player.getLevel().getCamera().setPosition(new Vector3(player.getX(), player.getY(), 0));

                }),
                Actions.fadeOut(1f, Interpolation.fade),
                Actions.run(() -> {
                    player.setHealth(90);
                    updateXP(player);
                    addToasterMessage("Oh no!", "You passed out and woke up in your bed...What happend?");
                    player.unFreezePlayerControl();
                })
        ));

    }

    public void hideMouseItem() {
        if (mouseItem == null) return;
        mouseItem.setVisible(false);
        lc.setVisible(true);
    }

    public void showMouseItem() {
        if (mouseItem == null) return;
        mouseItem.setVisible(true);
        lc.setVisible(true);
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
        SproutGame.playSound("inventory_bag_open");
        inventoryManagementWindow.setVisible(true);
        inventoryManagementWindow.show(getStage());
        // inventoryManagementWindow.setCloseCallback();
        inventoryManagementWindow.onInventoryChanged(level.player.getInventory());
        hideMouseItem();
    }

    public void startCinemaMode() {
        letterboxing.start();
    }

    public void endCinemaMode() {
        letterboxing.end();
    }

    /**
     *
     *
     */
    public void showChestManagementWindow(Chest chest) {
        hideInventory();
        level.screen.game.pause();
        level.player.releaseKeys();

        chestManagementWindow.setVisible(true);
        chestManagementWindow.show(getStage());
        chestManagementWindow.openChest(chest);
        hideMouseItem();
    }

    public void refreshInventory()  {
        inventoryWindow.onInventoryChanged(level.player.getInventory());
        removeMouseItem();
    }

    public void hideHud() {
        inventoryWindow.setVisible(false);
        locationLabel.setVisible(false);
        gameTimeWindow.setVisible(false);
    }

    public void showHud() {
        inventoryWindow.setVisible(true);
        locationLabel.setVisible(true);
        gameTimeWindow.setVisible(true);
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
        // Todo: use Pooling here
        buildNotificationsWindow(item);
    }

    private Map<String, ItemToaster> itemNotifications = new HashMap<String, ItemToaster>();

    // a test right now- should be pooled
    public void buildNotificationsWindow(Item item) {

        if (!itemNotifications.containsKey(item.getName())) {
            TextureAtlas.AtlasRegion icon = atlas.findRegion(item.getRegionId());
            ItemToaster notificationToaster = new ItemToaster(skin, item, new Image(icon), (notificationsInHud++ * 75) + 90);
            stage.addActor(notificationToaster);
            itemNotifications.put(item.getName(), notificationToaster);

            notificationToaster.addAction(Actions.sequence(
                    Actions.alpha(0f),
                    Actions.visible(true),
                    Actions.parallel(
                            Actions.alpha(1f, .4f, Interpolation.fade)
                    ),
                    Actions.delay(3f),
                    Actions.parallel(
                            Actions.alpha(0f, .1f, Interpolation.fade)
                    ),
                    Actions.run(() -> {
                        notificationToaster.remove();
                        notificationsInHud--;
                        itemNotifications.remove(item.getName());
                    })
            ));
        } else {
            ItemToaster it = itemNotifications.get(item.getName());
            it.increaseItemCounter(1);
        }

    }

    private void testAnimate(Actor actor) {
        actor.clearActions();
        actor.setOrigin(Align.center);
        actor.addAction(Actions.sequence(
                Actions.scaleTo(1.25f, 1.25f, .15f),
                Actions.scaleTo(1f, 1f, .10f)
        ));
    }

    /**
     * Move camera...should not be here?
     * @param targetX
     * @param targetY
     */
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

    /**
     *
     * @param level
     */
    private void rankedUp(int level) {
        addToasterMessage("LEVEL UP", "You reached Level " + level);
        SproutGame.playSound("magic_swish", .6f);
    }

    // a test right now, we need some graphics
    public void addToasterMessage(String title, String text) {
        final Window window = new Window(title, skin);
        window.setVisible(false);
        window.setRound(true);
        window.setKeepWithinStage(false);
        window.setMovable(false);

        TextureAtlas.AtlasRegion icon = atlas.findRegion("Book");
        if (icon != null) {
            Image image = new Image(icon);
            window.add(image).align(Align.center).pad(10);
        }

        window.row().fill().expandX();

        Label notLabel = new Label(text, skin);
        notLabel.setWrap(false);
        notLabel.setColor(0,0, 0, .6f);
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
                Actions.visible(true),
                Actions.parallel(
                        Actions.alpha(1f, 1f, Interpolation.fade),
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


    /**
     * Move this sucker to own class
     */
    public void createGameTimeWindow() {
        timeLabel = new Label("Day 0 00:00", skin);
        fpsLabel = new Label("", skin);
        moneyLabel = new Label("000000", skin);
        xpLabel = new Label("XP:", skin);

        Table table = new Table(skin);
        table.bottom();
        table.setFillParent(false);

        // Image icon = new Image(atlas.findRegion("Clock")); // time clock icon
        table.add(timeLabel).left();
        table.row();
        table.add(moneyLabel).left();
        table.row();
        table.add(xpLabel).left();
        table.row();
        table.add(fpsLabel).left();
        table.row();
        stage.addActor(table);
        gameTimeWindow = new Window(SproutGame.name, skin);
        gameTimeWindow.setStyle(skin.get("new-ui-win", Window.WindowStyle.class));
        gameTimeWindow.setKeepWithinStage(true);
        gameTimeWindow.setMovable(true);
        gameTimeWindow.row().fill().expandX();
        timeLabel.setWrap(false);
        gameTimeWindow.add(table).pad(4f);
        gameTimeWindow.row();
        gameTimeWindow.add(buildHealthMeters());
        gameTimeWindow.row();

        gameTimeWindow.add(buildLevelRankMeters()).pad(8f);
        gameTimeWindow.pack();

        gameTimeWindow.setPosition(10, Gdx.app.getGraphics().getHeight() - gameTimeWindow.getHeight()-10);
        stage.addActor(gameTimeWindow);
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

        levelBar = new ProgressBar(1, 100f, 1f, false, barStyle);
        levelBar.setAnimateDuration(0);
        levelBar.setValue(level.player.getStats().get("xp"));
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
        healthBar.setValue(level.player.getHealth());
        hudTable.add(healthBar);
        hudTable.row();
        return hudTable;
    }

    /**
     * Draw method
     */
    public void draw() {
        stage.draw();
        if (mouseItem != null)  mouseItem.setZIndex(9000);
        float alpha = fadeActor.getColor().a;

        if (alpha != 0) {
            Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
            fadeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            fadeRenderer.setColor(new Color(0f, 0f, 0.031f, alpha));
            // Flash effect - fadeRenderer.setColor(new Color(255f, 255f, 255f, alpha));
            fadeRenderer.rect(0, 0, Gdx.app.getGraphics().getWidth(), Gdx.app.getGraphics().getHeight());
            fadeRenderer.end();
            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    /**
     * Make actor out of this
     */
    public void renderCutSceneFrame() {
        // Cut Scene 16:9 animation
        // Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
        //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        fadeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        fadeRenderer.setColor(new Color(0f, 0f, 0.031f, 1));
        fadeRenderer.rect(0, 0, Gdx.app.getGraphics().getWidth(), 120);
        fadeRenderer.rect(0, Gdx.app.getGraphics().getHeight() - 120, Gdx.app.getGraphics().getWidth(),
                120);
        fadeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    public void updateFunds(int money) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        moneyLabel.setText("" + numberFormat.format(money));
    }

    private void updateHealth(Player player) {
        healthBar.setValue((float) player.getHealth());
    }

    private void updateXP(Player player) {
        int xp = player.getStats().get("xp");
        LevelRank rank = LevelRank.getLevelProgression(xp);
        xpLabel.setText("XP: " + NumberFormat.getNumberInstance(Locale.US).format(xp) + " Level: " + rank.getCurrentLevel());
        levelBar.setValue(rank.getProgress());
    }

    public void act(float delta) {
        // temp mouseItem
        if (mouseItem != null) {
            mouseItem.setPosition(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
            lc.setZIndex(100);
            lc.setPosition(Gdx.input.getX(), Gdx.app.getGraphics().getHeight() - Gdx.input.getY());
        }
        stage.act(delta);
        fadeActor.act(delta);
    }

    public void dispose() {
        //super.dispose();
        MessageManager.getInstance().clearListeners();
    }
}
