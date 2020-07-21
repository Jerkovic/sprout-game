package com.binarybrains.sprout.entity.npc.btree;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.binarybrains.sprout.entity.Mob;
import com.binarybrains.sprout.entity.npc.Npc;

public class AttackTask extends LeafTask<Npc> {

    private Status status;

    @Override
    public Status execute() {
        return Status.RUNNING;
    }

    @Override
    public void start () {
        /* This method will be called once before this task's first run. */
        getObject().setDirection(Mob.Direction.SOUTH);
        getObject().setState(Mob.State.STANDING);
        getObject().setActionState(Npc.ActionState.HOBBY);

    }

    @Override
    public void end () {
        /* This method will be called by success() fail() or cancel(), meaning that this task's status has
         * just been set to updated respectively. */
    }

    @Override
    protected Task<Npc> copyTo(Task<Npc> task) {
        return task;
    }

}
