package com.binarybrains.sprout.entity.tree;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.PickupItem;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.item.tool.Axe;
import com.binarybrains.sprout.level.Level;


public class Tree extends Entity { // extends Tree  or TerrainItem or Vegetation ?

    private Sprite sprite;
    private Sprite shadow;

    /*
    We need to dispose of this
    sprite.getTexture().dispose();
    shadow.getTexture().dispose();
    spriteBatch.Draw(

    */
    public boolean isProtected = false;
    private int damage = 0;
    private boolean falling = false;
    private boolean flipped = false;
    private int time = 0;
    private boolean isShaking = false;
    private long startShakeTimer;

    public void shake() {
        startShakeTimer = TimeUtils.nanoTime();
        isShaking = true;
    }


    public Tree(Level level, Vector2 position, float width, float height) {

        super(level, position, width, height);
        this.flipped = MathUtils.randomBoolean();

        // the code below is no good - remake this
        sprite = new Sprite(level.spritesheet, 48, 0, (int)width, (int)height);
        sprite.setSize(width, height);
        sprite.setFlip(flipped, false);
        sprite.setPosition(getX(), getY());
        sprite.setOrigin(getWidth() / 2, 8);


        shadow = new Sprite(level.spritesheet, 48, 0, (int)width, (int)height);
        shadow.setColor(Color.BLACK);
        shadow.setAlpha(0.3f);
        shadow.setPosition(getX(), getY());
        shadow.rotate(-21f);
    }

    @Override
    public void update(float delta) {


        if (falling) { // timber!!
            time+=delta * 100;

            if (time >= 70) {
                //remove();
                //falling = false;
            }
        } else { // not falling
            if (isShaking) {
                if (TimeUtils.nanoTime() < startShakeTimer + 1000000000 * .3) {
                    float currentPower = 5 * delta;
                    float x = (MathUtils.random(0f, 1f) - 0.5f) * 2 * currentPower;
                    float y = (MathUtils.random(0f, 1f) - 0.5f) * 2 * currentPower;
                    sprite.translate(-x, -y);
                }
                else
                {
                    isShaking = false;
                }
            }
        }
        super.update(delta);

    }
    @Override
    public void updateBoundingBox() {
        this.box.setWidth(getWidth());
        this.box.setHeight(getHeight());
        this.box.setPosition(getPosition());

        this.walkBox.setWidth(16);
        this.walkBox.setHeight(16);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    @Override
    public boolean blocks(Entity e) {
        // todo trees should not block birds for example
        return true;
    }

    @Override
    public void touchedBy(Entity entity) {
        if (!(entity instanceof Npc)) return;
        if (entity instanceof Player) {
            super.touchedBy(entity);
        }
    }

    public void leftContainTrigger(Entity entity) {
        sprite.setAlpha(1f);
    }

    public void containTrigger(Entity entity) {
        sprite.setAlpha(.55f);
    }


    @Override
    public void hurt(Entity e, int dmg) {

        if (falling) return;
        super.hurt(e, dmg); // just debug
        damage += dmg;
        shake();

        // todo implement tree falling both left and right.

        if (damage > 50 && !falling) {

            falling = true;
            SproutGame.playSound("tree_fall", .40f, .76f, 1f);
            addAction(
                    Actions.sequence(
                        Actions.delay(MathUtils.random(0.0001f, 0.1238f)),
                        Actions.rotateTo(90f, 1.5f, Interpolation.circleIn),
                        Actions.run(new Runnable() {
                            public void run () {
                                remove();
                                getLevel().getCamera().shake();

                                int count = MathUtils.random(3,9);
                                for (int i = 0; i < count; i++) {
                                    getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resources.wood), new Vector2(getPosition().x, getPosition().y)));
                                }

                                count = MathUtils.random(0,1);
                                for (int i = 0; i < count; i++) {
                                    getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resources.acorn), new Vector2(sprite.getX(), sprite.getY())));
                                }

                            }
                        })
            ));
        }
    }

    @Override
    public boolean interact(Player player, Item item, Mob.Direction attackDir) {
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof Axe && toolItem.tool.canUse()) {
                ((Axe) toolItem.tool).playRandomChopSound();
                hurt(player, toolItem.getDamage()); // hurt the tree with the correct damage level
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean use(Player player, Mob.Direction attackDir) {
        shake();
        SproutGame.playSound("leaves_rustling", MathUtils.random(0.5f, 0.8f), MathUtils.random(0.9f, 1.1f), 1f);
        int count = MathUtils.random(0,1);
        for (int i = 0; i < count; i++) {
            getLevel().add(getLevel(), new PickupItem(getLevel(), new ResourceItem(Resources.apple), new Vector2(getCenterPos().x, getCenterPos().y)));
        }
        return true;

    }

    public void draw(Batch batch, float parentAlpha) {
        if (!falling) {
            drawShadow(batch);
        }
        sprite.setRotation(getRotation());
        sprite.draw(batch);
    }

    public void drawShadow(Batch batch){
        shadow.setPosition(sprite.getX() + 15, sprite.getY()+1);
        shadow.draw(batch);
    }

}
