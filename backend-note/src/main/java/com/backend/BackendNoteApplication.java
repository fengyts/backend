package com.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.backend.*.mapper")
public class BackendNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendNoteApplication.class, args);
    }

}
