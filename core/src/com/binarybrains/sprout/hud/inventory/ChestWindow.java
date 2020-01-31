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

    Player player;
    Container container;
    Container playerInventoryContainer;

    private Item heldItem = null;

    public ChestWindow(Player player, Skin skin) {
        super("Chest", skin.get("new-ui-dialog", WindowStyle.class));
        setSkin(skin);
        this.player = player;
        // debug();

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
            player.getLevel().screen.hud.setMouseItem(item.getRegionId(), "");
        } else {
            player.getLevel().screen.hud.removeMouseItem();
        }
    }

    public void openChest(Chest chest) {
        clearChildren();
        container = new Container(getSkin());
        container.refreshInventory(chest.getInventory()); // hook up and refresh container table
        container.SetLeftClick(() -> {
            int selected = container.getButtonGroup().getCheckedIndex();

            if (heldItem != null) {
                if (chest.getInventory().getItems().get(selected) == null) {
                    chest.getInventory().add(selected, heldItem);
                    setHeldItem(null);
                } else {
                    Item item = chest.getInventory().replace(selected, heldItem);
                    setHeldItem(item);
                }
            } else {
                setHeldItem(chest.getInventory().getItems().get(selected));
                chest.getInventory().replace(container.getButtonGroup().getCheckedIndex(), null);

            }
            container.refreshInventory(chest.getInventory());
        });

        playerInventoryContainer = new Container(getSkin());
        playerInventoryContainer.refreshInventory(player.getInventory());
        playerInventoryContainer.SetLeftClick(() -> {
            int selected = playerInventoryContainer.getButtonGroup().getCheckedIndex();

            if (heldItem == null) {
                setHeldItem(player.getInventory().getItems().get(selected));
                player.getInventory().replace(playerInventoryContainer.getButtonGroup().getCheckedIndex(), null);

            } else {
                if (player.getInventory().getItems().get(selected) == null) {
                    player.getInventory().add(selected, heldItem);
                    setHeldItem(null);
                } else {
                    Item item = player.getInventory().replace(selected, heldItem);
                    setHeldItem(item);
                }
            }
            playerInventoryContainer.refreshInventory(player.getInventory());
        });

        build(chest);
        centerMe();
    }

    private void build(Chest chest) {

        TextButton buttonExit = new TextButton("   Close   ", getSkin(), "text-button-default");

        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (heldItem != null) return;
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
