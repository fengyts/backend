package com.backend.mulitidb;

import java.util.Map;
import javax.sql.DataSource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Data
@Slf4j
//@NoArgsConstructor
@Order(1)
public class MulitiDataSource extends AbstractRoutingDataSource {

//    private DataSource defaultTargetDataSource;
    private Object defaultTargetDataSource;
    private Map<Object, Object> targetDataSources;

    public MulitiDataSource(Object defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return MulitiDataSourceHolder.getDataSource();
    }


}
