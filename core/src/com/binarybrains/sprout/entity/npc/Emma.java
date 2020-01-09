package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.Timer;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.entity.actions.SequenceAction;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;

import java.util.List;

public class Emma extends Npc {

    public StateMachine<Emma, EmmaState> stateMachine;
    public List<PointDirection> findPath;
    private Texture spriteSheet;

    public Emma(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height, 3); // 3 is the spriteRow used
        setHealth(100);
        setState(State.STANDING);
        setDirection(Direction.EAST);
        setSpeed(24);

        stateMachine = new DefaultStateMachine<>(this, EmmaState.IDLE);
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

    public void updateWalkDirections(int x, int y, EmmaState state) {
        this.clearActions();
        clearFindPath();
        IntArray rawPath = generatePath(x, y);
        findPath = generatePathFindingDirections2(rawPath);

        SequenceAction seq = new SequenceAction();

        for (int i = 0; i < findPath.size(); i++) {
            PointDirection pd = findPath.get(i);
            Rectangle temp = getLevel().getTileBounds(pd.x, pd.y);
            seq.addAction(Actions.moveTo(temp.x, temp.y, .5f, Interpolation.linear));
            seq.addAction(Actions.run((new Runnable() {
                        public void run () {
                            setDirection(pd.direction);
                            setState(State.WALKING);
                        }
                    })
            ));
        }

        seq.addAction(Actions.run((new Runnable() {
               public void run () {
                   setState(State.STANDING);
                   stateMachine.changeState(state);
               }
            })
        ));

        this.addAction(seq);
    }

    // temp method
    public void leaveHouse() {
        setTilePos(18,91); // outside the house test
        SproutGame.playSound("door_close1", 0.4f);
        stateMachine.changeState(EmmaState.IDLE);
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
        stateMachine.update();
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
