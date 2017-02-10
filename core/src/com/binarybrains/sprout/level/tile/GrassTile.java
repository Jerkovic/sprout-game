package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.misc.WoodFence;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.FishingPole;
import com.binarybrains.sprout.level.Level;

import java.util.List;

public class GrassTile extends Tile {

    public GrassTile() {
        super(true);
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Entity entity) {
        Item item = entity.getActiveItem();

        // we have to be able to check if the tile is free from entities as well

        if (item instanceof ResourceItem) {
            if (level.isTileBlocked(xt, yt, entity)) {
                Gdx.app.log("Tile", "Tile is blocked!!!!!!!!!!!!!!!!!!!!!!");
            }
            Gdx.app.log("DEBUG", "Fiddling with grass:  " + xt + " " + yt + " " + entity + " -> " + item);
            if (level.getNumberEntitiesAtTile(xt, yt) > 0) {
                Gdx.app.log("Tile", "Tile is currently block by entities.");
            } else {
                // level.setTile(xt, yt, true);
                level.add(level, new WoodFence(level, new Vector2(xt*16, yt*16), 16, 16));
            }
        }

        return false;
    }


}
