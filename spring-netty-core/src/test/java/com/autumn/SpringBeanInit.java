package com.autumn;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 了解如何启动springbean容器
 */
public class SpringBeanInit {

    public static void main(String[] args) {
        SpringNettyApplication.run(args);



//        Class<?> aClass = new NettyConfig().serverSocketChannel(null);
//        System.out.println(aClass);
    }

    @ComponentScan
    @Configuration
    static class SpringConfig {

    }

}
