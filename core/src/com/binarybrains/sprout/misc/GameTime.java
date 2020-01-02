package com.binarybrains.sprout.misc;

import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.badlogic.gdx.utils.TimeUtils;
import com.binarybrains.sprout.events.TelegramType;

import java.util.concurrent.TimeUnit;

public class GameTime implements Telegraph
{
    private long start;
    private boolean paused = false;
    private long previousSecond = -1;
    private long previousMinute = -1;
    private long previousHour = -1;
    private long duration = 0;
    private Gdt gdt;

    @Override
    public boolean handleMessage(Telegram msg) {
        return false;
    }

    private class Gdt {
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

        public String toString() {
            return String.format("Day %d %02d:%02d", gdt.day, gdt.hour, gdt.minute);
        }
    }

    // make Singleton?
    public GameTime(long year, long season, long day, long hour, long minute)
    {
        gdt = new Gdt(year, season, day, hour, minute);
    }

    public void setDuration(long dur) {
        duration = dur;
    }

    public void start()
    {
        start = TimeUtils.millis() / 1000;
    }

    public void update() {
        if (!paused && currentSecond() != previousSecond) {
            duration++; // seconds duration since the game (timer) started
        }

        gdt.day = TimeUnit.SECONDS.toDays(duration);
        gdt.hour = TimeUnit.SECONDS.toHours(duration) - TimeUnit.DAYS.toHours(gdt.day);
        gdt.minute = TimeUnit.SECONDS.toMinutes(duration) - TimeUnit.DAYS.toMinutes(gdt.day) - TimeUnit.HOURS.toMinutes(gdt.hour);

        if (previousMinute != gdt.minute) {
            MessageManager.getInstance().dispatchMessage(TelegramType.TIME_MINUTE_INC, gdt);
        }
        if (previousHour != gdt.hour) {
            MessageManager.getInstance().dispatchMessage(TelegramType.TIME_HOUR_INC, gdt);
        }

        previousMinute = gdt.minute;
        previousHour = gdt.hour;
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

    public void pause() {
        paused = true;
    }
}
