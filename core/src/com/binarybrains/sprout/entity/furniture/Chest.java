package com.binarybrains.sprout.entity.furniture;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.*;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

/**
 * A chest is a furniture that can be moved around.
 * It also serves as a inventory space
 */
public class Chest extends Entity implements Portable { // extends Furniture that implements Portable instead?

    TextureRegion[][] frames;
    private boolean isOpen = false;
    private boolean carried = false;
    public Inventory inventory;
    private int capacity = 12;

    private TextureRegion openRegion, closedRegion;

    public Chest(Level level, Vector2 position) {
        super(level, position, 16, 16);
        // the chest is a inventory
        inventory = new Inventory(level, capacity);

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

        if (!carried) {
            isOpen = !isOpen;
            // player.getLevel().hud.setMenu(new ContainerMenu(player, "Chest", inventory));
            // open chest on right click
            // open chest inventory HUD window
            // be able to move stuff from chest inventory <-> player inventory
        }

        return false;
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (!carried) {
            carried = true;
            player.setCarriedItem(this);
            remove();
        }
        return true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // batch.draw(frames[15][0], getX(), getY());
        if (carried) return;

        if (isOpen) {
            batch.draw(openRegion, getX(), getY());
        } else {
            batch.draw(closedRegion, getX(), getY());
        }

    }
}
