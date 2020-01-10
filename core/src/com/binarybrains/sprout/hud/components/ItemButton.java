package com.binarybrains.sprout.hud.components;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.hud.utils.ItemTip;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;
import com.binarybrains.sprout.item.tool.WateringCan;


public class ItemButton extends Button {

    private int slotIndex = 0;
    private Item item;
    private Image icon;
    private int cnt = 0;
    private boolean tooltip = true;

    private Label lc;


    public ItemButton(Skin skin, Item item, Image icon, int cnt, int slotIndex) {
        super(skin, "default");
        this.item = item;
        this.icon = icon;
        this.cnt = cnt;
        setName("" + slotIndex);
        init();
    }

    public void setCountLabel(int val) {
        lc.setText("" + val);
    }

    /**
     * Use tooltip for this button
     * @param tooltip
     */
    public void setTooltip(boolean tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * Init
     */
    private void init() {
        boolean showStack = false;
        Skin skin = getSkin();

        String counter = "";
        if (item instanceof ResourceItem && cnt > 0) { // > 0 here but > 1 elsewhere
            counter = "" + cnt;
            showStack = true;
        }

        if (item instanceof ToolItem && ((ToolItem) item).tool instanceof WateringCan) {
            Tool tool = ((ToolItem) item).tool;
            counter = "" + ((WateringCan) tool).getWater() + "%";
            showStack = true;
        }

        Stack stack = new Stack();

        if (icon != null) {
            stack.add(icon);
        }

        if (showStack) {
            lc = new Label(counter, skin);
            lc.setAlignment(Align.bottomRight);
            lc.setColor(250, 250, 250, 1f);
            Table overlay = new Table();
            overlay.add(lc).expand().fillX().bottom().left();
            stack.add(overlay);
        }

        if (item != null && tooltip) {
            Tooltip toolTip = new Tooltip(ItemTip.createTooltipTable(skin, item));
            toolTip.getManager().animations = false;
            toolTip.setInstant(true);
            toolTip.hide();
            addListener(toolTip);
        }

        stack.layout();
        add(stack);
    }
}
