package org.miniserver.core.plugin;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.miniserver.workflow.core.Node;
import org.miniserver.workflow.core.NodeContext;
import org.miniserver.workflow.core.defaultimp.SingleWorkerNodeBase;
import org.miniserver.workflow.exceptions.NodeException;
import org.miniserver.workflow.io.Anything;

/**
 * 
 * @author guanxwei
 *
 */
public class FilterChainNode extends SingleWorkerNodeBase implements FilterChain {

    public static final String NAME = "FilterChainNode";
    public static final String INFO = "FilterChain hold a list of filters";

    private int index = 0;

    private Filter[] filters;

    public FilterChainNode(Filter[] filters) {
        this.filters = filters;
    }

    /**
     * Not all the filter will be invoked, only when some patterns are matached then the filter will be invoked.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        
    }

    @Override
    public String getInfo() {
        return INFO;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Node nextNode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Node errorNode() {
        // TODO Auto-generated method stub
        return null;
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

    @Override
    public void work(Anything request, Anything response) throws NodeException {
        // TODO Auto-generated method stub
        
    }

}
