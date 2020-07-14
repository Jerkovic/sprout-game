package com.binarybrains.sprout.quest;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.binarybrains.sprout.entity.Player;
import com.binarybrains.sprout.entity.npc.Npc;
import com.binarybrains.sprout.events.TelegramType;
import com.binarybrains.sprout.structs.TreeNode;

public class QuestManager implements Telegraph {


    private static QuestManager instance = null;
    private TreeNode<Dialog> tree; // NodeTypes: prompt, reply .. id(guid), Questnode (Different types as well), ActionNode
    //  AI_Dialog
    //  cutscene

    public static QuestManager getInstance()
    {
        if (instance == null)  instance = new QuestManager();
        return instance;
    }

    public void init() {
        setupListeners();
        testData(); // setup some test data
    }

    private void testData() {
        // Store Trees in Map<String, DialogTree> ?? Npc-id , ond tree(s)
        // tree = new DialogTree("npc_emma_dialog_teddy");

    }

    /**
     * Setup listener
     */
    private void setupListeners() {
        MessageManager.getInstance().addListeners(this,
                TelegramType.PLAYER_NPC_START_INTERACTION
        );
    }

    @Override
    public boolean handleMessage(Telegram msg) {
        switch(msg.message) {
            case TelegramType.PLAYER_NPC_START_INTERACTION:
                Npc npc = ((Npc) msg.sender);
                Player player = npc.getLevel().player;

                dialogBetween(player, npc);
                // QuestManager should keep state of where in the tree we are
                // What should the QuestManager show to the user?
                break;
            default:
                // code block
        }
        return false;
    }

    /**
     *
     * @param player
     * @param npc
     * @return
     */
    public void dialogBetween(Player player, Npc npc) {
        System.out.println("QuestManager got a player npc interaction event with: " + npc.getId());
    }


}