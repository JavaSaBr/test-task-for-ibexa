package com.ss.load.balancer.balancer;

import com.ss.load.balancer.host.instance.HostInstance;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public interface Balancer {

    @NotNull Optional<HostInstance> next(@NotNull List<HostInstance> hostInstances);
}
