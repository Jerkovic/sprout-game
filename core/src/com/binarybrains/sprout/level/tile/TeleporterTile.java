package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.bomb.Bomb;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;

/**
 * Created by erikl on 27/08/17.
 */
public class TeleporterTile extends Tile {
    public TeleporterTile() {
        super(true);
    }
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        System.out.println(player + " Interact with TELEPORTER");
        player.getLevel().screen.hud.teleportPlayer(player, 18,91);
        return false;
    }

}
