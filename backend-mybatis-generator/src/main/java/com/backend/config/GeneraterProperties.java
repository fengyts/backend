package com.backend.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:mybatis-generater.yml")
@ConfigurationProperties(prefix="mybatis-generate")
public class GeneraterProperties {
}
