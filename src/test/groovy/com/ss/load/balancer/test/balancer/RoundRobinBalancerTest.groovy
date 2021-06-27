package com.ss.load.balancer.test.balancer

import com.ss.load.balancer.balancer.impl.RoundRobinBalancer
import com.ss.load.balancer.host.instance.impl.StaticHostInstance
import com.ss.load.balancer.test.BaseSpec

class RoundRobinBalancerTest extends BaseSpec {

    def "should balance sequentially 3 host instances" () {

        given:

            def instances = [
                new StaticHostInstance(0),
                new StaticHostInstance(0),
                new StaticHostInstance(0),
            ]

            def balancer = new RoundRobinBalancer()
        when:
            def result = balancer.next(instances)
        then:
            result.get() == instances[0]
        when:
            result = balancer.next(instances)
        then:
            result.get() == instances[1]
        when:
            result = balancer.next(instances)
        then:
            result.get() == instances[2]
        when:
            result = balancer.next(instances)
        then:
            result.get() == instances[0]
        when:
            result = balancer.next(instances)
        then:
            result.get() == instances[1]
        when:
            result = balancer.next(instances)
        then:
            result.get() == instances[2]
    }

    def "should correctly works with one instance" () {

        given:

            def instances = [
                new StaticHostInstance(0)
            ]

            def balancer = new RoundRobinBalancer()
        when:
            def result = balancer.next(instances)
        then:
            result.get() == instances[0]
        when:
            result = balancer.next(instances)
        then:
            result.get() == instances[0]
    }

    def "should return empty optional when no available instances" () {

        given:
            def balancer = new RoundRobinBalancer()
        when:
            def result = balancer.next([])
        then:
            result.isEmpty()
    }

    def "should throw NPE when try to balance null" () {

        given:
            def balancer = new RoundRobinBalancer()
        when:
            balancer.next(null)
        then:
            thrown NullPointerException
    }
}
