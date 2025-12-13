package com.github.axinger.config;

import io.netty.util.concurrent.FastThreadLocal;

public class MyTrackId {

    private static final FastThreadLocal<Long> TrackId = new FastThreadLocal<>();

    public static Long getId() {
        return TrackId.get();
    }

    public static void setId(Long context) {
        TrackId.set(context);
    }

    public static void removeId() {
        TrackId.remove();
    }
}
