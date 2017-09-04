package com.binarybrains.sprout.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by erikl on 04/09/17.
 */
public class TabbedMenuWindow extends Dialog {

    TabbedPane tabbedPane;

    public TabbedMenuWindow(Skin skin) {
        super("Main Menu", skin);
        // actors should be crafting menu
        // inventory management
        // and exit to desktop button
        clearChildren();

        tabbedPane = new TabbedPane(skin);
        tabbedPane.addTab(" Inventory ", new Label("test 1\ntodo more", skin));
        tabbedPane.addTab(" Crafting ", new Label("test 2", skin));
        tabbedPane.addTab(" Options ", new Label("test 3", skin));
        tabbedPane.addTab(" Exit ", new Label("test 4", skin));
        tabbedPane.left().top();

        add(tabbedPane).pad(1);

        add().row();
        //debug();
        setKeepWithinStage(true);
        setMovable(false);
        setModal(true);
        center();
        TextButton buttonExit = new TextButton("   Close   ", skin);
        buttonExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        add(buttonExit).pad(5);
        pack();
        centerMe();
    }

    public void centerMe() {
        setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - getHeight()/2);

    }

}
