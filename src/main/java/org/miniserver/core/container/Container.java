package org.miniserver.core.container;

import org.miniserver.core.exceptions.ContainerExeception;
import org.miniserver.core.http.HttpRequest;
import org.miniserver.lifecycle.core.LifeCycle;
import org.miniserver.lifecycle.core.LifeCycleEvent;
import org.miniserver.workflow.core.Node;

public interface Container extends LifeCycle{

    /**
     * Do the real work the container should do 
     * @param request MiniServer internal representation of the HTTP request.
     * @param response MiniServer internal representation of the HTTP response.
     */
    public void invoke(HttpRequest request, org.miniserver.core.http.HttpResponse response);

    /**
     * Get the parent container.
     * @return
     */
    public Container getParent();

    /**
     * Set up the parent container.
     */
    public void setParent(Container parent);

    /**
     * Add a child container.
     */
    public void addChild(Container child) throws ContainerExeception;

    /**
     * Notify the event to listener if any.
     * @param event The detail event
     */
    public void fireEvent(LifeCycleEvent event);

    /**
     * Set the default node for the container, the default node will be invoked by the container
     * every time a request sent in.
     * @param defaultNode Default node.
     */
    public void setDefaultNode(Node defaultNode);

    /**
     * Get the default node of the container.
     * @return
     */
    public Node getDefaultNode();

    /**
     * Get the class loader of the container.
     * @return
     */
    public ClassLoader getLoader();

    /**
     * Get the introduction of the container.
     * @return
     */
    public String getInfo();
}
