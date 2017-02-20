package com.binarybrains.sprout.level.tile;


import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.WateringCan;

public class DirtTile extends Tile {

    public int waterCounter = 0;

    public DirtTile() {
        super(true);
        super.setTileSetIndex(300);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        Item item = player.getActiveItem();
        if (item instanceof ResourceItem) { // seeds
            // todo seeds and fertilizing
            System.out.println("!Interact with DirtTile with " + item);
            return true;
        } else if (item instanceof ToolItem) { // watering can usage
            ToolItem toolItem = (ToolItem) item;
            // toolItem.level
            System.out.println("!Interact with DirtTile with " + item + " watering: " + waterCounter);
            if (toolItem.tool instanceof WateringCan && toolItem.tool.use()) {
                waterCounter++;
                return true;
            }
        }
        return false;

    }
}
