package com.backend.config.mulitidb;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.google.common.collect.Maps;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DynamicDataSourceConfig {

    @Bean(name = "readDb")
    @ConfigurationProperties("spring.datasource.read")
    public DataSource readDataSource(){
        DataSource dataSource = DruidDataSourceBuilder.create().build();
        return dataSource;
    }

    @Primary
    @Bean(name = "writeDb")
    @ConfigurationProperties("spring.datasource.write")
    public DataSource writeDataSource(){
        DataSource dataSource = DruidDataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean(name = "otherDb")
    @ConfigurationProperties("spring.datasource.other")
    public DataSource otherDataSource(){
        DataSource dataSource = DruidDataSourceBuilder.create().build();
        return dataSource;
    }


    @Bean
    public DynamicDataSource dataSource(@Qualifier("readDb") DataSource readDataSource,
                                        @Qualifier("writeDb") DataSource writeDataSource,
                                        @Qualifier("otherDb") DataSource otherDataSource) {
        Map<Object, Object> targetDataSources = Maps.newHashMapWithExpectedSize(2);
        targetDataSources.put(GloableDatasourceEnum.READ, readDataSource);
        targetDataSources.put(GloableDatasourceEnum.WRITE, writeDataSource);
        targetDataSources.put(GloableDatasourceEnum.OTHER, otherDataSource);
        // 还有数据源,在targetDataSources中继续添加
        System.out.println("DataSources:" + targetDataSources);
        return new DynamicDataSource(readDataSource, targetDataSources);
    }

}
