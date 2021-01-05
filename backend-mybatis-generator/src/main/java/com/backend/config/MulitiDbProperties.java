package com.backend.config;


import java.util.Properties;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:application.yml")
@ConfigurationProperties(prefix = "muliti-db")
@Data
public class MulitiDbProperties {

    private Properties read;

}
