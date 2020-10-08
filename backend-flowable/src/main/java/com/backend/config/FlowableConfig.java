package com.backend.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 为解决flowable图片中的中文乱码
 *
 * @author puhaiyang
 * @date 2018/12/19
 */
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    private static final String FONT_TYPE = "宋体";

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName(FONT_TYPE);
        engineConfiguration.setLabelFontName(FONT_TYPE);
        engineConfiguration.setAnnotationFontName(FONT_TYPE);
    }

}
