package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.Hoe;

public class GrassTile extends Tile {

    public GrassTile() {
        super(true);
    }


    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        Item item = player.getActiveItem();
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof Hoe && toolItem.tool.use()) {
                if (MathUtils.random(1,2) == 1) {
                    player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resource.coal), new Vector2(xt * 16, yt * 16)));
                    player.getLevel().player.getLevel().setTile(xt, yt, new DirtTile());
                }
                return true;
            }
        }
        return false;
    }

}
