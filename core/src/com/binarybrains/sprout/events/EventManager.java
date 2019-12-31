package com.binarybrains.sprout.events;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class EventManager {
    Map<Class<? extends IGameEvent>, List<EventListener>> registry = new HashMap<Class<? extends IGameEvent>, List<EventListener>>();

    public EventManager() {

    }

    public void subscribe(Class<? extends IGameEvent> clazz, EventListener listener) {
        List<EventListener> listeners = registry.getOrDefault(clazz, new ArrayList<EventListener>());
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
        registry.put(clazz, listeners);
    }

    public void unsubscribe(Class<? extends IGameEvent> clazz, EventListener listener) {
        List<EventListener> listeners = registry.get(clazz);
        listeners.remove(listener);
        registry.put(clazz, listeners);
    }

    /**
     * @usage
     * @param eventType
     * @param event
     */
    public void notify(IGameEvent event) {
        List<EventListener> listeners = registry.get(event.getClass());
        for (EventListener listener : listeners) {
            listener.onReceivedEvent(event);
        }
    }
}
