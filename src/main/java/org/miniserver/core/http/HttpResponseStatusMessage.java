package org.miniserver.core.http;

import javax.servlet.http.HttpServletResponse;

import com.google.common.collect.ImmutableMap;

public final class HttpResponseStatusMessage {

    public static final ImmutableMap<Integer, String> HTTP_STATUS_CODE_TO_MESSAGE_MAPPER = ImmutableMap.<Integer, String>builder()
            .put(HttpServletResponse.SC_OK, "OK")
            .put(HttpServletResponse.SC_ACCEPTED ,"Accepted")
            .put(HttpServletResponse.SC_BAD_GATEWAY ,"Bad Gateway")
            .put(HttpServletResponse.SC_BAD_REQUEST ,"Bad Request")
            .put(HttpServletResponse.SC_CONFLICT,"Conflict")
            .put(HttpServletResponse.SC_CONTINUE,"Continue")
            .put(HttpServletResponse.SC_CREATED,"Created")
            .put(HttpServletResponse.SC_EXPECTATION_FAILED,"Expectation Failed")
            .put(HttpServletResponse.SC_FORBIDDEN,"Forbidden")
            .put(HttpServletResponse.SC_GATEWAY_TIMEOUT,"Gateway Timeout")
            .put(HttpServletResponse.SC_GONE, "Gone")
            .put(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED,"HTTP Version Not Supported") 
            .put(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"Internal Server Error") 
            .put(HttpServletResponse.SC_LENGTH_REQUIRED,"Length Required") 
            .put(HttpServletResponse.SC_METHOD_NOT_ALLOWED ,"Method Not Allowed") 
            .put(HttpServletResponse.SC_MOVED_PERMANENTLY ,"Moved Permanently") 
            .put(HttpServletResponse.SC_MOVED_TEMPORARILY ,"Moved Temporarily") 
            .put(HttpServletResponse.SC_MULTIPLE_CHOICES ,"Multiple Choices") 
            .put(HttpServletResponse.SC_NO_CONTENT ,"No Content") 
            .put(HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION ,"Non-Authoritative Information") 
            .put(HttpServletResponse.SC_NOT_ACCEPTABLE ,"Not Acceptable") 
            .put(HttpServletResponse.SC_NOT_FOUND ,"Not Found") 
            .put(HttpServletResponse.SC_NOT_IMPLEMENTED,"Not Implemented") 
            .put(HttpServletResponse.SC_NOT_MODIFIED,"Not Modified") 
            .put(HttpServletResponse.SC_PARTIAL_CONTENT,"Partial Content") 
            .put(HttpServletResponse.SC_PAYMENT_REQUIRED,"Payment Required") 
            .put(HttpServletResponse.SC_PRECONDITION_FAILED,"Precondition Failed") 
            .put(HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED,"Proxy Authentication Required") 
            .put(HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,"Request Entity Too Large") 
            .put(HttpServletResponse.SC_REQUEST_TIMEOUT,"Request Timeout") 
            .put(HttpServletResponse.SC_REQUEST_URI_TOO_LONG,"Request URI Too Long") 
            .put(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE,"Requested Range Not Satisfiable") 
            .put(HttpServletResponse.SC_RESET_CONTENT,"Reset Content") 
            .put(HttpServletResponse.SC_SEE_OTHER,"See Other") 
            .put(HttpServletResponse.SC_SERVICE_UNAVAILABLE,"Service Unavailable") 
            .put(HttpServletResponse.SC_SWITCHING_PROTOCOLS,"Switching Protocols") 
            .put(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized") 
            .put(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE,"Unsupported Media Type") 
            .put(HttpServletResponse.SC_USE_PROXY,"Use Proxy") 
            .put(207, "Multi-Status") 
            .put(422, "Unprocessable Entity") 
            .put(423, "Locked") 
            .put(507,"Insufficient Storage")
            .build();
            
}
