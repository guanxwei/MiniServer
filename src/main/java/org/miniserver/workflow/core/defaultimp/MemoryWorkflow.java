package org.miniserver.workflow.core.defaultimp;

import org.miniserver.workflow.core.Node;
import org.miniserver.workflow.core.NodeContext;
import org.miniserver.workflow.core.Workflow;
import org.miniserver.workflow.exceptions.WorkflowException;
import org.miniserver.workflow.io.Anything;

public class MemoryWorkflow implements Workflow{

    private static final String NAME = "MINI:WORKFLOW:DEFAULT:IMPLEMENTATION:1.0";

    private NodeContext context;
    private Node basicNode;
    private Node[] nodes;
    private boolean started;

    public MemoryWorkflow(NodeContext context) {
        this.context = context;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void setContext(NodeContext context) {
        this.context = context;
    }

    @Override
    public NodeContext getContext() {
        return this.context;
    }

    @Override
    public void setBasicNode(Node basicNode) {
        this.basicNode =  basicNode;
        this.basicNode.setContext(getContext());
    }

    @Override
    public void addNode(Node node) {
        if (nodes == null) {
            nodes = new Node[0];
        }
        Node[] newNodes = new Node[nodes.length + 1];
        System.arraycopy(nodes, 0, newNodes, 0, nodes.length);
        newNodes[nodes.length] = node;
        this.nodes = newNodes;
        node.setContext(getContext());
    }

    @Override
    public void removeNode(Node node) throws WorkflowException {
        if (nodes.length <=0 ) {
            throw new WorkflowException("There is no node in the workflow now");
        }

        Node[] newNodes = new Node[nodes.length - 1];
        System.arraycopy(nodes, 0, newNodes, 0, nodes.length -1);
        this.nodes = newNodes;
    }

    @Override
    public Node[] findNodes() {
        return nodes;
    }

    @Override
    public void copy(Workflow originalWorkflow) {
        this.nodes = originalWorkflow.findNodes();
        this.context = originalWorkflow.getContext();
    }

    @Override
    public void invoke(Anything request, Anything response) throws WorkflowException {
        if (!started) {
            throw new WorkflowException("Workflow has not been started, please start the workflow before invoke it");
        }

        if (basicNode != null) {
            basicNode.handleRequest(request, response, null);
        }

        if (nodes != null && nodes.length > 0 ) {
            Node starter = nodes[0];
            starter.handleRequest(request, response, context);
        }

    }

    @Override
    public void start() throws WorkflowException {
        if (started) {
            throw new WorkflowException("The workflow has been started!");
        }

        this.started = true;
    }

}
