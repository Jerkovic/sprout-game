package com.binarybrains.sprout.item.tool;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import com.binarybrains.sprout.level.tile.Tile;

public abstract class Tool {

    public static Hoe hoe = new Hoe();
    public static Axe axe = new Axe();
    public static PickAxe pickaxe = new PickAxe();
    public static WateringCan wateringcan = new WateringCan();
    public static Scythe scythe = new Scythe();
    public static FishingPole fishingpole = new FishingPole();
    public static GoldKey goldenKey = new GoldKey();
    public static Hammer hammer = new Hammer();

    // How should we handle consumable Tools... like keys?

    public final String name;
    public final String description;
    private long lastUseTime = 0;
    private long coolDownTime = 0;


    public Tool(String name, String description) {
        if (name.length() > 16) throw new RuntimeException("Tool name cannot be longer than 16 characters!");
        this.name = name;
        this.description = description;
    }

    public void setCoolDownTime(long cool) {
        coolDownTime = cool;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean canUse() {
        long useTime = TimeUtils.millis();
        if (TimeUtils.timeSinceMillis(lastUseTime) > coolDownTime)
        {
            lastUseTime = useTime;
            return true;
        } else {
            return false;
        }
    }

    public boolean use(Tile tile) {
        if (canUse())
        {
            Gdx.app.log("DEBUG", "Use tool: " + this +" on " + tile);
            return true;
        } else {
            Gdx.app.log("DEBUG", "Cooling down" + this);
            return false;
        }


    }

}

