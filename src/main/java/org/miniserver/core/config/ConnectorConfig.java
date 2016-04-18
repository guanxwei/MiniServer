package org.miniserver.core.config;

import java.util.HashMap;
import java.util.Map;

import org.miniserver.lifecycle.util.Source;

public class ConnectorConfig implements Config{

    private Map<String, String> params;
    private Source source;

    @Override
    public Source getSource() {
        return this.source;
    }

    @Override
    public Map<String, String> getParameters() {
        return this.params;
    }

    @Override
    public String getParameter(String key) {
        return this.params.get(key);
    }

    @Override
    public void initateParas(Map<String, String> params) {
        this.params = new HashMap<String, String>();
    }

    @Override
    public void addParam(String key, String value) {
        this.params.put(key, value);
    }

}
