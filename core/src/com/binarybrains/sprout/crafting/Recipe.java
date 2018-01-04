package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ListItem;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.ToolItem;
import com.binarybrains.sprout.item.resource.Resource;

import java.util.ArrayList;
import java.util.List;


public abstract class Recipe implements ListItem {
    public List<Item> costs = new ArrayList<Item>();
    public boolean canCraft = false;
    private Item resultItem;
    public boolean isUnlocked = true;
    public int xp = 0;
    private boolean removeRecipeOnCraft = false;
    private String identifier;

    public Recipe(Item resultTemplate) {
        this.identifier = resultTemplate.toString().replaceAll(" ", "_").toLowerCase();
        this.resultItem = resultTemplate;
    }

    public Recipe setLocked() {
        this.isUnlocked = false;
        return this;
    }

    /**
     * Unique identifier for recipe. for example "basic_scythe"
     * @return String
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Amount of Experience points gained when recipe is crafted
     * @param xp
     * @return
     */
    public Recipe setXP(int xp) {
        this.xp = xp;
        return this;
    }

    public int getXpGain() {
        return xp;
    }


    public Recipe setRemoveRecipeOnCrafted() {
        this.removeRecipeOnCraft = true;
        return this;
    }

    public boolean shouldRemoveRecipeOnCrafted() {
        return removeRecipeOnCraft;
    }

    public void setUnlocked() {
        this.isUnlocked = true;
    }

    public Recipe addCost(Resource resource, int count) {
        costs.add(new ResourceItem(resource, count));
        return this;
    }

    public Item getItem() {
        return resultItem;
    }

    public void checkCanCraft(Inventory inventory) {

        if (this instanceof ToolUpgradeRecipe) {
            // the inventory must have a previous level of that tool
            ToolItem upgrade = ((ToolItem) this.resultItem);
            ToolItem existTool = inventory.findToolByName(upgrade.getToolName());

            if (existTool != null && existTool.level == upgrade.level - 1) {
                // upgrade is legal ... check if we can afford it below
            } else {
                canCraft = false;
                return;
            }

        }

        for (int i = 0; i < costs.size(); i++) {
            Item item = costs.get(i);
            if (item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) item;
                if (!inventory.hasResources(ri.resource, ri.count)) {
                    canCraft = false;
                    return;
                }
            }
        }

        canCraft = true;
    }

    public abstract void craft(Inventory inventory);

    public void deductCost(Inventory inventory) {
        for (int i = 0; i < costs.size(); i++) {
            Item item = costs.get(i);
            if (item instanceof ResourceItem) {
                ResourceItem ri = (ResourceItem) item;
                inventory.removeResource(ri.resource, ri.count);
            }
        }
    }

    public List<Item> getCost() {
        return costs;
    }

    public String toString() {
        return resultItem.getName() + "(" + canCraft + ")";
    }

    public String getRegionId() {
        return resultItem.getName();
    }

    @Override
    public boolean isFood() {
        return false;
    }

    @Override
    public int getSellPrice() {
        return 0;
    }
}