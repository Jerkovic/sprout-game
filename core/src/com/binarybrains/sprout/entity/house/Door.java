package com.binarybrains.sprout.entity.house;

import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.AmbienceSound;
import com.binarybrains.sprout.misc.BackgroundMusic;


public class Door extends Entity {

    private boolean locked = false;
    private int teleport_x = 0;
    private int teleport_y = 0;
    // name?

    public Door(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height);
        this.teleport_x = 1;
        this.teleport_y = 1;
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (player.getInteractBox().overlaps(getBoundingBox()) && player.getDirection().equals(Mob.Direction.NORTH)) {
            SproutGame.playSound("door_open");
            getLevel().screen.hud.teleportPlayer(player, this.teleport_x, this.teleport_y);
            return true;
        }
        return false;
    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }

}
