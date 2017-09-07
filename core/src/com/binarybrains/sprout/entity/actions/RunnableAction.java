package com.binarybrains.sprout.entity.actions;

import com.badlogic.gdx.utils.Pool;

public class RunnableAction extends Action {
    private Runnable runnable;
    private boolean ran;

    public boolean act (float delta) {
        if (!ran) {
            ran = true;
            run();
        }
        return true;
    }


    public void run () {
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool inside the runnable.
        try {
            runnable.run();
        } finally {
            setPool(pool);
        }
    }

    public void restart () {
        ran = false;
    }

    public void reset () {
        super.reset();
        runnable = null;
    }

    public Runnable getRunnable () {
        return runnable;
    }

    public void setRunnable (Runnable runnable) {
        this.runnable = runnable;
    }
}
