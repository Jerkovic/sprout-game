package com.binarybrains.sprout.experience;

import java.text.NumberFormat;
import java.util.Locale;

public class LevelRank {

    public static int levelXP(int level) {
        return (int) (500d * (Math.pow((double) level+1, 2d)) - (500 * (double) (level+1)));
    }

    public static void progressionChartDebug() {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);

        for (int level = 0; level < 100; level++) {
            System.out.println("Lvl: " + level + " Points: " + numberFormat.format(levelXP(level)));
        }
    }
}
