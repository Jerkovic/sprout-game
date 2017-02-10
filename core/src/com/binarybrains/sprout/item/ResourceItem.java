package com.binarybrains.sprout.item;


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

    public String getName() {
        return resource.name;
    }

    public void onTake() {
    }


    public boolean isDepleted() {
        return count <= 0;
    }
}
