package com.binarybrains.sprout.entity.actions;


/** Moves an actor from its current position to a specific position. */
public class MoveToAction extends TemporalAction {
    private float startX, startY;
    private float endX, endY;

    protected void begin () {
        startX = target.getX();
        startY = target.getY();
    }

    protected void update (float percent) {
        target.setPosition(startX + (endX - startX) * percent, startY + (endY - startY) * percent);
    }

    public void reset () {
        super.reset();
    }

    public void setPosition (float x, float y) {
        endX = x;
        endY = y;
    }

    public float getX () {
        return endX;
    }

    public void setX (float x) {
        endX = x;
    }

    public float getY () {
        return endY;
    }

    public void setY (float y) {
        endY = y;
    }


}
