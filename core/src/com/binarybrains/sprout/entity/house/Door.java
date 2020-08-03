package com.binarybrains.sprout.entity.house;

import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;


public class Door extends Entity {

    private int teleport_x = 0;
    private int teleport_y = 0;

    public Door(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height);
    }

    public int getTeleportX() {
        return teleport_x;
    }

    public void setTeleportX(int teleport_x) {
        this.teleport_x = teleport_x;
    }

    public int getTeleportY() {
        return teleport_y;
    }

    public void setTeleportY(int teleport_y) {
        this.teleport_y = teleport_y;
    }

    @Override
    public void updateBoundingBox() {
        super.updateBoundingBox();
        this.walkBox.setWidth(getWidth());
        this.walkBox.setHeight(8);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (player.getInteractBox().overlaps(getBoundingBox())) {
            SproutGame.playSound("door_open", .5f);
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
