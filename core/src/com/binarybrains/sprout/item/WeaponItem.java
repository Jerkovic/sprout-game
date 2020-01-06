package com.binarybrains.sprout.item;

import com.binarybrains.sprout.item.weapon.Weapon;

public class WeaponItem extends ToolItem {
    public WeaponItem(Weapon weapon, int level) {
        super(weapon, level);
    }

    public String getRegionId() {
        return tool.getName().replace(" ", "_");
    }

    public String getName() {
        return tool.getName();
    }

    @Override
    public String getCategory() {
        return "Weapon";
    }

}
