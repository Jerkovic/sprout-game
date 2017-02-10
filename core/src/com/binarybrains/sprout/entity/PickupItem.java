package com.binarybrains.sprout.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

/**
 * Items that can be pickup on the map, right now just walk over them
 */

public class PickupItem extends ItemEntity {

    private int lifeTime;
    private int time = 0;

    public PickupItem(Level level, Item item, Vector2 position) {
        super(level, item, position);
        lifeTime = 60 * 10 + MathUtils.random(1, 60);
    }

    @Override
    public void touchedBy(Entity entity) {
        // if (time > 30) entity.touchItem(this);
        if (entity instanceof Player) {

            if (((Player)entity).getInventory().add(item)) {
                remove();
                // temp sound
                Sound testSfx = SproutGame.assets.get("sfx/blop.wav");
                testSfx.play();
                // item.onTake();
                // do some particleEffect item.onTake()
                // play pickup award sound
            }

        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        time++;
        if (time >= lifeTime) {
            remove();
            return;
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        if (time >= lifeTime - (6 * 20)) {
            if (time / 6 % 2 == 0) return;
        }
        super.draw(batch, parentAlpha);
    }


}
