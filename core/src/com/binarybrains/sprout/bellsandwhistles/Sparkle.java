package com.binarybrains.sprout.bellsandwhistles;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.level.Level;

public class Sparkle extends Entity {

    // sort them in another way

    Animation<TextureAtlas.AtlasRegion> animation;
    private float elapsedTime = 0;

    public Sparkle(Level level, Vector2 position) {
        super(level, position, 32, 32);
        TextureAtlas atlas = SproutGame.assets.get("items2.txt");
        animation = new Animation<TextureAtlas.AtlasRegion>(1/8f, atlas.findRegions("Explosion")); // todo Sparkle

        setWidth(animation.getKeyFrame(0).originalWidth);
        setHeight(animation.getKeyFrame(0).originalHeight);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        elapsedTime += deltaTime;

        if (animation.isAnimationFinished(elapsedTime)) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        TextureRegion frame = animation.getKeyFrame(elapsedTime, false);
        batch.draw(frame, getX()-(frame.getRegionWidth() / 2), getY() - (frame.getRegionHeight() / 2));
    }

}
