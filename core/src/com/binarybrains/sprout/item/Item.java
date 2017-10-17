package com.binarybrains.sprout.item;

import com.binarybrains.sprout.entity.ItemEntity;

public abstract class Item implements ListItem {

    public String regionId;

    public String category;

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public void onTake(ItemEntity itemEntity) {

    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public boolean interact() {
        return false;
    }

    public boolean isDepleted() {
        return false;
    }

    public String getName() {
        return "";
    }

    public String getDescription() {
        return "";
    }

    public boolean matches(Item item) {
        return item.getClass() == getClass();
    }

    public String toString() {
        return getName();
    }

    public String getNotificationText() {
        return getName();
    }

    @Override
    public boolean isFood() {
        return false;
    }
}