package com.binarybrains.sprout.entity.house;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;


public class Cottage extends Entity { // extend House that extends StaticEntity

    private TextureAtlas atlas; // move
    private Sprite sprite;

    public Cottage(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);

        atlas = SproutGame.assets.get("items2.txt");
        sprite = new Sprite(atlas.findRegion("home"));
        sprite.setSize(width, height);
        sprite.setPosition(getX(), getY());


    }

    @Override
    public void updateBoundingBox() {
        super.updateBoundingBox();
        // update the walkBox hit detector for tiles hit detection
        // this must be much easier see also the Tree entity
        this.walkBox.setWidth(getWidth());
        this.walkBox.setHeight(getHeight() - 32);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        // the cottage instance should have info on where in the world
        // the player should teleport to. and info where the exit is.
        // player.getLevel().gameTimer.getGameTime().hour
        player.setTilePos(9, 1.5f);
        // fade out music
        BackgroundMusic.stop();

        return super.interact(player, item, attackDir);
    }


    @Override
    public boolean blocks(Entity e) {
        return true;
    }

    public void draw(Batch batch, float parentAlpha) {

        sprite.draw(batch);
    }


}
