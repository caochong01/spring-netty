package com.autumn.mode;

public enum RequestMethod {

    GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;

    /**
     * 匹配对应的RequestMethod，如果发生异常或匹配字符错误，则返回 Null。
     *
     * @param name 匹配的字符
     * @return RequestMethod
     */
    public static RequestMethod parseOf(String name) {
        return parseOf(name, null);
    }

    /**
     * 匹配对应的RequestMethod，如果发生异常或匹配字符错误，则返回默认RequestMethod。
     *
     * @param name          匹配的字符
     * @param defaultMethod 默认RequestMethod策略
     * @return RequestMethod
     */
    public static RequestMethod parseOf(String name, RequestMethod defaultMethod) {
        if (name == null)
            return defaultMethod;
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return defaultMethod;
        }
    }
}
