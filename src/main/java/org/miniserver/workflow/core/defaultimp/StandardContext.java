package org.miniserver.workflow.core.defaultimp;

import org.miniserver.workflow.core.Node;
import org.miniserver.workflow.core.NodeContext;
import org.miniserver.workflow.exceptions.NodeException;
import org.miniserver.workflow.io.Anything;
import org.miniserver.workflow.io.Bag;
import org.miniserver.workflow.utils.WorkflowConstants;
import org.miniserver.workflow.utils.WorkflowStatus;

public class StandardContext implements NodeContext{

    private static final ThreadLocal<Bag> localStorge = new ThreadLocal<Bag>();

    @Override
    public void invokeNext(Anything request, Anything response, Node current) throws NodeException {
        WorkflowStatus currentStatus = (WorkflowStatus) StandardContext.localStorge.get().getValue(
                WorkflowConstants.STATUS).getValue();
        if (WorkflowStatus.FAILED.toString().equals(currentStatus.toString())) {
            current.errorNode().handleRequest(request, response, null);
        } else {
            current.nextNode().handleRequest(request, response, this);
        }
    }

    @Override
    public Anything getThreadLocalValue(String name) {
        return StandardContext.localStorge.get().getValue(name);
    }

    @Override
    public void setThreadLocalValue(String name, Anything value) {
        StandardContext.localStorge.get().storeValue(name, value);
    }

    /**
     * At the every begin time when outer environment invoke the workflow, they should initial the bag,
     * so that the bag will keep unique for each request.
     */
    @Override
    public void initial(Bag bag) {
        StandardContext.localStorge.set(bag);
    }

}
