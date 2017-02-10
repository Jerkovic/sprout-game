package com.binarybrains.sprout.entity.tree;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Axe;
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
        sprite = new Sprite(level.spritesheet, 0, 0, (int)width, (int)height);
        sprite.setSize(width, height);
        sprite.setPosition(getX(), getY());

        // move to shadow System?
        shadow = new Sprite(level.spritesheet, 0, 0, (int)width, (int)height);
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
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof Axe) {
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