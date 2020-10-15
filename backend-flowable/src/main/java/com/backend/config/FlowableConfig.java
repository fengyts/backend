package com.backend.config;

import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.boot.EngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 为解决flowable图片中的中文乱码
 *
 * @author DELL
 * @date
 */
@Configuration
public class FlowableConfig implements EngineConfigurationConfigurer<SpringProcessEngineConfiguration> {

    private static final String FONT_TYPE = "宋体";

    @Override
    public void configure(SpringProcessEngineConfiguration engineConfiguration) {
        engineConfiguration.setActivityFontName(FONT_TYPE);
        engineConfiguration.setLabelFontName(FONT_TYPE);
        engineConfiguration.setAnnotationFontName(FONT_TYPE);

        /*
         * DeploymentMode 可配置的选项:
         * default:
         *      将所有资源组织在一个部署中，整体用于重复检测过滤。这是默认值，在未设置这个参数时也会用这个值。
         * single-resource:
         *      为每个资源创建一个单独的部署，并用于重复检测过滤。
         *      这时您将每个流程定义分开部署, 并且只有在流程定义版本已更改时才创建新流程定义版本.
         * resource-parent-folder:
         *      为同一个目录下的资源创建一个单独的部署，并用于重复检测过滤。
         *      这个参数值可以为大多数资源创建独立的部署。同时仍可以通过将部分资源放在同一个目录下，将它们组织在一起
        */
        engineConfiguration.setDeploymentMode("single-resource");
    }


}
