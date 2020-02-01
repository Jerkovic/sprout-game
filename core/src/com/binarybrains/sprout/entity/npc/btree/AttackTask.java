package com.binarybrains.sprout.entity.npc.btree;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.ai.btree.annotation.TaskAttribute;
import com.badlogic.gdx.ai.utils.random.ConstantIntegerDistribution;
import com.badlogic.gdx.ai.utils.random.IntegerDistribution;
import com.binarybrains.sprout.entity.npc.Npc;

public class AttackTask extends LeafTask<Npc> {

    @TaskAttribute
    public IntegerDistribution times = ConstantIntegerDistribution.ONE;

    @Override
    public Status execute() {
        //
        // Task.Status - Status.SUCCEEDED;
        return Status.RUNNING;
    }

    @Override
    protected Task<Npc> copyTo(Task<Npc> task) {
        return null;
    }
}
