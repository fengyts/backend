package com.backend.config.mulitidb;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.collect.Maps;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.sql.DataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

/**
 * @author fengyts
 */
@Configuration
@Slf4j
@Data
public class MulitiDataSourceConfig {

    //    @Autowired
//    private Environment env;

    private String defaultDb;
    private static final Map<String, DataSource> DATA_SOURCE_MAP = Maps.newHashMap();
    private static final Map<Object, Object> TARGET_DATA_SOURCE_MAP = Maps.newHashMap();

    /*
    @Bean(name = "readDb")
    @ConfigurationProperties(prefix = "spring.datasource.muliti-db.read")
    public DataSource dataSource() {
        try {
            DataSource ds = new DruidDataSource();
            return ds;
//            DataSource dataSource = DruidDataSourceBuilder.create().build();
//            return dataSource;
        } catch (Exception e) {
            log.info("创建dataSource 异常：{}", e);
        }
        return null;
    }

    @Bean
    public Object obj(@Qualifier("readDb") DataSource ds) {
        System.out.println();
        return null;
    }
    */

    @Bean(name = "mulitiDb")
    @ConditionalOnBean(MulitiDataSourceProperties.class)
    public Map<String, DataSource> mulitiDatasources(MulitiDataSourceProperties mulitiDbProperties) {
        Map<String, Properties> datasources = mulitiDbProperties.getDataSources();
        if (null == datasources || datasources.isEmpty()) {
//            throw new MessageException("多数据源创建为空");
            log.info("多数据源配置为空");
            return DATA_SOURCE_MAP;
        }
        Set<String> dsNames = datasources.keySet();
        for (String dsName : dsNames) {
            try {
                Properties prop = datasources.get(dsName);
                DataSource ds = DruidDataSourceFactory.createDataSource(prop);
                DATA_SOURCE_MAP.put(dsName, ds);
                TARGET_DATA_SOURCE_MAP.put(dsName, ds);
            } catch (Exception e) {
                log.info("初始化数据库多数据源配置异常:dbName-{}, {}", dsName, e);
            }
        }
//        defaultDb = mulitiDbProperties.getDefaultDb();
        setDefaultDataSource(mulitiDbProperties.getDefaultDb());
        return DATA_SOURCE_MAP;
    }

    @Bean("dynamicDataSource")
    public MulitiDataSource createMulitiDynamicDbSource(@Qualifier("mulitiDb") Map<String, Object> mulitiDbs) {
        MulitiDataSource mulitiDataSource = new MulitiDataSource();
//        Map<Object, Object> targetDataSources = Maps.newHashMapWithExpectedSize(mulitiDbs.size());
//        for (String key : mulitiDbs.keySet()) {
//            targetDataSources.put(key, mulitiDbs.get(key));
//        }
//        mulitiDataSource.setTargetDataSources(targetDataSources);
        mulitiDataSource.setTargetDataSources(TARGET_DATA_SOURCE_MAP);
        mulitiDataSource.setDefaultDb(defaultDb);
        return mulitiDataSource;
    }

    private void setDefaultDataSource(String defaultDataSource) {
        if (ObjectUtils.isEmpty(defaultDataSource)) {
            if (ObjectUtils.isEmpty(DATA_SOURCE_MAP)) {
                return;
            }
            Set<String> keys = DATA_SOURCE_MAP.keySet();
            try {
                defaultDb = keys.stream()
                        .filter(key -> (keys.contains("write")
                                || keys.contains("write1")
                                || keys.contains("master")
                                || keys.contains("master1"))
                        ).findFirst().get();
                return;
            } catch (Exception e) {
            }
            if (ObjectUtils.isEmpty(defaultDb)) {
                defaultDb = keys.stream().findFirst().get();
            }
        }
    }

}
