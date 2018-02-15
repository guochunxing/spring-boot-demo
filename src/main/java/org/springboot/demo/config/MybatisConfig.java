package org.springboot.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.pagehelper.PageInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springboot.demo.utils.EnvironmentUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@MapperScan(basePackages = "org.springboot.demo.dao", sqlSessionFactoryRef = "sqlSessionFactory")
@EnableTransactionManagement
public class MybatisConfig {

    /**
     * @return DataSource
     * @DependsOn("environmentUtil") 等待 environmentUtil工具类加载
     */
    @Bean
    @Primary
    @DependsOn("environmentUtil")
    public DataSource dataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(EnvironmentUtil.getProperty("rdb.user"));
        dataSource.setDriverClassName(EnvironmentUtil.getProperty("rdb.driver"));
        dataSource.setPassword(EnvironmentUtil.getProperty("rdb.password"));
        dataSource.setUrl(EnvironmentUtil.getProperty("rdb.url"));
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