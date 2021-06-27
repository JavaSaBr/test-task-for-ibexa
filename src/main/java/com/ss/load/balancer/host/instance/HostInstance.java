package com.ss.load.balancer.host.instance;

import com.ss.load.balancer.request.Request;
import org.jetbrains.annotations.NotNull;

public interface HostInstance {

    float load();

    void handleRequest(@NotNull Request request);
}
