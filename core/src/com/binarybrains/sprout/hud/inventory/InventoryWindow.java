package com.binarybrains.sprout.hud.inventory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.hud.utils.ItemTip;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;
import com.binarybrains.sprout.item.tool.WateringCan;
import com.binarybrains.sprout.level.Level;

import java.util.HashMap;
import java.util.Map;


public class InventoryWindow extends Window {

    Skin skin;
    ButtonGroup group;
    TextureAtlas atlas;
    Level level;
    Float yPos;

    static Map<Integer, Integer> slotKeysMapping = new HashMap<Integer, Integer>();
    static {
        slotKeysMapping.put(Input.Keys.NUM_1, 0);
        slotKeysMapping.put(Input.Keys.NUM_2, 1);
        slotKeysMapping.put(Input.Keys.NUM_3, 2);
        slotKeysMapping.put(Input.Keys.NUM_4, 3);
        slotKeysMapping.put(Input.Keys.NUM_5, 4);
        slotKeysMapping.put(Input.Keys.NUM_6, 5);
        slotKeysMapping.put(Input.Keys.NUM_7, 6);
        slotKeysMapping.put(Input.Keys.NUM_8, 7);
        slotKeysMapping.put(Input.Keys.NUM_9, 8);
        slotKeysMapping.put(Input.Keys.NUM_0, 9);
        slotKeysMapping.put(Input.Keys.EQUALS, 10);
        slotKeysMapping.put(Input.Keys.LEFT_BRACKET, 11);
    }

    public InventoryWindow(Level level, Skin skin) {
        super("Inventory", skin);
        getTitleLabel().setColor(0,0,0,.7f);

        this.level = level;
        this.skin = skin;
        setKeepWithinStage(true);
        setMovable(false);
        setPosition((Gdx.app.getGraphics().getWidth() / 2 - getWidth() / 2)-getWidth(), getMinHeight() + 8);
        row().fill().expandX();

        atlas = SproutGame.assets.get("items2.txt");
        group = new ButtonGroup();
        setWindowBottom();

        // ignore clicking on the window
        InputListener ignoreTouchDown = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.cancel();
                return false;
            }
        };
        this.addListener(ignoreTouchDown);
    }

    /**
     * Activate inventory slot by shortcut key
     * @param keycode
     * @return
     */
    public boolean activateSlotByShortcutKey (int keycode) {
        if (!slotKeysMapping.containsKey(keycode) || slotKeysMapping.get(keycode) == group.getCheckedIndex()) return false;
        Array<Button> buttons = group.getButtons();
        Button btn = buttons.get(slotKeysMapping.get(keycode));
        Array<EventListener> listeners = btn.getListeners();
        for(int i = 0; i < listeners.size; i++) {
            if(listeners.get(i) instanceof ClickListener) {
                MessageManager.getInstance().dispatchMessage(TelegramType.PLAYER_INVENTORY_CHANGED_SELECTED_SLOT);
                // SproutGame.playSound("blop2", .3f, MathUtils.random(1.0f, 1.1f), 1f);
                ((ClickListener)listeners.get(i)).clicked(null, 0, 0);
            }
        }
        return true;
    }

    public void setWindowTop() {
        yPos = (float )Gdx.app.getGraphics().getHeight() - (getHeight() + 8);
        centerMe();
    }

    public void setWindowBottom() {
        yPos = 8f;
        centerMe();
    }

    /**
     * Generic method...move?
     */
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
            if ((i + 1) % 12 == 0) break; // itemTable.row();
        }

        add(itemTable);
        row();
        pack();
    }

    /**
     * Sync inv
     * @param inventory
     */
    private void syncInventory(final Inventory inventory) {
        group.clear();
        String selected = "";
        if (level.player.activeItem != null) selected = level.player.activeItem.getName();

        getTitleLabel().setText("Inventory " + inventory.count() + "/" + inventory.getCapacity());

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
            Stack stack = new Stack();

            TextureAtlas.AtlasRegion icon;
            if (item != null) {
                icon = atlas.findRegion(item.getRegionId());
            } else {
                icon = atlas.findRegion("Empty");

            }
            if (icon != null) {
                Image image = new Image(icon);
                // testAnimate(image);
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

            if (item != null && selected != null && item.getName().equals(selected)) {
                button.setChecked(true);
            }

            button.addListener(new ClickListener(Input.Buttons.LEFT) {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (group.getCheckedIndex() > -1) {
                        Item it = inventory.getItems().get(group.getCheckedIndex());
                        if (it != null) level.player.setActiveItem(it);
                    }

                }
            });

            group.add(button);
            level.player.setActiveItem(inventory.getItems().get(group.getCheckedIndex()));

        }
    }
}