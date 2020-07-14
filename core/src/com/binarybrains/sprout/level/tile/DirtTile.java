package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.crop.Crop;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.resource.SeedResource;

public class DirtTile extends Tile {

    public boolean hasCrops = false;

    public DirtTile(int x, int y) {
        super(x, y, true);
        super.setTileSetIndex(17);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        Item item = player.getActiveItem();

        if (item instanceof ResourceItem) {

            if (((ResourceItem) item).resource instanceof SeedResource && !hasCrops) {
                // todo fertilizing

                player.getInventory().removeResource(((ResourceItem) item).resource, 1);
                player.getLevel().screen.hud.refreshInventory();

                // add our test crop potato, it sure grows fast
                player.getLevel().add(player.getLevel(), new Crop(player.getLevel(), xt, yt));

                hasCrops = true;
                return true;
            }
        }
        return false;
    }
}
