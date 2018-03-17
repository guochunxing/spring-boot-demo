package org.springboot.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "org.springboot.demo.dao", sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
public class MybatisConfig {

    @Autowired
    private Environment env;

    @Bean
    @Primary
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
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

    /**
     * 分页插件需要自己写，不然无法添加其他的plugins
     *
     * @return page helper
     */
    @Bean
    public PageInterceptor pageInterceptor() {
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("reasonable", "true");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("pageSizeZero", "true");
        pageInterceptor.setProperties(properties);
        return pageInterceptor;
    }
}