package org.miniserver.core.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import jdk.nashorn.internal.parser.JSONParser;

import org.miniserver.core.container.Server;
import org.miniserver.core.http.utils.HttpRequestHeaders;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class HttpRequest implements HttpServletRequest{

    /**
     * InputStream from the received socket, will extract client request data from this.
     */
    private InputStream input;

    private ServletInputStream servletInputStream;

    private String requestURI;

    @Setter
    private String method;

    private String sessionID;

    private String characterEncoding;

    private int contentLnegth;

    private String contentType;

    @Setter
    private String scheme;

    private int port;

    @Setter
    private String protocol;

    private Locale locale;

    @Setter
    private String rawRequestURL;

    @Setter(value = AccessLevel.PRIVATE)
    private String queryString;

    @Getter(value = AccessLevel.PRIVATE)
    @Setter(value = AccessLevel.PRIVATE)
    private Map<String, String> queryStrings;

    private Map<String, String> parameters;

    private Cookie[] cookies;

    private Hashtable<String, Object> attributes;

    private Map<String, String> headers;

    @Setter
    private byte[] rawRequestBody;

    private HttpResponse response;

    public HttpRequest(InputStream input, HttpResponse response) {
        this.input = input;
        this.servletInputStream = new RequestStream(input);
        this.response = response;
    }

    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return this.attributes.keys();
    }

    /**
     * Client should sent the character encoding type to server, so that server parse the request headers and post body correctly.
     * If the client did not sent character encoding type to server, the server will defaultly use "ISO-8859-1" as the character encoding
     * type.
     * @return The client sent character encoding type, indicated by http request header "Content-type", the "Content-type" sent by clients will be
     * something like this "Content-Type: text/html;charset:utf-8;", "charset:utf-8" represents the character encoding type.
     */
    @Override
    public String getCharacterEncoding() {
        return this.characterEncoding;
    }

    @Override
    public void setCharacterEncoding(String env)
            throws UnsupportedEncodingException {
        this.characterEncoding = env;
    }

    @Override
    public int getContentLength() {
        return this.contentLnegth;
    }

    @Override
    public long getContentLengthLong() {
        return getContentLength();
    }

    /**
     * See detail in {@link HttpRequest#getCharacterEncoding()}, basically http protocol would support these content-type
     * @return
     */
    @Override
    public String getContentType() {
        if (this.contentType == null) {
            this.contentType = this.headers.get(HttpRequestHeaders.CONTENT_TYPE);
        }
        return this.contentType;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return this.servletInputStream;
    }

    /**
     * Http request posted body parameters.
     */
    @Override
    public String getParameter(String name) {
        if (this.parameters == null) {
            this.parameters = new HashMap<String, String>();
            //First we'll try to parse queryString
            String[] queryStrings = getQueryString().split("&");
            for (String query : queryStrings) {
                String[] pair = query.split("=");
                this.parameters.put(pair[0], pair[1]);
            }
            /**
             * If the HTTP request method is POST, then we have to parse the http body, Currently we only support json and urlencode style 
             * body.
             */
            if ("post".equalsIgnoreCase(this.method)) {
                if (this.getContentType().contains(HttpContentType.APPLICATION_JSON)) {
                     /** 
                      * We will do nothing, since MiniServe does not exactlly know the hierarchy of the Json object,
                      * it should be the Servelt writer to parse the requst body.
                      */
                } else if (this.getContentType().contains(HttpContentType.X_WWW_FORM_URLENCODED)) {
                    String rawString = new String(this.rawRequestBody);
                    String[] paraStrings = rawString.split("&");
                    for (String query : paraStrings) {
                        String[] pair = query.split("=");
                        this.parameters.put(pair[0], pair[1]);
                    }
                } else {
                    return null;
                }
            }
        }
        return this.parameters.get(name);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Vector<String> vector = new Vector<String>(this.parameters.keySet());

        return vector.elements();
    }

    @Override
    public String[] getParameterValues(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getProtocol() {
        return this.protocol;
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public String getServerName() {
        return Server.SERVER_NAME;
    }

    @Override
    public int getServerPort() {
        return this.port;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(input));
    }

    @Override
    public String getRemoteAddr() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRemoteHost() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttribute(String name, Object o) {
        this.attributes.put(name, o);
    }

    @Override
    public void removeAttribute(String name) {
        this.attributes.remove(name);
    }

    @Override
    public Locale getLocale() {
        if (this.locale == null) {
            this.locale = new Locale(getHeaders(HttpRequestHeaders.ACCEPTED_LANGUAGE).nextElement());
        }
        return this.locale;
    }

    @Override
    public Enumeration<Locale> getLocales() {
        Vector<Locale> locales = new Vector<Locale>();
        locales.add(locale);
        locales.add(Locale.getDefault());
        return locales.elements();
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRealPath(String path) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getRemotePort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getLocalName() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLocalAddr() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLocalPort() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest,
            ServletResponse servletResponse) throws IllegalStateException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getAuthType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Cookie[] getCookies() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getDateHeader(String name) {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * A HTTP request header may container several subtype, like Accept-Language:zh-CN,en-US;q=0.7,en;q=0.3
     */
    @Override
    public String getHeader(String name) {
        return this.headers.get(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        String header = this.headers.get(name);
        Vector<String> headers = new Vector<String>();
        String keys = header.split(";")[0];
        String[] split_keys = keys.split(",");
        for (String key : split_keys) {
            headers.add(key);
        }
        return headers.elements();
    }

    /**
     * Should avoid to use this method;
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        Vector<String> headers = new Vector<String>(this.headers.keySet());
        return headers.elements();
    }

    @Override
    public int getIntHeader(String name) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getPathInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getPathTranslated() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getContextPath() {
        int index = this.rawRequestURL.indexOf("/");
        String subString = this.rawRequestURL.substring(index, this.rawRequestURL.length());
        index = subString.indexOf("/");
        if (index > 0) {
            return subString.substring(0, index);
        } else {
            return subString;
        }
    }

    @Override
    public String getQueryString() {
        if (queryString != null) {
            return queryString;
        }
        int index = this.rawRequestURL.indexOf('?');
        if (index > 0 && index < rawRequestURL.length()) {
            this.queryString = rawRequestURL.substring(index + 1, rawRequestURL.length());
        }
  
        return this.queryString;
    }

    @Override
    public String getRemoteUser() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public Principal getUserPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestedSessionId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getRequestURI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringBuffer getRequestURL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getServletPath() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpSession getSession(boolean create) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpSession getSession() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String changeSessionId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean authenticate(HttpServletResponse response)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void login(String username, String password) throws ServletException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void logout() throws ServletException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Part getPart(String name) throws IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass)
            throws IOException, ServletException {
        // TODO Auto-generated method stub
        return null;
    }

    public void addHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<String, String>();
        }
        this.headers.put(key, value);
    }
}
