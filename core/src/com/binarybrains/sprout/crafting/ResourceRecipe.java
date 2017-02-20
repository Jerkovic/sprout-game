package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.resource.Resource;

public class ResourceRecipe extends Recipe {
    private Resource resource;

    public ResourceRecipe(Resource resource) {
        super(new ResourceItem(resource, 1));
        this.resource = resource;
    }

    public void craft(Inventory inventory) {

        inventory.add(new ResourceItem(resource, 1));
    }

    public String getDescription() {
        return "no description available";
    }
}
