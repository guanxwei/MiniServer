package org.miniserver.core.container;

import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import org.miniserver.core.exceptions.ContainerExeception;
import org.miniserver.core.http.HttpRequest;
import org.miniserver.core.http.HttpResponse;
import org.miniserver.lifecycle.core.LifeCycleEvent;
import org.miniserver.lifecycle.core.LifeCycleEventListener;
import org.miniserver.lifecycle.core.LifeCycleEvents;
import org.miniserver.lifecycle.core.LifeCycleSupport;
import org.miniserver.lifecycle.exceptions.LifeCycleException;
import org.miniserver.lifecycle.util.Source;
import org.miniserver.lifecycle.util.SourceType;
import org.miniserver.workflow.core.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context implements Container, ServletContext{

    private static final Logger logger = LoggerFactory.getLogger(Context.class);

    /**
     * Children containers list, to a context, its children are instances of Worker class, which is the wrapper of a servlet.
     */
    private List<Container> children;

    /**
     * Tag to indicate that if the container has been started.
     */
    private boolean started;

    /**
     * LifeCycleEevent supporter, which will provide lifecycycle related event tools.
     * Functionality like register a lifecycle evenet listener, fire a lifecycle event to a specific listener.
     */
    private LifeCycleSupport supporter;

    /**
     * The classes used by this context will be loaded by its contextLoader;
     */
    private ClassLoader contextLoader;

    /**
     * Servlet name to servlet implementation class mapping holder.
     */
    private Map<String, String> servletNameMapping;

    /**
     * Filters for this context.
     */
    private List<Filter> filters;

    /**
     * Representation of the base dir of the context, whoes {@link SourceType} is {@link SourceType.DIRECTORY}.
     */
    private Source source;

    /**
     * Registered listeners;
     */
    private List<ServletContextListener> listeners;

    /**
     * The rootPath of the context;
     */
    private String rootPath;

    @Override
    public void start() throws LifeCycleException {
        if (started) {
            System.err.println("Container has been started!");
        }
        fireEvent(new LifeCycleEvent(this, LifeCycleEvents.BEFORE_FTART));
        for (Container child : children) {
            child.start();
        }
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getContextPath() {
        File dir = (File) this.source.getSource();
        return dir.getPath();
    }

    @Override
    public ServletContext getContext(String uripath) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMajorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getMinorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getEffectiveMajorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getEffectiveMinorVersion() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getMimeType(String file) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public URL getResource(String path) throws MalformedURLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Servlet getServlet(String name) throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration<Servlet> getServlets() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration<String> getServletNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRealPath(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getServerInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getInitParameter(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean setInitParameter(String name, String value) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Object getAttribute(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(String name, Object object) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void removeAttribute(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getServletContextName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dynamic addServlet(String servletName, String className) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dynamic addServlet(String servletName, Servlet servlet) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Dynamic addServlet(String servletName,
            Class<? extends Servlet> servletClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Servlet> T createServlet(Class<T> clazz)
            throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ServletRegistration getServletRegistration(String servletName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public javax.servlet.FilterRegistration.Dynamic addFilter(
            String filterName, String className) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public javax.servlet.FilterRegistration.Dynamic addFilter(
            String filterName, Filter filter) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public javax.servlet.FilterRegistration.Dynamic addFilter(
            String filterName, Class<? extends Filter> filterClass) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends Filter> T createFilter(Class<T> clazz)
            throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FilterRegistration getFilterRegistration(String filterName) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SessionCookieConfig getSessionCookieConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setSessionTrackingModes(
            Set<SessionTrackingMode> sessionTrackingModes) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void addListener(String className) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public <T extends EventListener> void addListener(T t) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addListener(Class<? extends EventListener> listenerClass) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public <T extends EventListener> T createListener(Class<T> clazz)
            throws ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public JspConfigDescriptor getJspConfigDescriptor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ClassLoader getClassLoader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void declareRoles(String... roleNames) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getVirtualServerName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Container getParent() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setParent(Container parent) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addChild(Container child) throws ContainerExeception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void fireEvent(LifeCycleEvent event) {
        this.supporter.emitEvent(event);
    }

    @Override
    public void setDefaultNode(Node defaultNode) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Node getDefaultNode() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * A context's class loader will be responsible for loading the classed located in these locations : 
     * (1) {baseDir}/WEB-INF/lib
     * (2) {baseDir}/WEB-INF/classes
     */
    @Override
    public ClassLoader getLoader() {
        return this.contextLoader;
    }

    @Override
    public String getInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void log(String msg) {
        logger.info(msg);
    }

    @Override
    public void log(Exception exception, String msg) {
        logger.info(msg, exception);
    }

    @Override
    public void log(String message, Throwable throwable) {
        logger.info(message, throwable);
    }

    @Override
    public void invoke(HttpRequest request, HttpResponse response) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addListener(LifeCycleEventListener listener) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void emmitEvent(LifeCycleEvent event) {
        // TODO Auto-generated method stub
        
    }

    private void addServletContextListener(ServletContextListener listener) {
        if (this.listeners == null) {
            this.listeners = new LinkedList<ServletContextListener>();
        }
        this.listeners.add(listener);
    }
}
