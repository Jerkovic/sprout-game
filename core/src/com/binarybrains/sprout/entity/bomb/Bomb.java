package com.binarybrains.sprout.entity.bomb;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Portable;
import com.binarybrains.sprout.level.Level;

/**
 * Bomb - (iron ball full of gun powder)
 * Created by erikl on 03/03/17.
 */
public class Bomb extends Entity implements Portable {

    boolean carried = true;
    private TextureAtlas atlas; // move
    private TextureRegion region;

    private int lifeTime;
    private int time = 0;
    private int radius = 4;


    public Bomb(Level level, int tx, int ty) {
        super(level, new Vector2(16f * tx, 16f * ty), 16, 16);
        atlas = SproutGame.assets.get("items2.txt");
        region = atlas.findRegion("Bomb");

        lifeTime = 60 * 10 + MathUtils.random(1, 5);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        time++;
        if (time >= lifeTime) {
            // explode
            System.out.println("Boooooooooom!!!!");
            // hurt entities in radius from the bomb
            // getLevel().getEntitiesInTileRadius
            remove();
            return;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

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
