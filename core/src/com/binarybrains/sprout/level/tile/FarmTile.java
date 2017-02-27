package com.binarybrains.sprout.level.tile;

import com.binarybrains.sprout.level.Level;

/**
 * Created by erikl on 27/02/17.
 *  This will be our farmed land tile
 *  , will probably have some entity connected to it
 *  , will have time sense to know how far it has grown.
 */
public class FarmTile extends Tile{

    public FarmTile() {
        super(true);
    }


    @Override
    public void tick(Level level, int xt, int yt) {
        // growing our crops
    }
}
