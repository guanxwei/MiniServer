package org.miniserver.workflow.core;

import org.miniserver.workflow.exceptions.NodeException;
import org.miniserver.workflow.io.Anything;

public interface Node {

    public String getInfo();

    public void handleRequest(Anything request, Anything response, NodeContext context) throws NodeException;

    public String getName();

    public Node nextNode();

    public Node errorNode();

    public void setContext(NodeContext context);

    public Anything getThreadLocalValue(String name);

    public void setThreadLocalValue(String name, Anything value);
    
}
