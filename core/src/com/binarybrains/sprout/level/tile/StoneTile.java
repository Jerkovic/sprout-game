package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.bellsandwhistles.Sparkle;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.item.tool.PickAxe;
// dont make stone as a tile
public class StoneTile extends Tile {

    private int health = 30;

    public StoneTile(int x, int y) {
        super(x, y, false);
        setTileSetIndex(1951);
    }

    @Override
    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {

        Item item = player.getActiveItem();

        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;

            if (toolItem.tool instanceof PickAxe && toolItem.tool.use(this)) {


                health-=toolItem.getDamage();

                if (health < 1) {
                    SproutGame.playSound("break_stone", 0.9f);
                    player.getLevel().getCamera().shake();

                    for (int i = 0; i <  MathUtils.random(2,3); i++) {
                        player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resources.stone), new Vector2(xt * 16, yt * 16)));
                    }

                    if (MathUtils.randomBoolean()) {
                        player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resources.ironOre), new Vector2(xt * 16, yt * 16)));
                    }

                    if (MathUtils.random(1,4) == 1) {
                        player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resources.goldNugget), new Vector2(xt * 16, yt * 16)));

                        if (player.getStats(Resources.goldNugget.name) < 2) {
                            player.getLevel().screen.hud.addToasterMessage("Gold", "You found your first gold nugget! Great job!");

                        }
                    }
                    if (MathUtils.random(1,10) == 1) {
                        player.getLevel().add(player.getLevel(), new PickupItem(player.getLevel(), new ResourceItem(Resources.diamond), new Vector2(xt * 16, yt * 16)));

                    }

                    // reset
                    player.getLevel().setTile(xt, yt, new GrassTile(xt, yt, true));

                } else {
                    SproutGame.playSound("metal_hit", 0.5f, MathUtils.random(0.68f, .78f), 1f);
                }

                return true;
            }
        }
        return false;
    }

}
