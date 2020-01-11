package com.binarybrains.sprout.item;

import com.binarybrains.sprout.item.furniture.Furniture;

public class FurnitureItem extends Item {

    Furniture furniture;

    public FurnitureItem(Furniture furniture, int level)
    {
        this.furniture = furniture;
    }

    @Override
    public boolean interact() {
        return false;
    }

    @Override
    public String getName() {
        return furniture.getName();
    }

    @Override
    public String getDescription() {
        return furniture.getDescription();
    }

    @Override
    public String getRegionId() {
        return furniture.name.replaceAll(" ", "_");
    }

    @Override
    public String getCategory() {
        return "Crafting Station";
    }


}

