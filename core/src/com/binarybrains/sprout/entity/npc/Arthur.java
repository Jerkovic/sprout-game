package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.item.ArtifactItem;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tools;
import com.binarybrains.sprout.level.Level;


public class Arthur extends Npc {

    /**
     *
     * @param level
     * @param position
     * @param width
     * @param height
     */
    public Arthur(Level level, Vector2 position, float width, float height) {
        super(level, position, width, height, SproutGame.assets.get("willy.png"));
        setState(State.STANDING);
        setDirection(Direction.SOUTH);
        setSpeed(24);
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        return true;
    }

    @Override
    public void touchedBy(Entity entity) {
        if ((entity instanceof Arthur)) return;
    }

    @Override
    public boolean interact(Player player, Item item, Direction attackDir) {

            addAction(Actions.sequence(
                    Actions.run(() -> {
                        lookAt(player);
                    }),
                    Actions.delay(1.3f),
                    Actions.run(() -> {
                        player.getLevel().screen.hud.speakDialog(
                                "Arthur",
                                "Arghh! *cough* Nice to meet you fellow.\n *cough* Chop me some wood young lad.\n  I need 10 logs to build me some *cough* shelter. Let me fix that shitty axe you got there. *cough*"
                        );
                    }),
                    Actions.delay(1.6f),
                    Actions.run(() -> {
                        SproutGame.playSound("slap_iron_clatter", .5f);
                    }),
                    Actions.delay(1.5f),
                    Actions.run(() -> {
                        SproutGame.playSound("slap_iron_clatter", .6f,  MathUtils.random(.8f, 1f), 1f);
                    }),
                    Actions.delay(1f),
                    Actions.run(() -> {
                        SproutGame.playSound("slap_iron_clatter", .6f,  MathUtils.random(.8f, 1.2f), 1f);
                    }),
                    Actions.run(() -> {
                        SproutGame.playSound("blop", .7f, MathUtils.random(0.9f, 1.2f), 1f);
                        player.getInventory().upgradeTool("Axe", 1);
                        getLevel().screen.hud.refreshInventory();
                    })
            ));
            return true;
    }

    @Override
    public boolean blocks(Entity e) {
        if (e instanceof Arthur) return false;
        if (e instanceof Player) {
            return false;
        }
        return false;
    }
}
