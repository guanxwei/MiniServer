package org.miniserver.core.config;

import java.util.Map;

import org.miniserver.lifecycle.util.Source;

public interface Config {

    public Source getSource();

    public Map<String, String> getParameters();

    public String getParameter(String name) ;

    public void initateParas(Map<String, String> params);

    public void addParam(String key, String value);
}
