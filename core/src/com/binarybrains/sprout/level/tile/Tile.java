package com.binarybrains.sprout.level.tile;


import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.bomb.Bomb;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.level.Level;


public class Tile {

    private int tileSetIndex = 0;

    public boolean mayPass = true;

    public boolean isHoldingFence() {
        return isHoldingFence;
    }

    public void setIsHoldingFence(boolean isHoldingFence) {
        this.isHoldingFence = isHoldingFence;
    }

    public boolean isHoldingFence = false;

    // xt and yt also right?
    public Tile(boolean mayPass) {
        this.mayPass = mayPass;
    }

    public void setMayPass(boolean mayPass) {
        this.mayPass = mayPass;
    }

    public boolean mayPass(Entity e) {

        return mayPass;
    }

    // the index in the tileSet to be able render correct tile
    public int getTileSetIndex() {
        return tileSetIndex;
    }

    public void setTileSetIndex(int index) {
        tileSetIndex = index;
    }


    public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
    }

    public void bumpedInto(Level level, int xt, int yt, Entity entity) {
    }

    public void tick(Level level, int xt, int yt) {
    }

    public void steppedOn(Level level, int xt, int yt, Entity entity) {
    }

    public boolean interact(Player player, int xt, int yt, Mob.Direction attackDir) {
        Item item = player.getActiveItem();
        if (item != null) {
            System.out.println(player + " Interact with " + this + " using " + item);

            if (item.getName().equals("Bomb")) {
                // move this into a player method ?
                player.getInventory().removeResource(((ResourceItem) item).resource, 1);
                player.getLevel().screen.hud.refreshInventory();

                player.getLevel().add(player.getLevel(), new Bomb(player.getLevel(), xt, yt));
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        return "[" + this.getClass().getSimpleName() +  this.hashCode() + "@Pos:?]";
    }


    public boolean use(Level level, int xt, int yt, Player player, int attackDir) {
        return false;
    }

    public boolean connectsToLiquid() {
        return false;
    }
}
