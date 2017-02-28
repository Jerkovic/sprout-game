package com.binarybrains.sprout.level.tile;


import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
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
        if (((ResourceItem) item).resource instanceof PlantableResource) { // seeds
            // todo seeds and fertilizing
            System.out.println("Interact with DirtTile with " + item);
            // make transform to a FarmTile
            player.getLevel().player.getLevel().setTile(xt, yt, new FarmTile());
            return true;
        }
        return false;

    }
}
