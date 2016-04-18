package org.miniserver.core.plugin;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.miniserver.workflow.core.Node;
import org.miniserver.workflow.core.NodeContext;
import org.miniserver.workflow.core.defaultimp.SingleWorkerNodeBase;
import org.miniserver.workflow.exceptions.NodeException;
import org.miniserver.workflow.io.Anything;

public class ServletNode extends SingleWorkerNodeBase{

    private static final String NAME = "ServletNode";
    private static final String INFO = "Servlet node hold a servlet";
    private Servlet servlet;

    public ServletNode(Servlet servlet) {
        this.servlet = servlet;
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * To a servlet there will be no next node.
     */
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
        HttpServletRequest httpRequest = (HttpServletRequest) request.getValue();
        HttpServletResponse httpResponse = (HttpServletResponse) response.getValue();
        try {
            servlet.service(httpRequest, httpResponse);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setContext(NodeContext context) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Anything getThreadLocalValue(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setThreadLocalValue(String name, Anything value) {
        // TODO Auto-generated method stub
        
    }

}
