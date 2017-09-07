package com.binarybrains.sprout.entity.actions;

public class RemoveAction extends Action {
    private Action action;

    public boolean act (float delta) {
        target.removeAction(action);
        return true;
    }

    public Action getAction () {
        return action;
    }

    public void setAction (Action action) {
        this.action = action;
    }

    public void reset () {
        super.reset();
        action = null;
    }
}
