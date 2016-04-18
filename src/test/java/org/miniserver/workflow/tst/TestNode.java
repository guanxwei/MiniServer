package org.miniserver.workflow.tst;

import org.miniserver.workflow.core.Node;
import org.miniserver.workflow.core.NodeContext;
import org.miniserver.workflow.core.defaultimp.SingleWorkerNodeBase;
import org.miniserver.workflow.exceptions.NodeException;
import org.miniserver.workflow.io.Anything;

public class TestNode extends SingleWorkerNodeBase{

    public static final String NODE_NAME = "TEST";
    private NodeContext context;

    @Override
    public String getInfo() {
        return NODE_NAME;
    }

    @Override
    public String getName() {
        return NODE_NAME;
    }

    @Override
    public Node nextNode() {
        return null;
    }

    @Override
    public Node errorNode() {
        return null;
    }

    @Override
    public void work(Anything request, Anything response) throws NodeException {
        Anything value = new Anything();
        value.storeValue(NODE_NAME);
        setThreadLocalValue("name", value);
    }

    @Override
    public Anything getThreadLocalValue(String name) {
        return this.context.getThreadLocalValue(name);
    }

    @Override
    public void setThreadLocalValue(String name, Anything value) {
        this.context.setThreadLocalValue(name, value);
    }

    @Override
    public void setContext(NodeContext context) {
        this.context = context;
    }

}
