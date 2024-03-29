package com.binarybrains.sprout.hud.components;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.hud.utils.ItemTip;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;
import com.binarybrains.sprout.item.tool.WateringCan;


public class ItemButton extends ImageButton {

    private int slotIndex = 0;
    private Item item;
    private Image icon;
    private int cnt = 0;
    private Label lc;

    /**
     *
     * @param skin
     * @param item
     * @param icon
     * @param cnt
     * @param slotIndex
     */
    public ItemButton(Skin skin, Item item, Image icon, int cnt, int slotIndex) {
        super(skin, "inventory-slot-btn");
        this.item = item;
        this.icon = icon;
        this.cnt = cnt;
        setName("" + slotIndex);
        init();
    }

    private void animate(Actor actor) {
        actor.clearActions();
        actor.setOrigin(Align.center);
        actor.addAction(Actions.sequence(
                Actions.scaleTo(1.25f, 1.25f, .35f, Interpolation.pow2Out),
                Actions.scaleTo(1f, 1f, .25f, Interpolation.pow2In)
        ));
    }

    public void setCountLabel(int val) {
        lc.setText("" + val);
        animate(icon);
    }

    /**
     * Init
     */
    private void init() {
        Skin skin = getSkin();

        String counter = "";
        counter = "" + cnt;

        Stack stack = new Stack();
        stack.add(icon);
        lc = new Label(counter, skin);
        lc.setStyle(skin.get("small", Label.LabelStyle.class));
        lc.setAlignment(Align.bottomRight);
        lc.setColor(250, 250, 250, 1f);
        Table overlay = new Table();
        overlay.add(lc).expand().fillX().bottom().left();
        stack.add(overlay);
        stack.layout();
        add(stack);
    }
}
