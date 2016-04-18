package org.miniserver.lifecycle.util;

public enum SourceType {

    FILE("file"), CLASS("class"), MEMORY("memory"), DIRECTORY("directory");

    private String type;

    private SourceType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
