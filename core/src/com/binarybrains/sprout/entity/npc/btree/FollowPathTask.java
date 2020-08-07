package com.binarybrains.sprout.entity.npc.btree;

import com.badlogic.gdx.ai.btree.LeafTask;
import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.math.Vector2;
import com.binarybrains.sprout.entity.npc.Npc;

public class FollowPathTask extends LeafTask<Npc> {

    private Status status;
    private Vector2 destination;

    public FollowPathTask(Vector2 destination) {
        this.destination = destination;

        /** FRESH Means that the task has never run or has been reset. */
        /** RUNNING, Means that the task needs to run again. */
        /** FAILED Means that the task returned a failure result. */
        /** SUCCEEDED Means that the task returned a success result. */
        /** CANCELLED Means that the task has been terminated by an ancestor. */
    }

    @Override
    public void start () {
        this.status = Status.RUNNING;
        System.out.println("Start - Following path task!");
        getObject().updateWalkDirections((int)this.destination.x, (int)destination.y, () -> {
            this.status = Status.SUCCEEDED;
        });
    }

    @Override
    public Status execute() {
        return this.status;
    }

    @Override
    public void end () {
        System.out.println("Ending - Following path task! " + this.status);
        // should this send message ?
        this.status = getStatus();
    }

    @Override
    protected Task<Npc> copyTo(Task<Npc> task) {
        return task;
    }
}
