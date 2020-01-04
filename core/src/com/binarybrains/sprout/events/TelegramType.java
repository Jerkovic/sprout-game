package com.binarybrains.sprout.events;

public class TelegramType {

    // Player messages
    public static final int PLAYER_STATS_UPDATED = 0; // generic stat event
    public static final int PLAYER_STATS_XP_INCREASED = 1;
    public static final int PLAYER_STATS_MONEY_UPDATED = 5;

    public static final int PLAYER_STATS_RANK_INCREASED = 10;
    public static final int PLAYER_STATS_HEALTH_DECREASED = 20;
    public static final int PLAYER_PASSED_OUT = 30;

    public static final int PLAYER_ACHIEVEMENT_UNLOCKED = 99;
    public static final int PLAYER_CRAFTING_SUCCESS = 100;
    public static final int PLAYER_CRAFTING_FAILURE = 101;
    public static final int PLAYER_INVENTORY_UPDATED = 102;
    public static final int PLAYER_INVENTORY_CHANGED_SELECTED_SLOT = 110;

    // Some others
    public static final int TIME_MINUTE_INC = 1001;
    public static final int TIME_HOUR_INC = 1002;
    public static final int TIME_DAY_INC = 1003;

}
