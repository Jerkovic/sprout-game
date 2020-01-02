package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.utils.Timer;
import com.binarybrains.sprout.SproutGame;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.misc.GameTime;


public enum EmmaState implements State<Emma> {

    WALK_HOME() {
        @Override
        public void update(Emma emma) {
            if (emma.hasArrivedToTile(5, 1)) { // is by the door
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
            if (emma.hasArrivedToTile(1, 1)) {
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
            if (emma.hasArrivedToTile(6, 86)) {
                emma.clearFindPath();
                emma.stateMachine.changeState(GOTO_TREE);
            }
        }
        @Override
        public void enter(Emma emma) {
            emma.updateWalkDirections(6, 86);
            emma.setState(Emma.State.WALKING);
            // send a test message
        }

        @Override
        public void exit(Emma emma) {
            emma.setDirection(Mob.Direction.EAST);
        }
    },

    GOTO_TREE() {
        @Override
        public void update(Emma emma) {
            if (emma.hasArrivedToTile(27, 92)) {
                emma.stateMachine.changeState(EmmaState.IDLE);
                emma.clearFindPath();
            }
        }
        @Override
        public void enter(Emma emma) {
            emma.updateWalkDirections(27, 92);
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