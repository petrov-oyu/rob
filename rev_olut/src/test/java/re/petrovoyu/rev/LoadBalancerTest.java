package re.petrovoyu.rev;

import org.junit.jupiter.api.Test;
import re.petrovoyu.rev.exception.LoadBalancerAddressLimitException;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class LoadBalancerTest {

    /*
    Register instances

Address should be unique, it should not be possible to register the same address two times
Load balancer should accept up to 10 addresses
     */

        /*
    2 - Random invocation
Develop an algorithm that, when invoking the Load Balancer's get() method multiple times,
should return one backend-instance choosing between the registered ones randomly.
     */

    @Test
    void shouldRegisterAddress() {
        //given
        LoadBalancer loadBalancer = new LoadBalancer();
        String address = "new address";

        //when
        loadBalancer.register(address);

        //then
        assertTrue(loadBalancer.getAllAddresses().contains(address));
    }

    @Test
    void shouldNotRegisterDuplicatesAddresses() {
        //given
        LoadBalancer loadBalancer = new LoadBalancer();
        String address = "new address";
        int beforeRegister = loadBalancer.getAllAddresses().size();

        //when
        loadBalancer.register(address);
        loadBalancer.register(address);

        //then
        assertEquals(beforeRegister + 1, loadBalancer.getAllAddresses().size());
    }

    @Test
    void shouldNotRegisterUpToLimit() {
        //given
        int limit = 2;
        LoadBalancer loadBalancer = new LoadBalancer(limit);
        String address = "new address";
        String secondAddress = "second new address";
        String thirdAddress = "thrid new address";
        loadBalancer.register(address);
        loadBalancer.register(secondAddress);

        //then
        assertThrows(LoadBalancerAddressLimitException.class, () -> loadBalancer.register(thirdAddress));
    }

    @Test
    void shouldGetAddressByRandom() {
        LoadBalancer loadBalancer = new LoadBalancer();
        String address = "new address";
        String secondAddress = "second new address";
        String thirdAddress = "thrid new address";
        loadBalancer.register(address);
        loadBalancer.register(secondAddress);
        loadBalancer.register(thirdAddress);
        Set<String> expectedAddresses = Set.of(address, secondAddress, thirdAddress);

        Set<String> actualAddresses = new HashSet<>(100);
        for (int i = 0; i < 100; i++) {
            actualAddresses.add(loadBalancer.get());
        }

        assertEquals(expectedAddresses, actualAddresses);
    }
}