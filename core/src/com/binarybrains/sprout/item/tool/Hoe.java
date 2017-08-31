package com.binarybrains.sprout.item.tool;

import com.badlogic.gdx.math.MathUtils;
import com.binarybrains.sprout.SproutGame;

public class Hoe extends Tool {
    public Hoe() {
        super("Hoe", "A long-handled gardening tool with a thin metal blade, used mainly for weeding and breaking up soil.");
        setCoolDownTime(500); // half a second cooldown time
    }

    public void playDigSound() {
        SproutGame.playSound("dirt_digging", .2f, MathUtils.random(0.7f, 1f), 0);
    }
}
