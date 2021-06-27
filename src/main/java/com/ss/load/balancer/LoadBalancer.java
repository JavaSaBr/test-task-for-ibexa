package com.ss.load.balancer;

import com.ss.load.balancer.balancer.Balancer;
import com.ss.load.balancer.host.instance.HostInstance;
import com.ss.load.balancer.request.Request;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoadBalancer {

    public static final int SERVICE_UNAVAILABLE = 503;

    @NotNull List<HostInstance> hostInstances;
    @NotNull Balancer balancer;

    public LoadBalancer(
        @NotNull List<HostInstance> hostInstances,
        @NotNull Balancer balancer
    ) {
        this.hostInstances = List.copyOf(hostInstances);
        this.balancer = balancer;
    }

    public void handleRequest(@NotNull Request request) {

        balancer
            .next(hostInstances)
            .ifPresentOrElse(
                hostInstance -> hostInstance.handleRequest(request),
                () -> request.respond(SERVICE_UNAVAILABLE)
            );
    }
}
