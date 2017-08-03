package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.level.Level;

public class Fido extends Npc {

    private float changeTimer = 0;
    Sound testSfx;

    public Fido(Level level, Vector2 position) {
        super(level, position, 16f, 16f, 4); // 4 is the sprite row
        setDirection(Direction.EAST);
        setState(State.WALKING);
        setActionState(ActionState.EMPTY_NORMAL);
        setSpeed(MathUtils.random(32f, 32f));

        // temp dog sound
        testSfx = SproutGame.assets.get("sfx/dog_woof.wav");


    }

    @Override
    public void update(float delta) {
        super.update(delta);
        changeTimer = changeTimer + delta;

        if (changeTimer > 2 && distanceTo(getLevel().player) < 100) {
            lookAt(getLevel().player);
            changeTimer = 0;
            testSfx.play();
        }
        if (getState() == State.STANDING) {
            setState(State.WALKING);
            setDirection(Direction.getRandomDirection());
        }
    }

    @Override
    public void updateBoundingBox() {
        this.box.setWidth(getWidth());
        this.box.setHeight(getHeight());
        this.box.setPosition(getPosition());

        this.walkBox.setWidth(12);
        this.walkBox.setHeight(6);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

}
