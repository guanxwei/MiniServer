package org.miniserver.core.exceptions;

public class ConfigException extends Exception{

    private static final long serialVersionUID = -113007861349015611L;

    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(Throwable t) {
        super(t);
    }

    public ConfigException(String message, Throwable t) {
        super(message, t);
    }
}
