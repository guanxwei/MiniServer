package org.miniserver.core.exceptions;

public class ContainerExeception extends Exception{

    private static final long serialVersionUID = 8688137209664536052L;

    public ContainerExeception() {
        super();
    }

    public ContainerExeception(String message) {
        super(message);
    }

    public ContainerExeception(Throwable t) {
        super(t);
    }

    public ContainerExeception(String message, Throwable t) {
        super(message, t);
    }
}
