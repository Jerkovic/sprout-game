package com.binarybrains.sprout.entity.npc.btree;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.binarybrains.sprout.entity.npc.Npc;

public class PatrolTask extends LeafTask<Npc> {

    @Override
    public Status execute() {
        //
        // Task.Status - Status.SUCCEEDED;
        // loop times t
        System.out.println("Patrol task! " + getObject());
        // call getObject().attack();
        return Status.SUCCEEDED;
    }

    @Override
    protected Task<Npc> copyTo(Task<Npc> task) {
        return task;
    }
}
