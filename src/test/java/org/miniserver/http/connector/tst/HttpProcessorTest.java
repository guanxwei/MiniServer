package org.miniserver.http.connector.tst;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.miniserver.core.http.HttpRequest;
import org.miniserver.core.http.HttpResponse;
import org.miniserver.core.http.connector.HttpConnector;
import org.miniserver.core.http.connector.HttpProcessor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class HttpProcessorTest {

    private ExecutorService service;

    @Mock
    private Socket socket;

    @Mock
    private HttpConnector connector;

    private HttpProcessor processor;

    @BeforeMethod
    public void BeforeMethod() {
        this.service = Executors.newFixedThreadPool(1);
        MockitoAnnotations.initMocks(this);
        Thread.interrupted();
    }

    @Test
    public void testGetHttpRequestParse() throws Exception {
        String usrDir = System.getProperty("user.dir");
        String file_separator = System.getProperty("file.separator");
        InputStream fileInput = new FileInputStream(usrDir + file_separator + "target" + file_separator + "test-classes" + file_separator + "get.txt");
        OutputStream fileOutput = new FileOutputStream(usrDir + file_separator + "target" + file_separator + "test-classes" + file_separator + "get_output.txt");
        Mockito.when(socket.getInputStream()).thenReturn(fileInput);
        Mockito.when(socket.getOutputStream()).thenReturn(fileOutput);
        this.processor = new HttpProcessor(connector, socket);
        this.service.submit(processor);
        Thread.sleep(100);
        HttpRequest request = processor.getRequest();
        HttpResponse response = processor.getResponse();
        Assert.assertNotNull(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(request.getMethod(), "GET");
        Assert.assertEquals(request.getScheme(), "http");
        Assert.assertEquals(request.getProtocol(), "HTTP/1.1");
        Assert.assertEquals(request.getQueryString(), "query=fjdsalfjla&hello=jlfdkajlfkajl");
        Assert.assertEquals(request.getParameter("query"), "fjdsalfjla");
        Assert.assertEquals(request.getParameter("hello"), "jlfdkajlfkajl");
        Assert.assertEquals(request.getLocale(), new Locale("zh-CN"));
        Assert.assertNotNull(request.getCookies());
        Assert.assertEquals(request.getRequestedSessionId(), "832-2604240-6528132");
        Assert.assertEquals(request.getRequestURI(), "/root/index.html");
        Assert.assertNotNull(request.getCharacterEncoding());
        Assert.assertEquals(request.getCharacterEncoding(), "ISO-8859-1");

    }

    @Test
    public void testPostHttpRequestParse() throws Exception {
        String usrDir = System.getProperty("user.dir");
        String file_separator = System.getProperty("file.separator");
        InputStream fileInput = new FileInputStream(usrDir + file_separator + "target" + file_separator + "test-classes" + file_separator + "post.txt");
        OutputStream fileOutput = new FileOutputStream(usrDir + file_separator + "target" + file_separator + "test-classes" + file_separator + "post_output.txt");
        Mockito.when(socket.getInputStream()).thenReturn(fileInput);
        Mockito.when(socket.getOutputStream()).thenReturn(fileOutput);
        this.processor = new HttpProcessor(connector, socket);
        this.service.submit(processor);
        Thread.sleep(100);
        HttpRequest request = processor.getRequest();
        HttpResponse response = processor.getResponse();
        Assert.assertNotNull(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(request.getMethod(), "POST");
        Assert.assertEquals(request.getScheme(), "http");
        Assert.assertEquals(request.getProtocol(), "HTTP/1.1");
        Assert.assertEquals(request.getQueryString(), null);
        Assert.assertEquals(request.getParameter("query"), "fjdsalfjla");
        Assert.assertEquals(request.getParameter("hello"), "jlfdkajlfkajl");
        Assert.assertEquals(request.getLocale(), new Locale("zh-CN"));
        Assert.assertNotNull(request.getCookies());
        Assert.assertEquals(request.getRequestedSessionId(), "832-2604240-6528132");
        Assert.assertEquals(request.getRequestURI(), "/root/index.html");
        Assert.assertEquals(request.getCharacterEncoding(), "UTF-8");
    }
}
