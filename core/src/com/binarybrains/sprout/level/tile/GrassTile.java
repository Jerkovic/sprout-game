package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.Hoe;

import java.util.HashMap;
import java.util.Map;

public class GrassTile extends Tile {

    // temporary
    // keeps the bitwise index to Tileset (pos in image) mapping
    private static final Map<Integer, Integer> dirtAutoTiles;
    static
    {
        dirtAutoTiles = new HashMap<Integer, Integer>();
        dirtAutoTiles.put(0, 1326);
        dirtAutoTiles.put(1, 1326+1);
        dirtAutoTiles.put(2, 1326+2);
        dirtAutoTiles.put(3, 1326+3);
        dirtAutoTiles.put(4, 1351);
        dirtAutoTiles.put(5, 1351+1);
        dirtAutoTiles.put(6, 1351+2);
        dirtAutoTiles.put(7, 1351+3);
        dirtAutoTiles.put(8, 1376);
        dirtAutoTiles.put(9, 1376+1);
        dirtAutoTiles.put(10, 1376+2);
        dirtAutoTiles.put(11, 1376+3);
        dirtAutoTiles.put(12, 1401);
        dirtAutoTiles.put(13, 1401+1);
        dirtAutoTiles.put(14, 1401+2);
        dirtAutoTiles.put(15, 1401+3);
    }

    public GrassTile() {
        super(true);
    }


    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        super.interact(player, xt, yt, attackDir);
        Item item = player.getActiveItem();
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof Hoe && toolItem.tool.use(this)) {
                // Here we are handling autotiling test
                System.out.println("Tileindex; " + player.getLevel().getTileBitmaskIndex(xt,yt));
                player.getLevel().setTile(xt, yt, new DirtTile());
                player.getLevel().setAutoTile(xt, yt, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitmaskIndex(xt,yt)));
                // auto tile arround the newly placed tile:
                // south
                if (player.getLevel().getTile(xt, yt-1) instanceof DirtTile) {
                    player.getLevel().setAutoTile(xt, yt-1, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitmaskIndex(xt,yt-1)));
                }
                // west
                if (player.getLevel().getTile(xt-1, yt) instanceof DirtTile) {
                    player.getLevel().setAutoTile(xt-1, yt, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitmaskIndex(xt-1,yt)));
                }

                // east
                if (player.getLevel().getTile(xt+1, yt) instanceof DirtTile) {
                    player.getLevel().setAutoTile(xt+1, yt, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitmaskIndex(xt+1,yt)));
                }

                // south
                if (player.getLevel().getTile(xt, yt+1) instanceof DirtTile) {
                    player.getLevel().setAutoTile(xt, yt+1, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitmaskIndex(xt,yt+1)));
                }


                if (MathUtils.random(1,2) == 1) {
                    player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resource.coal), new Vector2(xt * 16, yt * 16)));

                }
                return true;
            }
        }


        return false;
    }

}
