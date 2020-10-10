package com.backend;

import com.backend.config.AppDispatcherServletConfiguration;
import com.backend.config.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @author DELL
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class
})
public class BackendFlowableMuiIntegrateApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendFlowableMuiIntegrateApplication.class, args);
    }

}
