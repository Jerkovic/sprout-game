package com.binarybrains.sprout.entity.ashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class PositionComponent implements Component {

    public Vector2 position = new Vector2();


    public PositionComponent(float x, float y) {
        this.position.x = x;
        this.position.y = y;

    }

    public PositionComponent(Vector2 position) {
        this.position = position;

    }

}