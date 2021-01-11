package com.backend.mulitidb.dprecated;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Deprecated
public class DynamicDataSource /*extends AbstractRoutingDataSource*/ {

    private static final ThreadLocal<GloableDatasourceEnum> contextHolder = new ThreadLocal<>();

//    public DynamicDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
//        super.setDefaultTargetDataSource(defaultTargetDataSource);
//        super.setTargetDataSources(targetDataSources);
//        super.afterPropertiesSet();
//    }
//
//    @Override
//    protected Object determineCurrentLookupKey() {
//        return getDataSource();
//    }

    public static void setDataSource(GloableDatasourceEnum dataSource) {
        contextHolder.set(dataSource);
    }

    public static GloableDatasourceEnum getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }


}
