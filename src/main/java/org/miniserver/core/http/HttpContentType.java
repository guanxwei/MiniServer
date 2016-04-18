package org.miniserver.core.http;

import com.google.common.collect.ImmutableList;

/**
 * Content-type that is used by clients to sent to server to indicate what kind of resource the client want.
 * The server will also sent it to the client to indivate what kind of response is being sent to the clients.
 * @author guanxwei
 *
 */
public class HttpContentType {

    public static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";

    public static final String MULTIPART_FORMDATA = "multipart/form-data";

    public static final String APPLICATION_JSON = "application/json";

    public static final String APPLICATION_XML = "application/XML";

    public static final String TEXT_HTML = "text/html";
 
    public static final String TEXT_PLAIN = "text/plain";

    public static final String TEXT_XML = "text/xml";

    public static final String IMAGE_GIF = "image/gif";

    public static final String IMAGE_JPEG = "image/jpeg";

    public static final String IMAGE_PNG = "image/png";

    public static final ImmutableList<String> CONTENT_TYPES = ImmutableList.<String>builder()
            .add("application/x-www-form-urlencoded")
            .add("application/xhtml+xml")
            .add("application/xml")
            .add("application/json")
            .add("application/pdf")
            .add("multipart/form-data")
            .add("text/html")
            .add("text/plain")
            .add("text/xml")
            .add("image/gif")
            .add("image/jpeg")
            .add("image/png")
            .add("text/xml")
            .add("text/xml")
            .build();

}
