package re.petrovoyu.rev;

import re.petrovoyu.rev.exception.LoadBalancerAddressLimitException;

import java.util.*;
import java.util.stream.Stream;

public class LoadBalancer {
    private static final int DEFAULT_ADDRESS_LIMIT = 10;

    private final List<String> addresses = new ArrayList<>();
    private final int limit;

    public LoadBalancer() {
        this.limit = DEFAULT_ADDRESS_LIMIT;
    }

    public LoadBalancer(int limit) {
        this.limit = limit;
    }

    public void register(String address) {
        if (addresses.size() >= limit) {
            throw new LoadBalancerAddressLimitException();
        }

        if (addresses.contains(address)) {
            return;
        }
        addresses.add(address);
    }

    public Collection<String> getAllAddresses() {
        return addresses;
    }

    public String get() {
        Random random = new Random();
        int choosedIndex = random.nextInt(addresses.size());
        return addresses.get(choosedIndex);
    }
}
