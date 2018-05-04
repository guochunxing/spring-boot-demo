package org.springboot.demo.stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.*;

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

        Integer reduce1 = menu.stream().map(Dish::getCalories).reduce(0, Integer::max);


        Map<Type, List<Dish>> collect = menu.stream().filter(dish -> dish.getCalories() > 400)
                .sorted(Comparator.comparing(Dish::getCalories).reversed())
                .collect(Collectors.groupingBy(Dish::getType));

        menu.parallelStream()
                .filter(dish -> dish.getCalories() < 400)
                .sorted(Comparator.comparing(Dish::getCalories).thenComparing(Dish::getName).reversed())
                .forEach(dish -> System.out.println(dish.toString()));

        Optional<Integer> reduce = menu.parallelStream().map(Dish::getCalories).reduce((x, y) -> x + y);
        reduce.ifPresent(System.out::println);

        List<String> strings = Arrays.asList("qwer", "qwer");

        IntSummaryStatistics menuStatistics = menu.stream().collect(summarizingInt(Dish::getCalories));

        testFunction(strings, String::length);

    }

    public static void testFunction(List<String> strArr, Function<String, Integer> function) {

        List<Integer> collect = strArr.stream().map(function).collect(Collectors.toList());

        collect.forEach(System.out::println);

        testFlatMap();
    }

    public static void testFlatMap() {
        String[] arrayOfWords = {"Goodbye", "World"};
        List<String> collect = Arrays.stream(arrayOfWords)
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .sorted(String::compareToIgnoreCase)
                .collect(Collectors.toList());
        String join = StringUtils.join(collect, ",");
        System.out.println(join);
    }
}


