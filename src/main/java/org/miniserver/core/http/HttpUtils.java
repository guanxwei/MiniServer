package org.miniserver.core.http;

import javax.servlet.http.Cookie;

import com.google.common.collect.ImmutableMap;

public final class HttpUtils {

    protected static final ImmutableMap<String, String> HTTP_UTILS_CONSTANT_KEYS_VALUES_PAIR = ImmutableMap.<String, String>builder()
            .put("sessionID", "sessionID")
            .build();

    public static boolean isCookiesContainSessionID(Cookie[] cookies) {
        boolean result = false;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(HTTP_UTILS_CONSTANT_KEYS_VALUES_PAIR.get("sessionID"))) {
                result = true;
                break;
            }
        }

        return result;
    }

    public static String fetchSessionIDFromCookies(Cookie[] cookies) {
        String sessionID = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(HTTP_UTILS_CONSTANT_KEYS_VALUES_PAIR.get("sessionID"))) {
                sessionID = cookie.getValue();
                break;
            }
        }

        return sessionID;
    }
}
