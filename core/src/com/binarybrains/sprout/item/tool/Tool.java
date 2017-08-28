package com.binarybrains.sprout.item.tool;


import com.badlogic.gdx.Gdx;
import com.binarybrains.sprout.level.tile.Tile;

public abstract class Tool {

    public static Hoe hoe = new Hoe();
    public static Axe axe = new Axe();
    public static PickAxe pickaxe = new PickAxe();
    public static WateringCan wateringcan = new WateringCan();
    public static Scythe scythe = new Scythe();
    public static FishingPole fishingpole = new FishingPole();

    // -----------------------------------------------------------

    public final String name;
    public final String description;

    public Tool(String name, String description) {
        if (name.length() > 16) throw new RuntimeException("Tool name cannot be longer than 16 characters!");
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean use(Tile tile) {
        Gdx.app.log("DEBUG", "Use tool: " + this +" on " + tile);
        return true;
    }

}

