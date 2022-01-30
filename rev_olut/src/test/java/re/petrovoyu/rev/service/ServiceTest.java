package re.petrovoyu.rev.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    @Mock
    Service service;

    @Test
    void getStream() {
        Stream<String> stream = new Service().getStream(List.of("one", "two"));
        assertEquals(2, stream.count());
    }
}