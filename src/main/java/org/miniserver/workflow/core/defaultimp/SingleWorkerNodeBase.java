package org.miniserver.workflow.core.defaultimp;

import org.miniserver.workflow.core.Node;
import org.miniserver.workflow.core.NodeContext;
import org.miniserver.workflow.exceptions.NodeException;
import org.miniserver.workflow.io.Anything;

public abstract class SingleWorkerNodeBase implements Node{

    @Override
    public void handleRequest(Anything request, Anything response,
            NodeContext context) throws NodeException {
        work(request, response);
        if (context != null)
            context.invokeNext(request, response, this);
    }

    public abstract void work(Anything request, Anything response) throws NodeException;
}
