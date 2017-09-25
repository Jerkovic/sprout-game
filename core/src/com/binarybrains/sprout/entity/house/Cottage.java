package com.binarybrains.sprout.entity.house;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;


public class Cottage extends Entity { // extend House that extends StaticEntity

    private Sprite sprite;
    private Rectangle door;
    private boolean isRepaired = false;

    public Cottage(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);

        // 25 tiles bred och 79 hög
        sprite = new Sprite(level.spritesheet, 20*16, 71*16, (int)width, (int)height);
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
        this.walkBox.setHeight(getHeight() / 2);
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
        if (player.getInteractBox().overlaps(door) && player.getDirection().equals(Mob.Direction.NORTH)) {
            SproutGame.playSound("door_open");
            getLevel().screen.hud.teleportPlayer(player, 4, 2);
            BackgroundMusic.stop(); // fade out music
            getLevel().screen.pauseAmbience();
            return true;
        }

        if (isRepaired) return false;

        if (item.getName().equals("Wood") && !player.getInventory().hasResources(Resources.wood, 500)) {
            getLevel().screen.hud.speakDialog("Repair house", "You need 500 wood to complete house repair. You currently have " + player.getInventory().count(item) + " wood. Chop chop!");
        } else if (item.getName().equals("Wood") && player.getInventory().hasResources(Resources.wood, 500)) {
            getLevel().screen.hud.addToasterMessage("House fixed", "Congratulations! Your house is repaired.");
            SproutGame.playSound("fancy_reward", 0.34f);
            player.getInventory().removeResource(Resources.wood, 500);
            player.getLevel().screen.hud.refreshInventory();
            // give player something
            // change house sprite
            sprite.setRegion(25*16, 71*16, (int)getWidth(), (int)getHeight());

            isRepaired = true;
        }
        return false;
    }


    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    public void draw(Batch batch, float parentAlpha) {
        // change sprite if isRepaired
        if (isRepaired) {

        }
        sprite.draw(batch);

    }


}
