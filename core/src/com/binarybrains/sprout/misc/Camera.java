package com.binarybrains.sprout.misc;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.binarybrains.sprout.level.Level;


public class Camera extends OrthographicCamera {
    private boolean isShaking = false;
    private long startShakeTimer;
    private Level level;
    private boolean noFollow = false;

    public Camera (Level level) {
        super();
        this.level = level;
        shake();
    }

    public void shake() {
        startShakeTimer = TimeUtils.nanoTime();
        isShaking = true;
    }

    public float getX() {
        return position.x;
    }

    public float getY() {
        return position.y;
    }


    Vector3 temp = position;

    public void followPosition(Vector2 followPos, float deltaTime) {

        if (noFollow) return; // disabled

        temp.x += (followPos.x - position.x) *  deltaTime;
        temp.y += (followPos.y - position.y) * deltaTime;
        temp.z = 0;
        position.set(temp);

        if (isShaking) {
            if (TimeUtils.nanoTime() < startShakeTimer + 1000000000 * .3) {
                position.x += MathUtils.random(-60f, 60f) * deltaTime;
                position.y += MathUtils.random(-60f, 60f) * deltaTime;
            } else {
                isShaking = false;
            }
        }
    }

    @Override
    public void update() {
        float cameraHalfWidth = viewportWidth * .5f;
        float cameraHalfHeight = viewportHeight * .5f;
        float cameraLeft = position.x - cameraHalfWidth;
        float cameraRight = position.x + cameraHalfWidth;
        float cameraBottom = position.y - cameraHalfHeight;
        float cameraTop = position.y + cameraHalfHeight;

        float mapTop =  level.height * level.tilePixelHeight;
        float mapRight = level.width * level.tilePixelWidth;

        if (cameraLeft <= 0)
        {
            position.x = cameraHalfWidth;
        }

        if (cameraTop >= mapTop) {
            position.y = mapTop - cameraHalfHeight;
        }

        if (cameraRight >= mapRight)
        {
            position.x = mapRight - cameraHalfWidth;
        }

        if (cameraBottom <= 0) // bottom of world
        {
            position.y = cameraHalfHeight;
        }

        position.set((int) position.x, (int) position.y, 0);
        super.update();
    }

    public boolean isCameraBottomWorld() {
        return position.y - (viewportHeight * .5f) <= 0;
    }

    public void setPosition(Vector3 pos) {
        position.set((int) pos.x,(int) pos.y, 0);
    }

    public void disableFollow() {
        this.noFollow = true;
    }

    public void enableFollow() {
        this.noFollow = false;
    }

    public void reset() {
        // prevent camera pause glitch here?
    }
}
