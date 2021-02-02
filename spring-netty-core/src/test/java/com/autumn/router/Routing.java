package com.autumn.router;

import com.autumn.mode.RequestMethod;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Function;

/**
 * 路由器的基础对象，包含设置等信息
 */
public class Routing {

    protected String path; // 记录的路由地址

    protected String[] tokens; // 参数值

    protected RequestMethod[] requestMethod; // 请求方式

    protected Method method; // 反射的方法信息

    protected Object classBean; // 原始spring bean对象

//    public Routing() {
//    }

    public Routing(String path) {
        this(path, null, null, null, null);
    }

//    public Routing(String path, Function<String, String> pathFun) {
//        this(path, null, null, null, pathFun);
//    }

    public Routing(String path, Method method, RequestMethod[] requestMethod, Object classBean) {
        this(path, method, requestMethod, classBean, null);
    }

    public Routing(String path, Method method, RequestMethod[] requestMethod,
                   Object classBean, Function<String, String> pathFun) {
        if (pathFun != null) {
            this.path = pathFun.apply(path);
        } else {
            this.path = removeSlashAtBothEnds(path);
        }
        this.tokens = this.path.split("/"); // TODO 优化
        this.method = method;
        this.requestMethod = requestMethod;
        this.classBean = classBean;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public RequestMethod[] getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(RequestMethod[] requestMethod) {
        this.requestMethod = requestMethod;
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

    public static String removeSlashAtBothEnds(String path) {
        if (path.isEmpty()) return path;

        int beginIndex = 0;
        while (beginIndex < path.length() && path.charAt(beginIndex) == '/') beginIndex++;
        if (beginIndex == path.length()) return "";

        int endIndex = path.length() - 1;
        while (endIndex > beginIndex && path.charAt(endIndex) == '/') endIndex--;

        return path.substring(beginIndex, endIndex + 1);
    }

    @Override
    public String toString() {
        return "Routing{" +
                "path='" + path + '\'' +
                ", tokens=" + Arrays.toString(tokens) +
                ", requestMethod=" + Arrays.toString(requestMethod) +
                ", method=" + method +
                ", classBean=" + classBean +
                '}';
    }
}
