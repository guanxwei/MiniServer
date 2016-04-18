package org.miniserver.lifecycle.core;

import org.miniserver.lifecycle.exceptions.LifeCycleException;

/**
 * Core interface that every object should implement if you want to be managed automatically.
 * Standard way to use the LifeCycle is start -> stop, we do not encourage to use or frozen and recover.
 */
public interface LifeCycle {

    /**
     * Start the component.
     */
    public void start() throws LifeCycleException;

    /**
     * Stop the component, the outer container or parent context should invoke this method.
     */
    public void stop();

    public void addListener(LifeCycleEventListener listener);

    public void emmitEvent(LifeCycleEvent event);
}
