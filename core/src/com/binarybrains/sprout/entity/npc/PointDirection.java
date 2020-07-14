package com.binarybrains.sprout.entity.npc;

import com.binarybrains.sprout.entity.Mob;

public class PointDirection extends Object {
    public Integer x, y;
    public Mob.Direction direction;

    /**
     *
     * @param x
     * @param y
     * @param direction
     */
    public PointDirection(Integer x, Integer y, Mob.Direction direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }


    @Override
    public String toString() {
        return "PointDirection{" +
                "x=" + x +
                ", y=" + y +
                ", direction=" + direction +
                '}';
    }
}
