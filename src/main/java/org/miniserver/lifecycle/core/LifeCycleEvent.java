package org.miniserver.lifecycle.core;

public class LifeCycleEvent {

    private LifeCycle source;
    private LifeCycleEvents type;

    public LifeCycleEvent(LifeCycle source, LifeCycleEvents type) {
        this.source = source;
        this.type = type;
    }

    public LifeCycle getSource() {
        return this.source;
    }

    public LifeCycleEvents getType() {
        return this.type;
    }
}
