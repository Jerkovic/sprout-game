package com.binarybrains.sprout.entity.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.binarybrains.sprout.entity.Entity;

public class Actions {

    static public <T extends Action> T action (Class<T> type) {
        Pool<T> pool = Pools.get(type);
        T action = pool.obtain();
        action.setPool(pool);
        return action;
    }

    static public AddAction addAction (Action action) {
        AddAction addAction = action(AddAction.class);
        addAction.setAction(action);
        return addAction;
    }

    static public AddAction addAction (Action action, Entity target) {
        AddAction addAction = action(AddAction.class);
        addAction.setTarget(target);
        addAction.setAction(action);
        return addAction;
    }


    static public RemoveAction removeAction (Action action) {
        RemoveAction removeAction = action(RemoveAction.class);
        removeAction.setAction(action);
        return removeAction;
    }

    static public RemoveAction removeAction (Action action, Entity target) {
        RemoveAction removeAction = action(RemoveAction.class);
        removeAction.setTarget(target);
        removeAction.setAction(action);
        return removeAction;
    }

    static public MoveToAction moveTo (float x, float y, float duration, Interpolation interpolation) {
        MoveToAction action = action(MoveToAction.class);
        action.setPosition(x, y);
        action.setDuration(duration);
        action.setInterpolation(interpolation);
        return action;
    }


}
