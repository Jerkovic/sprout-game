package com.binarybrains.sprout.achievement;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertFalse;

public class AchievementTest {

    Achievement achievement;

    @Before
    public void setUp() throws Exception {
        ArrayList<Property> propertyList = new ArrayList<Property>();
        propertyList.add(new Property("100kills", 0, "kills", 100));
        achievement = new Achievement("Slayer", "Get a hundred kills",propertyList);
    }

    @Test
    public void tesSomething() throws Exception {
        System.out.println(achievement.getDesc());
        assertFalse(achievement.isUnlocked());
    }
}
