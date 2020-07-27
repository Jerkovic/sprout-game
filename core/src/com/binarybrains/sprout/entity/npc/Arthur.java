package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.GdxAI;

import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibrary;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.entity.npc.btree.AttackTask;
import com.binarybrains.sprout.entity.npc.btree.FollowPathTask;
import com.binarybrains.sprout.entity.npc.btree.JumpTask;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Sequence;


public class Arthur extends Npc {

    BehaviorTree<Npc> behaviorTree;
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
        setSpeed(250);
        setupAI();
    }

    public void setupAI() {
        this.behaviorTree = new BehaviorTree<>(createBehavior());
        this.behaviorTree.setObject(this); // set blackboard object to this Arthur instance
    }

    /**
     *
     * @return
     */
    private static Task<Npc> createBehavior () {
        Selector<Npc> selector = new Selector<>();

        Sequence<Npc> sequence = new Sequence<>();
        selector.addChild(sequence);

        sequence.addChild(new FollowPathTask(new Vector2(310,160)));
        sequence.addChild(new JumpTask());
        sequence.addChild(new FollowPathTask(new Vector2(320,170)));
        sequence.addChild(new JumpTask());
        sequence.addChild(new FollowPathTask(new Vector2(310,160)));
        sequence.addChild(new AttackTask()); // digging

        return selector;
    }

    public BehaviorTree<Npc> getBehaviorTree () {
        return behaviorTree;
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
    public void update(float delta) {
        super.update(delta);
        GdxAI.getTimepiece().update(delta);
        getBehaviorTree().step();
    }

    @Override
    public boolean interact(Player player, Item item, Direction attackDir) {

        super.interact(player,item, attackDir);

        // Move to QuestLine or
        // Waiting state -
        // Quests System storage?
        if (player.getInventory().hasResources(Resources.wood, 10)) {
            player.getInventory().removeResource(Resources.wood, 10);
            speak(
                    "Arthur",
                    "Thanks young man *cough* Here is something for you.."
            );
            player.getInventory().add(new ResourceItem(Resources.seeds, 16));
        }
        // Arthur upgrade Tool
        // Wait for 50 woods
        // Quest line
        // this is a Part dependent on that the TeddyBear Quest is done and he got the axe
        // it also needs to be fullfilled that the player has seen the "Approach Arthur message"
        if (!player.getInventory().upgradeTool("Axe", ToolItem.COPPER)) {
            return false;
        }

        // animation, sound and dialog
        addAction(Actions.sequence(
                Actions.run(() -> {
                    setState(State.STANDING);
                    setActionState(ActionState.EMPTY_NORMAL);
                    lookAt(player);

                }),
                Actions.repeat(5, Actions.run(() -> {
                    // System.out.println("test");
                })),
                Actions.delay(1.3f),
                Actions.run(() -> {
                    speak(
                            "Arthur",
                            "Arghh! *cough* Nice to meet you fellow.\n *cough* Chop me some wood young lad.\n  I need 10 logs to build me some *cough* shelter. Let me fix that shitty axe you got there. *cough*"
                    );
                }),
                Actions.delay(1.6f),
                // Take axe from player inventory?
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
                    getLevel().screen.hud.refreshInventory();
                    BackgroundMusic.changeTrack(4);
                })
        ));
        return true;
    }

    @Override
    public String getId() {
        return "npc_arthur";
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
