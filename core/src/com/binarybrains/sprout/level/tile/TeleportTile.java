package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;


public class TeleportTile extends Tile {

    public TeleportTile(int x, int y) {

        super(x, y, true);
    }

    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        // improve this this is just test
        //player.getLevel().screen.hud.inventoryTop(); // a a test to make it align top
        player.getLevel().screen.hud.teleportPlayer(player, 18,91);
        // player.getLevel().screen.resumeAmbience(); // just temporary

        return false;
    }

}
