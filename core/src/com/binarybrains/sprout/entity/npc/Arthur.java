package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.btree.branch.RandomSelector;
import com.badlogic.gdx.ai.btree.branch.Selector;
import com.badlogic.gdx.ai.btree.decorator.AlwaysFail;
import com.badlogic.gdx.ai.btree.decorator.Include;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibrary;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeLibraryManager;
import com.badlogic.gdx.ai.btree.utils.BehaviorTreeParser;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Entity;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.actions.Actions;
import com.binarybrains.sprout.entity.npc.btree.AttackTask;
import com.binarybrains.sprout.entity.npc.btree.PatrolTask;
import com.binarybrains.sprout.entity.npc.btree.PlayerCloseCondition;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.item.resource.Resources;
import com.binarybrains.sprout.level.Level;
import com.binarybrains.sprout.misc.BackgroundMusic;
import com.badlogic.gdx.ai.btree.BehaviorTree;
import com.badlogic.gdx.ai.btree.BranchTask;
import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.branch.Parallel;
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
        setSpeed(500);

        // Sequence is a branch task that runs every children until one of them fails. If a child task
        // succeeds, the selector will start and run the next child task.
        // Npc is the blackboard object,

        // A BranchTask defines a behavior tree branch, contains logic of starting or running sub-branches and leaves
        // A LeafTask - is a terminal task of a behavior tree, contains action or condition logic, can not have any
        // child.

        // Actions -
        // which cause the execution of methods or functions on the game world,
        // e.g. move a character, decrease health, etc...
        //
        // Conditions -
        // which query the state of objects in the game world, e.g. location of character, inventory content,
        // amount of health, etc...

        /**
         * A Selector is a branch task that runs every children until one of them succeeds. If a
         * child task fails, the selector * will start and run the next child task. * * @param <E> type
         * of the blackboard object that tasks use to read or modify game state
         */

        // https://github.com/jsjolund/GdxDemo3D/blob/master/android/assets/btrees/dog.btree

        // Conditional

        // com/badlogic/gdx/ai/tests/btree/tests/ParallelVsSequenceTest.java
        setupAI();
    }

    private void registerBehavior (BehaviorTreeLibrary library) {

        Include<Npc> include = new Include<>();
        include.lazy = true;
        include.subtree = "npc.actual";

        BehaviorTree<Npc> includeBehavior = new BehaviorTree<>(include);
        library.registerArchetypeTree("arthur", includeBehavior);

        BehaviorTree<Npc> actualBehavior = new BehaviorTree<>(createBehavior());
        library.registerArchetypeTree("npc.actual", actualBehavior);
    }

    /**
     *
     * @return
     */
    private static Task<Npc> createBehavior () {
        Selector<Npc> selector = new Selector<>();

        Sequence<Npc> sequence = new Sequence<>();
        selector.addChild(sequence);

        sequence.addChild(new PlayerCloseCondition());
        sequence.addChild(new AttackTask());
        sequence.addChild(new PatrolTask());

        return selector;
    }


    public void setupAI() {
        BehaviorTreeLibraryManager libraryManager = BehaviorTreeLibraryManager.getInstance();
        BehaviorTreeLibrary library = new BehaviorTreeLibrary(BehaviorTreeParser.DEBUG_NONE);
        registerBehavior(library);
        libraryManager.setLibrary(library);
        behaviorTree = libraryManager.createBehaviorTree("arthur", this);
    }

    public BehaviorTree<Npc> getBehaviorTree () {
        return behaviorTree;
    }

    public void setBehaviorTree (BehaviorTree<Npc> behaviorTree) {
        this.behaviorTree = behaviorTree;
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
            speak(
                    "Arthur",
                    "Aaargh What do you want from me? argh *cough*"
            );
            return false;
        }

        // animation, sound and dialog
        addAction(Actions.sequence(
                Actions.run(() -> {
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
