package com.binarybrains.sprout.entity.bomb;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Portable;
import com.binarybrains.sprout.level.Level;

/**
 * Created by erikl on 03/03/17.
 */
public class Bomb extends Entity implements Portable {

    boolean carried = true;
    private TextureAtlas atlas; // move
    private TextureRegion region;

    public Bomb(Level level, int tx, int ty) {
        super(level, new Vector2(16f * tx, 16f * ty), 16, 16);
        atlas = SproutGame.assets.get("items2.txt");
        region = atlas.findRegion("Bomb");
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(region, getX(), getY(), 16, 16);

    }


    @Override
    public void setCarried() {
        // do some styff

    }

    @Override
    public void deleteCarried() {

    }
}
