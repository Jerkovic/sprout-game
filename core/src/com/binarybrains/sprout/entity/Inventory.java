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
    private static int UPGRADE_SLOTS = 12;
    private static int MAX_UPGRADE_SLOTS = 36;
    private int capacity;

    public Inventory(Level level, int capacity) {
        this.level = level;
        setCapacity(capacity);
        createEmptySlots(); // fill with empty slots
    }

    /**
     * Upgrade Inventory
     * @return
     */
    public boolean upgrade() {
        if (Inventory.MAX_UPGRADE_SLOTS == getCapacity()) return false;
        setCapacity(getCapacity()+ Inventory.UPGRADE_SLOTS);
        fillEmptySlots();
        return true;
    }

    private void createEmptySlots() {
        for (int i = 0; i < getCapacity(); i++) {
            items.add(i, null);
        }
    }

    private void fillEmptySlots() {
        for(int i = items.size(); i < getCapacity(); i++) {
            items.add(i, null);
        }
    }

    public boolean add(Item item) {
        return add(findEmptySlot(), item);
    }

    public Item replace(int slot, Item item) {
        // what to do
        try {
            Item replacedItem = items.get(slot);
            items.remove(slot);
            items.add(slot, item);
            return replacedItem;
        } catch (Exception e) {
            System.out.println("Error Inventory: " + e);
            return null;
        }
    }

    // not used but all new additions should not return bool but the item
    public boolean add(int slot, Item item) {
        if (item instanceof ResourceItem) {
            ResourceItem toTake = (ResourceItem) item;
            ResourceItem has = findResource(toTake.resource);

            if (has == null) {
                if (count() < capacity) {
                    items.remove(slot);
                    items.add(slot, toTake);
                    fillEmptySlots();
                } else {
                    return false;
                }
            } else {
                has.count += toTake.count;
            }

        } else { // like tools and single instance items
            if (count() < capacity) {
                items.remove(slot);
                items.add(slot, item);
                fillEmptySlots();
            } else {
                return false;
            }
        }

        // move the code below
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

    public boolean removeItem(Item item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null && items.get(i).matches(item)) {
                items.remove(i);
                fillEmptySlots();
                return true;
            }
        }
        return false;
    }

    public boolean removeResource(Resource r, int count) {
        ResourceItem ri = findResource(r);
        if (ri == null) return false;
        if (ri.count < count) return false;
        ri.count -= count;
        if (ri.count <= 0) items.remove(ri);
        fillEmptySlots();
        return true;
    }

    /**
     * @todo we acually have to check if when we craft something if that leaves a spot after crafting that we
     * can grant that
     * @param item
     * @return
     */
    public boolean hasSpaceFor(Item item) {
        if (item instanceof ResourceItem) {
            ResourceItem ri = findResource(((ResourceItem) item).resource);
            if (ri != null) {
                return true;
            }

            // new resource
            if (!isFull()) {
                return true;
            }
        } else {
            int count = 0;
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) != null && items.get(i).matches(item)) count++;
            }
            if (count > 0) {
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
            if (item == null) return 0;
            int count = 0;
            for (int i = 0; i < items.size(); i++) {
                Item it = items.get(i);
                if (it != null && it.matches(item)) count++;
            }
            return count;
        }
        return 0;
    }

    public int count() {
        int count = 0;
        for (Item it : items) {
            if (it != null) count++;
        }
        return count;

    }

    private int findEmptySlot() {
        int index = 0;
        for (Item it : items) {
            if (it == null) return index;
            index++;
        }
        return -1;
    }

    private boolean isFull() {
        return count() >= getCapacity();
    }

    public void renderDebug() {
        System.out.println("****************************************************************************");
        System.out.println("Inventory  Resources: " + items.size() + " / " + capacity);
        System.out.println("****************************************************************************");
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) != null) {
                System.out.println("Slot: " + i + " " + items.get(i).getRegionId() + ", " + items.get(i).getName() + " x " + count(items.get(i)));
            } else {
                System.out.println("Slot: " + i + " Empty");
            }

            System.out.println("-----------------------------------------------------------------------------------");
        }
        System.out.println("****************************************************************************");
    }

    public List<Item> getItems() {
        return items;
    }

    public void removeSlot(int checkedIndex) {
        items.remove(checkedIndex);
        items.add(checkedIndex, null); // test empty slot
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}