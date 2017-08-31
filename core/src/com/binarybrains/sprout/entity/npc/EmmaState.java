package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;


public enum EmmaState implements State<Emma> {

    WALK_HOME() {
        @Override
        public void update(Emma emma) {
            if (emma.getTileX() == 5 && emma.getTileY() == 5) { // is at home
                emma.clearFindPath();
                emma.leaveHouse();
                emma.stateMachine.changeState(EmmaState.IDLE);
            }
        }

        @Override
        public void enter(final Emma emma) {
            System.out.println("===================================");
            System.out.println("Entering state WALK_HOME");
            System.out.println("===================================");

            emma.updateWalkDirections(5,5);
            emma.setState(Emma.State.WALKING);
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
            System.out.println("===================================");
            System.out.println("Enter state WALK_LABYRINTH");
            System.out.println("===================================");
        }

        @Override
        public void exit(Emma emma) {
            System.out.println("===================================");
            System.out.println("Exit state WALK_LABYRINTH");
            System.out.println("===================================");
        }
    },

    LEAVE_HOUSE() {
        @Override
        public void update(Emma emma) {
            if (emma.getTileX() == 18 && emma.getTileY() == 90) { // is outside
                emma.stateMachine.changeState(EmmaState.IDLE);
                emma.clearFindPath();
            }
            if (emma.getTileX() == 5 && emma.getTileY() == 5) { // is outside
                emma.clearFindPath();
                emma.leaveHouse();
            }
        }
        @Override
        public void enter(Emma emma) {
            System.out.println("===================================");
            System.out.println("Enter state Leave house");
            System.out.println("===================================");
            emma.updateWalkDirections(3,0);
            emma.setState(Emma.State.WALKING);
        }

        @Override
        public void exit(Emma emma) {
            System.out.println("===================================");
            System.out.println("Exit leave house state");
            System.out.println("===================================");
        }

    },

    IDLE() {
        @Override
        public void update(Emma emma) {
            if (emma.distanceTo(emma.getLevel().player) < 20f) {
                System.out.println("Emma is close to the player make here stop and look at the player");
                emma.lookAt(emma.getLevel().player);
                // maybe another state
            }
        }
        @Override
        public void enter(Emma emma) {
            System.out.println("===================================");
            System.out.println("Enter state IDLE (arms in the air!)");
            System.out.println("===================================");
            emma.setState(Emma.State.STANDING);
        }

        @Override
        public void exit(Emma emma) {
            System.out.println("===================================");
            System.out.println("Exit state IDLE");
            System.out.println("===================================");
        }
    }; // end of states



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