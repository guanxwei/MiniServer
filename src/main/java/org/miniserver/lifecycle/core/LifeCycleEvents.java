package org.miniserver.lifecycle.core;

public enum LifeCycleEvents {

    START("START"), STOP("STOP"), 
    BEFORE_FTART("BEFORE_START"), BEFORE_STOP("BEFORE_STOP"),
    AFTER_START("AFTER_START"), AFTER_STOP("AFTER_STOP");

    private String eventName;

    private LifeCycleEvents(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public String toString() {
        return this.eventName;
    }
}
