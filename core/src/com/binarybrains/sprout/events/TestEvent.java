package com.binarybrains.sprout.events;

import com.binarybrains.sprout.entity.Player;

public class TestEvent implements IGameEvent {
    public Player player;

    public TestEvent(Player player) {
        this.player = player;
    }
}
