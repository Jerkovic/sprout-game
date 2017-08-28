package com.binarybrains.sprout.locations;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;

// A Trigger more than a Location
public class Location extends Entity {

    private String name, description;

    // SHACK, MINE, BANK, SALOON, QUARRY
    // HOME, FOREST, OLD WILLS HOUSE, BROKEN BRIDGE
    // The Manor

    public Location(Level level, Vector2 pos, float width, float height, String name, String description) {
        super(level, pos, width, height);
        this.name = name;
        this.description = description;
    }

    public void containTrigger(Entity entity) {
        if (entity instanceof Player) {
            ((Player) entity).releaseKeys();
            BackgroundMusic.stop();
            ((Sound) SproutGame.assets.get("sfx/fancy_reward.wav")).play();
            getLevel().screen.hud.speakDialog(name, description);
        }
    }

}
