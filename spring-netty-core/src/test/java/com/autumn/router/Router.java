package com.autumn.router;

import com.autumn.RouterManager;
import com.autumn.mode.RequestMethod;

/**
 * java编码方式操作类（支持NettyHandle方式）
 */
public class Router {

    private static final RouterManager routerManager = RouterManager.manager();


    /**
     * 供添加路由及反向路由功能
     *
     * @param method 方法类型
     * @param path   请求路径
     * @param cla    类类型
     * @param func   方法类型
     */
    public Router pattern(RequestMethod method, String path, Class<?> cla, String func) {
        return this;
    }

    /**
     * 提供匹配路由功能，并返回一个封装的路由体
     */
    public Routing route(String path) {
        return null;
    }

    /**
     * 提供反向路由查找的操作方法
     */
    public String path() {
        return null;
    }
}
