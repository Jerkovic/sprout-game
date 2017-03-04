package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;


public enum EmmaState implements State<Emma> {

    WALK_HOME() {
        @Override
        public void update(Emma emma) {
            if (emma.isThreatened()) {
                emma.stateMachine.changeState(WALK_HOME);
            }
            else {
                // emma.some
            }
        }

        @Override
        public void enter(Emma emma) {
            System.out.println("===================================");
            System.out.println("Entering state WALK_HOME");
            System.out.println("===================================");
        }

        @Override
        public void exit(Emma emma) {
            System.out.println("===================================");
            System.out.println("Ext state WALK_HOME");
            System.out.println("===================================");
        }


    },

    WALK_TO_NORTH_EAST() {
        @Override
        public void update(Emma emma) {
            if (emma.isThreatened()) {
                emma.stateMachine.changeState(WALK_HOME);
            }
            else {
                // emma.some
            }
        }

        @Override
        public void enter(Emma emma) {
            System.out.println("Entering state WALK_TO_NORTH_EAST");
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