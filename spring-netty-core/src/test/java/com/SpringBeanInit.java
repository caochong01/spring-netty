package com;

import com.autumn.SpringNettyApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 了解如何启动springbean容器
 */
@Configuration
@ComponentScan
public class SpringBeanInit {

    public static void main(String[] args) {
        SpringNettyApplication.run(SpringBeanInit.class, args);
    }
}

