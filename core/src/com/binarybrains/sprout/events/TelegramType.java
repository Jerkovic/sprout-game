package com.binarybrains.sprout.events;

public class TelegramType {

    // Player messages
    public static final int PLAYER_STATS_XP_INCREASED = 0;
    public static final int PLAYER_STATS_RANK_INCREASED = 10;
    public static final int PLAYER_STATS_HEALTH_DECREASED = 20;

    public static final int PLAYER_CRAFTING_SUCCESS = 30;
    public static final int PLAYER_CRAFTING_FAILURE = 31;

    // Some others
    public static final int TIME_MINUTE_INC = 1001;
    public static final int TIME_HOUR_INC = 1002;
    public static final int TIME_DAY_INC = 1003;
}
