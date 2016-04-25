package org.miniserver.core.http;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_ADDPeer;

public class HttpResponse implements HttpServletResponse{

    // the default buffer size
    private static final int BUFFER_SIZE = 1024;
    HttpRequest request;
    OutputStream output;
    PrintWriter writer;
    protected byte[] buffer = new byte[BUFFER_SIZE];
    protected int bufferCount = 0;

    /**
     * Has this response been committed yet?
     */
    protected boolean committed = false;

    /**
     * The actual number of bytes written to this Response.
     */
    protected int contentCount = 0;

    /**
     * The content length associated with this Response.
     */
    protected int contentLength = -1;

    /**
     * The content type associated with this Response.
     */
    protected String contentType = null;

    /**
     * The character encoding associated with this Response.
     */
    protected String encoding = null;

    /**
     * The set of Cookies associated with this Response.
     */
    protected ArrayList<Cookie> cookies = new ArrayList<Cookie>();

    /**
     * The HTTP headers explicitly added via addHeader(), but not including
     * those to be added with setContentLength(), setContentType(), and so on.
     * This collection is keyed by the header name, and the elements are
     * ArrayLists containing the associated values that have been set.
     */
    protected HashMap<String, List<String>> headers = new HashMap<String, List<String>>();

    /**
     * The date format we will use for creating date headers.
     */
    protected final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz",Locale.US);

    /**
     * The error message set by <code>sendError()</code>.
     */
    protected String message = getStatusMessage(HttpServletResponse.SC_OK);

    /**
     * The HTTP status code associated with this Response.
     */
    protected int status = HttpServletResponse.SC_OK;


    public HttpResponse(OutputStream output) {
        this.output = output;
    }

    /**
     * Call this method to send headers and response to the output.
     * The processor will process the request & response paire is responsible to invoke this method.
     */
    public void finishResponse() {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }


    protected String getProtocol() {
      return request.getProtocol();
    }

    /**
     * Returns a default status message for the specified HTTP status code.
     *
     * @param status The status code for which a message is desired
     */
    protected String getStatusMessage(int status) {
      if (!HttpResponseStatusMessage.HTTP_STATUS_CODE_TO_MESSAGE_MAPPER.containsKey(status)) {
          return ("HTTP Response Status " + status);
      } else {
          return HttpResponseStatusMessage.HTTP_STATUS_CODE_TO_MESSAGE_MAPPER.get(status);
      }
    }

    public OutputStream getStream() {
      return this.output;
    }
    /**
     * Send the HTTP response headers, if this has not already occurred.
     */
    protected void sendHeaders() throws IOException {
      if (isCommitted())
        return;
      // Prepare a suitable output writer
      OutputStreamWriter osr = null;
      try {
        osr = new OutputStreamWriter(getStream(), getCharacterEncoding());
      }
      catch (UnsupportedEncodingException e) {
        osr = new OutputStreamWriter(getStream());
      }
      final PrintWriter outputWriter = new PrintWriter(osr);
      // Send the "Status:" header
      outputWriter.print(this.getProtocol());
      outputWriter.print(" ");
      outputWriter.print(status);
      if (message != null) {
        outputWriter.print(" ");
        outputWriter.print(message);
      }
      outputWriter.print("\r\n");
      // Send the content-length and content-type headers (if any)
      if (getContentType() != null) {
        outputWriter.print("Content-Type: " + getContentType() + "\r\n");
      }
      if (getContentLength() >= 0) {
        outputWriter.print("Content-Length: " + getContentLength() + "\r\n");
      }
      // Send all specified headers (if any)
      synchronized (headers) {
        Iterator<String> names = headers.keySet().iterator();
        while (names.hasNext()) {
          String name = (String) names.next();
          ArrayList<String> values = (ArrayList<String>) headers.get(name);
          Iterator<String> items = values.iterator();
          while (items.hasNext()) {
            String value = items.next();
            outputWriter.print(name);
            outputWriter.print(": ");
            outputWriter.print(value);
            outputWriter.print("\r\n");
          }
        }
      }
      // Add the session ID cookie if necessary
  /*    HttpServletRequest hreq = (HttpServletRequest) request.getRequest();
      HttpSession session = hreq.getSession(false);
      if ((session != null) && session.isNew() && (getContext() != null)
        && getContext().getCookies()) {
        Cookie cookie = new Cookie("JSESSIONID", session.getId());
        cookie.setMaxAge(-1);
        String contextPath = null;
        if (context != null)
          contextPath = context.getPath();
        if ((contextPath != null) && (contextPath.length() > 0))
          cookie.setPath(contextPath);
        else

        cookie.setPath("/");
        if (hreq.isSecure())
          cookie.setSecure(true);
        addCookie(cookie);
      }
  */
      // Send all specified cookies (if any)
      synchronized (cookies) {
        Iterator<Cookie> items = cookies.iterator();
        while (items.hasNext()) {
          Cookie cookie = (Cookie) items.next();
          outputWriter.print(cookie.getName());
          outputWriter.print("=");
          outputWriter.print(cookie.getValue());
          outputWriter.print(";");
        }
      }

      // Send a terminating blank line to mark the end of the headers
      outputWriter.print("\r\n");
      outputWriter.flush();

      committed = true;
    }

    public void setRequest(HttpRequest request) {
      this.request = request;
    }

    public void write(int b) throws IOException {
      if (bufferCount >= buffer.length)
        flushBuffer();
      buffer[bufferCount++] = (byte) b;
      contentCount++;
    }

    public void write(byte b[]) throws IOException {
      write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
      // If the whole thing fits in the buffer, just put it there
      if (len == 0)
        return;
      if (len <= (buffer.length - bufferCount)) {
        System.arraycopy(b, off, buffer, bufferCount, len);
        bufferCount += len;
        contentCount += len;
        return;
      }

      // Flush the buffer and start writing full-buffer-size chunks
      flushBuffer();
      int iterations = len / buffer.length;
      int leftoverStart = iterations * buffer.length;
      int leftoverLen = len - leftoverStart;
      for (int i = 0; i < iterations; i++)
        write(b, off + (i * buffer.length), buffer.length);

      // Write the remainder (guaranteed to fit in the buffer)
      if (leftoverLen > 0)
        write(b, off + leftoverStart, leftoverLen);
    }

    /** implementation of HttpServletResponse  */

    public void addCookie(Cookie cookie) {
      if (isCommitted())
        return;
    //  if (included)
      //        return;     // Ignore any call from an included servlet
      synchronized (cookies) {
        cookies.add(cookie);
      }
    }

    public void addDateHeader(String name, long value) {
      if (isCommitted())
        return;
//      if (included)
    //          return;     // Ignore any call from an included servlet
      addHeader(name, format.format(new Date(value)));
    }

    public void addHeader(String name, String value) {
      if (isCommitted())
        return;
//          if (included)
    //          return;     // Ignore any call from an included servlet
      synchronized (headers) {
        ArrayList<String> values = (ArrayList<String>) headers.get(name);
        if (values == null) {
          values = new ArrayList<String>();
          headers.put(name, values);
        }

        values.add(value);
      }
    }

    public void addIntHeader(String name, int value) {
      if (isCommitted())
        return;
//      if (included)
    //    return;     // Ignore any call from an included servlet
      addHeader(name, "" + value);
    }

    public boolean containsHeader(String name) {
      synchronized (headers) {
        return (headers.get(name)!=null);
      }
    }

    public String encodeRedirectURL(String url) {
      return null;
    }

    public String encodeRedirectUrl(String url) {
      return encodeRedirectURL(url);
    }

    public String encodeUrl(String url) {
      return encodeURL(url);
    }

    public String encodeURL(String url) {
      return null;
    }

    public void flushBuffer() throws IOException {
      //committed = true;
      if (bufferCount > 0) {
        try {
          output.write(buffer, 0, bufferCount);
        }
        finally {
          bufferCount = 0;
        }
      }
    }

    public int getBufferSize() {
      return 0;
    }

    public String getCharacterEncoding() {
      if (encoding == null)
        return ("ISO-8859-1");
      else
        return (encoding);
    }

    public Locale getLocale() {
      return null;
    }

    public ServletOutputStream getOutputStream() throws IOException {
      return null;
    }

    public PrintWriter getWriter() throws IOException {
      writer = new PrintWriter(output);
      return writer;
    }

    /**
     * Has the output of this response already been committed?
     */
    public boolean isCommitted() {
      return (committed);
    }

    public void reset() {
    }

    public void resetBuffer() {
    }

    public void sendError(int sc) throws IOException {
    }

    public void sendError(int sc, String message) throws IOException {
    }

    public void sendRedirect(String location) throws IOException {
    }

    public void setBufferSize(int size) {
    }

    public void setContentLength(int length) {
        if (isCommitted()) {
            return;
        }
        this.contentLength = length;
    }

    public void setContentType(String type) {
        if (isCommitted()) {
            return;
        }
        this.contentType = type;
    }

    public void setDateHeader(String name, long value) {
        if (isCommitted())
          return;
        setHeader(name, format.format(new Date(value)));
    }

    public void setHeader(String name, String value) {
      if (isCommitted())
        return;
      ArrayList<String> values = new ArrayList<String>();
      values.add(value);
      synchronized (headers) {
        headers.put(name, values);
      }
      String match = name.toLowerCase();
      if (match.equals("content-length")) {
        int contentLength = -1;
        try {
          contentLength = Integer.parseInt(value);
        }
        catch (NumberFormatException e) {
          ;
        }
        if (contentLength >= 0)
          setContentLength(contentLength);
      } else if (match.equals("content-type")) {
        setContentType(value);
      }
    }

    public void setIntHeader(String name, int value) {
      if (isCommitted())
        return;
      setHeader(name, "" + value);
    }

    public void setLocale(Locale locale) {
      if (isCommitted())
        return;
      String language = locale.getLanguage();
      if ((language != null) && (language.length() > 0)) {
        String country = locale.getCountry();
        StringBuffer value = new StringBuffer(language);
        if ((country != null) && (country.length() > 0)) {
          value.append('-');
          value.append(country);
        }
        setHeader("Content-Language", value.toString());
      }
    }

    public void setStatus(int sc) {
        this.status = sc;
    }

    public void setStatus(int sc, String message) {
    }

    @Override
    public void setCharacterEncoding(String charset) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setContentLengthLong(long len) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getStatus() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getHeader(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getHeaders(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<String> getHeaderNames() {
        // TODO Auto-generated method stub
        return null;
    }
}
