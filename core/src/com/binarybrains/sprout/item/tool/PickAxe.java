package com.binarybrains.sprout.item.tool;

import com.badlogic.gdx.math.MathUtils;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.level.tile.Tile;

public class PickAxe extends Tool {
    public PickAxe() {

        super("Pickaxe", "A tool used to remove blocks and mine ores.");
        setCoolDownTime(600); // half a second cooldown time should be lower depending on level of the tool right?
    }

    public boolean use(Tile tile) {
        if (canUse()) {
            SproutGame.playSound("swing_03", .7f, MathUtils.random(0.9f, 1.05f), 1f);
            return true;
        }
        return false;
    }
}
