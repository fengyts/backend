package com.backend;

import com.backend.config.AppDispatcherServletConfiguration;
import com.backend.config.ApplicationConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;


/**
 * 此处必须导入 ApplicationConfiguration和AppDispatcherServletConfiguration,否则页面flowable-modeler-ui报错
 * @author DELL
 */
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
