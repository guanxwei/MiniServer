package org.miniserver.core.http.utils;

import java.io.BufferedReader;
import java.io.IOException;

import org.miniserver.core.http.HttpRequest;

public final class HttpRequestParser {

    public static void parseRequestLine(BufferedReader reader, HttpRequest request) throws IOException {
        String rawRequestLine = reader.readLine();

        String[] siblings = rawRequestLine.split(" ");
        if (siblings.length < 3) {
            throw new IOException("Not standard HTTP/1.1 request");
        }
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
     * Read all the http request header lines, util we meet a blank line, content below the blank line are the form parameter.
     * @param reader Socket inputstream wrapper, here it is a buffered reader.
     * @param request MiniServer internal http request abstract object.
     * @throws IOException
     */
    public static void parseHeaders(BufferedReader reader, HttpRequest request) throws IOException {
        String newLine = null;
        while ( (newLine = reader.readLine()) != null && newLine.length() > 0) {
            String[] pair = newLine.split(":");
            request.addHeader(pair[0], pair[1]);
        }
    }

    /**
     * 
     * @param reader
     * @param request
     * @throws IOException
     */
    public static void parseForm(BufferedReader reader, HttpRequest request) throws IOException {
        /**
         * If it is a Get request, do nothing.
         */
        if ("get".equalsIgnoreCase(request.getMethod())) {
            return ;
        }

        if ("post".equalsIgnoreCase(request.getMethod())) {
            StringBuffer buffer = new StringBuffer();
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (buffer.length() == 0) {
                return ;
            } else {
                /**
                 * HTML5 support json style form data, xml style form data and standard x-www-form-urlencoded, and multipart/form-data.
                 * MiniServer will not support multipart/form-data.
                 */
                request.setRawRequestBody(buffer.toString().getBytes());
            }
        }
    }
}
