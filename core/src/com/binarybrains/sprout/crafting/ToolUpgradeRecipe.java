package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.tool.Tool;


public class ToolUpgradeRecipe extends Recipe {
    private int level;
    private Tool tool;

    public ToolUpgradeRecipe(Tool tool, int level) {
        super(new ToolItem(tool, level));
        this.level = level;
        this.tool = tool;
    }

    @Override
    public void craft(Inventory inventory) {
        inventory.upgradeTool(tool.getName(), level);
    }

    public String getDescription() {
        return "";
    }
}
