package org.miniserver.lifecycle.core;

public interface LifeCycleEventListener {

    public String getName();

    public void handleEvent(LifeCycleEvent event);
}
