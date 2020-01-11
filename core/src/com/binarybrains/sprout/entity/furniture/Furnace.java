package com.binarybrains.sprout.entity.furniture;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.*;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.artifact.Artifacts;
import com.binarybrains.sprout.level.Level;


public class Furnace extends Entity implements Portable {

    private TextureRegion region;

    public Furnace(Level level, int tx, int ty) {
        super(level, new Vector2(16f * tx, 16f * ty), 16, 16);
        TextureAtlas atlas = SproutGame.assets.get("items2.txt");
        region = atlas.findRegion("Furnace");
    }

    @Override
    public void updateBoundingBox() {
        super.updateBoundingBox();
        this.walkBox.setWidth(getWidth());
        this.walkBox.setHeight(getHeight() - 8);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    @Override
    public boolean use(Player player, Mob.Direction attackDir) {
        player.getLevel().screen.hud.showCraftingWindow();
        SproutGame.playSound("fuse", 1f, MathUtils.random(0.75f, 0.85f), 1f);
        return true;
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(region, getX(), getY(), 16, 16);
    }

    @Override
    public void setCarried() {
    }

    @Override
    public void deleteCarried() {
    }
}
