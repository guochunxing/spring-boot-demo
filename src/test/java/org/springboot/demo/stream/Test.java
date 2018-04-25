package org.springboot.demo.stream;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Test {

    public static void main(String[] args) {

        List<Dish> menu = Arrays.asList(
                new Dish("pork", false, 800, Type.MEAT),
                new Dish("beef", false, 700, Type.MEAT),
                new Dish("chicken", false, 400, Type.MEAT),
                new Dish("french fries", true, 530, Type.OTHER),
                new Dish("rice", true, 350, Type.OTHER),
                new Dish("season fruit", true, 120, Type.OTHER),
                new Dish("pizza", true, 550, Type.OTHER),
                new Dish("prawns", false, 300, Type.FISH),
                new Dish("salmon", false, 450, Type.FISH));


        List<String> collect = menu.parallelStream()
                .filter(dish -> dish.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories))
                .map(Dish::toString)
                .collect(Collectors.toList());

        Optional<Integer> reduce = menu.parallelStream().map(Dish::getCalories).reduce((x, y) -> x + y);
        reduce.ifPresent(System.out::println);


        collect.forEach(System.err::println);

    }


}


