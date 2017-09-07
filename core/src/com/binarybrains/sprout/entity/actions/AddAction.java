package com.binarybrains.sprout.entity.actions;


public class AddAction extends Action {
    private Action action;

    public boolean act (float delta) {
        getTarget().addAction(action);
        return true;
    }

    public Action getAction () {
        return action;
    }

    public void setAction (Action action) {
        this.action = action;
    }

    public void restart () {
        if (action != null) action.restart();
    }

    public void reset () {
        super.reset();
        action = null;
    }
}
