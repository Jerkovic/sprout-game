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
    public Inventory inventory;
    private int capacity = 12;
    public boolean carried = false;
    private TextureRegion openRegion, closedRegion;

    public Chest(Level level, Vector2 position) {
        super(level, position, 16, 16);
        // the chest is a inventory
        inventory = new Inventory(level, capacity);

        frames = TextureRegion.split(getLevel().spritesheet, 16, 16);
        closedRegion = frames[48][23];
        openRegion = frames[48][24];
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

        return true;
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (!carried) {
            setCarried();
            player.setCarriedItem(this);
            remove();
        } else {
            System.out.println("The chest seems to be in carried state already");

        }
        return true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isOpen) {
            batch.draw(openRegion, getX(), getY());
        } else {
            batch.draw(closedRegion, getX(), getY());
        }
    }

    @Override
    public void setCarried() {
        carried = true;
    }

    @Override
    public void deleteCarried() {
        carried = false;
    }
}
