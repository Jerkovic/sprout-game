package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.furniture.Chest;
import com.binarybrains.sprout.item.Item;

public class ChestWindow extends Dialog implements Telegraph {

    Skin skin;
    Player player;
    Container container;
    Container playerInventoryContainer;

    private Item heldItem = null;

    public ChestWindow(Player player, Skin skin) {
        super("Chest", skin.get("dialog", WindowStyle.class));
        setSkin(skin);
        this.skin = skin;
        // initialize();
        getTitleLabel().setColor(0,0,0,.7f);
        this.player = player;
        this.skin = skin;
        setKeepWithinStage(true);


        // initialize ?
        setMovable(false);
        setModal(true);

        setPosition((Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2)-getWidth(), getMinHeight() + 20);
        row().fill().expandX();

        InputListener ignoreTouchDown = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.cancel();
                return false;
            }
        };

        this.addListener(ignoreTouchDown);

    }

    // Generic Window?
    public void centerMe() {
        setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight()/2);
    }

    private void setHeldItem(Item item) {
        heldItem = item;
        if (heldItem != null) {
            player.getLevel().screen.hud.setMouseItem(item.getRegionId());
        } else {
            player.getLevel().screen.hud.removeMouseItem();
        }
    }

    public void openChest(Chest chest) {
        clearChildren();
        container = new Container(skin);
        container.refreshInventory(chest.getInventory()); // hook up and refresh container table
        container.SetLeftClick(() -> {
            int selected = container.getButtonGroup().getCheckedIndex();

            if (heldItem != null) {
                chest.getInventory().add(selected, heldItem);
                container.refreshInventory(chest.getInventory());
                setHeldItem(null);
            }

        });

        playerInventoryContainer = new Container(skin);
        playerInventoryContainer.refreshInventory(player.getInventory());
        playerInventoryContainer.SetLeftClick(() -> {
            int selected = playerInventoryContainer.getButtonGroup().getCheckedIndex();
            if (heldItem == null) {
                setHeldItem(player.getInventory().getItems().get(selected));
                player.getInventory().replace(playerInventoryContainer.getButtonGroup().getCheckedIndex(), null);
                playerInventoryContainer.refreshInventory(player.getInventory());
            }
        });

        build(chest);
        centerMe();
    }

    private void build(Chest chest) {
        TextButton buttonExit = new TextButton("   Close   ", skin);
        buttonExit.setColor(0,0,0, .75f);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // if (getHeldItem() != null) return;
                hide();
                player.getLevel().screen.hud.showInventory();
                player.getLevel().screen.game.resume();
                player.getLevel().screen.hud.showMouseItem();
                player.getLevel().screen.hud.refreshInventory();
                chest.use(player,player.getDirection());
            }
        });

        row();
        add(this.container);
        row();
        add("Inventory").center();
        row();
        add(this.playerInventoryContainer);
        row();
        add(buttonExit).pad(5);
        pack();
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }
}
