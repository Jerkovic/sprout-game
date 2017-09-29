package com.binarybrains.sprout.entity.misc;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.actions.Action;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.level.Level;

/**
 * Player Mailbox - pending mail envelope
 * Created by erikl on 27/09/17.
 */
public class Mail extends Entity {

    TextureRegion envelope;
    float startY, endY;


    public Mail(Level level, Vector2 position) {
        super(level, position, 0, 0);

        TextureAtlas atlas = SproutGame.assets.get("items2.txt");
        envelope = atlas.findRegion("Envelope");
        setWidth(13);
        setHeight(9);

        applyBounce();

        startY = getY();
        endY = startY + 32;

        Action bounce = Actions.sequence(
                Actions.moveTo(getX(), endY, 0.9f, Interpolation.bounceOut)
                //Actions.moveTo(getX(), startY, 0.7f, Interpolation.bounceIn)
        );

        addAction(bounce);

        // todo shadow
    }

    public void applyBounce() {

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(envelope, getX(), getY(), getWidth(), getHeight());
    }
}
