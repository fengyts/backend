package com.backend;

import com.backend.config.AppDispatcherServletConfiguration;
import com.backend.config.ApplicationConfiguration;
import com.backend.util.SysUserHandler;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

/**
 * @author DELL
 */
@Import({
        ApplicationConfiguration.class,
        AppDispatcherServletConfiguration.class
})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableTransactionManagement
@Slf4j
public class BackendFlowableApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendFlowableApplication.class, args);
    }

    @Bean
    public ApplicationRunner applicationRunner(ApplicationContext ctx) {
        return args -> {
            log.info("==============项目启动成功=============");
            String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
            log.info("==============start at " + now + "==============");
        };
    }

    /**
     * viewResolver赋值给 SysUserHandler, 用于登陆时设置thymeleaf全局登陆用户变量
     * @param viewResolver
     */
    @Resource
    private void configureThymeleafStaticVars(ThymeleafViewResolver viewResolver) {
        SysUserHandler.initThymeleafViewResolver(viewResolver);
    }

}
