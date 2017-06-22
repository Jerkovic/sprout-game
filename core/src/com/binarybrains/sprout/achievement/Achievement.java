package com.binarybrains.sprout.achievement;

import java.util.ArrayList;

public class Achievement
{
    private String name;
    private String desc;
    private ArrayList <Property> props;
    private boolean unlocked;

    public Achievement(String theId, String desc, ArrayList <Property> theRelatedProps) {
        this.name = theId;
        this.desc = desc;
        this.props = theRelatedProps;
        this.unlocked = false;
    }

    public String getDesc() {
        return desc;
    }

    public ArrayList getProps() {
        return props;
    }

    public void setProps(ArrayList<Property> props) {
        this.props = props;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }

}