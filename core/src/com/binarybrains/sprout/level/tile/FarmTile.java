package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.WateringCan;
import com.binarybrains.sprout.level.Level;

/**
 * Created by erikl on 27/02/17.
 *  This will be our farmed land tile
 *  , will probably have some entity connected to it
 *  , will have time sense to know how far it has grown.
 */
public class FarmTile extends Tile {

    public int waterCounter = 0;
    public int fertilized = 0;
    public int someGrowTimeCounter = 0;

    public FarmTile(int x, int y) {
        super(x, y, true);
        super.setTileSetIndex(12); // same as dirtTile for the moment
    }


    @Override
    public void tick(Level level, int xt, int yt) {
        // growing our crops
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        Item item = player.getActiveItem();

        if (item instanceof ToolItem) { // watering can usage
            ToolItem toolItem = (ToolItem) item;
            // todo toolItem.level
            System.out.println("Interact with FarmTile with " + item);
            if (toolItem.tool instanceof WateringCan && toolItem.tool.use(this)) {
                //waterCounter++;
                return true;
            }
        }

        return false;
    }

}
