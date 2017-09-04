package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;
import com.binarybrains.sprout.item.tool.WateringCan;
import com.binarybrains.sprout.level.Level;


public class InventoryWindow extends Window {

    Skin skin;
    ButtonGroup group;
    TextureAtlas atlas;
    Level level;
    Float yPos;

    public InventoryWindow(Level level, Skin skin) {
        super("Inventory", skin);

        this.level = level;
        this.skin = skin;
        setKeepWithinStage(true);
        setMovable(false);
        setPosition((Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2)-getWidth(),
                getMinHeight() + 20);
        row().fill().expandX();

        atlas = SproutGame.assets.get("items2.txt");
        group = new ButtonGroup();
        setWindowBottom();
    }

    public void setWindowTop() {
        yPos = (float )Gdx.app.getGraphics().getHeight() - (getHeight()+20);
        centerMe();
    }

    public void setWindowBottom() {
        yPos = 20f;
        centerMe();
    }


    public void centerMe() {
        setPosition((Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2), yPos);
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
        pack();
    }

    private Table createTooltipTable(Item item) {
        Table tooltipTable = new Table(skin);
        tooltipTable.pad(10).background("default-round");
        tooltipTable.add(item.getName()).left();
        tooltipTable.row();
        tooltipTable.add(item.getDescription()).left();
        tooltipTable.align(Align.left | Align.top);
        return tooltipTable;
    }

    private void syncInventory(final Inventory inventory) {

        group.clear();
        String selected = level.player.activeItem.getName();

        getTitleLabel().setText("Inventory " + inventory.getItems().size() + "/" + inventory.getCapacity());

        for (Item item : inventory.getItems()) {

            Button button = new Button(skin, "toggle");
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

            TextureAtlas.AtlasRegion icon = atlas.findRegion(item.getRegionId());
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
            button.addListener(new Tooltip(createTooltipTable(item)));
            button.pack();

            if (selected != null && item.getName().equals(selected))
            {
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

            group.add(button);

        }
    }

}