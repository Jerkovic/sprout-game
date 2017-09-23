package com.binarybrains.sprout.entity.actions;

import com.badlogic.gdx.graphics.Color;

/**
 * Created by erikl on 23/09/17.
 */
public class AlphaAction extends TemporalAction {
    private float start, end;
    private Color color;

    protected void begin () {
        if (color == null) color = target.getColor();
        start = color.a;
    }

    protected void update (float percent) {
        color.a = start + (end - start) * percent;
    }

    public void reset () {
        super.reset();
        color = null;
    }

    public Color getColor () {
        return color;
    }

    /** Sets the color to modify. If null (the default), the {@link #getActor() actor's} {@link Actor#getColor() color} will be
     * used. */
    public void setColor (Color color) {
        this.color = color;
    }

    public float getAlpha () {
        return end;
    }

    public void setAlpha (float alpha) {
        this.end = alpha;
    }
}
