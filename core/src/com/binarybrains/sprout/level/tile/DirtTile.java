package com.binarybrains.sprout.level.tile;


import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.crop.Crop;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.PlantableResource;
import com.binarybrains.sprout.item.resource.SeedResource;
import com.binarybrains.sprout.item.tool.WateringCan;

public class DirtTile extends Tile {

    public boolean hasCrops = false;
    public int waterLevel = 0;

    public DirtTile() {
        super(true);
        super.setTileSetIndex(17);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        Item item = player.getActiveItem();

        if (item instanceof ToolItem) { // watering can usage
            ToolItem toolItem = (ToolItem) item;

            if (toolItem.tool instanceof WateringCan) {
                //waterCounter++;
                System.out.println("Watering " + player.getLevel().getEntitiesAtTile(xt, yt));
                return true;
            }
        }


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
