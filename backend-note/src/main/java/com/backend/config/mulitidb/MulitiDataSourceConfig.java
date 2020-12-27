package com.backend.config.mulitidb;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author fengyts
 */
@Configuration
@Slf4j
@Data
public class MulitiDataSourceConfig {

    private static final Map<String, DataSource> DATASOURCE_MAP = Maps.newHashMap();

    @Bean
    @ConditionalOnBean(MulitiDatasourcePropertie.class)
    public Map<String, DataSource> intialDataSource(MulitiDatasourcePropertie mulitiDs) {
        try {
            Map<String, Properties> dataSources = mulitiDs.getDataSources();
            Set<String> dbKeys = dataSources.keySet();
            for (String dbKey : dbKeys) {
                Properties prop = dataSources.get(dbKey);
                DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);
                DATASOURCE_MAP.put(dbKey, dataSource);
            }
            return DATASOURCE_MAP;
        } catch (Exception e) {
            log.info("初始化数据库配置对象异常:{}", e);
        }
        return null;
    }

    public Map<String, DataSource> getDataSourceList() {
        return DATASOURCE_MAP;
    }

}
