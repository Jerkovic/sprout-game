package com.binarybrains.sprout.entity.enemy;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.bellsandwhistles.TextParticle;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Mace;
import com.binarybrains.sprout.item.weapon.Weapon;
import com.binarybrains.sprout.level.Level;

public class Slime extends Mob {

    private TextureRegion texture;

    public Slime(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height);
        setHealth(80);
        setSpeed(MathUtils.random(5f, 9f));
        TextureAtlas atlas = SproutGame.assets.get("items2.txt");
        texture = atlas.findRegion("Gem_Node");
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        //speed = MathUtils.random(.13240234f, .97459395395f);

        lookAt(getLevel().player); // determine the slimes looking/walk dir

        float diffX = getLevel().player.getX() - getX();
        float diffY = getLevel().player.getY() - getY();

        float angle = (float)Math.atan2(diffY, diffX);

        float newX = getPosition().x + (float) (getSpeed() * Math.cos(angle));
        float newY = getPosition().y + (float) (getSpeed() * Math.sin(angle));

        // collision basics
        if (canMoveToPos(newX, newY)) {
            getPosition().x += getSpeed() * Math.cos(angle);
            getPosition().y += getSpeed() * Math.sin(angle);
        }    }

    @Override
    public void die() {
        if (!this.removed) {
            SproutGame.playSound("splat");
            // slime should drop something
            super.remove();
        }
    }

    @Override
    public boolean interact(Player player, Item item, Direction attackDir) {
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof Weapon && toolItem.tool.canUse()) {
                SproutGame.playSound("menu_select", 1f, MathUtils.random(0.7f, 1.1f), 1f);
                hurt(player, toolItem.getDamage(), player.getDirection()); // hurt slime
                return true;
            }
        }

        return false;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), 16, 16);
    }


}
