package com.binarybrains.sprout.hud.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
        tooltipTable.pad(10).background("default-round");
        Label lbl = new Label(item.getName() + " (" + item.getCategory() + ")", skin);
        lbl.setAlignment(Align.bottomRight);
        lbl.setColor(0,0,0,0.95f);

        Label lbld = new Label(item.getDescription(), skin);
        lbld.setColor(0,0,0,0.85f);


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
