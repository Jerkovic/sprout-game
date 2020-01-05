package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Mob;

public class PointDirection {
    public Integer x, y;
    public Mob.Direction direction;

    /**
     *
     * @param position
     * @param direction
     */
    public PointDirection(Integer x, Integer y, Mob.Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
}
