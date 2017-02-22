package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.level.Level;

import java.util.Map;

public class Emma extends Npc {

    public StateMachine<Emma, EmmaState> stateMachine;
    public Map<Long, Direction> findPath;

    public Emma(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height, 3); // 3 is the spriteRow used
        setState(State.WALKING);
        setDirection(Direction.EAST);
        setSpeed(MathUtils.random(16f, 32f));
        stateMachine = new DefaultStateMachine<Emma, EmmaState>(this, EmmaState.WALK_ABOUT);

        findPath = generatePathFindingDirections(52, 89);
    }

    @Override
    public void update(float delta) {

        super.update(delta);
        stateMachine.update();

        //System.out.println("Emma pos:" + getPosHash() + " " + stateMachine.getCurrentState());

        if (findPath.containsKey(getPosHash())) {
            setDirection(findPath.get(getPosHash()));
            setState(State.WALKING);
        } else {
            System.out.println("Could not find any more travelling directions. ");
            setState(State.STANDING);
        }

    }

    @Override
    public void touchedBy(Entity entity) {
        if ((entity instanceof Emma)) return;
    }

    @Override
    public boolean interact(Player player, Item item, Direction attackDir) {

        if (player.activeItem instanceof ResourceItem && ((ResourceItem) player.activeItem).resource.name.equals("Acorn")) {
            stateMachine.changeState(EmmaState.WALK_HOME);
            player.getLevel().screen.hud.speakDialog(
                    String.format("Ohh! I really love this %s %s", item.getName(), item.getDescription())
            );

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public boolean blocks(Entity e) {
        if (e instanceof Emma) return false;
        if (e instanceof Player) {
            return true;
        }
        return false;
    }

    public boolean isThreatened() {
        return false;
    }

    public boolean isSafe() {
        return false;
    }

    public void testAction() {
        // System.out.println(this + " test action called");
    }
}
