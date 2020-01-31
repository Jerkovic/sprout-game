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
    WAVE() {
        @Override
        public void enter(Npc entity) {
            entity.setDirection(Mob.Direction.SOUTH);
            entity.setActionState(Npc.ActionState.HOBBY);
        }
    },

    GOTO_ARTHUR() {
        @Override
        public void enter(Npc entity)
        {
            entity.updateWalkDirections(39, 31, WAVE);
        }

        @Override
        public void update(Npc entity) {

        }

        @Override
        public void exit(Npc entity) {

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