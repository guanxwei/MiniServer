package org.miniserver.workflow.io;

public class Anything {

    private Object value;

    public void storeValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    @Override
    public boolean equals(Object another) {
        return value.equals(((Anything)another).getValue());
    }
}
