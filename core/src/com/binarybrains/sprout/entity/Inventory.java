package com.binarybrains.sprout.entity;


import com.badlogic.gdx.Gdx;
import com.binarybrains.sprout.item.Item;
import com.binarybrains.sprout.item.ResourceItem;
import com.binarybrains.sprout.item.resource.Resource;
import com.binarybrains.sprout.level.Level;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    public Level level;
    public List<Item> items = new ArrayList<Item>();

    private int capacity; // this can be upgraded

    public Inventory(Level level, int capacity) {
        this.level = level;
        this.capacity = capacity;
    }

    public boolean add(Item item) {
        return add(items.size(), item);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean add(int slot, Item item) {

        Gdx.app.log("Inventory", "Adding " + item +  " to inventory slot " + slot);

        if (item instanceof ResourceItem) {
            ResourceItem toTake = (ResourceItem) item;
            ResourceItem has = findResource(toTake.resource);

            if (has == null) {
                if (count() < capacity) {
                    items.add(slot, toTake);
                    try {
                        level.screen.hud.addNotification(toTake);
                    } catch (NullPointerException e) {
                    }
                } else {
                    Gdx.app.log("Inventory", "Inventory is full. Max capacity:" + capacity);
                    return false;
                }
            } else {
                has.count += toTake.count;
                try {
                    level.screen.hud.addNotification(toTake);
                } catch (NullPointerException e) {
                }
            }
        } else { // like tools and stuff

            if (count() < capacity) {
                items.add(slot, item);

                try {
                    level.screen.hud.addNotification(item);
                } catch (NullPointerException e) {
                }
            } else {
                Gdx.app.log("Inventory", "Inventory is full. Max capacity:" + capacity);
                return false;
            }
        }

        try {
            level.screen.hud.refreshInventory();
        } catch (NullPointerException e) {
            //
        }
        return true;

    }

    private ResourceItem findResource(Resource resource) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof ResourceItem) {
                ResourceItem has = (ResourceItem) items.get(i);
                if (has.resource == resource) return has;
            }
        }
        return null;
    }

    public boolean hasResources(Resource r, int count) {
        ResourceItem ri = findResource(r);
        if (ri == null) return false;
        return ri.count >= count;
    }

    public boolean removeResource(Resource r, int count) {
        ResourceItem ri = findResource(r);
        if (ri == null) return false;
        if (ri.count < count) return false;
        ri.count -= count;
        if (ri.count <= 0) items.remove(ri);
        return true;
    }

    public boolean hasSpaceFor(Item item) {
        if (item instanceof ResourceItem) {
            ResourceItem ri = findResource(((ResourceItem) item).resource);
            if (ri != null) return true;
        } else {
            int count = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).matches(item)) count++;
            }
            if (count > 0) {
                Gdx.app.log("Inventory", "You already have a " + item.getName());
                return false;
            }

            if (!isFull()) {
                return true;
            }
        }
        return false;
    }

    public int count(Item item) {
        if (item instanceof ResourceItem) {
            ResourceItem ri = findResource(((ResourceItem)item).resource);
            if (ri != null) return ri.count;
        } else {
            int count = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).matches(item)) count++;
            }
            return count;
        }
        return 0;
    }

    public int count() {
        return items.size();
    }

    public boolean isFull() {

        return count() >= getCapacity();
    }

    public void renderDebug() {
        System.out.println("****************************************************************************");
        System.out.println("Inventory  Items: " + items.size() + " / " + capacity);
        System.out.println("****************************************************************************");
        for (int i = 0; i < items.size(); i++) {
            System.out.println("Slot: " + i + " " + items.get(i).getRegionId() + ", " + items.get(i).getName() + " x " + count(items.get(i)));
            System.out.println("-----------------------------------------------------------------------------------");
        }
        System.out.println("****************************************************************************");
    }

    public List<Item> getItems() {
        return items;
    }

    public void equipTo(Player player, int checkedIndex) {
        // do something like this pif the entity already has a activeItem
        // items.add(0, entityPlayer.activeItem);
        items.remove(checkedIndex);
        // entityPlayer.activeItem = item;
    }

    public int getCapacity() {
        return capacity;
    }
}