package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.binarybrains.sprout.entity.Mob;


public enum EmmaState implements State<Emma> {

    WALK_HOME() {
        @Override
        public void update(Emma emma) {
            if (emma.getTileX() == 1 && emma.getTileY() == 40) { // is at home
                emma.stateMachine.changeState(EmmaState.WALK_LABYRINTH);
                //emma.setState(Emma.State.STANDING);
            }
        }

        @Override
        public void enter(Emma emma) {
            System.out.println("===================================");
            System.out.println("Entering state WALK_HOME");
            System.out.println("===================================");
            emma.updateWalkDirections(1,40);
        }

        @Override
        public void exit(Emma emma) {
            System.out.println("===================================");
            System.out.println("Exit state WALK_HOME");
            System.out.println("===================================");
        }


    },

    WALK_LABYRINTH() {
        @Override
        public void update(Emma emma) {
            if (emma.getTileX() == 1 && emma.getTileY() == 1) { // made the labyrinth
                emma.setState(Emma.State.STANDING);
                emma.stateMachine.changeState(WALK_HOME);
            }
        }

        @Override
        public void enter(Emma emma) {
            emma.updateWalkDirections(1,1);
            System.out.println("Entering state WALK_LAYRINTH");
        }

        @Override
        public void exit(Emma emma) {
            System.out.println("===================================");
            System.out.println("Exit state WALK_LABYRINTH");
            System.out.println("===================================");
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