package org.miniserver.lifecycle.util;

public class Source {

    private SourceType sourceType;
    private Object source;
    private long lastModifyTime;

    public Source(SourceType sourceType, Object source) {
        this.sourceType = sourceType;
        this.source = source;
    }

    public SourceType getSourceType() {
        return this.sourceType;
    }

    public Object getSource() {
        return this.source;
    }

    public void setModifyTime(long time) {
        this.lastModifyTime = time;
    }

    public long getLastModifyTime() {
        return this.lastModifyTime;
    }
}
