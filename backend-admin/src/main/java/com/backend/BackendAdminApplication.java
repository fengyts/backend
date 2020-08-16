package com.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.backend.*.mapper")
public class BackendAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendAdminApplication.class, args);
    }

}
