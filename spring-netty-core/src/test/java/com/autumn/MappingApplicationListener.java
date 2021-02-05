package com.autumn;

import com.autumn.mode.RequestMethod;
import com.autumn.router.NettyController;
import com.autumn.router.RouteNode;
import com.autumn.router.Routed;
import com.autumn.router.Routing;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

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

        /**
         * 1. 处理路由注解，如果类上有注解，则与方法注解合并
         * 2. 添加到 路由树中（压缩字典树）管理
         */
        Map<String, Object> nettyControllerAnnotation = context.getBeansWithAnnotation(NettyController.class);

        nettyControllerAnnotation.forEach((nck, ncv) -> {
            Class<?> ncvClass = ncv.getClass();

            // 检测类上是否有RouteMapping，如果有，进行解析; 没有则设置一些默认值
            RouteMapping classRouteMapping;
            String classRouteName = "";
            RequestMethod[] classSuppotMethods = null;
            if ((classRouteMapping = ncvClass.getAnnotation(RouteMapping.class)) != null) {
                classRouteName = classRouteMapping.value();
                classSuppotMethods = classRouteMapping.method();
                System.out.println("CLass: " + nck + " " +
                        classRouteName + " " + Arrays.toString(classSuppotMethods));
            }


            // 检测哪些方法具有RouteMapping：
            // yes，将类上与方法上合并 -> 将映射存储至路由树；
            // no，跳过；
            Method[] methods = ncvClass.getDeclaredMethods();
            for (Method method : methods) {
                RouteMapping methodRouteMapping;
                if ((methodRouteMapping = method.getAnnotation(RouteMapping.class)) != null) {
                    String methodRouteName = methodRouteMapping.value();
                    RequestMethod[] methodSuppotMethods = methodRouteMapping.method();

                    String routeName = classRouteName + methodRouteName;
                    RequestMethod[] requestMethods = concat(classSuppotMethods, methodSuppotMethods);

//                    System.out.println("MEthod: " + nck + " " +
//                            routeName + " " + Arrays.toString(requestMethods));

                    Routing routing = new Routing(routeName, method, requestMethods, ncvClass);
                    RouterManager.manager().pattern(routeName, routing);
                }
            }
        });
        RouterManager routerManager = RouterManager.manager();
        Routed<Routing> route = routerManager.route("/testControl/123//789/r1");
        System.out.println(route);


//        Map<String, Object> routeAnnotation = context.getBeansWithAnnotation(RouteMapping.class);
//
//        routeAnnotation.forEach((key, val) -> {
//            Class<?> aClass = val.getClass();
//            Method[] methods = aClass.getDeclaredMethods();
//            RouteNode node = new RouteNode(key, aClass.getAnnotation(RouteMapping.class), val, methods.length);
//
//            for (Method method : methods) {
//                RouteMapping routeMapping;
//                if ((routeMapping = method.getAnnotation(RouteMapping.class)) != null) {
//                    RouteNode childNode = new RouteNode(routeMapping.value(), routeMapping, method, val);
//                    node.addChildNode(childNode);
//                }
//            }
//            if (!node.isNullChildNode())
//                RouterManager.routeMap().put(key, node);
//
//            // TODO delete 测试输出
//            for (Method method : methods) {
//                Arrays.stream(method.getAnnotations()).forEach(System.out::println);
//                Type[] types = method.getParameterTypes();
//                for (Type type : types) {
//                    System.out.println(type.getTypeName());
//                }
//            }
//        });

        System.out.println("ContextRefreshedEvent 触发。");
    }

    private static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}
