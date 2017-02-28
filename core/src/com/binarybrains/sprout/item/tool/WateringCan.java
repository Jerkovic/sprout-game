package com.binarybrains.sprout.item.tool;


import com.badlogic.gdx.Gdx;
import com.binarybrains.sprout.level.tile.Tile;
import com.binarybrains.sprout.level.tile.WaterTile;

public class WateringCan extends Tool {

    private int water = 100;

    public WateringCan() {
        super("Watering Can", "A portable water container with a spout, used for watering plants.");
    }


    public boolean use(Tile tile) {
        if (tile instanceof WaterTile) {
            Gdx.app.log("WATERING_CAN", "Filling the watering can");
            water = 100; // fill up water again
        } else {
            water--;
            Gdx.app.log("WATERING_CAN", "Using Wateringcan" + water);

        }
        return true;
    }
}
