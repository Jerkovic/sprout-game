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
        dirtAutoTiles.put(0, 17);
        dirtAutoTiles.put(1, 17+1);
        dirtAutoTiles.put(2, 17+2);
        dirtAutoTiles.put(3, 17+3);
        dirtAutoTiles.put(4, 49);
        dirtAutoTiles.put(5, 49+1);
        dirtAutoTiles.put(6, 49+2);
        dirtAutoTiles.put(7, 49+3);
        dirtAutoTiles.put(8, 81);
        dirtAutoTiles.put(9, 81+1);
        dirtAutoTiles.put(10, 81+2);
        dirtAutoTiles.put(11, 81+3);
        dirtAutoTiles.put(12, 113);
        dirtAutoTiles.put(13, 113+1);
        dirtAutoTiles.put(14, 113+2);
        dirtAutoTiles.put(15, 113+3);
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
                if (MathUtils.random(1,2) == 1) {
                    player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resource.coal), new Vector2(xt * 16, yt * 16)));
                    // Here we have to handle bitmasking
                    System.out.println("Tileindex; " + player.getLevel().getTileBitmaskIndex(xt,yt));
                    player.getLevel().setTile(xt, yt, new DirtTile());
                    player.getLevel().setAutoTile(xt, yt, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitmaskIndex(xt,yt)));

                }
                return true;
            }
        }


        return false;
    }

}
