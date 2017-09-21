package com.binarybrains.sprout.hud.tweens;

import aurelienribon.tweenengine.TweenAccessor;
import com.badlogic.gdx.math.Vector3;
import com.binarybrains.sprout.misc.Camera;

public class CameraAccessor implements TweenAccessor<Camera>
{
    public static final int POSITION_XY = 1;
    // add rotate and zoom

    @Override
    public int getValues(Camera target, int tweenType, float[] returnValues)
    {
        switch (tweenType)
        {
            case POSITION_XY:
                returnValues[0] = target.getX();
                returnValues[1] = target.getY();
                return 2;
            default:
                assert false;
                return -1;
        }
    }

    @Override
    public void setValues(Camera target, int tweenType, float[] newValues)
    {
        switch (tweenType)
        {
            case POSITION_XY:
                target.setPosition(new Vector3(newValues[0], newValues[1], 0));
                break;
            default:
                assert false;
        }
    }
}