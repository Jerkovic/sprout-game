package com.binarybrains.sprout.locations;

import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;

// A Trigger point, location
abstract class Location extends Entity implements Trigger {

    public String name, description;

    public Location(Level level, Vector2 pos, float width, float height, String name, String description) {
        super(level, pos, width, height);
        this.name = name;
        this.description = description;
    }


}
