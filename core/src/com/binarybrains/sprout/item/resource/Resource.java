package com.binarybrains.sprout.item.resource;


import com.binarybrains.sprout.item.Saleable;

public class Resource implements Saleable {

    public final String name;
    public final String description;
    public int sellPrice = 0;

    public Resource(String name, String description, int sellPrice) {
        this.name = name;
        this.description = description;
        this.sellPrice = sellPrice;
    }

    @Override
    public int getSellPrice() {
        return sellPrice;
    }
}