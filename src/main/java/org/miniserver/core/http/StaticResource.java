package org.miniserver.core.http;


import com.google.common.collect.ImmutableList;

public final class StaticResource {

    public static final ImmutableList<String> STATIC_RESOURCE = ImmutableList.<String>builder()
            .add("css")
            .add("js")
            .add("jpg")
            .add("gif")
            .add("swf")
            .build();

}
