package com.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRouter {

    public static void main(String[] args) {
//        Router router = new Router();
//        router.pattern(RequestMethod.GET, "/test/tt/:id", TestBean.UserService.class, "");
//        router.route("/test/123");


        // TODO 字符串存储位置的研究

        char[] f = {'a', 's', 'd'};

        String a = "asd"; // 常量池
        String b2 = new String("asd").intern();
        String b = new String("asd"); // 字符串形式new
        String c = new String(f); // 数组形式new
        String d = new String(f).intern(); // 数组形式new，使用intern方法

        System.out.println(a + b + c + d); // 断点打在这里


        System.out.println(a == b); // false
        System.out.println(a == b2); // true
        System.out.println(a == d); // true
        System.out.println(b == d); // false
        System.out.println(c == d); // false


//        System.out.println(Arrays.toString(twoSum(new int[]{2, 7, 15, 20}, 9)));
//
//
//        TreeNode treeNode = new TreeNode(1);
//        TreeNode t1 = new TreeNode(2);
//        t1.left = new TreeNode(3);
//        t1.right = new TreeNode(4);
//        treeNode.left = t1;
//
//        TreeNode t2 = new TreeNode(5);
//        t2.right = new TreeNode(6);
//        treeNode.right = t2;
//
//        flatten(treeNode);
    }

    public static int[] twoSum(int[] nums, int target) {
        for (int i = 0; i < nums.length - 1; i++) {
            int fist = nums[i];
            for (int j = i + 1; j < nums.length; j++) {
                int two = nums[j];
                if (fist + two == target) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    public static final List<TreeNode> list = new ArrayList<>();

    public static void flatten(TreeNode root) {
        read(root);
        if (list.size() <= 0)
            return;

        TreeNode rs = root;
        for (TreeNode temp : list) {
            rs.right = temp;
            rs.left = null;
            rs = temp;
        }
        System.out.println(root); // 这样才对
    }

    public static void read(TreeNode root) {
        TreeNode left = root.left;
        if (left != null) {
            list.add(left);
            read(left);
        }
        TreeNode right = root.right;
        if (right != null) {
            list.add(right);
            read(right);
        }
    }


    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }
}
