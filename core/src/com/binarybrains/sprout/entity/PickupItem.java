package com.binarybrains.sprout.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

import java.util.Random;

/**
 * Items that can be pickup on the map, right now just walk over them
 */

public class PickupItem extends ItemEntity {

    private int lifeTime;
    private int time = 0;

    public double xa, ya, za;
    public double xx, yy, zz;


    private Sprite shadow;
    private boolean magnet = true;

    public PickupItem(Level level, Item item, Vector2 position) {
        super(level, item, position);
        lifeTime = 60 * 10 + MathUtils.random(1, 60);

        if (this.distanceTo(level.player) < 48) {
            // no magnet
            magnet = false;
        }

        shadow = new Sprite(new Texture(Gdx.files.internal("sprites/shadow.png")));
        // bounce
        Random random = new Random();
        xx = position.x;
        yy = position.y;
        zz = 2;
        xa = random.nextGaussian() * 0.3;
        ya = random.nextGaussian() * 0.2;
        za = random.nextFloat() * 0.7 + 2;

        // addAction(new LifeTime(lifeTime))
        // addAction(new BounceAction())

    }

    @Override
    public void touchedBy(Entity entity) {
        if (entity instanceof Player) {
            if (((Player)entity).getInventory().add(item)) {
                remove();
                SproutGame.playSound("blop", 1f, MathUtils.random(0.6f, 1.2f), 1f);
                ((Player)entity).increaseStats(item.getName(), 1);
                // test some money
                getLevel().player.increaseFunds(1);
            }
        }
    }

    @Override
    public void containTrigger(Entity entity) {
        super.containTrigger(entity);
        touchedBy(entity);
    }

    @Override
    public void update(float deltaTime) {

        tickActions(deltaTime);

        time+=deltaTime * 100;
        if (time >= lifeTime) {
            remove();
            return;
        }

        xx += xa;
        yy += ya;
        zz += za;
        if (zz < 0) {
            zz = 0;
            za *= -0.5;
            xa *= 0.6;
            ya *= 0.6;
        }
        za -= 0.15;

        float distance = distanceTo(getLevel().player);
        if (getActions().size < 1) setPosition((float)xx, (float)yy + (float)zz);


        if (distance < 32 && getActions().size < 1 && magnet && getLevel().player.inventory.hasSpaceFor(this.item)) {
            // item in state of being sucked to the player
            addAction(Actions.sequence(
                    Actions.moveTo(getLevel().player.getX(), getLevel().player.getY()-8, .3f, Interpolation.pow3),
                    Actions.run((new Runnable() {
                        public void run () {
                            touchedBy(getLevel().player);
                        }
                    })

            )));
        }

        updateBoundingBox();
    }

    public void drawShadow(Batch batch, float delta) {
        shadow.setX(getX());
        shadow.setY(getY()-(float)zz - 2);
        shadow.draw(batch, 0.35f);
    }


    public void draw(Batch batch, float parentAlpha) {
        if (time >= lifeTime - (6 * 20)) {
            if (time / 6 % 2 == 0) return;
        }
        drawShadow(batch, 0f);
        super.draw(batch, parentAlpha);

    }

}