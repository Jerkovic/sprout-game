package com.binarybrains.sprout.entity;

import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

/**
 * A simple class to be able to make Rare Item pickups / and upgrades stand out a bit more.
 * The player holds it over the head
 */
public class TemporaryCarriedItem extends ItemEntity implements Portable {

    public boolean carried = false;
    public static float OFFSET_PLAYER_Y = 12f;

    public TemporaryCarriedItem(Level level, Item item) {
        super(level, item, new Vector2(level.player.getPosition().x,level.player.getPosition().y + TemporaryCarriedItem.OFFSET_PLAYER_Y));
    }

    @Override
    public void setCarried() {
        carried = true;
    }

    @Override
    public void deleteCarried() {
        carried = false;
    }
}
