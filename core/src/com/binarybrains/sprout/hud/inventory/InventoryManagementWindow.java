package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.FocusListener;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.hud.TypeWriterDialog;
import com.binarybrains.sprout.hud.utils.ItemTip;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;
import com.binarybrains.sprout.item.tool.WateringCan;
import com.binarybrains.sprout.level.Level;

import java.util.function.Function;


public class InventoryManagementWindow extends Dialog {

    private Skin skin;
    private ButtonGroup group;
    private TextureAtlas atlas;
    private Level level;
    private Function closeCallback;

    private Player player;
    private Item heldItem;

    public InventoryManagementWindow(Level level, Skin skin) {
        super("Inventory Management", skin.get("dialog", WindowStyle.class));
        setSkin(skin);
        this.skin = skin;
        initialize();
        getTitleLabel().setColor(0,0,0,.7f);
        this.player = level.player;
        this.level = level;
        this.skin = skin;
        setKeepWithinStage(true);
        setMovable(false);
        setModal(true);
        setPosition((Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2)-getWidth(), getMinHeight() + 20);
        row().fill().expandX();

        atlas = SproutGame.assets.get("items2.txt");
        group = new ButtonGroup();
        group.uncheckAll();

        InputListener ignoreTouchDown = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.cancel();
                return false;
            }
        };

        this.addListener(ignoreTouchDown);
    }

    public void setCloseCallback(Function func) {
        this.closeCallback = func;
    }

    private void initialize () {
        setModal(true);
        setMovable(false);
    }

    private Button trashCan() {
        Image image = new Image(atlas.findRegion("Garbage_Can")); // Trash Can todo
        Button button = new Button(skin, "default");
        button.add(image);

        button.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getHeldItem() != null && !(getHeldItem() instanceof ToolItem)) {
                    setHeldItem(null);
                    SproutGame.playSound("garbage_can", .4f, MathUtils.random(0.8f, 1.1f), 1f);
                }
            }
        });
        return button;
    }

    private Button sellItem() {
        Image image = new Image(atlas.findRegion("Cash_Register"));
        Button button = new Button(skin, "default");
        button.add(image);

        button.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getHeldItem() != null && !(getHeldItem() instanceof ToolItem)) {
                    int sellingQuantity = 1;
                    if (getHeldItem() instanceof ResourceItem) {
                        sellingQuantity = ((ResourceItem) getHeldItem()).count;
                    }
                    player.increaseFunds(getHeldItem().getSellPrice() * sellingQuantity);
                    SproutGame.playSound("cash_register", .8f, MathUtils.random(0.92f, 1.02f), 1f);
                    setHeldItem(null);
                    onInventoryChanged(player.getInventory());
                }
            }
        });
        return button;
    }

    public void centerMe() {
        setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight()/2);
    }

    /*
     * Update this onInventory has changed
     */
    public void onInventoryChanged(Inventory inventory) {
        clearChildren();
        syncInventory(inventory);
        build();
        centerMe();
    }

    /**
     * Build UI moved to Container
     */
    private void build() {
        Table itemTable = new Table(skin);
        for (int i = 0, n = group.getButtons().size; i < n; i++) {
            itemTable.add((Actor) group.getButtons().get(i));
            if ((i + 1) % 12 == 0) itemTable.row();
        }

        Table buttonTable = new Table(skin);
        buttonTable.add(organize()).pad(8);
        buttonTable.add(sellItem()).pad(8);
        buttonTable.add(trashCan()).pad(8);

        add(itemTable);
        row();
        add(buttonTable);
        row();

        TextButton buttonExit = new TextButton("   Close   ", skin);
        buttonExit.setColor(0,0,0, .75f);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getHeldItem() != null) return;
                hide();
                SproutGame.playSound("inventory_close");
                player.getLevel().screen.hud.showInventory();
                player.getLevel().screen.game.resume();
                player.getLevel().screen.hud.showMouseItem();
                player.getLevel().screen.hud.refreshInventory();
            }
        });
        add(buttonExit).pad(5);
        pack();
    }

    private Button organize() {
        Image image = new Image(atlas.findRegion("Sort_Inventory"));
        Button button = new Button(skin, "default");
        button.add(image);

        button.addListener(new ClickListener(Input.Buttons.LEFT) {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getHeldItem() == null) {
                    SproutGame.playSound("button_click", .5f, 1f, 1f);
                    player.getInventory().sortInventory();
                    onInventoryChanged(player.getInventory());
                }
            }
        });
        return button;
    }

    /**
     * @todo We need to display stack counter as well
     * @param item
     */
    private void setHeldItem(Item item) {
        heldItem = item;
        if (item != null) {
            level.screen.hud.setMouseItem(item.getRegionId());
        } else {
            level.screen.hud.removeMouseItem();
        }
    }

    private Item getHeldItem() {
        return heldItem;
    }

    private void syncInventory(final Inventory inventory) {
        group.clear();
        getTitleLabel().setText("Inventory Management");
        int slotIndex = 0;

        for (Item item : inventory.getItems()) {
            final Button button = new Button(skin, "default");
            button.setName("" + slotIndex); //set the slotIndex
            String counter = "";
            if (item instanceof ResourceItem && inventory.count(item) > 1) {
                counter = "" + inventory.count(item);
            }

            if (item instanceof ToolItem && ((ToolItem) item).tool instanceof WateringCan) {
                Tool tool = ((ToolItem) item).tool;
                counter = "" + ((WateringCan) tool).getWater() + "%";
            }

            Label lc = new Label(counter, skin);
            lc.setAlignment(Align.bottomRight);
            lc.setColor(255, 255, 255, 1f);

            Stack stack = new Stack();

            TextureAtlas.AtlasRegion icon;
            if (item != null) {
                icon = atlas.findRegion(item.getRegionId());
            } else {
                icon = atlas.findRegion("Empty");

            }
            if (icon != null) {
                Image image = new Image(icon);
                stack.add(image);
            } else {
                stack.add(new Label("n/a", skin)); // should not happen
            }

            Table overlay = new Table();
            overlay.add(lc).expand().fillX().bottom().left();
            stack.add(overlay);

            button.add(stack);
            if (item != null) {
                Tooltip toolTip = new Tooltip(ItemTip.createTooltipTable(skin, item));
                toolTip.getManager().animations = false;
                toolTip.setInstant(true);
                button.addListener(toolTip);
            }
            button.pack();
            slotIndex++;

            // Actions listeners
            // Right click
            button.addListener(new ClickListener(Input.Buttons.RIGHT)
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {

                }
            });

            // left
            button.addListener(new ClickListener(Input.Buttons.LEFT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (group.getCheckedIndex() > -1) {
                        if (heldItem == null) {
                            SproutGame.playSound("button_click", .4f, 0.9f, 1f);
                            setHeldItem(inventory.getItems().get(group.getCheckedIndex()));
                            inventory.removeSlot(group.getCheckedIndex());
                            onInventoryChanged(inventory);
                        } else {
                            SproutGame.playSound("button_click", .45f, 0.95f, 1f);
                            heldItem = inventory.replace(group.getCheckedIndex(), heldItem);
                            setHeldItem(heldItem);
                            onInventoryChanged(inventory);
                        }
                    }
                }
            });
            group.add(button);
        }
    }
}