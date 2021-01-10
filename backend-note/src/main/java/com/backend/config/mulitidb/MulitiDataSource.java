package com.backend.config.mulitidb;

import java.util.Map;
import javax.sql.DataSource;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

@Data
@Slf4j
@NoArgsConstructor
public class MulitiDataSource extends AbstractRoutingDataSource {

    private String defaultDb;
    private Map<Object, Object> targetDataSources;

    public MulitiDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return MulitiDataSourceHolder.getDataSource();
    }


}
