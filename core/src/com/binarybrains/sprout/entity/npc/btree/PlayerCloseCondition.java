package com.binarybrains.sprout.entity.npc.btree;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.binarybrains.sprout.entity.npc.Npc;

public class PlayerCloseCondition extends LeafTask<Npc> {

    public PlayerCloseCondition() {
    }

    @Override
    public Status execute() {
        return getObject().distanceTo(getObject().getLevel().player) < 128 ? Status.SUCCEEDED : Status.FAILED;
    }

    @Override
    protected Task<Npc> copyTo(Task<Npc> task) {
        return task;
    }
}
