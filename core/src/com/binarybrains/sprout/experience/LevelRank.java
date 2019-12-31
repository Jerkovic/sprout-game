package com.binarybrains.sprout.experience;

import java.text.NumberFormat;
import java.util.Locale;

public class LevelRank {

    private int startThreshold;
    private int endThreshold;
    private int currentLevel;
    private int nextLevel;
    private float progress; // percent done until next level up
    private int levelGap;
    private int xp;

    @Override
    public String toString() {
        return "LevelRank{" +
                "xp=" + xp +
                ", startThreshold=" + startThreshold +
                ", endThreshold=" + endThreshold +
                ", currentLevel=" + currentLevel +
                ", nextLevel=" + nextLevel +
                ", levelGap=" + levelGap +
                ", progress=" + progress +
                '}';
    }

    private static int levelXP(int level) {
        return (int) (500 * (Math.pow((double) level+1, 2d)) - (500 * (double) (level+1)));
    }

    public static LevelRank getLevelProgression(int xp) {

        int currentLevel =  getLevelRankByXP(xp);
        int nextLevelXp = levelXP(currentLevel + 1);
        LevelRank levelRank = new LevelRank();
        levelRank.startThreshold = levelXP(currentLevel);
        levelRank.endThreshold = nextLevelXp - 1;
        levelRank.currentLevel = currentLevel;
        levelRank.nextLevel = currentLevel + 1;
        levelRank.xp = xp;

        levelRank.levelGap = levelRank.endThreshold - levelRank.startThreshold;
        int xpToNextLevel = (levelRank.levelGap - (xp - levelRank.startThreshold));
        levelRank.progress = ((float)(xp - levelRank.startThreshold) / (float)levelRank.levelGap) * 100f;
        return levelRank;
    }

    public float getProgress() {
        return progress;
    }

    public int getStartThreshold() {
        return startThreshold;
    }

    public int getEndThreshold() {
        return endThreshold;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getNextLevel() {
        return nextLevel;
    }

    public int getLevelGap() {
        return levelGap;
    }

    public int getXp() {
        return xp;
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
