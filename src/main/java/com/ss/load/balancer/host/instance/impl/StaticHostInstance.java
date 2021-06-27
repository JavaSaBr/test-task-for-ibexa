package com.ss.load.balancer.host.instance.impl;

import com.ss.load.balancer.host.instance.HostInstance;
import com.ss.load.balancer.request.Request;
import org.jetbrains.annotations.NotNull;

public record StaticHostInstance(float load) implements HostInstance {

    @Override
    public void handleRequest(@NotNull Request request) {

        // handle the request
        request.respond(200);
    }
}
