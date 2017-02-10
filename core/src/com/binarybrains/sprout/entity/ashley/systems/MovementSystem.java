package com.binarybrains.sprout.entity.ashley.systems;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.systems.IteratingSystem;
import com.binarybrains.sprout.entity.ashley.components.MovementComponent;
import com.binarybrains.sprout.entity.ashley.components.PositionComponent;

public class MovementSystem extends IteratingSystem {

    private ComponentMapper<PositionComponent> pm = ComponentMapper.getFor(PositionComponent.class);
    private ComponentMapper<MovementComponent> mm = ComponentMapper.getFor(MovementComponent.class);

    public MovementSystem () {
        super(Family.all(PositionComponent.class, MovementComponent.class).get());
    }

    @Override
    public void processEntity (Entity entity, float deltaTime) {
        PositionComponent pc = pm.get(entity);
        MovementComponent mc = mm.get(entity);

        pc.position.x += mc.velocityX * deltaTime;
        pc.position.y += mc.velocityY * deltaTime;
    }

    @Override
    public void update (float deltaTime) {

        super.update(deltaTime);
        System.out.println("Entities updated in MovementSystem.");
    }
}