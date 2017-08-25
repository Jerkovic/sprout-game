package com.binarybrains.sprout.entity.tree;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.*;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.item.tool.Axe;


public class Tree extends Entity { // extends Vegitation or ?

    private Sprite sprite;
    private Sprite shadow;

    /*
    We need to dispose of this
    sprite.getTexture().dispose();
    shadow.getTexture().dispose();
    */

    private int damage = 0;
    private boolean falling = false;

    private int time = 0;


    @Override
    public void update(float delta) {

        if (falling) {
            time+=2;
            if (time < 60) {
                sprite.setRotation(time);
            }

            if (time >= 60) {
                remove();
                falling = false;
            }
        }
        super.update(delta);

    }
    @Override
    public void updateBoundingBox() {
        this.box.setWidth(getWidth());
        this.box.setHeight(getHeight());
        // Sets the x and y-coordinates of the bottom left corner
        this.box.setPosition(getPosition());

        this.walkBox.setWidth(16);
        this.walkBox.setHeight(16);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    public Tree(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);

        // the code below is no good - remake this
        sprite = new Sprite(level.spritesheet, 48, 0, (int)width, (int)height);
        sprite.setSize(width, height);
        sprite.setPosition(getX(), getY());
        sprite.setOrigin(getWidth() / 2, 8);


        shadow = new Sprite(level.spritesheet, 48, 0, (int)width, (int)height);
        shadow.setColor(Color.BLACK);
        shadow.setAlpha(0.4f);
        shadow.setPosition(getX(), getY());
        shadow.rotate(-21f);
    }


    @Override
    public boolean blocks(Entity e) {
        // trees should not block birds for example todo check
        return true;
    }

    @Override
    public void touchedBy(Entity entity) {
        if (!(entity instanceof Npc)) return;
        if (entity instanceof Player) {
            // sprite.setAlpha(.5f); // if player is behind a tree make the tree transparent
            super.touchedBy(entity);
        }
    }

    @Override
    public void hurt(Entity e, int dmg) {
        damage += dmg;

        if (damage > 10) {
            // falling = true;
            remove();
            // add some loot just as a test
            int count = MathUtils.random(1,9);
            for (int i = 0; i < count; i++) {
                getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resource.wood), new Vector2(getPosition().x, getPosition().y)));
            }

            count = MathUtils.random(0,2);
            for (int i = 0; i < count; i++) {
                getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resource.acorn), new Vector2(getPosition().x, getPosition().y)));
            }

        }
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof Axe) {
                //toolItem.tool.use(); send tile
                hurt(player, 1);
                return true;
            }
        }
        return false;
    }

    public void draw(Batch batch, float parentAlpha) {

        drawShadow(batch);
        sprite.draw(batch);

    }

    public void drawShadow(Batch batch){
        shadow.setPosition(sprite.getX() + 10, sprite.getY()+1);
        shadow.draw(batch);
    }


}
