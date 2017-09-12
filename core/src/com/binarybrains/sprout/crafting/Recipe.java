package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ListItem;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.resource.Resource;

import java.util.ArrayList;
import java.util.List;


public abstract class Recipe implements ListItem {
    public List<Item> costs = new ArrayList<Item>();
    public boolean canCraft = false;
    public Item resultTemplate;
    public boolean isUnlocked = true;

    public Recipe(Item resultTemplate) {
        this.resultTemplate = resultTemplate;
    }

    public Recipe setLocked() {
        this.isUnlocked = false;
        return this;
    }

    public Recipe unlockRecipes() {
        return this;
    }

    public void setUnlocked() {
        this.isUnlocked = true;
    }

    public Recipe addCost(Resource resource, int count) {
        costs.add(new ResourceItem(resource, count));
        return this;
    }

    public Item getItem() {
        return resultTemplate;
    }

    public void checkCanCraft(Inventory inventory) {
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
        return resultTemplate.getName() + "(" + canCraft + ")";
    }

    public String getRegionId() {
        return resultTemplate.getName();
    }
}