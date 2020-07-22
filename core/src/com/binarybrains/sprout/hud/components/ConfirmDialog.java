package com.binarybrains.sprout.hud.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ConfirmDialog extends Dialog {

    private String title;
    private String text;
    private Runnable confirmAction;
    private Runnable cancelAction;


    public ConfirmDialog(Skin skin, String title, String text) {
        super(title, skin.get("new-ui-dialog", WindowStyle.class));
        setSkin(skin);
        this.title = title;
        this.text = text;

        setKeepWithinStage(true);
        setMovable(false);
        setModal(true);
        build();
        centerMe();
        row().fill().expandX();

        InputListener ignoreTouchDown = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.cancel();
                return false;
            }
        };
        this.addListener(ignoreTouchDown);
    }

    public void setConfirmAction(Runnable runnable) {
        this.confirmAction = runnable;
    }

    public void setCancelAction(Runnable runnable) {
        this.cancelAction = runnable;
    }

    public void centerMe() {
        setPosition(Gdx.graphics.getWidth() / 2 - getWidth() / 2, Gdx.graphics.getHeight() / 2 - getHeight()/2);
    }

    private void build() {
        TextButton cancelButton = new TextButton("   Cancel   ", getSkin(), "text-button-default");
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                if (cancelAction != null) {
                    cancelAction.run();
                }
            }
        });
        TextButton confirmButton = new TextButton("   YES   ", getSkin(), "text-button-default");
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
                if (confirmAction != null) {
                    confirmAction.run();
                }
            }
        });
        row();
        add(this.text).pad(10);
        row();
        row();
        add(confirmButton).pad(10);
        add(cancelButton).pad(10);
        pack();
    }


}
