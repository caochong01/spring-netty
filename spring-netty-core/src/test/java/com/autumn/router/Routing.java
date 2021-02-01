package com.autumn.router;

import java.util.function.Function;

/**
 * 路由器的基础对象，包含设置等信息
 */
public abstract class Routing<T> {
    protected String path;
    protected String[] tokens;
    protected T target;

    public Routing(String path, T target) {
        this.path = removeSlashAtBothEnds(path);
        this.tokens = this.path.split("/");
        this.target = target;
    }

    public Routing(String path, T target, Function<String, String> pathFun) {
        if (pathFun != null) {
            this.path = pathFun.apply(path);
        } else {
            this.path = removeSlashAtBothEnds(path);
        }
        this.tokens = this.path.split("/");
        this.target = target;
    }

    public abstract String removeSlashAtBothEnds(String path);

    public String path() {
        return path;
    }

    public String[] tokens() {
        return tokens;
    }

    public T target() {
        return target;
    }
}
