package com.binarybrains.sprout.item.tool;

import com.badlogic.gdx.math.MathUtils;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.level.tile.Tile;

public class Axe extends Tool {
    public Axe() {
        super("Axe", "A tool typically used for chopping wood, \nusually a blade attached at a right angle to a wooden handle.");
        setCoolDownTime(500); // half a second cooldown time
    }

    public void playRandomChopSound() {
        if (MathUtils.randomBoolean())
            SproutGame.playSound("chopping_Wood_1");
        else
            SproutGame.playSound("chopping_Wood_2");
    }

    @Override
    public boolean use(Tile tile) {
        return true;
    }
}
