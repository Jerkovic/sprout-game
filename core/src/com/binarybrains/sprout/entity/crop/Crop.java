package com.binarybrains.sprout.entity.crop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.level.Level;

/**
 * Created by erikl on 01/03/17.
 */
public class Crop extends Entity {

    private TextureAtlas atlas; // move
    private Array<TextureAtlas.AtlasRegion> regions;
    private int regionIndex = 0;
    private float growTimer = 0;


    // Crop should be abstract class
    public Crop(Level level, int tx, int ty) {

        super(level, new Vector2(16f * tx, 16f * ty), 16, 16);
        atlas = SproutGame.assets.get("items2.txt");
        regions = atlas.findRegions("Potato_Stage"); // potato test

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(regions.get(regionIndex), getX(), getY(), 16, 16);

    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        growTimer = growTimer + deltaTime;
        if (growTimer > 2 && regionIndex < 5) {
            regionIndex++;
            growTimer = 0;

        }

    }
}
