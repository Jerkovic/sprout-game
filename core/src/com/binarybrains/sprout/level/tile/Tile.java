package com.binarybrains.sprout.level.tile;


import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.bomb.Bomb;
import com.binarybrains.sprout.entity.furniture.Furnace;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.level.Level;


public class Tile {

    private int x, y, tileSetIndex = 0;
    public boolean mayPass = true;
    public boolean isHoldingFence = false;

    // xt and yt also right?
    public Tile(int x, int y, boolean mayPass) {
        this.x = x;
        this.y = y;
        setMayPass(mayPass);
    }

    public boolean isHoldingFence() {
        return isHoldingFence;
    }

    public void setIsHoldingFence(boolean isHoldingFence) {
        this.isHoldingFence = isHoldingFence;
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
            System.out.println(player + " Interact with tile " + this + " using " + item);

            item.interact();

            // Does they implement the interface we need to see if
            // portables instead of getname?
            if (item.getName().equals("Bomb") && mayPass &&  !player.getLevel().isBlockingEntitiesAtTile(player, xt, yt)) {
                // move this into a player method ?
                SproutGame.playSound("bump_against", .6f);

                player.getInventory().removeResource(((ResourceItem) item).resource, 1);
                player.getLevel().screen.hud.refreshInventory();

                player.getLevel().add(player.getLevel(), new Bomb(player.getLevel(), xt, yt));
                return true;
            }

            if (item.getName().equals("Furnace") && mayPass && player.getLevel().getEntitiesAtTile(xt, yt).size() == 0) {
                // move this into a player method ?
                SproutGame.playSound("bump_against", .6f);

                player.getLevel().add(player.getLevel(), new Furnace(player.getLevel(), xt, yt));
                player.getInventory().removeItem(player.getActiveItem());
                player.getLevel().screen.hud.refreshInventory();
                return true;
            }
        }

        return false;
    }

    public String toString()
    {
        return "[" + this.getClass().getSimpleName() +  this.hashCode() + "@Pos: (" + x + "x" + y + ")]";
    }

    public boolean use(Level level, int xt, int yt, Player player, int attackDir) {
        return false;
    }

    public boolean connectsToLiquid() {
        return false;
    }
}
