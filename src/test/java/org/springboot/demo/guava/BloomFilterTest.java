package org.springboot.demo.guava;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.util.ArrayList;
import java.util.List;

public class BloomFilterTest {

    public static void main(String[] args) {

        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 100000, 0.0001);

        for (int i = 0; i < 10000; i++) {
            bloomFilter.put(i);
        }
        List<Integer> intercept = new ArrayList<>();
        for (int i = 9700; i < 12000; i++) {
            boolean mightContain = bloomFilter.mightContain(i);
            if (mightContain) {
                intercept.add(i);
            }
            //不包含操作
        }
        System.out.println("拦截相同值：" + intercept.size());

        List<Integer> accidentallyInjure = new ArrayList<>();
        for (int i = 20000; i < 30000; i++) {
            if (bloomFilter.mightContain(i)) {
                accidentallyInjure.add(i);
            }
        }
        System.out.println("有误伤的数量：" + accidentallyInjure.size());

    }
}
