package org.miniserver.core.config;

import java.util.Map;

import org.miniserver.lifecycle.util.Source;

public class ServerConfig implements Config{

    /**
     * The souce that this config serves.
     */
    private Source source;

    private Map<String, String> params;

    public ServerConfig(Source source) {
        this.source = source;
    }

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
        return params.get(key);
    }

    @Override
    public void initateParas(Map<String, String> params) {
        this.params = params;
    }

    @Override
    public void addParam(String key, String value) {
        this.params.put(key, value);
    }
}
