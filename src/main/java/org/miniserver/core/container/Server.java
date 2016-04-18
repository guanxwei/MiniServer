package org.miniserver.core.container;

import java.util.List;

import org.miniserver.core.config.Config;
import org.miniserver.core.config.ServerConfig;
import org.miniserver.core.exceptions.ContainerExeception;
import org.miniserver.core.http.HttpRequest;
import org.miniserver.core.http.HttpResponse;
import org.miniserver.core.http.connector.Connector;
import org.miniserver.lifecycle.core.LifeCycleEvent;
import org.miniserver.lifecycle.core.LifeCycleEventListener;
import org.miniserver.workflow.core.Node;

/**
 * The representation of MiniServer. MiniServer will implement part the servlet 3.0 specification but not all of them, MiniServer is not a full function
 * Servelet container server. MiniServer is aimed at solving the problems that customers want to set up a server quickly, clients will always sent concise requests
 * so that MiniServer can quickly response to them. As to why MiniServer implement the basic functions of a servlet container, it's because developers who is familiar
 * with java EE espetially Servlet/JSP developing can easily holds up MiniServer and set up a mini server to fulfill his/her requirement.
 * 
 * The original resson why we developed a miniser like MiniServer is tha Amazon' China Payments Team need a light weight server, who will play the rule in 
 * Devo environment as vendors (banks, third party payment platform) play in Prod environment. The MiniServer will quickly sent back response to clients based on
 * the rules set by developers.
 * 
 * A server is the abstract of MiniServer runtime components. It is mainly composed of two components the connectors and mini servet container. The connectors will
 * take the reponsibility of monitoring the internet and delegating the request to the servlet container, then send back response to customer.
 * Whenever you want to setup a server, you need to create an instance of {@link Server} first, then give
 * the configuration to the server, the server will use the configurations to load all the components in contains. 
 */
public class Server implements Container{

    private Node defaultNode;

    /**
     * MiniServer support HTTP & HTTPS protocol requests, 
     */
    private List<Connector> connectors;

    private List<Container> contexts;

    private Config config;

    public static final String SERVER_NAME = "MiniServer:1.0";

    public Server(ServerConfig config) {
        this.config = config;
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        
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
        // TODO Auto-generated method stub
        
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

    @Override
    public ClassLoader getLoader() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getInfo() {
        // TODO Auto-generated method stub
        return null;
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

}
