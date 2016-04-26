package org.miniserver.core.http.utils;

public final class HttpRequestHeaders {

    /**
     * Content-Type HTTP request header.
     */
    public static final String CONTENT_TYPE = "Content-Type";

    /**
     * Accept-Language HTTP request header.
     */
    public static final String ACCEPTED_LANGUAGE = "Accept-Language";

    /**
     * When the first time client call the MiniServer will add the session-id into the cookie and send back to the client.
     */
    public static final String SESSION_ID = "session-id";
}
