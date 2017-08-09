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

        achievement = new Achievement("Slayer", "Get a hundred kills");
        achievement.addProperty(new Property("100kills", 0, "kills", 100));

    }

    @Test
    public void tesSomething() throws Exception {
        System.out.println(achievement.getDesc());
        assertFalse(achievement.isUnlocked());
    }
}
