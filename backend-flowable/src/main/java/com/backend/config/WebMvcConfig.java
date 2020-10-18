package com.backend.config;

import com.backend.interceptor.AuthorityInterceptor;
import com.backend.util.SysUserHandler;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private ResourceProperties resourceProperties;

//    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/META-INF/resources/",
//            "classpath:/resources/", "classpath:/static/", "classpath:/public/" };

    private static ThymeleafViewResolver viewResolver;

    /**
     * 设置thymeleaf全局变量，在common/common_header.html中的script中接收为全局变量
     * @param viewResolver
     */
    @Resource
    public void initThymeleafViewResolver(ThymeleafViewResolver viewResolver) {
        WebMvcConfig.viewResolver = viewResolver;
//        configureThymeleafStaticVars("contextPath", "/");
    }

    private static void configureThymeleafStaticVars(String key, Object value) {
        viewResolver.addStaticVariable(key, value);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler( "/**", "/static/**", "/templates/**")
                .addResourceLocations(resourceProperties.getStaticLocations())
                .addResourceLocations("classpath:/templates/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new AuthorityInterceptor());
        //所有路径都被拦截
        registration.addPathPatterns("/**");
        //添加不拦截路径
        registration.excludePathPatterns("/static/**", "/templates/**");
        registration.excludePathPatterns("/login", "", "/login/doLogin", "/plugins/**");
    }


}

