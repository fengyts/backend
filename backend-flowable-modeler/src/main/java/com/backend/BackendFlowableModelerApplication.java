package com.backend;

import com.backend.config.AppDispatcherServletConfiguration;
import com.backend.config.ApplicationConfiguration;
import org.flowable.ui.modeler.properties.FlowableModelerAppProperties;
import org.flowable.ui.modeler.service.AppDefinitionPublishService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/*@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class
})
@SpringBootApplication(
        exclude = {SecurityAutoConfiguration.class})
public class BackendFlowableModelerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendFlowableModelerApplication.class, args);
    }

}*/

@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class
})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
//@ComponentScan(basePackages = {"com.backend","org.flowable.ui.modeler","org.flowable.ui.common"})
public class BackendFlowableModelerApplication {


    public static void main(String[] args) {
        SpringApplication.run(BackendFlowableModelerApplication.class, args);
    }

}
