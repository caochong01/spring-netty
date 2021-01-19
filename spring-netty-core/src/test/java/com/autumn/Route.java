package com.autumn;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由管理类，分发器类dispatch
 */
public class Route {

    // 保存 类路径、方法路径、Method对象、参数对象列表、原始类实例化的bean、注解参数
    private static final ConcurrentHashMap<String, RouteNode> routeMap = new ConcurrentHashMap<>();

    private Route() {
    }

    public static Map<String, RouteNode> routeMap() {
        return routeMap;
    }
}
