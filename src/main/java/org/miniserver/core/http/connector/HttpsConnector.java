package org.miniserver.core.http.connector;

import org.miniserver.core.container.Container;
import org.miniserver.core.http.HttpRequest;
import org.miniserver.core.http.HttpResponse;
import org.miniserver.lifecycle.core.LifeCycleEvent;
import org.miniserver.lifecycle.core.LifeCycleEventListener;
import org.miniserver.lifecycle.exceptions.LifeCycleException;

public class HttpsConnector implements Connector{

    @Override
    public void start() throws LifeCycleException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void stop() {
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

    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void addContainer(String basePath, Container container) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isKeepAliveOn() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void delegate(HttpRequest request, HttpResponse response) {
        // TODO Auto-generated method stub
        
    }

}
