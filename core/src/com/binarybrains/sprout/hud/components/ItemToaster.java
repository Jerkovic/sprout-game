package com.binarybrains.sprout.hud.components;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.binarybrains.sprout.item.Item;

public class ItemToaster extends Table {

    private int cnt = 1;
    private ItemButton itemButton;
    private Item item;
    private Image icon;
    private float y;

    public ItemToaster(Skin skin, Item item, Image icon, float y) {
        super(skin);
        this.icon = icon;
        this.item = item;
        this.y = y;
        init();
    }

    /**
     *
     * @param cnt
     */
    public void increaseItemCounter(int cnt) {
        this.cnt += cnt;
        itemButton.setCountLabel(this.cnt);
    }

    /**
     * Init the component
     */
    public void init() {
        Skin skin = getSkin();
        itemButton = new ItemButton(skin, item, icon, 1, 0);
        itemButton.setHeight(48);
        itemButton.setWidth(48);
        itemButton.setVisible(true);

        Label itemLabel = new Label(item.getName(), skin);
        itemLabel.setWrap(false);
        itemLabel.setWidth(200);
        itemLabel.setColor(0,0, 0, .6f);
        itemLabel.setAlignment(Align.left);

        pad(5).background("default-round");
        setWidth(260);
        setHeight(48);
        setClip(false);
        setVisible(true);

        setPosition(10, y + 5);
        add(itemButton).width(48);
        add(itemLabel).pad(2f).expandX().fillX();
        pack();
    }
}
