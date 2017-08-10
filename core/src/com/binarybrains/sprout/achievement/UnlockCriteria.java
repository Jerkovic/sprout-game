package com.binarybrains.sprout.achievement;

public class UnlockCriteria
{
    private String name;
    private int valueNeeded;
    private String statKey;
    private Boolean unlocked;

    public UnlockCriteria(String name, String statKey, int valueNeeded) {
        this.name = name;
        this.valueNeeded = valueNeeded;
        this.statKey = statKey;
        this.unlocked = false;
    }

    public void setUnlocked() {
        unlocked = true;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public long getValueNeeded() {
        return valueNeeded;
    }

    public String getStatKey() { return statKey; }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "UnlockCriteria{" +
                "name='" + name + '\'' +
                ", valueNeeded=" + valueNeeded +
                ", statKey='" + statKey + '\'' +
                ", unlocked=" + unlocked +
                '}';
    }
}