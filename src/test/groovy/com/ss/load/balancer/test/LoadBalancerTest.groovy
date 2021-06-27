package com.ss.load.balancer.test

import com.ss.load.balancer.LoadBalancer
import com.ss.load.balancer.balancer.Balancer
import com.ss.load.balancer.balancer.impl.HighLoadBasedBalancer
import com.ss.load.balancer.balancer.impl.RoundRobinBalancer
import com.ss.load.balancer.host.instance.HostInstance
import com.ss.load.balancer.host.instance.impl.StaticHostInstance
import com.ss.load.balancer.request.impl.SimpleRequest

class LoadBalancerTest extends BaseSpec {

    def "should forward request to instance"(List<HostInstance> instances, Balancer balancer) {

        given:
            def loadBalancer = new LoadBalancer(instances, balancer)
            def request = new SimpleRequest()
        when:
            loadBalancer.handleRequest(request)
        then:
            request.respond == 200
        where:
            instances << [
                [
                    new StaticHostInstance(0),
                    new StaticHostInstance(0),
                    new StaticHostInstance(0),
                ],
                [
                    new StaticHostInstance(0),
                ],
                [
                    new StaticHostInstance(0.9),
                    new StaticHostInstance(0.9),
                    new StaticHostInstance(0.9),
                ],
                [
                    new StaticHostInstance(0),
                ],
                [
                    new StaticHostInstance(0.1),
                    new StaticHostInstance(0.2),
                    new StaticHostInstance(0.3),
                ],
            ]
            balancer << [
                new RoundRobinBalancer(),
                new RoundRobinBalancer(),
                new HighLoadBasedBalancer(0.75),
                new HighLoadBasedBalancer(0.75),
                new HighLoadBasedBalancer(0.75),
            ]
    }

    def "should not forward request when no any available instances"(Balancer balancer) {

        given:
            def loadBalancer = new LoadBalancer([], balancer)
            def request = new SimpleRequest()
        when:
            loadBalancer.handleRequest(request)
        then:
            request.respond == 503
        where:
            balancer << [
                new RoundRobinBalancer(),
                new HighLoadBasedBalancer(0.75),
            ]
    }
}
