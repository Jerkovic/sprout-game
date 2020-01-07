package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.hud.utils.ItemTip;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;
import com.binarybrains.sprout.item.tool.WateringCan;

public class Container extends Table {

    private Skin skin;
    private ButtonGroup group;
    private TextureAtlas atlas;

    public Container(Skin skin) {
        this.skin = skin;
        this.atlas = SproutGame.assets.get("items2.txt");
        this.group = new ButtonGroup<>();
        build();
    }

    /**
     *
     * @param inventory
     */
    public void connectInventory(Inventory inventory) {
        clearChildren();
        syncInventory(inventory);
        build();
    }

    /**
     * Run this after syncInventory
     * @return
     */
    private Table build() {
        for (int i = 0, n = group.getButtons().size; i < n; i++) {
            add((Actor) group.getButtons().get(i));
            if ((i + 1) % 12 == 0) row();
        }
        return this;
    }

    /**
     * Sync with Inventory container
     * @param inventory
     */
    private void syncInventory(final Inventory inventory) {

        int slotIndex = 0;
        group.clear();

        for (Item item : inventory.getItems()) {
            final Button button = new Button(skin, "default");
            button.setName("" + slotIndex); //set the slotIndex

            boolean showStack = false;

            String counter = "";
            if (item instanceof ResourceItem && inventory.count(item) > 1) {
                counter = "" + inventory.count(item);
                showStack = true;
            }

            if (item instanceof ToolItem && ((ToolItem) item).tool instanceof WateringCan) {
                Tool tool = ((ToolItem) item).tool;
                counter = "" + ((WateringCan) tool).getWater() + "%";
                showStack = true;
            }

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
            }

            if (showStack) {
                Label lc = new Label(counter, skin);
                lc.setAlignment(Align.bottomRight);
                lc.setColor(255, 255, 255, 1f);

                Table overlay = new Table();
                overlay.add(lc).expand().fillX().bottom().left();
                stack.add(overlay);
            }
            stack.layout();
            button.add(stack);

            if (item != null) {
                Tooltip toolTip = new Tooltip(ItemTip.createTooltipTable(skin, item));
                toolTip.getManager().animations = false;
                toolTip.setInstant(true);
                toolTip.hide();
                button.addListener(toolTip);
            }
            slotIndex++;

            // Right click
            button.addListener(new ClickListener(Input.Buttons.RIGHT)
            {
                @Override
                public void clicked(InputEvent event, float x, float y)
                {

                }
            });

            button.addListener(new ClickListener(Input.Buttons.LEFT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                }
            });
            button.pack();
            group.add(button);

        }
    }
}
