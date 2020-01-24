package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.binarybrains.sprout.entity.Mob;


public enum NpcState implements State<Npc> {

    IDLE() { // STATE FOR PLAYER
        @Override
        public void update(Npc npc) {
        }

        @Override
        public void exit(Npc npc) {
        }

        @Override
        public void enter(Npc npc) {
        }
    },
    WALK_TO_FRONT_DOOR() {
        @Override
        public void update(Npc npc) {

        }

        @Override
        public void enter(final Npc npc) {
            npc.updateWalkDirections(4,0, LEAVE_HOUSE);
        }

        @Override
        public void exit(Npc npc) {
            npc.setState(Mob.State.STANDING);
        }
    },

    WALK_SOME_IN_PLAYER_HOUSE() {
        @Override
        public void enter(Npc entity) {
            entity.updateWalkDirections(1,1, WALK_TO_FRONT_DOOR);
        }

        @Override
        public void update(Npc entity) {

        }

        @Override
        public void exit(Npc entity) {

        }
    },

    GOTO_SEWER_HATCH() {
        @Override
        public void enter(Npc entity)
        {
            entity.updateWalkDirections(35, 94, IDLE);
        }

        @Override
        public void update(Npc entity) {

        }

        @Override
        public void exit(Npc entity) {

        }
    },

    LEAVE_HOUSE() {
        @Override
        public void enter(Npc npc) {
            npc.setState(Npc.State.STANDING);
            npc.leaveHouse();
        }

    };

   // end of states


    @Override
    public void update(Npc entity) {

    }

    @Override
    public void exit(Npc entity) {

    }

    @Override
    /*
     * @param entity the entity that received the message
	 * @param telegram the message sent to the entity
	 * @return true if the message has been successfully handled; false otherwise. */
    public boolean onMessage(Npc npc, Telegram telegram) {
        return false;
    }
}