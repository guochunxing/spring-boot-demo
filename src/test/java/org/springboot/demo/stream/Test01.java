package org.springboot.demo.stream;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class Test01 {

    public static void main(String[] args) {

        fibonacci();
    }

    public static void infiniteFlow() {
        Stream.iterate(0, n -> n + 2).limit(10).forEach(System.err::println);
    }

    public static void fibonacci() {
        Stream.iterate(new int[]{0, 1}, t -> new int[]{t[1], t[0] + t[1]}).limit(20).forEach(t -> System.out.println(t[0]));

        LongStream.rangeClosed(0, 10).forEach(System.out::println);
    }
}
