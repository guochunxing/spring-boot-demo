package org.springboot.demo;

import sun.rmi.rmic.iiop.ClassPathLoader;
import sun.tools.java.ClassPath;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class testClassIdentity {

    public static void main(String[] args) throws Exception {
        test();
    }

    private static void test() throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        String classPath = "org.springboot.demo.Sample";
        ClassLoader cs1 = new ClassPathLoader(new ClassPath(classPath));
        ClassLoader cs2 = new ClassPathLoader(new ClassPath(classPath));

        Class<?> aClass = cs1.loadClass(classPath);
        Class<?> bClass = cs2.loadClass(classPath);
        Object obj1 = aClass.newInstance();
        Object obj2 = bClass.newInstance();
        Method setSampleMethod = aClass.getMethod("setSample", java.lang.Object.class);
        setSampleMethod.invoke(obj1, obj2);
    }
}
