package com.binarybrains.sprout.item.tool;

import com.badlogic.gdx.audio.Sound;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.level.tile.Tile;

public class Axe extends Tool {
    public Axe() {
        super("Axe", "A tool typically used for chopping wood, \nusually a steel blade attached at a right angle to a wooden handle.");
    }

    @Override
    public boolean use(Tile tile) {
        Sound testSfx = SproutGame.assets.get("sfx/chop_wood2_converted.wav");
        testSfx.play();
        // SproutGame.playSound("seeds");
        return true;
    }
}
