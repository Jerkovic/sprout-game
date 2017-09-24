package com.binarybrains.sprout.level.tile;

// used temp for cave wall generation
public class WallTile extends Tile {

    public WallTile(int x, int y) {
        super(x, y, false);
        super.setTileSetIndex(1326); // temp
    }
}
