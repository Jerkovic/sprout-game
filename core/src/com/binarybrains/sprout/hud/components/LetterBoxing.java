package com.binarybrains.sprout.hud.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class LetterBoxing extends Actor {

    FilledRectangle rectTop;
    FilledRectangle rectBottom;
    Color color;
    float shrinkHeight;
    float duration;

    public LetterBoxing(Stage stage, Color color, float shrinkHeight, float duration) {
        this.color = color;
        this.shrinkHeight = shrinkHeight;
        this.duration = duration;

        rectBottom = new FilledRectangle(
                0,
                -shrinkHeight, // hidden mode
                Gdx.app.getGraphics().getWidth(),
                shrinkHeight,
                color
        );
        rectTop = new FilledRectangle(
                0,
                Gdx.app.getGraphics().getHeight(),
                Gdx.app.getGraphics().getWidth(),
                shrinkHeight,
                color
        );
        stage.addActor(rectBottom);
        stage.addActor(rectTop);
    }

    /**
     * Start LetterBoxing cinema mode show anim
     */
    public void start() {
        rectBottom.addAction(Actions.moveTo(0, 0, duration, Interpolation.fade));
        rectTop.addAction(Actions.moveTo(0, Gdx.app.getGraphics().getHeight() - shrinkHeight, duration, Interpolation.fade));
    }

    /**
     * End LetterBoxing cinema mode hide anim
     */
    public void end() {
        rectBottom.addAction(Actions.moveTo(0, -shrinkHeight, duration, Interpolation.fade));
        rectTop.addAction(Actions.moveTo(0, Gdx.app.getGraphics().getHeight(), duration, Interpolation.fade));
    }
}
