package com.binarybrains.sprout.entity.actions;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.binarybrains.sprout.entity.Entity;

/** Actions attach to an Entity and perform some task, often over time.**/

abstract public class Action implements Poolable {

    protected Entity entity; /* The entity this action is attached to, or null if it is not attached. */
    protected Entity target; /* The actor this action targets, or null if a target has not been set. */

    private Pool pool;

    abstract public boolean act (float delta);

    public void restart () {
    }

    public void setEntity (Entity entity) {
        this.entity = entity;
        if (target == null) setTarget(entity);
        if (entity == null) {
            if (pool != null) {
                pool.free(this);
                pool = null;
            }
        }
    }

    public Entity getEntity () {
        return entity;
    }

    public void setTarget (Entity target) {
        this.target = target;
    }

    public Entity getTarget () {
        return target;
    }

    public void reset () {
        entity = null;
        target = null;
        pool = null;
        restart();
    }

    public Pool getPool () {
        return pool;
    }

    public void setPool (Pool pool) {
        this.pool = pool;
    }

    public String toString () {
        return getClass().getName();
    }
}
