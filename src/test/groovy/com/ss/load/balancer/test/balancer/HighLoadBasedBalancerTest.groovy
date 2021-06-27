package com.ss.load.balancer.test.balancer

import com.ss.load.balancer.balancer.impl.HighLoadBasedBalancer
import com.ss.load.balancer.host.instance.HostInstance
import com.ss.load.balancer.host.instance.impl.StaticHostInstance
import spock.lang.Specification

class HighLoadBasedBalancerTest extends Specification {

    def "should return first instance with load less than 0.75" () {

        given:

            def instance1 = Mock(HostInstance)
            def instance2 = Mock(HostInstance)
            def instance3 = Mock(HostInstance)

            def instances = [
                instance1,
                instance2,
                instance3
            ]

            def balancer = new HighLoadBasedBalancer(0.75F)
        when:
            def result = balancer.next(instances)
        then:
            instance1.load() >> 0.9F
            instance2.load() >> 0.8F
            instance3.load() >> 0.7F
        then:
            result.get() == instances[2]
        when:
            result = balancer.next(instances)
        then:
            instance1.load() >> 0.7F
            instance2.load() >> 0.8F
            instance3.load() >> 0.9F
        then:
            result.get() == instances[0]
        when:
            result = balancer.next(instances)
        then:
            instance1.load() >> 0.8F
            instance2.load() >> 0.7F
            instance3.load() >> 0.9F
        then:
            result.get() == instances[1]
    }

    def "should return instance with lowest load when all instances have load more than 0.75" () {

        given:

            def instance1 = Mock(HostInstance)
            def instance2 = Mock(HostInstance)
            def instance3 = Mock(HostInstance)

            def instances = [
                instance1,
                instance2,
                instance3
            ]

            def balancer = new HighLoadBasedBalancer(0.75F)
        when:
            def result = balancer.next(instances)
        then:
            instance1.load() >> 0.9F
            instance2.load() >> 0.95F
            instance3.load() >> 0.8F
        then:
            result.get() == instances[2]
        when:
            result = balancer.next(instances)
        then:
            instance1.load() >> 0.9F
            instance2.load() >> 0.99F
            instance3.load() >> 0.93F
        then:
            result.get() == instances[0]
        when:
            result = balancer.next(instances)
        then:
            instance1.load() >> 0.86F
            instance2.load() >> 0.85F
            instance3.load() >> 0.97F
        then:
            result.get() == instances[1]
        when:
            result = balancer.next(instances)
        then:
            instance1.load() >> 0.99F
            instance2.load() >> 0.99F
            instance3.load() >> 0.99F
        then:
            result.isPresent()
    }

    def "should correctly works with one instance" () {

        given:

            def instances = [
                new StaticHostInstance(0)
            ]

            def balancer = new HighLoadBasedBalancer(0.75F)
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
            def balancer = new HighLoadBasedBalancer(0.75F)
        when:
            def result = balancer.next([])
        then:
            result.isEmpty()
    }

    def "should throw NPE when try to balance null" () {

        given:
            def balancer = new HighLoadBasedBalancer(0.75F)
        when:
            balancer.next(null)
        then:
            thrown NullPointerException
    }
}
