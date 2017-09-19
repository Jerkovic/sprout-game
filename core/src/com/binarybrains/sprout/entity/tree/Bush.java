package com.binarybrains.sprout.entity.tree;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.level.Level;

public class Bush extends Entity {

    private Sprite sprite;

    public Bush(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);

        sprite = new Sprite(level.spritesheet, 13 * 16, 0, (int)width, (int)height);
        sprite.setSize(width, height);
        sprite.setPosition(getX(), getY());

    }
    @Override
    public boolean use(Player player, Mob.Direction attackDir) {
        // shake();
        SproutGame.playSound("leaves_rustling", MathUtils.random(0.4f, 0.8f), MathUtils.random(0.9f, 1.2f), 1f);
        // could produce Chuck Berries
        int count = MathUtils.random(2, 6);
        for (int i = 0; i < count; i++) {
            getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resources.chuckBerry), new Vector2(getWalkBoxCenterX(), getWalkBoxCenterY())));
        }
        return true;

    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

}
