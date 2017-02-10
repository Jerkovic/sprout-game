package com.binarybrains.sprout.entity.furniture;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.Portable;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

/**
 * A chest is a furniture that can be moved around.
 * It also serves as a inventory space
 */
public class Chest extends Entity implements Portable { // extends Furniture that implements Portable instead?

    TextureRegion[][] frames;
    private boolean isOpen = false;

    private TextureRegion openRegion, closedRegion;

    public Chest(Level level, Vector2 position) {
        super(level, position, 16, 16);

        // this should not be here .. move IT! it is all the tiles
        frames = TextureRegion.split(getLevel().spritesheet, 16, 16);
        closedRegion = frames[8][11];
        openRegion = frames[8][12];
        // something like if activeItem instanceof Portable (like chests)
    }

    @Override
    public void updateBoundingBox() {
        super.updateBoundingBox();
        this.walkBox.setWidth(getWidth());
        this.walkBox.setHeight(getHeight() - 8);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    @Override
    public boolean use(Player player, Mob.Direction attackDir) {
        // open chest on right click
        isOpen = !isOpen;
        // open chest inventory HUD window
        // be able to move stuff from chest inventory <-> player inventory
        return false;
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        return super.interact(player, item, attackDir);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // batch.draw(frames[15][0], getX(), getY());
        if (isOpen) {
            batch.draw(openRegion, getX(), getY());
        } else {
            batch.draw(closedRegion, getX(), getY());
        }

    }
}
