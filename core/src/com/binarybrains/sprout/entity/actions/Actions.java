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

    static public DelayAction delay (float duration) {
        DelayAction action = action(DelayAction.class);
        action.setDuration(duration);
        return action;
    }

    static public DelayAction delay (float duration, Action delayedAction) {
        DelayAction action = action(DelayAction.class);
        action.setDuration(duration);
        action.setAction(delayedAction);
        return action;
    }

    static public RunnableAction run (Runnable runnable) {
        RunnableAction action = action(RunnableAction.class);
        action.setRunnable(runnable);
        return action;
    }


    static public SequenceAction sequence (Action action1) {
        SequenceAction action = action(SequenceAction.class);
        action.addAction(action1);
        return action;
    }

    static public SequenceAction sequence (Action action1, Action action2) {
        SequenceAction action = action(SequenceAction.class);
        action.addAction(action1);
        action.addAction(action2);
        return action;
    }

    static public SequenceAction sequence (Action action1, Action action2, Action action3) {
        SequenceAction action = action(SequenceAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        return action;
    }

    static public SequenceAction sequence (Action action1, Action action2, Action action3, Action action4) {
        SequenceAction action = action(SequenceAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        action.addAction(action4);
        return action;
    }

    static public SequenceAction sequence (Action action1, Action action2, Action action3, Action action4, Action action5) {
        SequenceAction action = action(SequenceAction.class);
        action.addAction(action1);
        action.addAction(action2);
        action.addAction(action3);
        action.addAction(action4);
        action.addAction(action5);
        return action;
    }

    static public SequenceAction sequence (Action... actions) {
        SequenceAction action = action(SequenceAction.class);
        for (int i = 0, n = actions.length; i < n; i++)
            action.addAction(actions[i]);
        return action;
    }

    static public SequenceAction sequence () {
        return action(SequenceAction.class);
    }



}
