package org.miniserver.core.config;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

import org.miniserver.core.container.Context;

public class ServletConfigImp implements ServletConfig{

    private String servletName;
    private Context context;
    private Map<String, String> initiateParameters;

    public ServletConfigImp(String servletName, Context context) {
        this.servletName = servletName;
        this.context = context;
        this.initiateParameters = new Hashtable<String, String>();
    }

    @Override
    public String getServletName() {
        return this.servletName;
    }

    @Override
    public ServletContext getServletContext() {
        return this.context;
    }

    @Override
    public String getInitParameter(String name) {
        return initiateParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return ((Hashtable<String, String>)initiateParameters).elements();
    }

}
