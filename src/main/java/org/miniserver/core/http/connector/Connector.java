package org.miniserver.core.http.connector;

import org.miniserver.core.container.Container;
import org.miniserver.core.container.Context;
import org.miniserver.lifecycle.core.LifeCycle;

public interface Connector extends LifeCycle, Runnable{

    /**
     * A connector will always coopreate with a container, whoes type is {@link Context}. Every time the connector receive a request from a client,
     * the connector will parse the request first, done via related processors, then the processor will delegate the parsed request to the container.
     * 
     * @param basePath A web application's identifier, every request whose url begin with the basePath will be delegated to the mapping container. 
     * @param container An instanse of {@link Context}, who will be responsible of tackling the request.
     */
    public void addContainer(String basePath, Container container);

    /**
     * In HTTP/1.1 specification, a client(like a web browser) will always send a HTTP head "Connection:keep-alive", the processors behind the connector will
     * determine to follow the keep-alive mechanism by this method. Defautly, MiniServer chooses to ignore the "keep-alive" flag, once the processor complete
     * handling the request, the processor will close the socket immediately.
     * @return
     */
    public boolean isKeepAliveOn();

}
