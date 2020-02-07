package com.binarybrains.sprout.quest;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.binarybrains.sprout.events.TelegramType;

// QuestSystem/Manager
public class QuestsManager implements Telegraph {

    private static QuestsManager instance = null;

    public static QuestsManager getInstance()
    {
        if (instance == null)  instance = new QuestsManager();
        return instance;
    }

    public void init() {
        setupListeners();
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
                System.out.println("QuestManager got player npc interaction");
                // findQuest(npc).getState()
                break;
            default:
                // code block
        }
        return false;
    }
}