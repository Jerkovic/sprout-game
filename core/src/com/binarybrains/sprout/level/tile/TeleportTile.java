package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.misc.AmbienceSound;


public class TeleportTile extends Tile {

    public TeleportTile(int x, int y) {

        super(x, y, true);
    }

    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        // improve this this is just test
        player.getLevel().screen.hud.teleportPlayer(player, 18,91);
        return false;
    }

}
