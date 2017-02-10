package com.binarybrains.sprout.misc;

import com.badlogic.gdx.utils.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class Timer
{
    private long start;
    private long secsToWait;

    public Timer()
    {
        this.secsToWait = secsToWait;
    }

    public void start()
    {
        start = TimeUtils.millis() / 1000;
    }

    public void paus() {

    }

    public long getDuration() {
        return 0; //(TimeUtils.millis() / 1000 - start);
    }

    public boolean hasCompleted()
    {
        return getDuration() - start >= secsToWait;
    }



    public static String formatTime(long sec) {
        long seconds = sec * 60;
        int day = (int) TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) -
                TimeUnit.DAYS.toHours(day);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) -
                TimeUnit.DAYS.toMinutes(day) -
                TimeUnit.HOURS.toMinutes(hours);

        return String.format("Day %d %02d:%02d", day, hours, minute); // => "6:30 AM 1:00 PM"
    }
}