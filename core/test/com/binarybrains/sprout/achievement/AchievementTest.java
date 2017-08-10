package com.binarybrains.sprout.achievement;

import com.binarybrains.sprout.entity.Stats;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("deprecation")
public class AchievementTest {

    Achievement achievement;
    Stats stats;

    @Before
    public void setUp() throws Exception {
        stats = new Stats();
        stats.score = 100;
        stats.zombie_kills = 11;
        stats.potatoes = 10;
    }

    @Test
    public void testZombieSlayer1() throws Exception {
        Achievement achievement = Achievement.achievements.get("zombieSlayer1");
        Assert.assertEquals(true, achievement.shallBeAwarded(stats));

    }

    @Test
    public void testPotatofarmer() throws Exception {
        Achievement achievement = Achievement.achievements.get("potatofarmer");
        Assert.assertEquals(false, achievement.shallBeAwarded(stats));

    }


}
