package org.miniserver.core.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
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

import org.miniserver.core.container.Server;
import org.miniserver.core.http.utils.HttpRequestHeaders;

import lombok.AccessLevel;
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

    private Map<String, String[]> parameters;

    private Cookie[] cookies;

    private Hashtable<String, Object> attributes;

    private Map<String, String> headers;

    @Setter
    private byte[] rawRequestBody;

    public HttpRequest(InputStream input) {
        this.input = input;
        this.servletInputStream = new RequestStream(input);
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
        if (this.characterEncoding == null) {
            String content_type_header = getContentType();
            if (content_type_header == null) {
                //Clinet did not send content-type to server, the server will use the default encoding "ISO-8859-1";
                this.characterEncoding = "ISO-8859-1";
            } else {
                int semicolon_index = content_type_header.indexOf(";");
                String charset = content_type_header.substring(semicolon_index, content_type_header.length());
                if (charset.length() > 0) {
                    int equal_index = charset.indexOf("=");
                    this.characterEncoding = charset.substring(equal_index + 1);
                }
            }
            
        }

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
            this.contentType = getHeader(HttpRequestHeaders.CONTENT_TYPE);
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
        assembleParameterMap();
        /**
         * If the querystrin and http body both contain a parameter with the same name, we will defaultly return this first one,
         * that is the query string edition.
         */
        return this.parameters.get(name) == null ? null : this.parameters.get(name)[0];
    }

    @Override
    public Enumeration<String> getParameterNames() {
        assembleParameterMap();
        Vector<String> vector = new Vector<String>(this.parameters.keySet());

        return vector.elements();
    }

    @Override
    public String[] getParameterValues(String name) {
        assembleParameterMap();

        return this.parameters.get(name);
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
        if (cookies != null) {
            return this.cookies;
        }

        String rawCookiesText = getHeader(HttpRequestHeaders.COOKIE);
        if (rawCookiesText == null || rawCookiesText.length() == 0) {
            return null;
        } else {
            String[] paris = rawCookiesText.split(";");
            List<Cookie> cookies = new ArrayList<Cookie>();
            for (String pair : paris) {
                String[] key_to_value = pair.split("=");
                Cookie cookie = new Cookie(key_to_value[0], key_to_value[1]);
                cookies.add(cookie);
            }

            int size = cookies.size();
            this.cookies = new Cookie[size];
            while (size > 0) {
                this.cookies[--size] = cookies.get(size);
            }
        }

        return this.cookies;
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
        if (this.sessionID == null) {
            /**
             * We'll first try to fetch the sessionID from the query String ,if we can not find it , then we will try to find it from the cookies.
             */
            if (getParameter(HttpUtils.HTTP_UTILS_CONSTANT_KEYS_VALUES_PAIR.get("sessionID")) != null) {
                this.sessionID = getParameter(HttpUtils.HTTP_UTILS_CONSTANT_KEYS_VALUES_PAIR.get("sessionID"));
            } else if (HttpUtils.isCookiesContainSessionID(getCookies())) {
                this.sessionID = HttpUtils.fetchSessionIDFromCookies(getCookies());
            } else {
                this.sessionID = "";
                return null;
            }
        }

        return this.sessionID.length() == 0 ? null : this.sessionID;
    }

    @Override
    public String getRequestURI() {
        if (this.requestURI == null) {
            if (this.rawRequestURL.indexOf("http") < 0) {
                int index = this.rawRequestURL.indexOf("?");
                this.requestURI =  this.rawRequestURL.substring(0, index);
            } else {
                String temp = this.rawRequestURL.replace(this.getScheme() + "://", "");
                int index = temp.indexOf("/");
                int index_question_mark = temp.indexOf("?");
                if (index_question_mark < 0) {
                    this.requestURI = temp.substring(index, temp.length());
                } else {
                    this.requestURI = temp.substring(index, index_question_mark);
                }
            }
        }

        return this.requestURI;
    }

    @Override
    public StringBuffer getRequestURL() {
        int index = rawRequestURL.indexOf('?');

        StringBuffer buffer = new StringBuffer();
        buffer.append(rawRequestURL.subSequence(0, index));
        return buffer;
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

    private void assembleParameterMap() {
        if (this.parameters == null) {
            this.parameters = new HashMap<String, String[]>();
            //First we'll try to parse queryString
            if (getQueryString() != null) {
                String[] queryStrings = getQueryString().split("&");
                for (String query : queryStrings) {
                    String[] pair = query.split("=");
                    if (this.parameters.containsKey(pair[0])) {
                        String[] src = this.parameters.get(pair[0]);
                        String[] values = new String[src.length + 1];
                        System.arraycopy(src, 0, values, 0, src.length);
                        values[src.length] = pair[1];
                        this.parameters.put(pair[0], values);
                    } else {
                        String[] values = new String[1];
                        values[0] = pair[1];
                        this.parameters.put(pair[0], values);
                    }
                }
            };
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
                        if (this.parameters.containsKey(pair[0])) {
                            String[] src = this.parameters.get(pair[0]);
                            String[] values = new String[src.length + 1];
                            System.arraycopy(src, 0, values, 0, src.length);
                            values[src.length] = pair[1];
                            this.parameters.put(pair[0], values);
                        } else {
                            String[] values = new String[1];
                            values[0] = pair[1];
                            this.parameters.put(pair[0], values);
                        }
                    }
                } else {
                    /**
                     * MiniServer will not help parse other form type http body data, it is the customers' duty to parse the body. We just do nothing. 
                     */
                }
            }
        }
    }
}
