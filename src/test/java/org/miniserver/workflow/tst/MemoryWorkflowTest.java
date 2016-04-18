package org.miniserver.workflow.tst;

import org.miniserver.workflow.core.Node;
import org.miniserver.workflow.core.NodeContext;
import org.miniserver.workflow.core.Workflow;
import org.miniserver.workflow.core.defaultimp.MemoryWorkflow;
import org.miniserver.workflow.core.defaultimp.StandardContext;
import org.miniserver.workflow.exceptions.WorkflowException;
import org.miniserver.workflow.io.Anything;
import org.miniserver.workflow.io.Bag;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class MemoryWorkflowTest {

    private NodeContext context;
    private Node node;
    private Workflow workflow;
    private Anything request;
    private Anything response;

    @BeforeMethod
    public void BeforeMethod() {
        this.context = new StandardContext();
        this.node = new TestNode();
        this.workflow = new MemoryWorkflow(context);
        this.request = new Anything();
        this.response = new Anything();
        workflow.setBasicNode(node);
        context.initial(new Bag());
    }

    @Test
    public void testHappyCase() throws Exception {
        workflow.start();
        workflow.invoke(request, response);
        Assert.assertEquals(node.getThreadLocalValue("name").toString(), TestNode.NODE_NAME);
    }

    @Test(expectedExceptions = WorkflowException.class,
          expectedExceptionsMessageRegExp = "Workflow has not been started, please start the workflow before invoke it")
    public void testUnhappyCase() throws Exception {
        workflow.invoke(request, response);
    }
}
