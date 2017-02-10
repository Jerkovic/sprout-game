package com.binarybrains.sprout.item.resource;

public class FoodResource extends Resource {
    private int heal;
    private int staminaCost;

    public FoodResource(String name, String desc) {
        super(name, desc);
        this.heal = 10;
        this.staminaCost = 1;
    }

}