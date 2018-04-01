package org.springboot.demo;

public class DynamicProgram {

    public static void main(String[] args) {
        int dynamic = Dynamic(10);
        System.out.println(dynamic);
    }

    private static int Dynamic(int n) {
        if (n == 1 || n == 2) {
            return n;
        }
        int a = 1;
        int b = 2;
        int temp = 0;
        for (int j = 3; j < n; j++) {
            temp = a + b;
            a = b;
            b = temp;
        }
        return temp;
    }

}
