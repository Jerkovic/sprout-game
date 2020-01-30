package com.binarybrains.sprout.hud.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ToolItem;

public class ItemTip {

    /**
     *
     * @param skin
     * @param item
     * @return
     */
    public static Table createTooltipTable(Skin skin, Item item) {
        Table tooltipTable = new Table(skin);
        tooltipTable.setWidth(140);
        tooltipTable.pad(20).background(skin.get("test-draw", NinePatchDrawable.class));

        Label lbl = new Label(item.getName() + " (" + item.getCategory() + ")", skin);
        lbl.getStyle().font = skin.getFont("ruin-font");
        lbl.setAlignment(Align.bottomRight);
        lbl.setColor(Color.YELLOW);

        Label lbld = new Label(item.getDescription(), skin);
        lbld.getStyle().font = skin.getFont("ruin-font");

        tooltipTable.add(lbl).expandX().left();
        tooltipTable.row();
        tooltipTable.add(lbld).left();
        if (item instanceof ToolItem) {
            tooltipTable.row();
            tooltipTable.add(((ToolItem) item).getDamageRange()).left();
        }
        tooltipTable.align(Align.left | Align.top);
        return tooltipTable;
    }
}
