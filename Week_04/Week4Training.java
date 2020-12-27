package com.freezing.leetcode.jike;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class Week4Training {
    /**
     * 102. 二叉树的层序遍历
     * https://leetcode-cn.com/problems/binary-tree-level-order-traversal/
     *
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> res = new ArrayList<>();
        Deque<TreeNode> deque = new ArrayDeque<>();
        if (root != null) deque.addLast(root);
        while (!deque.isEmpty()) {
            int size = deque.size();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                TreeNode node = deque.pollFirst();
                list.add(node.val);
                if (node.left != null) deque.addLast(node.left);
                if (node.right != null) deque.addLast(node.right);
            }
            res.add(list);
        }
        return res;
    }

    /**
     * 433. 最小基因变化
     * https://leetcode-cn.com/problems/minimum-genetic-mutation/#/description
     *
     * @param start
     * @param end
     * @param bank
     * @return
     */
    private char[] genes = {'A', 'T', 'C', 'G'};

    public int minMutation(String start, String end, String[] bank) {
        Set<String> bankSet = new HashSet<>(Arrays.asList(bank));
        if (!bankSet.contains(end)) return -1;
        if (start.equals(end)) return 0;
        int count = 0;
        Queue<String> queue = new LinkedList<>();
        queue.offer(start);
        bankSet.remove(start);
        while (!queue.isEmpty()) {
            count++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                String s = queue.poll();
                for (String neighbor : getNeighbors(s, bankSet)) {
                    if (end.equals(neighbor)) return count;
                    queue.offer(neighbor);
                }
            }
        }
        return -1;
    }

    private List<String> getNeighbors(String s, Set<String> bankSet) {
        List<String> list = new ArrayList<>();
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            for (int j = 0; j < genes.length; j++) {
                if (c == genes[j]) continue;
                chars[i] = genes[j];
                String newStr = new String(chars);
                if (bankSet.contains(newStr)) list.add(newStr);
            }
            chars[i] = c;
        }
        return list;
    }

    /**
     * 515. 在每个树行中找最大值
     * https://leetcode-cn.com/problems/find-largest-value-in-each-tree-row/#/description
     *
     * @param root
     * @return
     */
    public List<Integer> largestValues(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        if (root != null) queue.offer(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            int max = Integer.MIN_VALUE;
            for (int i = 0; i < size; i++) {
                TreeNode node = queue.poll();
                max = Math.max(node.val, max);
                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }
            res.add(max);
        }
        return res;
    }

    /**
     * 69. x 的平方根
     * https://leetcode-cn.com/problems/sqrtx/
     *
     * @param x
     * @return
     */
    public int mySqrt(int x) {
        long left = 0, right = x / 2 + 1;
        while (left < right) {
            long mid = (left + right + 1) >>> 1;
            long temp = mid * mid;
            if (temp <= x) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        return (int) left;
    }

    /**
     * 367. 有效的完全平方数
     * https://leetcode-cn.com/problems/valid-perfect-square/
     *
     * @param num
     * @return
     */
    public boolean isPerfectSquare(int num) {
        int res = mySqrt(num);
        return res * res == num;
    }

    public static void main(String[] args) {
        Week4Training training = new Week4Training();
        System.out.println(training.isPerfectSquare(16));
    }
}
