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

public class Emma extends Npc {

    public StateMachine<Emma, EmmaState> stateMachine;

    public Emma(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height, 3);
        setState(State.WALKING);
        setSpeed(MathUtils.random(16f, 32f));
        stateMachine = new DefaultStateMachine<Emma, EmmaState>(this, EmmaState.WALK_ABOUT);
    }

    @Override
    public void update(float delta) {

        super.update(delta);
        stateMachine.update();

        float distance = getPosition().dst(getLevel().player.getPosition());

        if (distance < 32f) {
            // fake standing still and listening to our hero

            // Move this to a more generic facingHelper method!
            // this.distance(other)
            // this.face(other)
            float angle = (float) Math.toDegrees(Math.atan2(getLevel().player.getX() - getX(), getLevel().player.getY() - getY()));
            angle = (float) (angle + Math.ceil( -angle / 360 ) * 360);

            String directions[] = {"NORTH", "EAST",  "SOUTH", "WEST"};
            String direction = directions[ Math.round((Math.abs(angle)) / 90) % 4 ];

            if (direction.equals("WEST")) {
                setDirection(Direction.WEST);

            } else if (direction.equals("EAST")) {
                setDirection(Direction.EAST);

            } else if (direction.equals("NORTH")) {
                setDirection(Direction.NORTH);

            } else if (direction.equals("SOUTH")) {
                setDirection(Direction.SOUTH);
            }

            setState(State.STANDING);

        } else if (getState().equals(State.STANDING)) {
            int changeDir = MathUtils.random(Direction.SOUTH.ordinal(), Direction.WEST.ordinal());
            setDirection(Direction.values()[changeDir]); // test same Dir as player
            setState(State.WALKING);
        }
    }

    @Override
    public void touchedBy(Entity entity) {
        if ((entity instanceof Emma)) return;
        // super.touchedBy(entity);
    }

    @Override
    public boolean interact(Player player, Item item, Direction attackDir) {

        if (player.activeItem instanceof ResourceItem && ((ResourceItem) player.activeItem).resource.name.equals("Acorn")) {
            stateMachine.changeState(EmmaState.WALK_HOME);
            player.getLevel().screen.hud.speakDialog(
                    String.format("Ohh! I really love this %s %s", item.getName(), item.getDescription())
            );
            // setActionState(ActionState.CARRYING); // acorn

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
