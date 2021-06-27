package com.ss.load.balancer.balancer.impl;

import com.ss.load.balancer.balancer.Balancer;
import com.ss.load.balancer.host.instance.HostInstance;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoundRobinBalancer implements Balancer {

    @NotNull AtomicInteger nextIndex = new AtomicInteger(-1);

    @Override
    public @NotNull Optional<HostInstance> next(@NotNull List<HostInstance> hostInstances) {

        if (hostInstances.isEmpty()) {
            return Optional.empty();
        } else if (hostInstances.size() == 1) {
            return Optional.of(hostInstances.get(0));
        }

        for (;;) {

            var index = nextIndex.incrementAndGet();

            if (index >= hostInstances.size()) {
                nextIndex.compareAndSet(index, -1);
                continue;
            }

            return Optional.of(hostInstances.get(index));
        }
    }
}
