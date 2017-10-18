package com.binarybrains.sprout.experience;

import java.text.NumberFormat;
import java.util.Locale;

public class LevelRank {

    private int startThreshold;
    private int endThreshold;
    private int progress;

    public static int levelXP(int level) {
        return (int) (500 * (Math.pow((double) level+1, 2d)) - (500 * (double) (level+1)));
    }

    public static LevelRank getLevelProgression() {
        return new LevelRank();
    }

    public static void progressionChartDebug() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        for (int level = 0; level <= 100; level++) {
            System.out.println("Lvl: " + level + " Points: " + numberFormat.format(levelXP(level)));
        }
    }

    public static int getLevelRankByXP(int xp) {
        for (int level = 100; level >= 0; level--) {
            if (xp >= levelXP(level)) return level;
        }
        return 100; // max rank?
    }
}
