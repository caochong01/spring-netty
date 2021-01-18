package com.autumn;

import io.netty.channel.EventLoopGroup;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * SpringNetty启动器
 */
public class SpringNettyApplication {

    /**
     * 1. 加载application配置文件，启动spring容器，并初始化；
     * 2. 获取配置参数，使用配置参数初始化Netty的配置（EventLoopGroup、Bootstrap）；
     * 3. 获取自定义的channel及Handle等，构成完整的调度程序（Bootstrap.childHandler）；
     * 4. 启动Netty服务等；
     * 5. 加载Netty关闭钩子、spring容器关闭钩子；
     *
     * <p>
     * 需要做的：
     * 1. 自定义扫描器，实现netty路由功能，并加载到容器中；
     * 2. 自定义netty channelHandle扫描，支持路由，并加载到容器中；
     * 3. 加载application配置文件
     * 4. 主要在于handle的注入：（协议处理和动态注册器、拦截器、过滤器、路由选择器、处理类环切AOP[异常、事务]、真正处理类、Netty堆外内存销毁）
     *
     * @param args 启动参数
     */
    public static void run(String[] args) {
        System.out.println("启动Netty服务和spring初始化容器");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringBeanInit.SpringConfig.class);
        SpringStaticEnv.setApplicationContext(context); // 存储一份上下文信息
        System.out.println(context.getApplicationName());

        // TODO 测试
        TestBean.UserService userService = context.getBean(TestBean.UserService.class);
        EventLoopGroup loopGroup = context.getBean(EventLoopGroup.class);
        Class<?> socketChannelClass = (Class<?>) context.getBean("serverSocketChannel");
        Object tRoute = context.getBean("testRoute");

        System.out.println(userService.getSysUser());
        System.out.println(loopGroup);
        System.out.println(socketChannelClass);
        System.out.println(tRoute);


        context.close();


        /*****************************启动规划********************************/

        // 读取application配置文件，并缓存

        // 启动spring容器并初始化

        // 使用配置参数初始化Netty的外部配置值

        // 读取自定义注解，并组织好逻辑（过滤、路由、json格式化、异常处理、事务等功能）

        // 将逻辑注入到Netty的channel中

        // 注册Netty关闭钩子、spring容器关闭钩子；

        // 完成。

    }

}
