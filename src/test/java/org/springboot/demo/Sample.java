package org.springboot.demo;

public class Sample {

    private Sample instance;

    public void setSample(Sample instance) {
        this.instance = instance;
        System.out.println("执行了");
    }
}