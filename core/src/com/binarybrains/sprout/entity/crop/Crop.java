package com.binarybrains.sprout.entity.crop;

import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.level.Level;

/**
 * Created by erikl on 01/03/17.
 */
public class Crop extends Entity {

    public Crop(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }
}
