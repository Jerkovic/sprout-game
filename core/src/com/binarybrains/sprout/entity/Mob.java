package com.binarybrains.sprout.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.level.Level;

/**
 * Mobile Entities - we should maybe move stuff from NPC
 * - like player, enemies, cows, pigs etc
 */
public abstract class Mob extends Entity {

    public static enum State {
        STANDING, WALKING, ATTACKING
    }

    public static enum Direction {
        SOUTH, EAST, NORTH, WEST, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST;

        public static Direction getAnimationDirection(Direction dir) {
            if (dir == SOUTH_EAST || dir == NORTH_EAST) {
                return EAST;
            } else if (dir == SOUTH_WEST || dir == NORTH_WEST) {
                return WEST;
            } else {
                return dir;
            }
        }
    }

    private State state = State.STANDING;
    private Direction direction = Direction.SOUTH;
    private Vector2 velocity;

    private int health = 0;
    private int damage = 0;
    private float speed = 0;


    public Mob(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);
        setState(State.STANDING);
        setDirection(Direction.SOUTH);
        velocity = new Vector2(0, 0);
        setHealth(MathUtils.random(1, 100));
    }


    public void update(float delta) {

        super.update(delta);
        if (getHealth() <= 0)
        {
            die();
        }
    }

    public float getSpeed() {
        return speed;
    }

    /*
    public float getDiagonalSpeed() {
        return speed / 1.41421356237f;
    } */

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    protected void die() {
        if (!this.removed) {
            super.remove();
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void hurt(Mob mob, int damage, Direction attackDir) {
        if (damage > 0) {
            System.out.println(mob + " gives " + damage + " damage to " + this + " " + direction);
            health -= damage;
        }
        //if (attackDir == Direction.SOUTH) yKnockback = +6; // knockback north
        //if (attackDir == Direction.WEST) yKnockback = 6; // knockback east
        //if (attackDir == Direction.EAST) xKnockback = -6; // knockback west
        //if (attackDir == Direction.NORTH) xKnockback = -6; // knockback south
    }


    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString()
    {
        return super.toString() + " Direction: " + getDirection() + " State: " + getState();
    }


}
