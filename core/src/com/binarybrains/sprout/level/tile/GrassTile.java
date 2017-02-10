package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;

public class GrassTile extends Tile {

    public GrassTile() {
        super(true);
    }


    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        System.out.println("TODO implement all ways to interact with grass. Like hoe for example");
        return true;
    }
}
