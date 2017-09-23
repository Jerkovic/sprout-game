package com.binarybrains.sprout.bellsandwhistles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.level.Level;

/**
 * Created by erikl on 23/09/17.
 */
public class TextParticle extends Entity {


    private String text;

    public TextParticle(Level level, Vector2 position, String text) {
        super(level, position, 20, 20);
        this.text = text;
        // meausure text todo
        fade();
    }

    public void fade() {
        addAction(Actions.sequence(
                Actions.moveTo(getPosition().x , getPosition().y+getHeight()+getHeight() ,.2f, Interpolation.fade),
                Actions.alpha(0.01f, 1f, Interpolation.fade),
                Actions.run((new Runnable() {
                    public void run () {
                        remove();
                    }
                })
        )));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        getLevel().font.setUseIntegerPositions(false);
        getLevel().font.setColor(getColor());
        getLevel().font.draw(batch, text, getPosition().x, getPosition().y);
    }
}
