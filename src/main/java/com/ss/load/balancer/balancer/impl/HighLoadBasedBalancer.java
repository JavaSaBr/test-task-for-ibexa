package com.ss.load.balancer.balancer.impl;

import com.ss.load.balancer.balancer.Balancer;
import com.ss.load.balancer.host.instance.HostInstance;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HighLoadBasedBalancer implements Balancer {

    float highLoad;

    public HighLoadBasedBalancer(float highLoad) {

        if (highLoad <= 0) {
            throw new IllegalArgumentException("High load should not be less or equal 0");
        } else if (highLoad > 1F) {
            throw new IllegalArgumentException("High load should not be bigger or equal 1");
        }

        this.highLoad = highLoad;
    }

    @Override
    public @NotNull Optional<HostInstance> next(@NotNull List<HostInstance> hostInstances) {

        if (hostInstances.isEmpty()) {
            return Optional.empty();
        } else if (hostInstances.size() == 1) {
            return Optional.of(hostInstances.get(0));
        }

        return hostInstances
            .stream()
            .filter(hostInstance -> hostInstance.load() < highLoad)
            .findAny()
            .or(() -> hostInstances
                .stream()
                .min((first, second) -> Float.compare(first.load(), second.load())));
    }
}
