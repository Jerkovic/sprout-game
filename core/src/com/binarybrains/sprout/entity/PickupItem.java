package com.binarybrains.sprout.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.bellsandwhistles.Sparkle;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PickupItem extends ItemEntity {

    private boolean bounce = true;
    private int lifeTime;
    private int time = 0;

    public double xa, ya, za;
    public double xx, yy, zz;


    private Sprite shadow;
    private boolean magnet = true;
    private Sparkle sparkle;
    private float shadowFallingY = 0;
    private float endPositionY = 0;
    private boolean isPlayed = false;

    public PickupItem(Level level, Item item, Vector2 position) {
        super(level, item, position);
        setCenterPos(position.x, position.y);
        lifeTime = 60 * 10 + MathUtils.random(1, 60);

        List<String> valuables = Arrays.asList("Gold Nugget", "Diamond", "Apple");

        List<String> fallfruit = Arrays.asList("Apple", "Orange");

        if (fallfruit.contains(item.getName())) {
            bounce = false;
            if (MathUtils.randomBoolean())
                setX(getX()+MathUtils.random(8, 20));
            else
                setX(getX()-MathUtils.random(8,20));

            // this has to be connected to the tree?
            shadowFallingY = position.y-45;
            endPositionY = position.y-42;

            addAction(Actions.moveTo(position.x, endPositionY, MathUtils.random(.98f, 1.1f), Interpolation.bounceOut));
        }

        // todo rareness level on item
        if (valuables.contains(item.getName())) {
            sparkle = new Sparkle(level, getCenterPos());
            sparkle.setLooping();
            level.add(level, sparkle);
        }

        if (this.distanceTo(level.player) < 48) {
            // no magnet
            magnet = false;
        }

        shadow = new Sprite(new Texture(Gdx.files.internal("sprites/shadow.png")));
        // bounce
        if (bounce)
        {
            Random random = new Random();
            xx = position.x;
            yy = position.y;
            zz = 2;
            xa = random.nextGaussian() * 0.3;
            ya = random.nextGaussian() * 0.2;
            za = random.nextFloat() * 0.7 + 2;

        }

    }


    @Override
    public boolean remove() {
        super.remove();
        if (sparkle != null) sparkle.remove();
        return true;
    }

    @Override
    public void touchedBy(Entity entity) {
        if (entity instanceof Player) {
            if (((Player)entity).getInventory().add(item)) {
                remove();
                SproutGame.playSound("blop", 1f, MathUtils.random(0.6f, 1.2f), 1f);
                ((Player)entity).increaseStats(item.getName(), 1);
                // test some money earning
                getLevel().player.increaseFunds(10);
                getLevel().screen.hud.addNotification(item);
            }
        }
    }

    @Override
    public void containTrigger(Entity entity) {
        super.containTrigger(entity);
        touchedBy(entity);
    }

    private void updateBounce() {
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

        if (getActions().size < 1) setPosition((float)xx, (float)yy + (float)zz);

    }

    @Override
    public void update(float deltaTime) {

        tickActions(deltaTime);

        time+=deltaTime * 100;
        if (time >= lifeTime) {
            remove();
            return;
        }

        if (bounce) {
            updateBounce();
        }


        if (getY() <= endPositionY+2 && bounce == false && !isPlayed) {
            SproutGame.playSound("small_ground_hit", .18f, MathUtils.random(0.8f, .9f), 1);
            isPlayed = true;
        }

        float distance = distanceTo(getLevel().player);
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

    private void drawShadow(Batch batch, float delta) {
        shadow.setX(getX());
        if (bounce)
        {
            shadow.setY(getY()-(float)zz - 2);

        } else
        {
            shadow.setY(shadowFallingY);
        }
        shadow.draw(batch, 0.35f);
    }


    public void draw(Batch batch, float parentAlpha) {
        if (time >= lifeTime - (6 * 20)) {
            if (time / 6 % 2 == 0) return;
        }
        if (sparkle != null) {
            sparkle.setCenterPos(getCenterPos().x, getCenterPos().y);
        }
        drawShadow(batch, 0f);
        super.draw(batch, parentAlpha);

    }

}