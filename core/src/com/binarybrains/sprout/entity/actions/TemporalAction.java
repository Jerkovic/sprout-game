package com.binarybrains.sprout.entity.actions;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Pool;

abstract public class TemporalAction extends com.binarybrains.sprout.entity.actions.Action {
    private float duration, time;
    private Interpolation interpolation;
    private boolean reverse, began, complete;

    public TemporalAction () {
    }

    public TemporalAction (float duration) {
        this.duration = duration;
    }

    public TemporalAction (float duration, Interpolation interpolation) {
        this.duration = duration;
        this.interpolation = interpolation;
    }

    @Override
    public boolean act (float delta) {
        if (complete) return true;
        Pool pool = getPool();
        setPool(null); // Ensure this action can't be returned to the pool while executing.
        try {
            if (!began) {
                begin();
                began = true;
            }
            time += delta;
            complete = time >= duration;
            float percent;
            if (complete)
                percent = 1;
            else {
                percent = time / duration;
                if (interpolation != null) percent = interpolation.apply(percent);
            }
            update(reverse ? 1 - percent : percent);
            if (complete) end();
            return complete;
        } finally {
            setPool(pool);
        }
    }

    // Called each frame. The percentage of completion for this action, growing from 0 to 1 over the duration.
    abstract protected void update (float percent);


    /* Called the first time update is called. This is a good place to query the entity action starting */
    protected void begin () {
    }

    /** Called the last time {@link #update(float)} is called. */
    protected void end () {
    }

    /** Skips to the end of the transition. */
    public void finish () {
        time = duration;
    }

    public void restart () {
        time = 0;
        began = false;
        complete = false;
    }

    public void reset () {
        super.reset();
        reverse = false;
        interpolation = null;
    }

    /** Gets the transition time so far. */
    public float getTime () {
        return time;
    }

    /** Sets the transition time so far. */
    public void setTime (float time) {
        this.time = time;
    }

    public float getDuration () {
        return duration;
    }

    /** Sets the length of the transition in seconds. */
    public void setDuration (float duration) {
        this.duration = duration;
    }

    public Interpolation getInterpolation () {
        return interpolation;
    }

    public void setInterpolation (Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    public boolean isReverse () {
        return reverse;
    }

    /** When true, the action's progress will go from 100% to 0%. */
    public void setReverse (boolean reverse) {
        this.reverse = reverse;
    }
}
