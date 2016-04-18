package org.miniserver.core.http.connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletResponse;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.miniserver.core.container.Server;
import org.miniserver.core.http.HttpRequest;
import org.miniserver.core.http.HttpResponse;
import org.miniserver.core.http.RequestLine;

/**
 * HttpProcessor which is used to handle the http request, since MiniServer is aimed at dynamic response business scenario,
 * Upload or download files are always bandwidth costly and may take long time to fulfill.
 * @author guanxwei
 *
 */
@Slf4j
public class HttpProcessor implements Runnable {

    private static AtomicInteger aliveConnections = new AtomicInteger(0);
    private Connector connector;
    private Socket socket;
    //private RequestLine requestLine;
    HttpRequest request;
    HttpResponse response;
    @Setter
    private boolean isKeepAlive = false;

    public HttpProcessor(Connector connector, Socket socket) {
        this.connector = connector;
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream input;
        OutputStream output;
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();
            request = new HttpRequest(input);
            response = new HttpResponse(output);
            response.setRequest(request);
            response.setHeader("server", Server.SERVER_NAME);
            /**
             * Before we delegate request to servlet container, we need to parse the http request line(the first line of the request entity)
             * After we determine which context is responsible for handling this request, we will select the target context registerd in the connector
             * then delegate the reqeust.
             */
            parseRequest(input, request);
            delegateRequest(request, response);

            boolean isKeepAliveOn = this.connector.isKeepAliveOn();
            if (isKeepAliveOn && request.getHeader("Connection").equals("keep-alive") && aliveConnections.get() <=100) {
                aliveConnections.decrementAndGet();
                /**
                 * If keep-alive is set on, the soccket will be reused by another processor, but we will set the timeout to 10 seconds
                 * if the read method of the socket's inputstream does not return in 10 seconds, the processor should close the socket and release 
                 * the resource;
                 */
                socket.setSoTimeout(10000);
                HttpProcessor processor = new HttpProcessor(this.connector, socket);
                ((HttpConnector) this.connector).getExecutor().submit(processor);
            } else {
                response.finishResponse();
                socket.close();
            }
        } catch (IOException e) {
            log.error("Failed to handle the request");
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.finishResponse();
            } catch (IOException e1) {
                log.error(String.format("Can not response to client due to error [{}]", e1.getStackTrace().toString()));
            }
        }

    }

    private void parseRequest(InputStream input, HttpRequest request) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        String rawRequestLine = null;
        try {
            while ((rawRequestLine = reader.readLine()) == null || rawRequestLine.length() > 0) {
                log.info("Read a empty line, keep going");
            }
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException e1) {
                log.error("Could not read request, close the socket!");
            }
        }
        String[] siblings = rawRequestLine.split(" ");
        request.setMethod(siblings[0]);
        /**
         * Set the raw requestURL to a MiniServer specific parameter defined in HttpRequest. The HttpServletRequest's methods getRequestUIL and getRequestURL
         * will depend on this valuable when first time they are called. The raw requestURL will contain the queryString.
         */
        request.setRawRequestURL(siblings[1]);
        if (siblings.length == 3) {
            request.setProtocol(siblings[2]);
        }

    }

    /**
     * Parse the query string into key-values style, 
     */
    private void parseQueryStrings(BufferedReader reader, HttpRequest request) {
        String queryString = request.getq
    }

    private void parseHttpHeaders() {
        
    }

    private void delegateRequest(HttpRequest request, HttpResponse response) {
        /**
         * Before the processor delegate the reques to the servlet container, the processor will help parse the http request first.
         * Since MiniServer is aimed at solving highly dynamic http request, file uploading is not supported.
         */
        
        
    }
}