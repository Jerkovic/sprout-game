package com.binarybrains.sprout.quest;

import com.binarybrains.sprout.award.Award;
import com.binarybrains.sprout.entity.npc.Npc;

import java.util.ArrayList;

public class Quest {

    private Npc npc;
    private String name;
    private Boolean active;
    private String desc;
    private ArrayList<ItemStack> itemStacks;
    private ArrayList <Award> awards;
    private boolean unlocked;


    public Quest(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public Quest setName(String name) {
        this.name = name;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public Quest setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Quest setDesc(String desc) {
        this.desc = desc;
        return this;
    }

    public ArrayList<ItemStack> getItemStacks() {
        return itemStacks;
    }

    public Quest setItemStacks(ArrayList<ItemStack> itemStacks) {
        this.itemStacks = itemStacks;
        return this;
    }

    public ArrayList<Award> getAwards() {
        return awards;
    }

    public Quest setAwards(ArrayList<Award> awards) {
        this.awards = awards;
        return this;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public Quest setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
        return this;
    }
}
