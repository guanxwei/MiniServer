package org.miniserver.workflow.exceptions;

public class WorkflowException extends Exception{

    private static final long serialVersionUID = 2607111255961547310L;

    public WorkflowException() {
        super();
    }

    public WorkflowException(String message) {
        super(message);
    }

    public WorkflowException(Throwable t) {
        super(t);
    }

    public WorkflowException(String message, Throwable t) {
        super(message, t);
    }
}
