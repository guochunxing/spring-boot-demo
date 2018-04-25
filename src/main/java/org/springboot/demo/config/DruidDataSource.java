package org.springboot.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
public class DruidDataSource {


    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        com.alibaba.druid.pool.DruidDataSource dataSource = new com.alibaba.druid.pool.DruidDataSource();
        dataSource.setUsername(env.getProperty("rdb.user"));
        dataSource.setDriverClassName(env.getProperty("rdb.driver"));
        dataSource.setPassword(env.getProperty("rdb.password"));
        dataSource.setUrl(env.getProperty("rdb.url"));
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(10);
        dataSource.setMaxActive(50);
        dataSource.setMaxWait(60000);
        dataSource.setTimeBetweenConnectErrorMillis(60000);//配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
        dataSource.setBreakAfterAcquireFailure(false);
        return dataSource;
    }

}
