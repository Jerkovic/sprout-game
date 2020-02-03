package com.binarybrains.sprout.entity.npc.arthur.btree;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.binarybrains.sprout.entity.npc.Arthur;

public class HasFixedAxeCondition extends LeafTask<Arthur> {
    @Override
    public Status execute() {
        // return getObject().stateCheckMethod ? Status.SUCCEEDED : Status.FAILED;
        return Status.FAILED;

    }

    @Override
    protected Task<Arthur> copyTo(Task<Arthur> task) {
        return null;
    }
}
