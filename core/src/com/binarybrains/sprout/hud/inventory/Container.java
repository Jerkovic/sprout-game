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
        this.group = new ButtonGroup();
        build();
    }

    /**
     *
     * @param inventory
     */
    public void onInventoryChanged(Inventory inventory) {
        clearChildren();
        syncInventory(inventory);
        build();
    }

    /**
     *
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

            group.add(button);
        }
    }
}
