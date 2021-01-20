package com.autumn;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 配置文件管理类：
 * 1. 默认读取位置为 classpath:application.yml。
 * 2. 由 spring管理来自 yml文件读取到的属性，交给 @See {@link YamlPropertySourceFactory} 管理
 */
@ComponentScan
@Configuration
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class SpringNettyPropertySource {
}
