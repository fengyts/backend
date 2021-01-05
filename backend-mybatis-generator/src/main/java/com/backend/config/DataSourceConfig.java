package com.backend.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class DataSourceConfig {

//    @Autowired
//    private Environment env;

//    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.read")
//    public DataSource dataSource(DataSourceProperties properties) {
//        try {
//            BeanMap beanMap = BeanMap.create(properties);
//            DataSource dataSource = DruidDataSourceFactory.createDataSource(beanMap);
//            return dataSource;
//        } catch (Exception e) {
//            log.info("创建dataSource 异常：{}", e);
//        }
//        return null;
//    }

    @Bean
//    @ConfigurationProperties(prefix = "spring.datasource.read")
//    @ConfigurationProperties(prefix = "muliti-db")
    public DataSource dataSource(MulitiDbProperties prop) {
        try {
            DataSource dataSource = DruidDataSourceBuilder.create().build();
            return dataSource;
        } catch (Exception e) {
            log.info("创建dataSource 异常：{}", e);
        }
        return null;
    }


}
