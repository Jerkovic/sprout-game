package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;
import com.binarybrains.sprout.item.tool.WateringCan;
import com.binarybrains.sprout.level.Level;


public class InventoryManagementWindow extends Dialog {

    private Skin skin;
    private ButtonGroup group;
    private TextureAtlas atlas;
    private Level level;

    private Player player;
    private Item heldItem;

    public InventoryManagementWindow(Level level, Skin skin) {
        super("Inventory Management", skin);

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

        // ignore clicking on the window
        InputListener ignoreTouchDown = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.cancel();
                return false;
            }
        };

        this.addListener(ignoreTouchDown);
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

    private void build() {

        Table itemTable = new Table(skin);

        for (int i = 0, n = group.getButtons().size; i < n; i++) {
            itemTable.add((Actor) group.getButtons().get(i));
            if ((i + 1) % 12 == 0) itemTable.row();
        }

        add(itemTable);

        row();
        add(trashCan()).pad(15);
        row();

        TextButton buttonExit = new TextButton("   Close   ", skin);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (getHeldItem() != null) return;
                hide();
                player.getLevel().screen.hud.showInventory();
                player.getLevel().screen.game.resume();
                player.getLevel().screen.hud.showMouseItem();
                player.getLevel().screen.hud.refreshInventory();

            }
        });
        add(buttonExit).pad(5);

        pack();
    }

    /**
     * Creates a tooltip table for an item
     * @param item
     * @return
     */
    private Table createTooltipTable(Item item) {
        Table tooltipTable = new Table(skin);
        tooltipTable.pad(10).background("default-round");
        tooltipTable.add(item.getName()).left();
        tooltipTable.row();
        tooltipTable.add(item.getDescription()).left();
        tooltipTable.align(Align.left | Align.top);
        return tooltipTable;
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

            //button.debug();
            Label lc = new Label(counter, skin);
            lc.setAlignment(Align.bottomRight);
            lc.setColor(Color.WHITE);

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

            //Second add wrapped overlay object
            Table overlay = new Table();
            overlay.add(lc).expand().fillX().bottom().left();
            stack.add(overlay);

            button.add(stack);
            if (item != null) {
                button.addListener(new Tooltip(createTooltipTable(item)));
            }
            button.pack();
            slotIndex++;


            button.addListener(new ClickListener(Input.Buttons.RIGHT)
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {

                    button.setChecked(true);
                    if (group.getCheckedIndex() > -1) {
                        
                        if (heldItem == null) {
                            setHeldItem(inventory.getMoveItemWithQuantity(group.getCheckedIndex(), 1));
                            SproutGame.playSound("button_click", .4f, 0.9f, 1f);
                            System.out.println("heldItem -> " + heldItem);
                            onInventoryChanged(inventory);
                        } else {
                            // fixa så att man inte kan höger klicka på en emtpySlot
                            if (inventory.items.get(group.getCheckedIndex()) == null) {
                                System.out.println("no allowed");
                            }

                            SproutGame.playSound("button_click", .45f, 0.95f, 1f);
                            inventory.add(group.getCheckedIndex(), heldItem);
                            setHeldItem(null);
                            onInventoryChanged(inventory);
                        }
                    }
                }
            });

            button.addListener(new ClickListener(Input.Buttons.LEFT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (group.getCheckedIndex() > -1) {
                        if (heldItem == null) {
                            SproutGame.playSound("button_click", .4f, 0.9f, 1f);
                            setHeldItem(inventory.getItems().get(group.getCheckedIndex()));
                            inventory.removeSlot(group.getCheckedIndex());
                            System.out.println("heldItem" + heldItem);
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