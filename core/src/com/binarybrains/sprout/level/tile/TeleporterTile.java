package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;


public class TeleporterTile extends Tile {
    public TeleporterTile() {
        super(true);
    }
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        // improve this this is just test
        player.getLevel().screen.hud.adjustInventoryWindow(); // a a test to make it align top
        player.getLevel().screen.hud.teleportPlayer(player, 18,91);

        return false;
    }

}
