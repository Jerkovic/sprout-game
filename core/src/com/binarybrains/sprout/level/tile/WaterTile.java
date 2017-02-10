package com.binarybrains.sprout.level.tile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.FoodResource;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.tool.FishingPole;
import com.binarybrains.sprout.level.Level;

public class WaterTile extends Tile {


    public WaterTile() {
        super(false);
    }

    @Override
    public boolean interact(Level level, int xt, int yt, Entity entity) {

        Item item = entity.getActiveItem();

        if (item instanceof ToolItem && this instanceof WaterTile) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof FishingPole) {
                Gdx.app.log("DEBUG", "Fishing in the " + this + " by " + entity);
                Sound testSfx = SproutGame.assets.get("sfx/water_splash.wav");
                testSfx.play();
                if (MathUtils.random(1,4) == 1) {
                    level.player.getInventory().add(new ResourceItem(Resource.salmon, 1));
                    // we should delay sounds like the one below and be able to delay other sorts of actions
                    Sound testSfx2 = SproutGame.assets.get("sfx/powerup.wav");
                    testSfx2.play();
                    // Some nice ResourceManager.giveLoot(player.skill, activeItem.level, timeOfday, placeOnMap etc)
                };
                return true;
            }
        }
        return false;
    }

}
