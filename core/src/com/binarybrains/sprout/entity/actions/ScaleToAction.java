package com.binarybrains.sprout.entity.actions;

public class ScaleToAction extends TemporalAction {
    private float startX, startY;
    private float endX, endY;

    protected void begin () {
        startX = target.getCenterPos().x;
        startY = target.getCenterPos().y;
    }

    protected void update (float percent) {
        // todoo
    }

    public void setScale (float x, float y) {
        endX = x;
        endY = y;
    }

    public void setScale (float scale) {
        endX = scale;
        endY = scale;
    }

    public float getX () {
        return endX;
    }

    public void setX (float x) {
        this.endX = x;
    }

    public float getY () {
        return endY;
    }

    public void setY (float y) {
        this.endY = y;
    }
}
