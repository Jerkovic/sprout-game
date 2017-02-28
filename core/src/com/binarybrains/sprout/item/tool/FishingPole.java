package com.binarybrains.sprout.item.tool;


import com.binarybrains.sprout.item.Attachable;
import com.binarybrains.sprout.level.tile.Tile;

public class FishingPole extends Tool implements Attachable {
    public int bate = 50000; // should this be some sort of smart attachment?

    public FishingPole() {

        super("Fishing Pole", "A long, tapering rod to which a fishing line is attached.");
    }

    @Override
    public boolean use(Tile tile) {
        if (bate < 1) {
            return false;
        }
        bate--;
        return true;

    }

}
