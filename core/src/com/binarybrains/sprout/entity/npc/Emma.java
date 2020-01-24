package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.level.Level;


public class Emma extends Npc {

    /**
     *
     * @param level
     * @param position
     * @param width
     * @param height
     */
    public Emma(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height, SproutGame.assets.get("haley-sheet.png"));
        setHealth(100);
        setState(State.STANDING);
        setDirection(Direction.EAST);
        setSpeed(24);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        System.out.println("Emma knows about" + msg);
        return true;
    }

    @Override
    public void touchedBy(Entity entity) {
        if ((entity instanceof Emma)) return;
    }

    @Override
    public boolean interact(Player player, Item item, Direction attackDir) {

        if (player.activeItem instanceof ArtifactItem && player.activeItem.getName().equals("Teddy")) {
            addAction(Actions.sequence(
                    Actions.run(() -> {
                        jump();
                        ArtifactItem ai = (ArtifactItem) player.activeItem;
                        player.getInventory().removeItem(ai);
                        player.getLevel().screen.hud.refreshInventory();
                    }),
                    Actions.delay(5),
                    Actions.run(() -> {
                        stateMachine.changeState(NpcState.GOTO_SEWER_HATCH);
                    })
            ));
            return true;
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
}
