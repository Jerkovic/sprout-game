package com.binarybrains.sprout.entity.terrain;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.bellsandwhistles.TextParticle;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.item.tool.PickAxe;
import com.binarybrains.sprout.item.tool.Scythe;
import com.binarybrains.sprout.level.Level;

public class Grass extends Entity {

    private Sprite sprite;
    private int health = 1; // change
    private boolean isShaking = false;
    private long startShakeTimer;

    public float defaultShakeRate = 0.03926991f;
    public float maximumShake = 0.3926991f;
    public float shakeDecayRate = 0.008975979f;

    public Grass(Level level, float x, float y) {

        super(level, new Vector2(x,y), 16, 16);
        this.health = 1;
        sprite = new Sprite(level.spritesheet, 2 * 16, 78 * 16, 16, 16);
        sprite.setSize(16, 16);
        sprite.setPosition(getX(), getY());

    }

    public void shake() {
        startShakeTimer = TimeUtils.nanoTime();
        isShaking = true;
    }

    @Override
    public void updateBoundingBox() {
        this.box.setWidth(getWidth());
        this.box.setHeight(getHeight());
        this.box.setPosition(getPosition());
        this.walkBox.setWidth(getWidth());
        this.walkBox.setHeight(getHeight());
        this.walkBox.setPosition(getPosition());
    }

    @Override
    public void containTrigger(Entity entity) {
        shake();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isShaking) {
            if (TimeUtils.nanoTime() < startShakeTimer + 1000000000 * .3) {
                float currentPower = 16 * deltaTime;
                float x = (MathUtils.random(0f, 1.1f) - 0.5f) * 2 * currentPower;
                float y = (MathUtils.random(0f, 1.1f) - 0.5f) * 2 * currentPower;
                sprite.translate(-x, -y);
            }
            else
            {
                isShaking = false;
            }
        }
    }


    @Override
    public void hurt(Entity ent, int damage) {
        health -= damage;
        if (health < 1) remove();
        shake();

    }

        @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {

        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;

            if (toolItem.tool instanceof Scythe && toolItem.tool.canUse()) {

                hurt(player, toolItem.getDamage());
                return true;
            }
        }
        return false;
    }

    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }


}
