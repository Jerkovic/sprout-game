package com.binarybrains.sprout.item;


import com.binarybrains.sprout.item.tool.Tool;

import java.util.Random;

public class ToolItem extends Item {

    public Tool tool;
    public int count = 1;

    public static final int MAX_LEVEL = 3;
    public static final String[] LEVEL_NAMES = { //
            "Basic", "Iron", "Gold"//
    };

    public int level = 0;


    public ToolItem(Tool tool, int level) {
        this.tool = tool;
        this.level = level;
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
