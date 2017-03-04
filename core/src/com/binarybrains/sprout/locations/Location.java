package com.binarybrains.sprout.locations;

public class Location {

    private String name, description;
    private int posX, posY; // rect?

    // SHACK, MINE, BANK, SALOON, QUARRY
    // HOME, FOREST, OLD WILLS HOUSE
    // The Manor

    public Location(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
