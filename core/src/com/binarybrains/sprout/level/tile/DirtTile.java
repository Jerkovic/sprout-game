package com.binarybrains.sprout.level.tile;


import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.crop.Crop;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.PlantableResource;
import com.binarybrains.sprout.item.tool.WateringCan;

public class DirtTile extends Tile {

    public DirtTile() {
        super(true);
        super.setTileSetIndex(300);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        Item item = player.getActiveItem();
        if (item instanceof ResourceItem) {

            if (((ResourceItem) item).resource instanceof PlantableResource) { // seeds
                // todo seeds and fertilizing
                // deduct from player inventory
                // move this into a player method ?
                player.getInventory().removeResource(((ResourceItem) item).resource, 1);
                player.getLevel().screen.hud.refreshInventory();

                // BUG. Inventory UI resource counter is not updated
                System.out.println("Interact with DirtTile with " + item);
                // make transform to a FarmTile
                player.getLevel().setTile(xt, yt, new FarmTile());
                // add our test crop potato, it sure grows fast
                player.getLevel().add(player.getLevel(), new Crop(player.getLevel(), xt, yt));
                return true;
            }
        }
        return false;
    }
}
