package org.miniserver.workflow.core;

import org.miniserver.workflow.exceptions.NodeException;
import org.miniserver.workflow.io.Anything;
import org.miniserver.workflow.io.Bag;

public interface NodeContext {

    public void invokeNext(Anything request, Anything response, Node current) throws NodeException;

    public Anything getThreadLocalValue(String name);

    public void setThreadLocalValue(String name, Anything value);

    public void initial(Bag bag);
}
