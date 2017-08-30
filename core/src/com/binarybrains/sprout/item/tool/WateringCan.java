package com.binarybrains.sprout.item.tool;


import com.badlogic.gdx.Gdx;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.level.tile.Tile;
import com.binarybrains.sprout.level.tile.WaterTile;

public class WateringCan extends Tool {

    private int water = 100;

    public WateringCan() {
        super("Watering Can", "A portable water container used for watering plants.");
    }

    public int pour() {
        if (water > 0) {
            SproutGame.playSound("watering");
            water = water - 5;
            return 5;
        } else {
            Gdx.app.log("WATERING_CAN", "Empty is the can :( Fill it again");
        }
        return 0;
    }

    public int getWater() {
        return water;
    }

    public void fill(int value) {
        water += value;
        if (water > 100) {
            water = 100;
        }
        Gdx.app.log("WATERING_CAN", "Filling can to: " +  water);
    }

    @Override
    public boolean use(Tile tile) {
        if (tile instanceof WaterTile) {
            fill(10);
            return true;
        }
        return false;
    }
}
