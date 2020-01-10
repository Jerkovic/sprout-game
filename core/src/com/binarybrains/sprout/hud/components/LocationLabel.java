package com.binarybrains.sprout.hud.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

public class LocationLabel extends Table {

    Label locationLabel;

    /**
     *
     * @param title
     * @param skin
     * @param styleName
     */
    public LocationLabel(String title, Skin skin, String styleName) {
        super(skin);

        locationLabel = new Label(title, skin);
        locationLabel.setWrap(false);
        locationLabel.setWidth(300);
        locationLabel.setColor(0,0, 0, .6f);
        locationLabel.setAlignment(Align.center);

        pad(10).background("default-round");
        add(locationLabel);
        setWidth(300);
        setHeight(30);
        setVisible(false);
        setPosition(Gdx.app.getGraphics().getWidth() - getWidth() - 30, Gdx.app.getGraphics().getHeight() - getHeight() - 30);
    }

    /**
     *
     * @param text
     */
    public void setText(String text) {
        locationLabel.setText(text);
    }
}