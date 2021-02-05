package com.autumn.router;

import com.autumn.mode.RequestMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * 路由管理类，分发器类dispatch
 */
public class RouterManager {
    private static final Logger log = LoggerFactory.getLogger(RouterManager.class);

    private static final RouterManager routerManager = new RouterManager();

    // 保存 类路径、方法路径、Method对象、参数对象列表、原始类实例化的bean、注解参数
    private static final RouteNode<Routing> routeMap = new RouteNode<>();

    private static final List<Routing> dynamicRoutings = new ArrayList<>();

    private static Function<String, String> noRouteHandler; // TODO 临时放这里, 全局的未找到，日后可以设置针对于方法的

    private RouterManager() {
    }

    public static RouterManager manager() {
        return routerManager;
    }

    public static void setNoRouteHandler(Function<String, String> noRouteHandler) {
        RouterManager.noRouteHandler = noRouteHandler;
    }


    /**
     * 正向路由添加管理，同时添加反向查找及正向路由缓存
     */
    public RouterManager pattern(String path, Routing obj) {
        if (path != null) {
            boolean isDynamic = false;
            if (path.contains(":")) {
                dynamicRoutings.add(obj);
                isDynamic = true;
            }
            routeMap.add(path, obj);
            log.debug("MEthod: " + isDynamic + " " +
                    path + " " + obj.toString());
        }
        return this;
    }

    /**
     * 正向路由查找管理
     */
    public Routed<Routing> route(RequestMethod requestMethod, String path) {
        log.debug("正在解析路由：" + path);

        Map<String, String> params = new HashMap<>();
        Routing routing = routeMap.findNode(path, uri -> {
            final String[] split = Routing.removeSlashRetainOnlyOne(uri).split("/");

            RouteNode.Node<Routing> node = routeMap.getRootNode();
            for (String s : split) {
                RouteNode.Node<Routing> nodeTmp = node.getTail().get(s);
                // 没有路径匹配
                if (nodeTmp == null) {
                    return dynamicRoute(split, params);

//                    // TODO 读取缓存并检查该节点下所有的key，匹配动态url，如果有匹配，则缓存，方便下次快速找到
//                    Map<String, RouteNode.Node<Routing>> nodeMap = node.getTail();
//
//                    Map<String, RouteNode.Node<Routing>> cMap = nodeCache.computeIfAbsent(node.hashCode(), key -> {
//                        // TODO 进行匹配计算, 如：/test/:id 、test/:id/:name、 test/:name/:id
//
//                        // 如果没有找到节点，则对 nodeMap 做一些匹配处理；
//                        //      匹配成功：则缓存节点信息（key=节点路径, val=节点对象）；
//                        //      匹配不成功：则直接返回null。
//                        // 如果找到了节点，则直接返回；
//                        Map<String, RouteNode.Node<Routing>> newVal = null;
//                        // TODO 逻辑
//                        return newVal;
//                    });
//
//                    if (cMap != null && cMap.get("上一步匹配到的路径") != null) { // 具有动态匹配
//                        // TODO 匹配到了就改变赋值
//                        node = cMap.get("上一步匹配到的路径");
//                        continue;
//                    } else {
//                        break;
//                    }
                }

                node = nodeTmp;
                if (!node.getNodePath().equals(s) ||
                        node.getNodeType().equals(RouteNode.NodeType.ROOT)) { // 路径错误或节点是ROOT类型
                    break;
                }
            }
            if (node != null && !node.getNodeType().equals(RouteNode.NodeType.ROOT)) { // 找到所要的数据
                return node.getObj();
            }

            return null;
        });

        if (routing != null) {
            // 拦截不设置的情况下是否有配置可通过
            if (false && routing.getRequestMethod().length == 0) {
                // TODO 拦截不设置的情况下是否有配置可通过,目前都通过
                System.out.println("未检索到可用 RequestMethod");
            }

            // 拦截Method不匹配
            if (!Arrays.asList(routing.getRequestMethod()).contains(requestMethod)){
                // TODO 拦截Method不匹配
                return new Routed<>(null, true, null);
            }
        }


        // TODO 如果没有找到，则执行没有找到路径的程序（类似于404返回）
        return new Routed<>(routing, routing == null, params);
    }

    private Routed<Routing> dynamicRoute(String[] tokens) {
        final Map<String, String> params = new HashMap<>();
        Routing routing = dynamicRoute(tokens, params);
        // TODO 如果没有找到，则执行没有找到路径的程序（类似于404返回）
        return new Routed<>(routing, routing == null, params);
    }

    private Routing dynamicRoute(String[] tokens, Map<String, String> params) {

        for (Routing routing : dynamicRoutings) {
            final String[] currTokens = routing.getTokens();

            params.clear();
            boolean matched;

            if (tokens.length == currTokens.length) {
                matched = matchParams(currTokens, tokens, params);
            } else if (currTokens.length > 0 &&
                    currTokens[currTokens.length - 1].equals(":*") &&
                    tokens.length >= currTokens.length) {

                matched = matchParams(currTokens, tokens, params);

                if (matched) {
                    final StringBuilder b = new StringBuilder(tokens[currTokens.length - 1]);
                    for (int i = currTokens.length; i < tokens.length; i++) {
                        b.append('/');
                        b.append(tokens[i]);
                    }
                    params.put("*", b.toString());
                }
            } else {
                matched = false;
            }

            if (matched)
                return routing;
        }

        return null;
    }

    private boolean matchParams(String[] currTokens, String[] tokens, Map<String, String> params) {
        for (int i = 0; i < currTokens.length; i++) {
            final String token = tokens[i];
            final String currToken = currTokens[i];

            if (currToken.length() > 0 && currToken.charAt(0) == ':') {
                params.put(currToken.substring(1), token);
            } else if (!currToken.equals(token)) {
                return false;
            }
        }
        return true;
    }


    /**
     * 反向路由查找管理
     */
//    public String path(T target, Object... params) {
//        if (params.length == 0) return path(target, Collections.emptyMap());
//
//        if (params.length == 1 && params[0] instanceof Map<?, ?>)
//            return pathMap(target, (Map<Object, Object>) params[0]);
//
//        if (params.length % 2 == 1) throw new RuntimeException("Missing value for param: " + params[params.length - 1]);
//
//        final Map<Object, Object> map = new HashMap<Object, Object>();
//        for (int i = 0; i < params.length; i += 2) {
//            final String key = params[i].toString();
//            final String value = params[i + 1].toString();
//            map.put(key, value);
//        }
//        return pathMap(target, map);
//    }

//    private String pathMap(T target, Map<Object, Object> params) {
//        final List<Pattern<T>> patterns = (target instanceof Class<?>) ?
//                getPatternsByTargetClass((Class<?>) target) : reverse.get(target);
//        if (patterns == null) return null;
//
//        try {
//            // The best one is the one with minimum number of params in the query
//            String bestCandidate = null;
//            int minQueryParams = Integer.MAX_VALUE;
//
//            boolean matched = true;
//            final Set<String> usedKeys = new HashSet<String>();
//
//            for (final Pattern<T> pattern : patterns) {
//                matched = true;
//                usedKeys.clear();
//
//                final StringBuilder b = new StringBuilder();
//
//                for (final String token : pattern.tokens()) {
//                    b.append('/');
//
//                    if (token.length() > 0 && token.charAt(0) == ':') {
//                        final String key = token.substring(1);
//                        final Object value = params.get(key);
//                        if (value == null) {
//                            matched = false;
//                            break;
//                        }
//
//                        usedKeys.add(key);
//                        b.append(value.toString());
//                    } else {
//                        b.append(token);
//                    }
//                }
//
//                if (matched) {
//                    final int numQueryParams = params.size() - usedKeys.size();
//                    if (numQueryParams < minQueryParams) {
//                        if (numQueryParams > 0) {
//                            boolean firstQueryParam = true;
//
//                            for (final Map.Entry<Object, Object> entry : params.entrySet()) {
//                                final String key = entry.getKey().toString();
//                                if (!usedKeys.contains(key)) {
//                                    if (firstQueryParam) {
//                                        b.append('?');
//                                        firstQueryParam = false;
//                                    } else {
//                                        b.append('&');
//                                    }
//
//                                    final String value = entry.getValue().toString();
//                                    b.append(URLEncoder.encode(key, "UTF-8"));    // May throw UnsupportedEncodingException
//                                    b.append('=');
//                                    b.append(URLEncoder.encode(value, "UTF-8"));  // May throw UnsupportedEncodingException
//                                }
//                            }
//                        }
//
//                        bestCandidate = b.toString();
//                        minQueryParams = numQueryParams;
//                    }
//                }
//            }
//
//            return bestCandidate;
//        } catch (UnsupportedEncodingException e) {
//            return null;
//        }
//    }

}
