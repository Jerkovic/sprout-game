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
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tools;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;


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
        setSpeed(700);
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

        super.interact(player,item, attackDir); // sends a message to QuestManager

        if (player.activeItem != null && player.activeItem.getName().equals("Teddy")) {
            addAction(Actions.sequence(
                    Actions.run(() -> {
                        BackgroundMusic.setVolume(.5f);
                        BackgroundMusic.changeTrack(3);
                        lookAt(player);
                        jump();
                        ArtifactItem ai = (ArtifactItem) player.activeItem;
                        player.getInventory().removeItem(ai);
                        player.getLevel().screen.hud.refreshInventory();
                    }),
                    Actions.delay(2f),
                    Actions.run(() -> {
                        speak(
                                "Teddy is back",
                                "You are sooo kind! Maybe you could help my old grandpa. \nHe needs some help chopping down trees.\n Here is an old axe I used to kill cats with..\n Follow me I will lead you to him."

                        );
                        player.getInventory().add(new ToolItem(Tools.axe, 0));
                        SproutGame.playSound("blop", .7f, MathUtils.random(0.9f, 1.2f), 1f);
                        stateMachine.changeState(NpcState.GOTO_ARTHUR);
                    })
            ));
            return true;
        }
        return false;
    }

    @Override
    public String getId() {
        return "npc_emma";
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
