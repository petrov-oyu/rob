package re.petrovoyu.rev.service;

import java.util.Collection;
import java.util.stream.Stream;

public class Service {

    public <T> Stream<T> getStream(Collection<T> collection) {
        return collection.stream();
    }
}
