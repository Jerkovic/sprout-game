package com.binarybrains.sprout.entity.bomb;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Portable;
import com.binarybrains.sprout.level.Level;

import java.util.List;

/**
 * Bomb - (iron ball full of gun powder)
 * Created by erikl on 03/03/17.
 */
public class Bomb extends Entity implements Portable {

    boolean carried = true;
    private TextureAtlas atlas; // move
    private TextureRegion region;
    private Animation explosionAnimation;
    private float elapsedTime = 0;


    private int lifeTime;
    private int time = 0;
    private int radius = 4;

    private boolean explode = false;

    public Bomb(Level level, int tx, int ty) {
        super(level, new Vector2(16f * tx, 16f * ty), 16, 16);
        atlas = SproutGame.assets.get("items2.txt");
        region = atlas.findRegion("Bomb");

        explosionAnimation = new Animation(1/12f, atlas.findRegions("Explosion"));


        lifeTime = 60 * 2 + MathUtils.random(1, 2);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        if (explosionAnimation.isAnimationFinished(elapsedTime)) {
            remove();
            return;
        }

        time++;
        if (!explode && time >= lifeTime) {
            explode = true;
            SproutGame.playSound("bomb_explosion");
            getLevel().getCamera().shake();

            // hurt entities in radius from the bomb
            List<Entity> entities = getLevel().getEntities(new Rectangle(getCenterPos().x - 48,getCenterPos().y -48, 48*2, 48*2));

            for (int i = 0; i < entities.size(); i++) {
                Entity e = entities.get(i);
                if (e != this) e.hurt(this, 100); // get damage value from bomb or not?
            }

        }
    }

    @Override
    public void renderDebug(ShapeRenderer renderer, Color walkBoxColor) {
        Color restoreColor = renderer.getColor();

        renderer.setColor(Color.YELLOW); // bounding box

        Rectangle explosion = new Rectangle(getCenterPos().x-48, getCenterPos().y-48, 48*2, 48*2);
        renderer.rect(explosion.getX(), explosion.getY(), explosion.getWidth(), explosion.getHeight());

        renderer.setColor(Color.YELLOW); // center cross hair
        renderer.line(getCenterPos().x - 1, getCenterPos().y, getCenterPos().x + 1, getCenterPos().y);
        renderer.line(getCenterPos().x, getCenterPos().y - 1, getCenterPos().x, getCenterPos().y + 1);
        renderer.setColor(restoreColor);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (explode) {
            elapsedTime += Gdx.graphics.getDeltaTime();
            TextureRegion frame = (TextureRegion) explosionAnimation.getKeyFrame(elapsedTime, false);
            batch.draw(frame, getCenterPos().x-(frame.getRegionWidth() / 2), getCenterPos().y - (frame.getRegionHeight() / 2));
            return;

        }

        if (time / 6 % 2 == 0) return;
        batch.draw(region, getX(), getY(), 16, 16);

    }

    @Override
    public void setCarried() {
        // do some stuff

    }

    @Override
    public void deleteCarried() {

    }
}
