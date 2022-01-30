package ru.petrovoyu.bestpractice.stream;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class StreamApi {

    public void EnumCollector() {
        enum Numbers {
            ONE, TWO, THREE;
        }

        Arrays.stream(Numbers.values())
                .collect(Collectors.toSet());
    }
}
