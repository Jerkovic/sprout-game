package com.binarybrains.sprout.entity;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;


public class ItemEntity extends Entity {

    public Item item;
    public Image img;
    private Sprite shadow;
    TextureAtlas atlas;

    private int lifeTime;
    private int time = 0;

    public ItemEntity(Level level, Item item, Vector2 position) {
        super(level, position, 16, 16);
        this.item = item;
        // not a good solution
        atlas = SproutGame.assets.get("items2.txt", TextureAtlas.class);
        img = new Image(atlas.findRegion(item.getRegionId()));
        img.setSize(16, 16);
        img.setPosition(position.x, position.y);
        setupShadow();

        lifeTime = 60 * 10 + MathUtils.random(1, 60);

    }

    public void setLifeTime() {

    }

    private void setupShadow() {
        //System.out.println(item.getRegionId());
        shadow = new Sprite(atlas.findRegion(item.getRegionId()));
        shadow.setSize(16,16);
        shadow.setColor(Color.BLACK);
        shadow.setAlpha(0.4f);
        shadow.setPosition(getX(), getY());
    }
    public void drawShadow(Batch batch){
        shadow.setPosition(getX() + 1, getY() + 2);
        shadow.draw(batch);
    }

    public void touchedBy(Entity entity) {
        // if (time > 30) entity.touchItem(this);
        if (entity instanceof Player) {

            if (((Player)entity).getInventory().add(item)) {
                remove();
                // temp sound
                Sound testSfx = SproutGame.assets.get("sfx/blop.wav");
                testSfx.play();
                // item.onTake();
                // do some particleEffect item.onTake()
                // play pickup award sound
            }

        }
    }


    public void updateBoundingBox() {

        super.updateBoundingBox();
        // update the walkBox hit detector for tiles hit detection
        this.walkBox.setWidth(4);
        this.walkBox.setHeight(4);
        this.walkBox.setPosition(getCenterPos().x - 2, getCenterPos().y-2);
    }

    @Override
    public void update(float deltaTime) {
        //super.update(deltaTime);
        time++;
        if (time >= lifeTime) {
            remove();
            return;
        }

    }

    public void take(Player player) {
        item.onTake(this);
        remove();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (time >= lifeTime - (6 * 20)) {
            if (time / 6 % 2 == 0) return;
        }
        drawShadow(batch);
        img.draw(batch, parentAlpha);
    }
}
