package com.binarybrains.sprout.achievement;

public class UnlockCriteria
{
    private String name;
    private int valueNeeded;
    private int currentValue;
    private String statKey;
    private Boolean unlocked;

    public UnlockCriteria(String name, String statKey, int valueNeeded) {
        this.name = name;
        this.valueNeeded = valueNeeded;
        this.currentValue = 0;
        this.statKey = statKey;
        this.unlocked = false;
    }

    public void setUnlocked() {
        unlocked = true;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public int getValueNeeded() {
        return valueNeeded;
    }

    public void setCurrentValue(int value) {
        currentValue = value;
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
                ", currentValue=" + currentValue +
                ", valueNeeded=" + valueNeeded +
                ", statKey='" + statKey + '\'' +
                ", unlocked=" + unlocked +
                '}';
    }
}