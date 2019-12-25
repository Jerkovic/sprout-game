package com.binarybrains.sprout.bellsandwhistles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.level.Level;

/**
 * Created by erikl on 23/09/17.
 */
public class TextParticle extends Entity {

    private String text;
    private final Color shadow = new Color(0f, 0f, 0f, 1f);

    public TextParticle(Level level, Vector2 position, String text, Color color) {
        super(level, position, 20, 20);
        this.text = text;
        setColor(new Color(1f, 1f, 1f, 1f));
        fade();
    }

    public float getSortOrder() {
        return 0;
    }

    public void fade() {
        addAction(Actions.sequence(
                Actions.delay(MathUtils.random(.15f, .298f)),
                Actions.parallel(
                    Actions.moveTo(getPosition().x , getPosition().y + (getHeight() * 2) ,MathUtils.random(0.91f, 1.4f), Interpolation.exp5In),
                    Actions.alpha(0.00f, 1.3f, Interpolation.fade)
                ),
                Actions.run((new Runnable() {
                    public void run () {
                        remove();
                    }
                })
        )));
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        //getLevel().font.getData().setScale(1f);
        getLevel().font.setUseIntegerPositions(false);
        shadow.set(shadow.r, shadow.g, shadow.b, getColor().a);
        getLevel().font.setColor(shadow);
        getLevel().font.draw(batch, text, getPosition().x+1, getPosition().y-1);
        getLevel().font.setColor(getColor());
        getLevel().font.draw(batch, text, getPosition().x, getPosition().y);
    }
}
