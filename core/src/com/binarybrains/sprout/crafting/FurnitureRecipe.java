package com.binarybrains.sprout.crafting;

import com.binarybrains.sprout.entity.Inventory;
import com.binarybrains.sprout.item.FurnitureItem;
import com.binarybrains.sprout.item.furniture.Furniture;
import com.binarybrains.sprout.item.furniture.Furnitures;

public class FurnitureRecipe extends Recipe {

    Furniture furniture;
    int level;

    public FurnitureRecipe(Furniture furniture, int level) {
        super(new FurnitureItem(furniture, level));
        this.furniture = furniture;
        this.level = level;
    }

    @Override
    public void craft(Inventory inventory) {
        inventory.add(new FurnitureItem(Furnitures.furnace, level));
    }
}
