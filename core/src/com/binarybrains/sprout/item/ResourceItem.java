package com.binarybrains.sprout.item;


import com.binarybrains.sprout.item.resource.FoodResource;
import com.binarybrains.sprout.item.resource.Resource;

public class ResourceItem extends Item {

    public Resource resource;
    public int count = 1;

    public ResourceItem(Resource resource) {
        this.resource = resource;
    }

    public ResourceItem(Resource resource, int count) {
        this.resource = resource;
        this.count = count;
    }

    public String getRegionId() {
        return resource.name.replaceAll(" ", "_");
    }

    @Override
    public String getDescription() {
        return resource.description;
    }

    @Override
    public boolean isFood() {
        return (resource instanceof FoodResource);
    }

    @Override
    public int getSellPrice() {
        return this.resource.getSellPrice();
    }

    @Override
    public String getCategory() {
        if (resource.getClass().getSimpleName().equals("Resource")) {
            return "Resource";
        } else {
            return  resource.getClass().getSimpleName().replace("Resource", "");
        }
    }

    public String getName() {
        return resource.name;
    }

    public void onTake() { }

    public boolean isDepleted() {
        return count <= 0;
    }
}
