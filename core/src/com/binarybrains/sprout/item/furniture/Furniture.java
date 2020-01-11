package com.binarybrains.sprout.item.furniture;

public class Furniture {

    public final String name;
    public final String description;

    public Furniture(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
