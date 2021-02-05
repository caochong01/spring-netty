package com.autumn.router;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * 压缩字典树，简略版, 只负责节点的CRUD
 */
public class RouteNode<T> {

    private static final Logger log = LoggerFactory.getLogger(RouteNode.class);

    private final Node<T> RootNode = new Node<>("", NodeType.ROOT);

    public Node<T> getRootNode() {
        return RootNode;
    }

    public void add(String path, T obj) {
        String[] split = path.split("/");
        Node<T> node = RootNode;
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (i == (split.length - 1)) {
                if (node.tail.get(s) != null) {
                    log.warn("路由节点（" + node.tail.get(s).nodePath + "）被重置为: " + path);
                }
                node.tail.put(s, new Node<>(s, NodeType.Method, obj)); // 插入带数据的节点 或 更新PASS节点
            } else if (node.tail.get(s) == null) {
                node.tail.put(s, new Node<>(s, NodeType.PASS));
            }
            node = node.tail.get(s);
        }
    }

    public T findNode(String path) {
        String[] split = path.split("/");
        Node<T> node = RootNode;
        for (String s : split) {
            node = node.tail.get(s);
            if (node == null) {
                break;
            }
            if (!node.nodePath.equals(s) || node.nodeType.equals(NodeType.ROOT)) {
                break;
            }
        }
        if (node != null && !node.nodeType.equals(NodeType.ROOT)) { // 找到所要的数据
            return node.obj;
        }
        return RootNode.obj;
    }

    public T findNode(String path, Function<String, T> func) {
        return func.apply(path);
    }


//    public static void main(String[] args) {
//
//        RouteNode<Routing> routeNode = new RouteNode<>();
//        System.out.println(routeNode.findNode("test/123/asd"));
//        System.out.println(routeNode.findNode("test/zxc"));
//        System.out.println(routeNode.RootNode.toString());
//    }


    public enum NodeType {
        ROOT, Method, PASS;
    }

    public static class Node<T> {

        String nodePath; // 节点路径
        NodeType nodeType; // 节点类型
        T obj; // 节点数据
        Map<String, Node<T>> tail = new HashMap<>(2);

        public Node(String nodePath, NodeType nodeType) {
            this(nodePath, nodeType, null);
        }

        public Node(String nodePath, NodeType nodeType, T obj) {
            this.nodePath = nodePath;
            this.nodeType = nodeType;
            this.obj = obj;
        }

        public Map<String, Node<T>> getTail() {
            return tail;
        }

        public void setTail(Map<String, Node<T>> tail) {
            this.tail = tail;
        }

        public T getObj() {
            return obj;
        }

        public void setObj(T obj) {
            this.obj = obj;
        }

        public String getNodePath() {
            return nodePath;
        }

        public void setNodePath(String nodePath) {
            this.nodePath = nodePath;
        }

        public NodeType getNodeType() {
            return nodeType;
        }

        public void setNodeType(NodeType nodeType) {
            this.nodeType = nodeType;
        }

        @Override
        public String toString() {
            return "{" +
                    "\"nodePath\": \"" + nodePath + "\"" +
                    ", \"tail\": " + tail +
                    '}';
        }
    }
}
