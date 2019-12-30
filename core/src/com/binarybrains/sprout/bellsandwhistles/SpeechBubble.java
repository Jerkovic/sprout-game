package com.binarybrains.sprout.bellsandwhistles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.level.Level;

public class SpeechBubble extends Entity {

    private GlyphLayout layout;

    public SpeechBubble(Level level, String text) {
        super(level, new Vector2(0,0), 16*3, 16);
        setColor(new Color(0f, 0f, 0f, 1f));
        layout = new GlyphLayout(); // Obviously stick this in a field to avoid allocation each frame.
        layout.setText(getLevel().font, text);
        setWidth((int) layout.width);
        fade();
    }

    public float getSortOrder() {
        return -1;
    }

    private void fade() {
        addAction(Actions.sequence(
                Actions.delay(MathUtils.random(17.5f, 19.298f)),
                Actions.parallel(
                        Actions.alpha(0.00f, 2f, Interpolation.fade)
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
        setPosition(getLevel().player.getTopCenterPos().x, getLevel().player.getTopCenterPos().y + 10);
        getLevel().font.setColor(getColor());
        getLevel().font.draw(batch, layout, getPosition().x, getPosition().y);
    }
}
