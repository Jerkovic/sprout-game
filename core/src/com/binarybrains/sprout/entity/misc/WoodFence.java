package com.binarybrains.sprout.entity.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;


public class WoodFence extends Entity {


    private Sprite sprite;

    public WoodFence(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);

        int x = getTileX();
        int y = getTileY();

        level.getTile(x, y).setIsHoldingFence(true);

        boolean d = level.getTile(x, y - 1).isHoldingFence();
        boolean u = level.getTile(x, y + 1).isHoldingFence();
        boolean l = level.getTile(x - 1, y).isHoldingFence();
        boolean r = level.getTile(x + 1, y).isHoldingFence();

        int srcTileX = 9;
        int srcTileY = 6;

        if (!u && !l && !d && !r) {
            srcTileX = 9;
            srcTileY = 6;
        }

        if (!u && l && !d && !r) {
            srcTileX = 10;
            srcTileY = 3;
        }

        if (!u && !l && d && !r) {
            srcTileX = 12;
            srcTileY = 4;
        }

        if (!d && !l) {
        } else  {

        }

        if (!d && !r) {

        } else  {

        }



        sprite = new Sprite(level.spritesheet, srcTileX * 16, srcTileY *16, (int)width, (int)height);
        sprite.setSize(width, height);
        sprite.setPosition(getX(), getY());

        System.out.println("u:" + u + " d:" + d + " l:" + l + " r:" + r);

    }

    public void updateBoundingBox() {
        super.updateBoundingBox();
        // update the walkBox hit detector for tiles hit detection
        // this must be much easier see also the Tree entity
        this.walkBox.setWidth(getWidth());
        this.walkBox.setHeight(getHeight() - 12);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }


    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    public void draw(Batch batch, float parentAlpha) {

        sprite.draw(batch);
    }


}


