package com.autumn;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {

    /**
     * Create a {@link PropertySource} that wraps the given resource.
     *
     * @param name     the name of the property source
     *                 (can be {@code null} in which case the factory implementation
     *                 will have to generate a name based on the given resource)
     * @param resource the resource (potentially encoded) to wrap
     * @return the new {@link PropertySource} (never {@code null})
     * @throws IOException if resource resolution failed
     * @See 解析器 org.springframework.context.annotation.ConfigurationClassParser
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        // 请配置注解参数：ignoreResourceNotFound = true
        try {
            String filename = Objects.requireNonNull(resource.getResource().getFilename());
            //1.创建yaml文件解析工厂
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            //2.设置资源内容
            yaml.setResources(resource.getResource());
            //3.解析成properties文件
            Properties properties = yaml.getObject();
            if (properties == null) {
                throw new FileNotFoundException();
            }
            //4.返回符合spring的PropertySource对象
            return name != null ? new PropertiesPropertySource(name, properties)
                    : new PropertiesPropertySource(filename, properties);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Yaml解析异常，异常原因：" + e.getMessage());
        }

    }
}
