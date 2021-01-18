package com.autumn;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 了解如何启动springbean容器
 */
public class SpringBeanInit {

    public static void main(String[] args) {
        SpringNettyApplication.run(args);
    }


    /**
     * 1. 扫描当前包
     * 2. spring Bean配置
     * 3. 由 spring管理来自 yml文件读取到的属性
     */
    @ComponentScan
    @Configuration
    @PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
    static class SpringConfig {

    }

}
