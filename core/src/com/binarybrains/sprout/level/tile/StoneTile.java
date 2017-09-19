package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Items;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.PickAxe;

public class StoneTile extends Tile {

    public StoneTile(int x, int y) {
        super(x, y, false);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {

        Item item = player.getActiveItem();

        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof PickAxe && toolItem.tool.use(this)) {
                if (MathUtils.random(1,1) == 1) {
                    player.getLevel().add(
                            player.getLevel(),
                            new PickupItem(player.getLevel(), new ResourceItem(Items.stone), new Vector2(xt * 16, yt * 16))
                    );
                    // Some nice ResourceManager.giveLoot(player.skill, activeItem.level, timeOfday, placeOnMap etc)
                }
                player.getLevel().setTile(xt, yt, new GrassTile(xt, yt, true));
                return true;
            }
        }
        return false;
    }

}
