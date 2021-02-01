package com.autumn.router;

import java.util.function.Function;

public class DefaultRouting extends Routing<Object> {


    public DefaultRouting(String path, Object target, Function<String, String> pathFun) {
        super(path, target, pathFun);
    }

    public DefaultRouting(String path, Object target) {
        super(path, target);
    }

    public String removeSlashAtBothEnds(String path) {
        if (path.isEmpty()) return path;

        int beginIndex = 0;
        while (beginIndex < path.length() && path.charAt(beginIndex) == '/') beginIndex++;
        if (beginIndex == path.length()) return "";

        int endIndex = path.length() - 1;
        while (endIndex > beginIndex && path.charAt(endIndex) == '/') endIndex--;

        return path.substring(beginIndex, endIndex + 1);
    }
}
