package com.binarybrains.sprout.entity.actions;

/**
 * Created by erikl on 24/09/17.
 */
public class RotateToAction extends TemporalAction {

    private float start, end;

    protected void begin () {
        start = target.getRotation();
    }

    protected void update (float percent) {
        target.setRotation(start + (end - start) * percent);
    }

    public float getRotation () {
        return end;
    }

    public void setRotation (float rotation) {
        this.end = rotation;
    }

}
