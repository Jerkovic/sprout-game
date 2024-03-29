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
import com.binarybrains.sprout.item.tool.FishingPole;
import com.binarybrains.sprout.item.tool.WateringCan;

public class WaterTile extends Tile {


    public WaterTile(int x, int y) {
        super(x, y, false);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {

        Item item = player.getActiveItem();
        // here the player goes in to a fishing state.
        // - player should not be able to hammer the fish button

        if (item instanceof ToolItem && this instanceof WaterTile) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof FishingPole && toolItem.tool.use(this)) {

                SproutGame.playSound("water_splash");

                if (MathUtils.random(1,4) == 1) {
                    player.getLevel().add(
                            player.getLevel(),
                            new PickupItem(player.getLevel(), new ResourceItem(Resources.salmon), new Vector2(xt * 16, yt * 16))
                    );
                    // we should delay sounds like the one below and be able to delay other sorts of actions
                    // Some nice ResourceManager.giveLoot(player.skill, activeItem.level, timeOfday, placeOnMap etc)
                }
                return true;
            }

            if (toolItem.tool instanceof WateringCan && toolItem.tool.use(this)) {
                SproutGame.playSound("water_splash");
                player.getLevel().screen.hud.refreshInventory();
                return true;
            }
        }
        return false;
    }

}
