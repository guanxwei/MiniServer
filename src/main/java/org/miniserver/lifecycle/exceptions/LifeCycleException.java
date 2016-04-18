package org.miniserver.lifecycle.exceptions;

public class LifeCycleException extends Exception {

    private static final long serialVersionUID = 6356558522245864620L;

    public LifeCycleException() {
        super();
    }

    public LifeCycleException(String message) {
        super(message);
    }

    public LifeCycleException(Throwable t) {
        super(t);
    }

    public LifeCycleException(String message, Throwable t) {
        super(message, t);
    }
}
