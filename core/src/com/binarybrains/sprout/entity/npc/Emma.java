package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Axe;
import com.binarybrains.sprout.level.Level;

import java.util.Map;

public class Emma extends Npc {

    public StateMachine<Emma, EmmaState> stateMachine;
    public Map<Long, Direction> findPath;

    public Emma(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height, 3); // 3 is the spriteRow used

        setState(State.WALKING);
        setDirection(Direction.EAST);
        setSpeed(32f);
        stateMachine = new DefaultStateMachine<Emma, EmmaState>(this, EmmaState.IDLE);
        stateMachine.changeState(EmmaState.WALK_LABYRINTH);

    }

    public void updateWalkDirections(int x, int y) {
        clearFindPath();
        findPath = generatePathFindingDirections(generatePath(x, y));
        // printMap(findPath);
    }

    // temp method
    public void leaveHouse() {
        setTilePos(18,91); // outside the house test
        SproutGame.playSound("door_close1");
    }

    /**
     * Use this to clear this Mob's path finding route
     */
    public void clearFindPath() {
        findPath = null;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (findPath != null && findPath.containsKey(getPosHash())) {
            setDirection(findPath.get(getPosHash()));
            // System.out.println("go " + getDirection() + " " + getPosHash());
            setState(State.WALKING);
            //findPath.remove(getPosHash());
        } else {
            //System.out.println("Emma lost?");
        }


        stateMachine.update();
    }

    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }

    @Override
    public void updateBoundingBox() {
        this.box.setWidth(getWidth());
        this.box.setHeight(getHeight());
        this.box.setPosition(getPosition());

        this.walkBox.setWidth(12);
        this.walkBox.setHeight(6);
        this.walkBox.setPosition(getCenterPos().x - (walkBox.getWidth() / 2), getPosition().y);
    }

    @Override
    public void renderDebug(ShapeRenderer renderer, Color walkBoxColor) {
        super.renderDebug(renderer, walkBoxColor);
    }

    @Override
    public void touchedBy(Entity entity) {
        if ((entity instanceof Emma)) return;
    }

    @Override
    public boolean interact(Player player, Item item, Direction attackDir) {
        if (item instanceof ToolItem) {
            ToolItem toolItem = (ToolItem) item;
            if (toolItem.tool instanceof Axe && toolItem.tool.canUse()) {
                ((Axe) toolItem.tool).playRandomChopSound();
                hurt(player, toolItem.getDamage(), player.getDirection()); // hurt emma
                return true;
            }
        }

        if (player.activeItem instanceof ArtifactItem && ((ArtifactItem) player.activeItem).getName().equals("Teddy")) {
            stateMachine.changeState(EmmaState.WALK_HOME);
            player.getLevel().screen.hud.speakDialog(
                    this.getClass().getSimpleName(),
                    String.format("Ohh! Is that %s? I have really missed him! Thank you!", item.getName())
            );
            ArtifactItem ai = (ArtifactItem) player.activeItem;
            player.getInventory().removeItem(ai);
            player.getLevel().screen.hud.refreshInventory();

            return true;
        }

        return false;
    }

    @Override
    public boolean blocks(Entity e) {
        if (e instanceof Emma) return false;
        if (e instanceof Player) {
            return false; // true
        }
        return false;
    }

    @Override
    public String toString()
    {
        return super.toString() + " StateMachine-> " + stateMachine.getCurrentState();
    }


}
