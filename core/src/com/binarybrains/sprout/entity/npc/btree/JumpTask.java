package com.binarybrains.sprout.entity.npc.btree;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.binarybrains.sprout.entity.npc.Npc;

public class JumpTask extends LeafTask<Npc> {

    private Status status;

    @Override
    public void start () {
        this.status = Status.RUNNING;
        getObject().jump(() -> {
            this.status = Status.SUCCEEDED;
        });
    }

    @Override
    public Status execute() {
        return this.status;
    }

    @Override
    public void end () {
        this.status = getStatus();
    }

    @Override
    protected Task<Npc> copyTo(Task<Npc> task) {
        return null;
    }
}
