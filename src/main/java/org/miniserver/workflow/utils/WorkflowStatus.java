package org.miniserver.workflow.utils;

public enum WorkflowStatus {

    NORMAL("NORMAL"), FAILED("FAILED");

    private String statusCode;

    private WorkflowStatus(String statusCode) {
        this.statusCode = statusCode;
    };

    @Override
    public String toString() {
        return this.statusCode;
    }
}
