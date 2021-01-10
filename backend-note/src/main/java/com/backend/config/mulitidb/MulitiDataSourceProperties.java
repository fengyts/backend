package com.backend.config.mulitidb;

import com.alibaba.druid.pool.DruidDataSource;
import com.backend.config.YamlPropertyLoaderFactory;
import java.util.Map;
import java.util.Properties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author fengyts
 */
@Component
//@PropertySource(value = {"classpath:muliti-db.yml"}, encoding = "utf-8", factory = YamlPropertyLoaderFactory.class)
@ConfigurationProperties(prefix = "spring.datasource.muliti-db")
@Data
public class MulitiDataSourceProperties {

    /**
     * 默认数据库
     */
    private String defaultDb;

    /**
     * 动态数据源配置属性
     */
    private Map<String, Properties> dataSources;

}
