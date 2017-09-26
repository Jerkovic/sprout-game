package com.binarybrains.sprout.entity.enemy;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.level.Level;

public class Slime extends Mob {

    private float speed = .123133489879f;

    public Slime(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height);
    }

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);

        speed = MathUtils.random(.13240234f, .97459395395f);

        lookAt(getLevel().player); // determine the slimes looking/walk dir

        float diffX = getLevel().player.getX() - getX();
        float diffY = getLevel().player.getY() - getY();

        float angle = (float)Math.atan2(diffY, diffX);

        float newX = getPosition().x + (float) (speed * Math.cos(angle));
        float newY = getPosition().y + (float) (speed * Math.sin(angle));

        // collision basics
        if (canMoveToPos(newX, newY)) {
            getPosition().x += speed * Math.cos(angle);
            getPosition().y += speed * Math.sin(angle);
        } else {
            // what do todo when slime is stuck?
        }

    }


}
