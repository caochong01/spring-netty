package com.autumn.router;

import java.util.HashMap;
import java.util.Map;

/**
 * 压缩字典树，简略版
 */
public class RouteNode {

    private static final Node RootNode = new Node("");

    public void add(String path) {
        String[] split = path.split("/");
        Node node = RootNode;
        for (String s : split) {
            if (node.tail.get(s) == null) {
                node.tail.put(s, new Node(s));
            }
            node = node.tail.get(s);
        }


    }

    public static void main(String[] args) {
        RouteNode routeNode = new RouteNode();
        routeNode.add("test/123/asd");
        routeNode.add("test/zxc");
        System.out.println(RootNode.toString());
    }


    static class Node {
        String path;
        Map<String, Node> tail = new HashMap<>(2);

        public Node() {
        }

        public Node(String path) {
            this.path = path;
        }

        public Map<String, Node> getTail() {
            return tail;
        }

        public void setTail(Map<String, Node> tail) {
            this.tail = tail;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        @Override
        public String toString() {
            return "{" +
                    "\"path\": \"" + path + "\"" +
                    ", \"tail\": " + tail +
                    '}';
        }
    }
}
