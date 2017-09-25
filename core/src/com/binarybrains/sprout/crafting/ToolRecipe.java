package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;


public class ToolRecipe extends Recipe {
    private int level;
    private Tool tool;

    public ToolRecipe(Tool tool, int level) {
        super(new ToolItem(tool, level));
        this.level = level;
        this.tool = tool;
    }

    @Override
    public void craft(Inventory inventory) {
        // remove recently upgraded tool?
        inventory.add(new ToolItem(tool, level));
    }

    public String getDescription() {
        return "tools are fun and useful";
    }
}
