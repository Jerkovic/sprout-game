package com.binarybrains.sprout.entity.house;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;


public class Tower extends Entity { // extend House that extends StaticEntity

    private Sprite sprite;
    private Rectangle door;
    private boolean isLocked = true;

    public Tower(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);

        sprite = new Sprite(level.spritesheet, 0, 25*16, (int)width, (int)height);
        sprite.setSize(width, height);
        sprite.setPosition(getX(), getY());
        sprite.setOrigin(getWidth() / 2, 8);

    }

    /**
     * Only need to set X so far
     * @param x
     */
    public void setDoorTilePos(int x)
    {
        // A door is 16 pixels wide and 32 pixels high
        this.door = new Rectangle(walkBox.getX() + ((x-1) * 16), walkBox.getY(), 16, 32);
    }

    @Override
    public void updateBoundingBox() {
        super.updateBoundingBox();
        this.walkBox.setWidth(getWidth());
        this.walkBox.setHeight(36);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    public void renderDebug(ShapeRenderer renderer, Color walkBoxColor) {
        super.renderDebug(renderer, walkBoxColor);

        // door
        renderer.setColor(Color.YELLOW);
        renderer.rect(door.getX(), door.getY(), door.getWidth(), door.getHeight());
    }


    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {

        if (player.getInteractBox().overlaps(door)  && !isLocked &&  player.getDirection().equals(Mob.Direction.NORTH)) {
            SproutGame.playSound("door_open");
            return true;
        }

        if (player.getInteractBox().overlaps(door) && item.getName().equals("Basic Key") && player.getDirection().equals(Mob.Direction.NORTH)) {
            isLocked = false;
            BackgroundMusic.stop(); // fade out music
            SproutGame.playSound("fancy_reward", 0.34f);
            player.getInventory().removeItem(item);
            player.getLevel().screen.hud.refreshInventory();
            player.getLevel().screen.hud.speakDialog(
                    "Tower",
                    "The key opens the door to the majestic tower."
            );

            return true;
        }

        player.getLevel().screen.hud.speakDialog(
                this.getClass().getSimpleName(),
                String.format("The door to the old tower is locked...")
        );
        // play locked door sfx and show message
        // The door is locked...
        return false;
    }


    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    public void draw(Batch batch, float parentAlpha) {
        sprite.draw(batch);

    }


}
