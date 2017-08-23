package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.FishingPole;
import com.binarybrains.sprout.item.tool.PickAxe;
import com.binarybrains.sprout.item.tool.WateringCan;

/**
 * Created by erikl on 23/08/17.
 */
public class StoneTile extends Tile {

    public StoneTile() {
        super(false);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {

        Item item = player.getActiveItem();

        if (item instanceof ToolItem && this instanceof StoneTile) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof PickAxe && toolItem.tool.use(this)) {
                if (MathUtils.random(1,1) == 1) {
                    player.getLevel().add(
                            player.getLevel(),
                            new PickupItem(player.getLevel(), new ResourceItem(Resource.stone), new Vector2(xt * 16, yt * 16))
                    );
                    // Some nice ResourceManager.giveLoot(player.skill, activeItem.level, timeOfday, placeOnMap etc)
                }
                player.getLevel().setTile(xt, yt, new GrassTile());
                return true;
            }
        }
        return false;
    }

}
