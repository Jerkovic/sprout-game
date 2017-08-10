package com.binarybrains.sprout.achievement;

import com.binarybrains.sprout.entity.Stats;
import org.junit.Before;
import org.junit.Test;

public class AchievementTest {

    Achievement achievement;
    Stats stats;

    @Before
    public void setUp() throws Exception {

        stats = new Stats();
        stats.score = 100;
        stats.zombie_kills = 11;

    }

    @Test
    public void tesSomething() throws Exception {
        for (int i = 0; i < Achievement.achievements.size(); i++) {
            Achievement.achievements.get(i).shallBeAwarded(stats);
            System.out.println(Achievement.achievements);
        }
    }
}
