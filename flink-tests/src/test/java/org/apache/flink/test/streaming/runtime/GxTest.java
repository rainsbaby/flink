package org.apache.flink.test.streaming.runtime;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

public class GxTest {

    public static void main(String[] args) {
        Optional<Integer> res = Stream.of("f", "ab", "abdcd")
                .map(s -> s.length())
                .filter(l -> l <= 3)
                .max(Comparator.comparingInt(o -> o));
        System.out.println(res);
    }
}
