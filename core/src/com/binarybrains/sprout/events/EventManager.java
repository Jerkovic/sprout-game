package com.binarybrains.sprout.events;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class EventManager {
    Map<String, List<EventListener>> listeners = new HashMap<String, List<EventListener>>();

    public EventManager(EventListener... operations) {
        for (EventListener operation : operations) {
            this.listeners.put(operation.getClass().getSimpleName(), new ArrayList<EventListener>());
        }
    }

    public void subscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.add(listener);
    }

    public void unsubscribe(String eventType, EventListener listener) {
        List<EventListener> users = listeners.get(eventType);
        users.remove(listener);
    }

    /**
     * @usage notify(PurseEvent(10));
     * @param eventType
     * @param event
     */
    public void notify(IGameEvent event) {
        List<EventListener> users = listeners.get(event.getClass().getSimpleName());
        for (EventListener listener : users) {
            listener.update(event);
        }
    }
}
