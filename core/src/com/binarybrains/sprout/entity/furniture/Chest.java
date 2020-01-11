package com.binarybrains.sprout.entity.furniture;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.*;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.artifact.Artifacts;
import com.binarybrains.sprout.level.Level;

/**
 * A chest is a furniture that can be moved around.
 * It also serves as a inventory container space
 * But some chests are only for looting not storing?
 */
public class Chest extends Entity implements Portable { // extends Furniture that implements Portable instead?

    TextureRegion[][] frames;
    private boolean isOpen = false;
    private Inventory container; // container
    private boolean carried = false;
    private TextureRegion openRegion, closedRegion;

    public Chest(Level level, Vector2 position) {
        super(level, position, 16, 16);

        int capacity = 24;
        container = new Inventory(capacity); // the chest is an inventory
        container.add(new ArtifactItem(Artifacts.teddy));

        frames = TextureRegion.split(getLevel().spritesheet, 16, 16);
        closedRegion = frames[48][23];
        openRegion = frames[48][24];
    }

    public Inventory getInventory() {
        return container;
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
            if (isOpen) {
                SproutGame.playSound("open_chest", .4f);
                player.getLevel().screen.hud.showChestManagementWindow(this);
            } else {
                SproutGame.playSound("close_chest", .5f);
            }
        }
        return true;
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (!carried) {
            //player.setCarriedItem(this);
            //remove();
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
