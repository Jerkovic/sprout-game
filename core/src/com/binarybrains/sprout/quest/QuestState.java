package com.binarybrains.sprout.quest;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

/**
 * The Axe quest state machine
 */
public enum QuestState implements State<Quest> {

    TEDDY() {
        @Override
        public void update(Quest quest) {
        }

        @Override
        public void exit(Quest quest) {
        }

        @Override
        public void enter(Quest quest) {
        }
    };

    // end of states

    @Override
    public void enter(Quest entity) {

    }

    @Override
    public void update(Quest entity) {

    }

    @Override
    public void exit(Quest entity) {

    }

    @Override
    public boolean onMessage(Quest entity, Telegram telegram) {
        return false;
    }
}