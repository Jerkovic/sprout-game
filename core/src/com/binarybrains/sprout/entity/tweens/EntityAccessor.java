package com.binarybrains.sprout.entity.tweens;


import aurelienribon.tweenengine.TweenAccessor;
import com.binarybrains.sprout.entity.Entity;


public class EntityAccessor implements TweenAccessor<Entity>
{
    public static final int POSITION_XY = 1;

    @Override
    public int getValues(Entity target, int tweenType, float[] returnValues)
    {
        switch (tweenType)
        {
            case POSITION_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 1;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Entity target, int tweenType, float[] newValues)
    {
        switch (tweenType)
        {
            case POSITION_XY:
                target.setX(newValues[0]);
                target.setY(newValues[1]);
                break;
            default:
                assert false;
        }
    }
}