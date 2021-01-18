package com.autumn;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

/**
 * spring 的事件监听示例
 * 事件种类详见 @See {@link org.springframework.context.event.ApplicationContextEvent} 的实现子类
 */
@Component
public class TestApplicationListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        System.out.println("存储一份上下文信息");
        SpringStaticEnv.setApplicationContext(context); // 存储一份上下文信息

        Map<String, Object> routeAnnotation = context.getBeansWithAnnotation(Route.class);
        routeAnnotation.forEach((key, val) -> {
            System.out.println(key + " " + val.getClass());
            Class<?> aClass = val.getClass();
            Arrays.stream(aClass.getDeclaredMethods()).forEach(System.out::println);
            Method[] methods = aClass.getDeclaredMethods();
            for (Method method : methods) {
                Arrays.stream(method.getAnnotations()).forEach(System.out::println);
                Type[] types = method.getParameterTypes();
                for (Type type : types) {
                    System.out.println(type.getTypeName());
                }
            }
        });

        System.out.println(event.getSource());
        System.out.println("ContextRefreshedEvent 触发。");
    }
}
