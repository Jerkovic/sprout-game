package com.binarybrains.sprout.entity.tree;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.level.Level;


public class SmallTree extends Entity { // extends Vegitation or ?


    private Sprite sprite;
    private Sprite shadow;

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    public SmallTree(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);

        // the code below is no good - remake this
        sprite = new Sprite(level.spritesheet, 96, 0, (int)width, (int)height);
        sprite.setSize(width, height);
        sprite.setPosition(getX(), getY());

        // move to shadow System?
        shadow = new Sprite(level.spritesheet, 96, 0, (int)width, (int)height);
        shadow.setColor(Color.BLACK);
        shadow.setAlpha(0.4f);
        shadow.setPosition(getX(), getY());
        shadow.rotate(-21f);
    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (item != null) {
            if (item.getName().equals("Ladder")) {
                // move this into a player method ?
                SproutGame.playSound("fancy_reward");
                player.getInventory().removeResource(((ResourceItem) item).resource, 1);
                player.getLevel().screen.hud.refreshInventory();
                player.getLevel().screen.hud.speakDialog(
                        "The secret tree house",
                        "The ladder is perfect! You climb the secret tree and up there is a tree house."
                );
                return true;
            } else {
                super.interact(player, item, player.getDirection());
            }
        }
        return false;
    }

    public void draw(Batch batch, float parentAlpha) {
        drawShadow(batch);
        sprite.draw(batch);
    }

    public void drawShadow(Batch batch){
        shadow.setPosition(sprite.getX() + 15, sprite.getY()+1);
        shadow.draw(batch);
    }


}