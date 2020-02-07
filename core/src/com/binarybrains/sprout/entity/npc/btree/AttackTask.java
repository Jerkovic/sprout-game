package com.binarybrains.sprout.entity.npc.btree;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
import com.binarybrains.sprout.entity.npc.Npc;

public class AttackTask extends LeafTask<Npc> {

    @Override
    public Status execute() {
        // System.out.println("Run attack task! " + getObject());
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<Npc> copyTo(Task<Npc> task) {
        return task;
    }

    @Override
    public void reset() {
        super.reset();
    }
}
