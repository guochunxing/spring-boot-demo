package org.springboot.demo;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(Application.class);
        springApplicationBuilder.web(true);
        Properties properties = getProperties();
        StandardEnvironment environment = new StandardEnvironment();
        environment.getPropertySources().addLast(new PropertiesPropertySource("micro", properties));
        springApplicationBuilder.environment(environment);
        springApplicationBuilder.run(args);
    }

    private static Properties getProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        propertiesFactoryBean.setIgnoreResourceNotFound(true);
        Resource fileSystemResource = resolver.getResource("file:/opt/fit2cloud/fit2cloud-test.properties");
        propertiesFactoryBean.setLocations(fileSystemResource);
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}
