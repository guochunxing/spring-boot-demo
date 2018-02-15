package org.springboot.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class EnvironmentUtil {

    private static Environment environment;

    @Autowired
    public void setEnvironment(Environment environment) {
        EnvironmentUtil.environment = environment;
    }

    public static <T> T getProperty(String key, Class<T> targetType, T defaultValue) {
        return environment.getProperty(key, targetType, defaultValue);
    }

    public static <T> T getProperty(String key, Class<T> targetType) {
        return environment.getProperty(key, targetType);
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return environment.getProperty(key, defaultValue);
    }

    public static Integer getInteger(String key, Integer defaultValue) {
        return environment.getProperty(key, Integer.class, defaultValue);
    }
}
