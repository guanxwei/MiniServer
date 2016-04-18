package org.miniserver.workflow.io;

import java.util.HashMap;
import java.util.Map;

public class Bag {

    private Map<String, Anything> storage;

    public Bag() {
        this.storage = new HashMap<String, Anything>();
    }

    public Bag(Map<String, Anything> map) {
        this.storage = map;
    }

    public Anything getValue(String name) {
        return this.storage.get(name);
    }

    public void storeValue(String name, Anything value) {
        this.storage.put(name, value);
    }
}
