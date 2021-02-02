package com.autumn;

import com.autumn.router.RouteNode;
import com.autumn.router.Routing;

import java.util.function.Function;

/**
 * 路由管理类，分发器类dispatch
 */
public class RouterManager {

    // 保存 类路径、方法路径、Method对象、参数对象列表、原始类实例化的bean、注解参数
    private static final RouteNode<Routing> routeMap = new RouteNode<>();

    private static Function<String, String> noRouteHandler;

    private RouterManager() {
    }

    public static RouteNode<Routing> routeMap() {
        return routeMap;
    }

    public static void setNoRouteHandler(Function<String, String> noRouteHandler) {
        RouterManager.noRouteHandler = noRouteHandler;
    }
}
