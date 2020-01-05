package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Timer;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Mace;
import com.binarybrains.sprout.level.Level;

import java.util.Map;

public class Emma extends Npc {

    public StateMachine<Emma, EmmaState> stateMachine;
    public Map<Long, Direction> findPath;

    public Emma(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height, 3); // 3 is the spriteRow used

        setHealth(100);
        setState(State.STANDING);
        setDirection(Direction.EAST);
        setSpeed(24);

        stateMachine = new DefaultStateMachine<>(this, EmmaState.IDLE);
        stateMachine.changeState(EmmaState.WALK_LABYRINTH);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        System.out.println("Emma knows about" + msg);
        return true;
    }

    /**
     *
     * @param secondsDelay
     * @param state
     */
    public void changeStateDelayed(int secondsDelay, EmmaState state) {
        System.out.println("Scheduled state change in" + secondsDelay);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                System.out.println("Run scheduled state change");
                stateMachine.changeState(state);
            }
        }, secondsDelay);
    }

    public void updateWalkDirections(int x, int y) {
        clearFindPath();
        IntArray rawPath = generatePath(x, y);
        findPath = generatePathFindingDirections(rawPath);
    }

    // temp method
    public void leaveHouse() {
        setTilePos(18,91); // outside the house test
        SproutGame.playSound("door_close1", 0.4f);
    }

    /**
     * Use this to clear this Mob's path finding route
     */
    public void clearFindPath() {
        findPath = null;
    }

    public int getWBTileY() {
        return (int)(getPosition().y + 8f) >> 4;
    }

    public int getWBTileX() {
        return (int)(getPosition().x + 8f) >> 4;
    }
    @Override
    public void update(float delta) {
        super.update(delta);
        stateMachine.update();
        if (getLevel().getTileBounds(getWBTileX(), getWBTileY()).contains(getAiBox())) {
            long hash = getWBTileX() + (getWBTileY() * 256); // grid[x + y * width]
            if (findPath != null && findPath.containsKey(hash)) {
                setDirection(findPath.get(hash));
                setState(State.WALKING);
            }
            else {
                setState(State.STANDING);
            }
        }


    }

    @Override
    public void renderDebug(ShapeRenderer renderer, Color walkBoxColor) {
        super.renderDebug(renderer, walkBoxColor);

        renderer.setColor(Color.YELLOW); // AI box
        renderer.rect(getAiBox().getX(),
                getAiBox().getY(),
                getAiBox().getWidth(),
                getAiBox().getHeight());

    }

    @Override
    public void touchedBy(Entity entity) {
        if ((entity instanceof Emma)) return;
    }

    @Override
    public boolean interact(Player player, Item item, Direction attackDir) {

        if (player.activeItem instanceof ArtifactItem && player.activeItem.getName().equals("Teddy")) {

            player.getLevel().screen.hud.speakDialog(
                    this.getClass().getSimpleName(),
                    String.format("Ohh! Is that my long lost %s? I have really missed him! Thank you!", item.getName())
            );

            ArtifactItem ai = (ArtifactItem) player.activeItem;
            player.getInventory().removeItem(ai);
            player.getLevel().screen.hud.refreshInventory();

            stateMachine.changeState(EmmaState.GOTO_TREE);
            return true;
        } else {
            player.getLevel().screen.hud.speakDialog(
                    this.getClass().getSimpleName(),
                    String.format("Scram Creep!")
            );

        }

        return false;
    }

    @Override
    public boolean blocks(Entity e) {
        if (e instanceof Emma) return false;
        if (e instanceof Player) {
            return false;
        }
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + " StateMachine-> " + stateMachine.getCurrentState();
    }

}
