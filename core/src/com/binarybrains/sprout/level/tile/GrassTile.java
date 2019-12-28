package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.item.tool.Hoe;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrassTile extends Tile {

    // temporary
    // keeps the bitwise index to Tileset (pos in image) mapping
    private static final Map<Integer, Integer> dirtAutoTiles;
    static
    {
        dirtAutoTiles = new HashMap<Integer, Integer>();
        dirtAutoTiles.put(0, 1591);
        dirtAutoTiles.put(1, 1591+1);
        dirtAutoTiles.put(2, 1591+2);
        dirtAutoTiles.put(3, 1591+3);
        dirtAutoTiles.put(4, 1621);
        dirtAutoTiles.put(5, 1621+1);
        dirtAutoTiles.put(6, 1621+2);
        dirtAutoTiles.put(7, 1621+3);
        dirtAutoTiles.put(8, 1651);
        dirtAutoTiles.put(9, 1651+1);
        dirtAutoTiles.put(10, 1651+2);
        dirtAutoTiles.put(11, 1651+3);
        dirtAutoTiles.put(12, 1681);
        dirtAutoTiles.put(13, 1681+1);
        dirtAutoTiles.put(14, 1681+2);
        dirtAutoTiles.put(15, 1681+3);
    }

    public GrassTile(int x, int y, Boolean mayPass) {
        super(x, y, mayPass);
    }

    public GrassTile(int x, int y) {
        super(x, y, true);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        super.interact(player, xt, yt, attackDir);
        Item item = player.getActiveItem();
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            // this is too easy, now we can hoe places that are not allowed
            if (toolItem.tool instanceof Hoe && toolItem.tool.canUse() && mayPass) {

                List<Entity> entities = player.getLevel().getEntitiesAtTile(xt, yt);
                entities.remove(player);
                if (entities.size() > 0) return false;

                ((Hoe) toolItem.tool).playDigSound();
                player.getLevel().setTile(xt, yt, new DirtTile(xt, yt));
                player.getLevel().setAutoTile(xt, yt, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitwiseIndex(xt,yt)));

                // auto tile arround the newly placed tile:
                // south
                if (player.getLevel().getTile(xt, yt-1) instanceof DirtTile) {
                    player.getLevel().setAutoTile(xt, yt-1, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitwiseIndex(xt,yt-1)));
                }
                // west
                if (player.getLevel().getTile(xt-1, yt) instanceof DirtTile) {
                    player.getLevel().setAutoTile(xt-1, yt, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitwiseIndex(xt-1,yt)));
                }

                // east
                if (player.getLevel().getTile(xt+1, yt) instanceof DirtTile) {
                    player.getLevel().setAutoTile(xt+1, yt, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitwiseIndex(xt+1,yt)));
                }

                // south
                if (player.getLevel().getTile(xt, yt+1) instanceof DirtTile) {
                    player.getLevel().setAutoTile(xt, yt+1, GrassTile.dirtAutoTiles.get(player.getLevel().getTileBitwiseIndex(xt,yt+1)));
                }

                if (MathUtils.randomBoolean()) {
                    player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resources.coal), new Vector2(xt * 16, yt * 16)));

                }
                return true;
            }
        }
        return false;
    }

}
