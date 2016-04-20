package org.miniserver.core.http.connector;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;

import org.miniserver.core.config.Config;
import org.miniserver.core.container.Container;
import org.miniserver.lifecycle.core.LifeCycleEvent;
import org.miniserver.lifecycle.core.LifeCycleEventListener;
import org.miniserver.lifecycle.core.LifeCycleEvents;
import org.miniserver.lifecycle.core.LifeCycleSupport;
import org.miniserver.lifecycle.exceptions.LifeCycleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnector implements Connector {

    private Logger logger = LoggerFactory.getLogger(HttpConnector.class);

    private static final String CONNECTOR_NAME = "StandardConnector";

    private LifeCycleSupport supporter;

    private boolean started;

    private Config config;

    private Map<String, Container> requestMapper;

    private boolean isKeepAliveOn = false;

    /**
     * Executor pool used to parse client request and delegate to servlet container.
     * Each executor in this pool will pretreat the client requests and wrap them into httpservletrequest, so that the servlet container can 
     * handle such requests.
     */
    @Getter
    private ExecutorService executor;

    public HttpConnector(Config connectorConfig) {
        this.config = connectorConfig;
        this.supporter = new LifeCycleSupport();
        loadListener(this.config);

    }

    @Override
    public void start() throws LifeCycleException {
        if (started) {
            throw new LifeCycleException(String.format("Connectoer [{0}] has been started", CONNECTOR_NAME));
        }
        emmitEvent(new LifeCycleEvent(this, LifeCycleEvents.BEFORE_FTART));
        try {
            if (this.config.getParameter("connector") == null || this.config.getParameter("connector").equals("bio")) {
                initiate();
            } else if (this.config.getParameter("connector").equals("nio")){
                //initiateV2();
            } else {
                throw new LifeCycleException("Unsupported connector type");
            }
        } catch (IOException e) {
            /*
             * If the connector can not start successfully, we should exit the JVM directly since we can do nothing.
             */
            System.exit(0);
        }
        emmitEvent(new LifeCycleEvent(this, LifeCycleEvents.AFTER_START));
    }

    @Override
    public void stop() {
        emmitEvent(new LifeCycleEvent(this, LifeCycleEvents.BEFORE_STOP));
        this.started = false;
        this.executor.shutdown();
        emmitEvent(new LifeCycleEvent(this, LifeCycleEvents.AFTER_STOP));
    }

    @Override
    public void addContainer(String basePath, Container container) {
        if (this.requestMapper == null) {
            this.requestMapper = new HashMap<String, Container>();
        }
        this.requestMapper.put(basePath, container);
    }

    /**
    private void initiateV2() throws IOException {
        InetSocketAddress address = new InetSocketAddress(this.config.getParameter("host"),
                Integer.valueOf(this.config.getParameter("port")));
        ServerSocketChannel server = ServerSocketChannel.open();
        Selector selector = Selector.open();
        server.configureBlocking(false);
        server.socket().bind(address);
        server.register(selector, SelectionKey.OP_ACCEPT);

        logger.info("Server socket started");

        while (started) {
            int key = selector.select();
            if (key > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while ( iterator.hasNext()) {
                    SelectionKey selctionKey = iterator.next();
                    if (selctionKey.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) selctionKey.channel();
                        SocketChannel client = channel.accept();
                        StringBuffer buffer = new StringBuffer();
                        byte[] bucket = new byte[1024];
                        
                    }
                }
            }
        }
    }
    **/

    /**
     * Default miniserver will use BIO to accept client request.
     * @throws IOException
     * @throws NumberFormatException 
     */
    private void initiate() throws NumberFormatException, IOException {
        ServerSocket server;
        this.executor = Executors.newFixedThreadPool(300);
        if (this.config.getParameter(ConnectorConstant.HOST) == null) {
            //If the connector's host is not specified, the connector will monitor all IP address.
            server = new ServerSocket(Integer.valueOf(this.config.getParameter(
                    ConnectorConstant.PORT)));
        } else {
            server = new ServerSocket(Integer.valueOf(this.config.getParameter(
                    ConnectorConstant.PORT)), 100, InetAddress.getByName(this.config.getParameter(ConnectorConstant.HOST)));
        }
        while (started) {
            Socket socket = server.accept();
            // call processor to generate http request and http response for the current client request.
            HttpProcessor processor = new HttpProcessor(this, socket);
            executor.submit(processor);
        }
        server.close();
    }

    
    @Override
    public void run() {
        try {
            start();
        } catch (LifeCycleException e) {
            logger.error("Failed to start the connector", e);
        }
    }

    @Override
    public void addListener(LifeCycleEventListener listener) {
        supporter.addListener(listener);
    }

    @Override
    public void emmitEvent(LifeCycleEvent event) {
        supporter.emitEvent(event);
    }

    private void loadListener(Config config) {
        Class<?> clazz = null;
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(this.config.getParameter("before_connector_start_listener"));
            addListener((LifeCycleEventListener) clazz.newInstance());
        } catch (ClassNotFoundException e) {
            logger.warn(String.format("Can not load listener class : [{0}]", "before_connector_start_listener"));
        } catch (InstantiationException e) {
            logger.warn(String.format("Can not instantiate listener class : [{0}]", "before_connector_start_listener"));
        } catch (IllegalAccessException e) {
            logger.warn(String.format("Can not access the class : [{0}]", "before_connector_start_listener"));
        }
        try {
            clazz = Thread.currentThread().getContextClassLoader().loadClass(this.config.getParameter("after_connector_start_listener"));
            addListener((LifeCycleEventListener) clazz.newInstance());
        } catch (ClassNotFoundException e) {
            logger.warn(String.format("Can not load listener class : [{0}]", "before_connector_start_listener"));
        } catch (InstantiationException e) {
            logger.warn(String.format("Can not instantiate listener class : [{0}]", "before_connector_start_listener"));
        } catch (IllegalAccessException e) {
            logger.warn(String.format("Can not access the class : [{0}]", "before_connector_start_listener"));
        }
    }

    @Override
    public boolean isKeepAliveOn() {
        return isKeepAliveOn;
    }
}
