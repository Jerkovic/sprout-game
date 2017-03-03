package com.binarybrains.sprout.entity.bomb;

import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Portable;
import com.binarybrains.sprout.level.Level;

/**
 * Created by erikl on 03/03/17.
 */
public class Bomb extends Entity implements Portable {

    public Bomb(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height);
    }

    @Override
    public void setCarried() {

    }

    @Override
    public void deleteCarried() {

    }
}
