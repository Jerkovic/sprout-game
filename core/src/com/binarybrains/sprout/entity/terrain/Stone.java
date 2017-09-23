package com.binarybrains.sprout.entity.terrain;

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
import com.binarybrains.sprout.level.Level;

public class Stone extends Entity {

    private Sprite sprite;
    private int health = 50; // change
    private boolean isShaking = false;
    private long startShakeTimer;


    public Stone(Level level, int tx, int ty) {

        super(level, new Vector2(0,0), 16, 16);

        setTilePos(tx, ty);
        sprite = new Sprite(level.spritesheet, 0 * 16, 78 * 16, 16, 16);
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
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (isShaking) {
            if (TimeUtils.nanoTime() < startShakeTimer + 1000000000 * .3) {
                float currentPower = 5 * deltaTime;
                float x = (MathUtils.random(0f, 1f) - 0.5f) * 2 * currentPower;
                float y = (MathUtils.random(0f, 1f) - 0.5f) * 2 * currentPower;
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
        shake();

        getLevel().add(getLevel(), new TextParticle(getLevel(), getTopCenterPos(), "" + "+"  + damage));

        if (health < 1) {
            SproutGame.playSound("break_stone", 0.9f);
            getLevel().getCamera().shake();

            for (int i = 0; i <  MathUtils.random(2,3); i++) {
                getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resources.stone), getCenterPos()));
            }

            if (MathUtils.randomBoolean()) {
                getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resources.ironOre), getCenterPos()));
            }

            if (MathUtils.random(1,4) == 1) {
                getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resources.goldNugget), getCenterPos()));

                if (getLevel().player.getStats(Resources.goldNugget.name) < 2) {
                    getLevel().player.getLevel().screen.hud.addToasterMessage("Gold", "You found your first gold nugget! Great job!");
                }
            }
            if (MathUtils.random(1,10) == 1) {
                getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resources.diamond), getCenterPos()));

            }

            remove();

        } else {
            SproutGame.playSound("metal_hit", 0.5f, MathUtils.random(0.68f, .78f), 1f);
        }

    }

        @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {

        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;

            if (toolItem.tool instanceof PickAxe && toolItem.tool.canUse()) {

                hurt(player, toolItem.getDamage());
                return true;
            }
        }
        return false;
    }

    public void draw(Batch batch, float parentAlpha) {
        // shadow?
        sprite.draw(batch);
    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }


}
