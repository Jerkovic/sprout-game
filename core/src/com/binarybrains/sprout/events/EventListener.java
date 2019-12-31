package com.binarybrains.sprout.events;

public interface EventListener {
    void onReceivedEvent(IGameEvent event);
}