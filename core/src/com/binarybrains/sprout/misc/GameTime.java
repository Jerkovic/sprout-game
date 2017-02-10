package com.binarybrains.sprout.misc;

import com.badlogic.gdx.utils.TimeUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class GameTime
{
    private long start;
    private boolean paused = false;
    private long previousSecond;
    private long duration = 0;
    private Gdt gdt;

    public class Gdt {
        public long year = 1;
        public long season = 1; // if season > 4 then year +1
        public long day = 1; // if day > 28 then season +1
        public long hour = 0;
        public long minute = 0;

        public Gdt(long year, long season, long day, long hour, long minute) {
            this.year = year;
            this.season = season;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
        }
    }

    // make Singleton?
    public GameTime(long year, long season, long day, long hour, long minute)
    {
        gdt = new Gdt(year, season, day, hour, minute);
    }

    // use for fast forward time,mostly debug purposes
    public void setDuration(long dur) {
        duration = dur;
    }

    public void start()
    {
        start = TimeUtils.millis() / 1000;
    }

    public void update() {
        if (!paused && currentSecond() != previousSecond) {
            duration++; // seconds duration since the timer started
        }

        long seconds = duration; // make seconds to minutes
        gdt.day = TimeUnit.SECONDS.toDays(seconds);
        gdt.hour = TimeUnit.SECONDS.toHours(seconds) - TimeUnit.DAYS.toHours(gdt.day);
        gdt.minute = TimeUnit.SECONDS.toMinutes(seconds) - TimeUnit.DAYS.toMinutes(gdt.day) - TimeUnit.HOURS.toMinutes(gdt.hour);
    }

    public long currentSecond() {
        return TimeUtils.millis() / 1000;
    }

    public void resume() {
        paused = false;
    }

    public Gdt getGameTime() {
        return gdt;
    }


    public void paus() {
        paused = true;
    }

    public String toString() {
        return String.format("Day %d %02d:%02d", gdt.day, gdt.hour, gdt.minute); // => "6:30 AM 1:00 PM"
    }

}
