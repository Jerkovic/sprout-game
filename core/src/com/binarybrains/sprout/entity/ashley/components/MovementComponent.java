package com.binarybrains.sprout.entity.ashley.components;

import com.badlogic.ashley.core.Component;

public class MovementComponent implements Component {
    public float velocityX;
    public float velocityY;

    public MovementComponent (float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}