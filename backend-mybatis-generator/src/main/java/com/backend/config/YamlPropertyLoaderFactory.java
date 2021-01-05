package com.backend.config;

import java.io.IOException;
import java.util.List;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.DefaultPropertySourceFactory;
import org.springframework.core.io.support.EncodedResource;

/**
 * 在使用@PropertySource和@ConfigurationProperties时,
 * 默认配置文件为*.properties类型，若为 *.yml类型则解析结果是null,
 * 配置该类即可解决该问题
 * 使用方法：在使用@PropertySource时，指定{factory为=YamlPropertyLoaderFactory.class}即可
 * @author fengyts
 */
public class YamlPropertyLoaderFactory extends DefaultPropertySourceFactory {

    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        if (resource == null) {
            return super.createPropertySource(name, resource);
        }
        YamlPropertySourceLoader loader = new YamlPropertySourceLoader();
        List<PropertySource<?>> sources = loader.load(resource.getResource().getFilename(), resource.getResource());
        return sources.get(0);
    }

}
