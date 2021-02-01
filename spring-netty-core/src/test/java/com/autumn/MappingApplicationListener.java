package com.autumn;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

/**
 * spring 的事件监听示例
 * 事件种类详见 @See {@link org.springframework.context.event.ApplicationContextEvent} 的实现子类
 */
@Component
public class MappingApplicationListener implements ApplicationListener<ContextRefreshedEvent> {


    /**
     * Handle an application event.
     *
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext context = event.getApplicationContext();
        SpringStaticEnv.setApplicationContext(context); // 存储一份上下文信息
        Map<String, Object> routeAnnotation = context.getBeansWithAnnotation(RouteMapping.class);

        routeAnnotation.forEach((key, val) -> {
            Class<?> aClass = val.getClass();
            Method[] methods = aClass.getDeclaredMethods();
            RouteNode node = new RouteNode(key, aClass.getAnnotation(RouteMapping.class), val, methods.length);

            for (Method method : methods) {
                RouteMapping routeMapping;
                if ((routeMapping = method.getAnnotation(RouteMapping.class)) != null) {
                    RouteNode childNode = new RouteNode(routeMapping.value(), routeMapping, method, val);
                    node.addChildNode(childNode);
                }
            }
            if (!node.isNullChildNode())
                Route.routeMap().put(key, node);

            // TODO delete 测试输出
            for (Method method : methods) {
                Arrays.stream(method.getAnnotations()).forEach(System.out::println);
                Type[] types = method.getParameterTypes();
                for (Type type : types) {
                    System.out.println(type.getTypeName());
                }
            }
        });

        System.out.println("ContextRefreshedEvent 触发。");
    }

}
