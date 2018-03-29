package com.binarybrains.sprout.item.artifact;


import com.binarybrains.sprout.item.Sellable;

public class Artifact implements Sellable {

    public final String name;
    public final String description;
    public int sellPrice;

    public Artifact(String name, String description, int sellPrice) {
        this.name = name;
        this.description = description;
        this.sellPrice = sellPrice;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int getSellPrice() {
        return sellPrice;
    }
}
