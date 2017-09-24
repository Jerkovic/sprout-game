package com.binarybrains.sprout.item;


import com.badlogic.gdx.math.MathUtils;
import com.binarybrains.sprout.item.tool.Tool;

public class ToolItem extends Item {

    public Tool tool;
    public int count = 1;
    public int level;

    public static final int MAX_LEVEL = 3;
    public static final String[] LEVEL_NAMES = { //
            "Basic", "Iron", "Gold"//
    };

    public ToolItem(Tool tool, int level) {
        this.tool = tool;
        this.level = level;
    }

    /**
     * Gets damage value (tool level * 5)
     * 0 = 5
     * 1 = 10
     * 2 = 15
     * @return
     */
    public int getDamage() {
        return MathUtils.random((level+1) * 5, (level+2) * 5);
    }

    public String getRegionId() {

        String regid = LEVEL_NAMES[level] + "_" + tool.getName().replace(" ", "_");
        return regid.replace("Basic_", "");
    }

    public String getName() {
        return LEVEL_NAMES[level] + " " + tool.getName();
    }

    public boolean matches(Item item) {
        if (item instanceof ToolItem) {
            ToolItem other = (ToolItem) item;
            if (!other.tool.getClass().equals(tool.getClass())) return false;
            if (other.level != level) return false;
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return tool.getDescription();
    }
}
