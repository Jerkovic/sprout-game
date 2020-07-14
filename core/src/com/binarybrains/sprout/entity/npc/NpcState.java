package com.binarybrains.sprout.entity.npc;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.events.TelegramType;


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
    WAIT() {
        @Override
        public void enter(Npc entity) {

        }
        @Override
        public void update(Npc npc) {
            if (npc.distanceTo(npc.getLevel().player) < 64) {
                npc.stateMachine.changeState(WAVE);
            }
        }
    },

    WAVE() {
        @Override
        public void enter(Npc npc) {
            npc.setDirection(Mob.Direction.SOUTH);
            npc.setActionState(Npc.ActionState.HOBBY);
            MessageManager.getInstance().dispatchMessage(
                    npc,
                    TelegramType.NPC_MESSAGE,
                    new PointDirection(npc.getTileX(), npc.getTileY(), npc.getDirection())
            );
        }
    },

    GOTO_ARTHUR() {
        @Override
        public void enter(Npc npc)
        {
            npc.updateWalkDirections(39, 31, () -> {
                npc.setState(Mob.State.STANDING);
                npc.setDirection(Mob.Direction.SOUTH); // should this be here even?
                npc.stateMachine.changeState(WAIT); // wait for player
            });

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