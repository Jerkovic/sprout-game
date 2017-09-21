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

    Animation<TextureAtlas.AtlasRegion> animation;
    private float elapsedTime = 0;
    private boolean looping = false;

    public Sparkle(Level level, Vector2 position) {
        super(level, position, 32, 32);
        setCenterPos(position.x, position.y);
        // change this
        TextureAtlas atlas = SproutGame.assets.get("items2.txt");
        animation = new Animation<TextureAtlas.AtlasRegion>(1/8f, atlas.findRegions("Sparkle"));
        setWidth(animation.getKeyFrame(0).originalWidth);
        setHeight(animation.getKeyFrame(0).originalHeight);

    }

    public void setLooping() {
        this.looping = true;
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
    }

    @Override
    public float getSortOrder() {
        return -1;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        elapsedTime += deltaTime;

        if (animation.isAnimationFinished(elapsedTime) && !looping) {
            remove();
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = animation.getKeyFrame(elapsedTime, looping);
        batch.draw(frame, getCenterPos().x-(frame.getRegionWidth() / 2), getCenterPos().y - (frame.getRegionHeight() / 2));
    }

}
