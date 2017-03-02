package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.level.Level;



public class InventoryWindow extends Window {

    Skin skin;
    ButtonGroup group;
    TextureAtlas atlas;
    Level level;

    public InventoryWindow(Level level, Skin skin) {
        super("Inventory", skin);
        this.level = level;
        this.skin = skin;
        setKeepWithinStage(true);
        setPosition((Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2)-getWidth(),
                getMinHeight() + 10);
        row().fill().expandX();

        atlas = SproutGame.assets.get("items2.txt");
        group = new ButtonGroup();
        centerMe();
    }

    public void centerMe() {
        setPosition((Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2), 20);
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

    public void build() {

        // Table of items
        Table itemTable = new Table(skin);

        for (int i = 0, n = group.getButtons().size; i < n; i++) {
            itemTable.add((Actor) group.getButtons().get(i));
            if ((i + 1) % 12 == 0) itemTable.row();
        }

        Table container = new Table();
        ScrollPane itemTableScrollPane = new ScrollPane(itemTable, skin);

        itemTableScrollPane.setFlickScroll(false);
        itemTableScrollPane.setFadeScrollBars(false);
        itemTableScrollPane.setForceScroll(false, false);
        container.add(itemTableScrollPane).height((50 * 1) + 2);
        container.align(Align.top);
        add(container);
        row();
        pack();
    }

    public void syncInventory(final Inventory inventory) {

        group.clear();
        String selected = level.player.activeItem.getName();

        for (Item item : inventory.getItems()) {

            Button button = new Button(new Image(atlas.findRegion(item.getRegionId())), skin, "toggle");

            String counter = "";
            if (item instanceof ResourceItem) {
                counter = "" + inventory.count(item);
            }

            Label lc = new Label(counter, skin);
            lc.setAlignment(Align.bottomRight);
            lc.setColor(Color.BLACK);
            button.add(lc);

            // tooltip test
            button.addListener(new TextTooltip(item.getDescription(), skin));

            if (selected != null && item.getName().equals(selected))
            {
                //System.out.println(item.getName() + " - " + selected);
                button.setChecked(true);
            }



            button.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    if (group.getCheckedIndex() > -1) {
                        level.player.setActiveItem(inventory.getItems().get(group.getCheckedIndex()));
                    }
                }
            });

            //System.out.println(button.getListeners());


            group.add(button);

        }
    }


}