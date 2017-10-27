package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.binarybrains.sprout.entity.Mob;


public enum EmmaState implements State<Emma> {

    WALK_HOME() {
        @Override
        public void update(Emma emma) {
            if (emma.getTileX() == 5 && emma.getTileY() == 1) { // is by the door
                emma.setDirection(Mob.Direction.SOUTH);
                emma.clearFindPath();
                emma.leaveHouse();
                emma.stateMachine.changeState(EmmaState.IDLE);
            }
        }

        @Override
        public void enter(final Emma emma) {
            emma.updateWalkDirections(5,1);
            emma.setState(Emma.State.WALKING);
        }

        @Override
        public void exit(Emma emma) {
        }


    },

    WALK_LABYRINTH() {
        @Override
        public void update(Emma emma) {
            if (emma.getTileX() == 1 && emma.getTileY() == 1) { // made the walking around in the house
                emma.setState(Emma.State.STANDING);
                emma.stateMachine.changeState(WALK_HOME);
            }
        }

        @Override
        public void enter(Emma emma) {
            emma.updateWalkDirections(1,1);
        }

        @Override
        public void exit(Emma emma) {
        }
    },

    GOTO_LAMP_POST() {
        @Override
        public void update(Emma emma) {
            if (emma.getTileX() == 51 && emma.getTileY() == 102) {
                emma.stateMachine.changeState(EmmaState.IDLE);
                emma.clearFindPath();
            }
        }
        @Override
        public void enter(Emma emma) {
            emma.updateWalkDirections(51,102);
            emma.setState(Emma.State.WALKING);
        }

        @Override
        public void exit(Emma emma) {
            emma.setDirection(Mob.Direction.WEST);
        }

    },

    IDLE() {
        @Override
        public void update(Emma emma) {
            if (emma.distanceTo(emma.getLevel().player) < (16*4)) {
                // System.out.println("Emma is close to the player make here stop and look at the player");
                emma.lookAt(emma.getLevel().player);
                // maybe another state
            }
        }
        @Override
        public void enter(Emma emma) {
            System.out.println("===================================");
            System.out.println("Enter state IDLE");
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
    /*
     * @param entity the entity that received the message
	 * @param telegram the message sent to the entity
	 * @return true if the message has been successfully handled; false otherwise. */
    public boolean onMessage(Emma emma, Telegram telegram) {
        // We don't use messaging (yet)
        // but we really want that
        return false;
    }
}