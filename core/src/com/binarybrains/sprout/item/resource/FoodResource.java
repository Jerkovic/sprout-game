package com.binarybrains.sprout.item.resource;

public class FoodResource extends Resource {
    private int heal;
    private int staminaCost;

    public FoodResource(String name, String desc, int sellPrice) {
        super(name, desc, sellPrice);
        this.heal = 10;
        this.staminaCost = 1;
    }

    public int heal() {
        return heal;
    }

}