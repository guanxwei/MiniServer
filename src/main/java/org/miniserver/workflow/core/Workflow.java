package org.miniserver.workflow.core;

import org.miniserver.workflow.exceptions.WorkflowException;
import org.miniserver.workflow.io.Anything;

public interface Workflow {

    public String getName();

    public void setBasicNode(Node basicNode);

    public void setContext(NodeContext context);

    public NodeContext getContext();

    public void addNode(Node node);

    public void removeNode(Node node) throws WorkflowException;

    public Node[] findNodes();

    public void copy(Workflow originalWorkflow);

    public void start() throws WorkflowException;

    public void invoke(Anything request, Anything response) throws WorkflowException;

}
