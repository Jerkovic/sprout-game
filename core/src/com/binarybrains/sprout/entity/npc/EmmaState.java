package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;


public enum EmmaState implements State<Emma> {

    WALK_HOME() {
        @Override
        public void update(Emma emma) {
            if (emma.isSafe()) {
                emma.stateMachine.changeState(WALK_ABOUT);
            }
            else {
                emma.testAction();
            }
        }
    },

    WALK_ABOUT() {
        @Override
        public void update(Emma emma) {
            if (emma.isThreatened()) {
                emma.stateMachine.changeState(WALK_HOME);
            }
            else {
                emma.testAction();
            }
        }
    },

    GOT_ACORN() {
        @Override
        public void update(Emma emma) {

        }
    };


    @Override
    public void enter(Emma emma) {
        // enter a state
    }

    @Override
    public void exit(Emma emma) {
        // exit a state
    }

    @Override
    public boolean onMessage(Emma emma, Telegram telegram) {
        // We don't use messaging (yet)
        // but we really want that
        return false;
    }
}