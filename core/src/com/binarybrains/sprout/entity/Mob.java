package com.binarybrains.sprout.entity;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.level.Level;

import java.util.Random;

/**
 * Mobile Entities - we should maybe move stuff from NPC
 * - like player, enemies, cows, pigs etc
 * - should all Mob(iles) be able to FindPath, Yes probably
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

        public static Direction getRandomDirection() {
            int ordinalDirection = new Random().nextInt(4);
            return Direction.values()[ordinalDirection];
        }
    }

    private State state = State.STANDING;
    private Direction direction = Direction.SOUTH;

    private int health = 0;
    private int damage = 0;
    private float speed = 0;


    public Mob(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);
        setState(State.STANDING);
        setDirection(Direction.SOUTH);
        setHealth(MathUtils.random(1, 100));
    }

    public void update(float delta) {
        super.update(delta);
        if (getHealth() <= 0) {
            die();
        }
    }

    public void lookAt(Entity entity) {
        float angle = angleTo(entity);
        String directions[] = {"NORTH", "EAST",  "SOUTH", "WEST"};
        String direction = directions[Math.round((Math.abs(angle)) / 90) % 4];

        if (direction.equals("WEST")) {
            setDirection(Direction.WEST);

        } else if (direction.equals("EAST")) {
            setDirection(Direction.EAST);

        } else if (direction.equals("NORTH")) {
            setDirection(Direction.NORTH);

        } else if (direction.equals("SOUTH")) {
            setDirection(Direction.SOUTH);
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

    public void die() {
        if (!this.removed) {
            System.out.println(this + " died");
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
            health -= damage;
            System.out.println(mob + " gives " + damage + " damage to " + this + " " + direction + " health remaining: " + getHealth());
        }

        float knockBackForce = MathUtils.random(28f, 34f);
        float knockBackTime = MathUtils.random(.212234f, .334242f);

        if (attackDir == Direction.SOUTH) {
            addAction(Actions.moveTo(getX(), getY() - knockBackForce, knockBackTime, Interpolation.exp5Out));
        }
        if (attackDir == Direction.NORTH) {
            addAction(Actions.moveTo(getX(), getY() + knockBackForce, knockBackTime, Interpolation.exp5Out));
        }

        if (attackDir == Direction.WEST) {
            addAction(Actions.moveTo(getX()-knockBackForce, getY(), knockBackTime, Interpolation.exp5Out));
        }
        if (attackDir == Direction.EAST) {
            addAction(Actions.moveTo(getX()+knockBackForce, getY(), knockBackTime, Interpolation.exp5Out));
        }

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
