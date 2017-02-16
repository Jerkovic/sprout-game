package com.binarybrains.sprout.item.tool;


public class FishingPole extends Tool {
    public int bate = 5; // should this be some sort of smart attachment?
    public FishingPole() {
        super("Fishing Pole", "A long, tapering rod to which a fishing line is attached.");
    }

    public boolean use() {
        if (bate < 1) {
            return false;
        }
        bate--;
        return true;

    }

}
