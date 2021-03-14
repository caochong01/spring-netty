package com.spring;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

@Configuration
public class TestApplicationListener {

    /**
     * Event raised when an {@code ApplicationContext} gets initialized or refreshed.
     * 执行 refresh()方法时
     */
    @Component
    class A implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            System.out.println("ContextRefreshedEvent: Spring上下文正在初始化或刷新");
            System.out.println(event.getSource());
        }
    }

    /**
     * Event raised when an {@code ApplicationContext} gets started.
     */
    @Component
    class B implements ApplicationListener<ContextStartedEvent> {

        @Override
        public void onApplicationEvent(ContextStartedEvent event) {
            System.out.println("ContextStartedEvent: Spring上下文正在启动");
        }
    }

    /**
     * Event raised when an {@code ApplicationContext} gets stopped.
     */
    @Component
    class C implements ApplicationListener<ContextStoppedEvent> {

        @Override
        public void onApplicationEvent(ContextStoppedEvent event) {
            System.out.println("ContextStoppedEvent: Spring上下文正在停止");
        }
    }

    /**
     * Event raised when an {@code ApplicationContext} gets closed.
     * <p>
     * 调用JVM钩子，销毁 ApplicationContext上下文
     */
    @Component
    class D implements ApplicationListener<ContextClosedEvent> {

        @Override
        public void onApplicationEvent(ContextClosedEvent event) {
            System.out.println("ContextClosedEvent: Spring上下文正在关闭");
        }
    }
}
