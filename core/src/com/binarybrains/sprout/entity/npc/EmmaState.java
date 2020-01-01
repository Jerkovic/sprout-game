package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.binarybrains.sprout.SproutGame;
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
            // this has arrived check needs to be fixed
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
            if (emma.getTileX() == 6 && emma.getTileY() == 86) {
                emma.stateMachine.changeState(EmmaState.IDLE);
                emma.clearFindPath();
            }
        }
        @Override
        public void enter(Emma emma) {
            emma.updateWalkDirections(6, 86);
            emma.setState(Emma.State.WALKING);
            // send a test messge
            MessageManager.getInstance().dispatchMessage(emma, 0);
        }

        @Override
        public void exit(Emma emma) {
            emma.setDirection(Mob.Direction.WEST);
        }

    },

    IDLE() {
        @Override
        public void update(Emma emma) {
            if (emma.distanceTo(emma.getLevel().player) < (16 * 4)) {
                emma.lookAt(emma.getLevel().player);
            }
        }
        @Override
        public void enter(Emma emma) {
            emma.setState(Emma.State.STANDING);
        }

        @Override
        public void exit(Emma emma) {

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