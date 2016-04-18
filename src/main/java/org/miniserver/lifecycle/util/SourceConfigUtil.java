package org.miniserver.lifecycle.util;

public final class SourceConfigUtil {

    public static boolean isModifiedSince(long time, Source source) {
        if (time > source.getLastModifyTime()) {
            return true;
        } else {
            return false;
        }
    }
}
