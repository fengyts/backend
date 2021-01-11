package com.backend;

import com.backend.mulitidb.dprecated.DynamicDataSourceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({DynamicDataSourceConfig.class})
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@MapperScan(basePackages = {"com.backend.*.mapper"})
public class BackendNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendNoteApplication.class, args);
    }

}
