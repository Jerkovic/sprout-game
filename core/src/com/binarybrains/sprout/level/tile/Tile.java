package com.binarybrains.sprout.level.tile;


import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.level.Level;


public class Tile {

    public boolean mayPass = true;

    public boolean isHoldingFence() {
        return isHoldingFence;
    }

    public void setIsHoldingFence(boolean isHoldingFence) {
        this.isHoldingFence = isHoldingFence;
    }

    public boolean isHoldingFence = false;

    public Tile(boolean mayPass) {
        this.mayPass = mayPass;
    }

    public void setMayPass(boolean mayPass) {
        this.mayPass = mayPass;
    }

    public boolean mayPass(Entity e) {

        return mayPass;
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
        System.out.println(player + " tries to interact with " + this);
        return false;
    }

    public boolean use(Level level, int xt, int yt, Player player, int attackDir) {
        return false;
    }

    public boolean connectsToLiquid() {
        return false;
    }
}
