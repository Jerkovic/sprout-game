package com.binarybrains.sprout.locations;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;

// A Trigger point, location
public class Location extends Entity {

    public String name, description;

    public Location(Level level, Vector2 pos, float width, float height, String name, String description) {
        super(level, pos, width, height);
        this.name = name;
        this.description = description;
        this.setNonInteractable(); // flag the location so that it doesn't blocks Player interactions.
        // do we need something here that tells if the location is inside or outside?
    }

    @Override
    public void containTrigger(Entity entity) {
        if (entity instanceof Player) {
            MessageManager.getInstance().dispatchMessage(TelegramType.PLAYER_LOCATION_REACHED, this);
        }
    }

    @Override
    public void leftContainTrigger(Entity entity) {
        if (entity instanceof Player) {
            MessageManager.getInstance().dispatchMessage(TelegramType.PLAYER_LOCATION_LEAVES, this);
        }
    }

}
