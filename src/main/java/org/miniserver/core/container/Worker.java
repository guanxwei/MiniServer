package org.miniserver.core.container;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.miniserver.core.exceptions.ContainerExeception;
import org.miniserver.core.http.HttpRequest;
import org.miniserver.core.http.HttpResponse;
import org.miniserver.core.plugin.ServletNode;
import org.miniserver.lifecycle.core.LifeCycleEvent;
import org.miniserver.lifecycle.core.LifeCycleEventListener;
import org.miniserver.lifecycle.core.LifeCycleSupport;
import org.miniserver.lifecycle.exceptions.LifeCycleException;
import org.miniserver.workflow.core.Node;
import org.miniserver.workflow.core.NodeContext;
import org.miniserver.workflow.core.Workflow;
import org.miniserver.workflow.core.defaultimp.MemoryWorkflow;
import org.miniserver.workflow.core.defaultimp.StandardContext;
import org.miniserver.workflow.exceptions.WorkflowException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A worker is a wrapper of a specific Servlet object.
 */
public class Worker implements Container{

    private static final Logger logger = LoggerFactory.getLogger(Worker.class);

    private Container parent;
    private LifeCycleSupport supporter;
    private boolean started;
    private Workflow workflow;
    private Node defaultNode;
    private ServletConfig servletConfig;

    /**
     * Worker will be initiated by a context container.
     * @param servletConfig
     */
    public Worker(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
        this.supporter = new LifeCycleSupport();
    }

    @Override
    public void stop() {
        synchronized (this) {
            this.started = false;
        }
    }

    @Override
    public Container getParent() {
        return this.parent;
    }

    @Override
    public void start() throws LifeCycleException {
        if (started) {
            logger.warn("Workeer has been started, this time will do nothing");
        }
        //Worker container will load the servlet once it is started,
        String servletName = this.servletConfig.getServletName();
        try {
            loadServlet(servletName);
        } catch (ContainerExeception e) {
            if (logger.isErrorEnabled()) {
                logger.error("Failed to start the worker container!");
            }
            throw new LifeCycleException(e);
        }
        initiateWorkflow();
        try {
            workflow.start();
        } catch (WorkflowException e) {
            if (logger.isErrorEnabled()) {
                logger.error("Failed to start the workflow of the worker.");
            }
        }
    }

    @Override
    public void addChild(Container child) throws ContainerExeception {
        throw new ContainerExeception("Woeker container does not contain any child");
    }

    @Override
    public void fireEvent(LifeCycleEvent event) {
        for (LifeCycleEventListener listener : supporter.findListeners()) {
            listener.handleEvent(event);
        }
    }

    @Override
    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override
    public void setDefaultNode(Node defaultNode) {
        this.defaultNode = defaultNode;
    }

    @Override
    public Node getDefaultNode() {
        return this.defaultNode;
    }

    @Override
    public ClassLoader getLoader() {
        if (getParent().getLoader() != null) {
            return getParent().getLoader();
        } else {
            return Thread.currentThread().getContextClassLoader();
        }
    }

    @Override
    public String getInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    private void loadServlet(String servletName) throws ContainerExeception {
        try {
            Class<?> clazz = getLoader().loadClass(servletName);
            HttpServlet servlet = (HttpServlet) clazz.newInstance();
            servlet.init();
            this.defaultNode = new ServletNode(servlet);
        } catch (ClassNotFoundException e) {
            throw new ContainerExeception("Failed to load servlet in the context dir", e);
        } catch (InstantiationException e) {
            throw new ContainerExeception("Not a standard http servlet.", e);
        } catch (IllegalAccessException e) {
            throw new ContainerExeception("Failed to load servlet in the context dir", e);
        } catch (ServletException e) {
            e.printStackTrace();
        } 
    }

    private void initiateWorkflow() {
        NodeContext standardContext = new StandardContext();
        this.workflow = new MemoryWorkflow(standardContext);
        workflow.setBasicNode(defaultNode);
    }

    /**
     * 
     * @param request The request that the father container (context container {@link Context}) passed in.
     * @param response The response representation that the father container (context container {@link Context}) passed in.
     */
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

}
