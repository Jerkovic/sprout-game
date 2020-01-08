package com.binarybrains.sprout.locations;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;


public class Bridge extends Location {

    public Bridge(Level level, Vector2 pos, float width, float height, String name, String description) {
        super(level, pos, width, height, name, description);
    }

}
