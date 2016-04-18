package org.miniserver.core.http;

import lombok.Data;

@Data
public class RequestLine {

    /**
     * The request protocol.
     */
    private String protocol;

    /**
     * The request method.
     */
    private String method;

    /**
     * The request url.
     */
    private String url;
}
