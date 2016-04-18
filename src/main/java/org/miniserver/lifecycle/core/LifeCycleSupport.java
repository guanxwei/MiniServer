package org.miniserver.lifecycle.core;

import java.util.LinkedList;
import java.util.List;

public class LifeCycleSupport {

    private List<LifeCycleEventListener> listeners;

    public LifeCycleSupport() {
        this.listeners = new LinkedList<LifeCycleEventListener>();
    }

    public void addListener(LifeCycleEventListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(LifeCycleEventListener listener) {
        this.listeners.remove(listener);
    }

    public List<LifeCycleEventListener> findListeners() {
        return this.listeners;
    }

    public LifeCycleEventListener findListener(String name) {
        for (LifeCycleEventListener listener : listeners) {
            if (listener.getName().equals(name)) {
                return listener;
            }
        }
        return null;
    }

    public void emitEvent(LifeCycleEvent event) {
        for (LifeCycleEventListener listener : listeners) {
            listener.handleEvent(event);
        }
    }
}
