package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;

public class StairsTile extends Tile {

    public StairsTile(int x, int y) {
        super(x, y, false);
        setTileSetIndex(2342);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        // leave caves
        player.getLevel().screen.hud.teleportPlayer(player, 20, 120);
        return true;
    }

}
