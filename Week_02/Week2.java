package com.freezing.leetcode.jike;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Week2 {
    /**
     * 242. 有效的字母异位词
     * https://leetcode-cn.com/problems/valid-anagram/
     *
     * @param s
     * @param t
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) return false;
        int[] arr = new int[26];
        for (int i = 0; i < s.length(); i++) {
            arr[s.charAt(i) - 'a']++;
        }
        for (int i = 0; i < t.length(); i++) {
            arr[t.charAt(i) - 'a']--;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != 0) return false;
        }
        return true;
    }

    /**
     * 1. 两数之和
     * https://leetcode-cn.com/problems/two-sum/description/
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int result = target - nums[i];
            if (map.containsKey(result)) {
                return new int[]{map.get(result), i};
            }
            map.put(nums[i], i);
        }
        return null;
    }

    /**
     * 49. 字母异位词分组
     * https://leetcode-cn.com/problems/group-anagrams/
     *
     * @param strs
     * @return
     */
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (int i = 0; i < strs.length; i++) {
            char[] chars = strs[i].toCharArray();
            Arrays.sort(chars);
            List<String> list;
            String str = new String(chars);
            if (map.containsKey(str)) {
                list = map.get(str);
            } else {
                list = new ArrayList<>();
            }
            list.add(strs[i]);
            map.put(str, list);
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 144. 二叉树的前序遍历
     * https://leetcode-cn.com/problems/binary-tree-preorder-traversal/
     *
     * @param root
     * @return
     */
    public List<Integer> preorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderTraversal(root, result);
        return result;
    }

    private void preorderTraversal(TreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);
        preorderTraversal(node.left, result);
        preorderTraversal(node.right, result);
    }

    /**
     * 94. 二叉树的中序遍历
     * https://leetcode-cn.com/problems/binary-tree-inorder-traversal/
     *
     * @param root
     * @return
     */
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderTraversal(root, result);
        return result;
    }

    private void inorderTraversal(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorderTraversal(node.left, result);
        result.add(node.val);
        inorderTraversal(node.right, result);
    }

    /**
     * 589. N叉树的前序遍历
     * https://leetcode-cn.com/problems/n-ary-tree-preorder-traversal/description/
     *
     * @param root
     * @return
     */
    public List<Integer> preorder(Node root) {
        List<Integer> result = new ArrayList<>();
        preorder(root, result);
        return result;
    }

    private void preorder(Node node, List<Integer> result) {
        if (node == null) return;
        result.add(node.val);
        if (node.children.isEmpty()) return;
        for (int i = 0; i < node.children.size(); i++) {
            preorder(node.children.get(i), result);
        }
    }

    /**
     * 429. N 叉树的层序遍历
     * https://leetcode-cn.com/problems/n-ary-tree-level-order-traversal/
     *
     * @param root
     * @return
     */
    public List<List<Integer>> levelOrder(Node root) {
        List<List<Integer>> result = new ArrayList<>();
        Deque<Node> deque = new LinkedList<>();
        if (root != null) deque.addLast(root);
        while (!deque.isEmpty()) {
            int size = deque.size();
            Node node = deque.pollFirst();
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                list.add(node.val);
                if (node.children == null || node.children.isEmpty()) continue;
                for (int j = 0; j < node.children.size(); j++) {
                    deque.addLast(node.children.get(j));
                }
            }
            result.add(list);
        }

        return result;
    }

    /**
     * 590. N叉树的后序遍历
     * https://leetcode-cn.com/problems/n-ary-tree-postorder-traversal/
     *
     * @param root
     * @return
     */
    public List<Integer> postorder(Node root) {
        List<Integer> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }

    private void postorder(Node node, List<Integer> result) {
        if (node == null) return;
        if (node.children != null) {
            for (int i = 0; i < node.children.size(); i++) {
                postorder(node.children.get(i), result);
            }
        }
        result.add(node.val);
    }

    /**
     * 剑指 Offer 49. 丑数
     * https://leetcode-cn.com/problems/chou-shu-lcof/
     *
     * @param n
     * @return
     */
    public int nthUglyNumber(int n) {
        int[] uglyNumbers = {2, 3, 5};
        Queue<Long> queue = new PriorityQueue<>();
        queue.add(1L);
        int count = 0;
        while (!queue.isEmpty()) {
            long num = queue.poll();
            if (++count >= n) {
                return (int) num;
            }
            for (int i = 0; i < uglyNumbers.length; i++) {
                long newUgly = num * uglyNumbers[i];
                if (!queue.contains(newUgly)) {
                    queue.add(newUgly);
                }
            }
        }
        return -1;
    }

    /**
     * 347. 前 K 个高频元素
     * https://leetcode-cn.com/problems/top-k-frequent-elements/
     *
     * @param nums
     * @param k
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int[] topKFrequent(int[] nums, int k) {
        final Map<Integer, Integer> map = new HashMap<>();
        int index = 0;
        int[] res = new int[k];
        Queue<Integer> queue = new PriorityQueue<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return map.get(o2) - map.get(o1);
            }
        });
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], map.getOrDefault(nums[i], 0) + 1);
        }
        for(int key:map.keySet()){
            queue.add(key);
        }
        while(index < k){
            res[index++] = queue.poll();
        }
        return res;
    }

}
