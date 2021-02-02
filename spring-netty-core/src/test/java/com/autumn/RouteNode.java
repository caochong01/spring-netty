package com.autumn;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由对象
 */
@Deprecated
public class RouteNode {


    private boolean isClass = false; // 是类注解

    private String path; // 记录的路由地址

    private RouteMapping routeMapping; // 注解及注解属性

    private Method method; // 反射的方法信息

    private Object classBean; // 原始spring bean对象

    private Map<String, RouteNode> childNode; // 子路由对象

    public RouteNode() {
    }

    public RouteNode(String path, RouteMapping routeMapping, Object classBean) {
        this.path = path;
        this.routeMapping = routeMapping;
        this.classBean = classBean;
    }

    public RouteNode(String path, RouteMapping routeMapping, Object classBean, boolean isClass) {
        this.path = path;
        this.routeMapping = routeMapping;
        this.classBean = classBean;
        this.isClass = isClass;
        if (isClass) {
            this.childNode = new ConcurrentHashMap<>(5);
        }
    }

    public RouteNode(String path, RouteMapping routeMapping, Object classBean, int isClassAndMethodCount) {
        this.path = path;
        this.routeMapping = routeMapping;
        this.classBean = classBean;
        if (isClassAndMethodCount > 0) {
            this.isClass = true;
            // 优化空间
            this.childNode = new ConcurrentHashMap<>((int) ((double) isClassAndMethodCount / 0.75 + 1));
        }
    }

    public RouteNode(String path, RouteMapping routeMapping, Method method, Object classBean) {
        this.path = path;
        this.routeMapping = routeMapping;
        this.method = method;
        this.classBean = classBean;
    }

    public boolean isClass() {
        return isClass;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RouteMapping getRoute() {
        return routeMapping;
    }

    public void setRoute(RouteMapping routeMapping) {
        this.routeMapping = routeMapping;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getClassBean() {
        return classBean;
    }

    public void setClassBean(Object classBean) {
        this.classBean = classBean;
    }

    public Map<String, RouteNode> getChildNode() {
        return childNode;
    }

    public void addChildNode(RouteNode childNode) {
        this.childNode.put(childNode.path, childNode);
    }

    public boolean isNullChildNode() {
        return this.childNode.isEmpty();
    }


    @Override
    public String toString() {
        return "RouteNode{" +
                "isClass=" + isClass +
                ", path='" + path + '\'' +
                ", routeMapping=" + routeMapping +
                ", method=" + method +
                ", classBean=" + classBean +
                ", childNode=" + childNode +
                '}';
    }
}
