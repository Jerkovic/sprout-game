package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resources;
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

                SproutGame.playSound("break_stone", 0.45f);

                for (int i = 0; i <  MathUtils.random(2,3); i++) {
                    player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resources.stone), new Vector2(xt * 16, yt * 16)));
                }

                // player.getLevel().setTile(xt, yt, new GrassTile(xt, yt, true));
                return true;
            }
        }
        return false;
    }

}
