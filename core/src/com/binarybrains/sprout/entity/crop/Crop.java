package com.binarybrains.sprout.entity.crop;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.WateringCan;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.level.tile.DirtTile;
import com.binarybrains.sprout.level.tile.Tile;


public class Crop extends Entity {

    private TextureAtlas atlas; // move
    private Array<TextureAtlas.AtlasRegion> regions;
    private int regionIndex = 0;
    private float growTimer = 0;
    public boolean watered = false;

    // Crop should be abstract class?
    public Crop(Level level, int tx, int ty) {

        super(level, new Vector2(16f * tx, 16f * ty), 16, 16);
        atlas = SproutGame.assets.get("items2.txt");
        regions = atlas.findRegions("Potato_Stage"); // potato test

    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof WateringCan && toolItem.tool.canUse()) {
                ((WateringCan)toolItem.tool).pour();
                player.getLevel().screen.hud.refreshInventory();
                watered = true;
                return true;
            }
        }
        return false;
    }

    public void grow() {

    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(regions.get(regionIndex), getX(), getY(), 16, 16);
    }

    public boolean canHarvest() {
        return (regionIndex == 5);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        // if not watered ... it is not growing
        if (!watered) return;

        growTimer = growTimer + deltaTime;
        if (growTimer > 2 && regionIndex < 5) {
            regionIndex++;
            growTimer = 0;
        }

        // can harvest and the time is right we make potatoes jump out
        if (growTimer > 5 && growTimer < 6 && canHarvest()) {
            //System.out.println(this +" can be harvested!");
            remove();
            // set back the tile to Dirt instead of FarmTile
            Tile tile = getLevel().getTile(getTileX(), getTileY());
            if (tile instanceof DirtTile) {
                ((DirtTile) tile).hasCrops = false;
            }

            int count = MathUtils.random(2, 6);
            for (int i = 0; i < count; i++) {
                getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resource.potato), new Vector2(getPosition().x, getPosition().y)));
            }

        }

    }
}
