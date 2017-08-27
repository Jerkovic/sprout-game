package com.binarybrains.sprout.entity.tree;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
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
    public boolean blocks(Entity e) {
        return true;
    }

    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);
    }

}
