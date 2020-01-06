package com.binarybrains.sprout.item.tool;

import com.binarybrains.sprout.item.weapon.Weapon;

public class Mace extends Weapon {
    public Mace() {
        super("Mace", "A medieval weapon. This is a replica.");
        setCoolDownTime(500);
    }
}
