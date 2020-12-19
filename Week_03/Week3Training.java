package com.freezing.leetcode.jike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Week3Training {
    private List<String> res;

    /**
     * 22. 括号生成
     * https://leetcode-cn.com/problems/generate-parentheses/
     *
     * @param n
     * @return
     */
    public List<String> generateParenthesis(int n) {
        res = new ArrayList<>();
        generateParenthesis(0, 0, n, "");
        return res;
    }

    private void generateParenthesis(int left, int right, int max, String s) {
        if (left == max && right == max) {
            res.add(s);
            return;
        }
        if (left < max)
            generateParenthesis(left + 1, right, max, s + "(");

        if (right < left)
            generateParenthesis(left, right + 1, max, s + ")");

    }

    /**
     * 226. 翻转二叉树
     * https://leetcode-cn.com/problems/invert-binary-tree/description/
     *
     * @param root
     * @return
     */
    public TreeNode invertTree(TreeNode root) {
        if (root == null) return null;
        TreeNode tmp = root.left;
        root.left = root.right;
        root.right = tmp;
        invertTree(root.left);
        invertTree(root.right);
        return root;
    }


    /**
     * 98. 验证二叉搜索树
     * https://leetcode-cn.com/problems/validate-binary-search-tree/
     *
     * @param root
     * @return
     */
    long preValue = Long.MIN_VALUE;

    public boolean isValidBST(TreeNode root) {
        if (root == null) return true;
        if (!isValidBST(root.left)) return false;
        if (root.val <= preValue) return false;
        preValue = root.val;
        return isValidBST(root.right);
    }


    /**
     * 104. 二叉树的最大深度
     * https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/
     *
     * @param root
     * @return
     */

    public int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    }

    /**
     * 111. 二叉树的最小深度
     * https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/
     *
     * @param root
     * @return
     */
    public int minDepth(TreeNode root) {
        if (root == null) return 0;
        if (root.left == null) return minDepth(root.right) + 1;
        if (root.right == null) return minDepth(root.left) + 1;
        return Math.min(minDepth(root.left), minDepth(root.right)) + 1;
    }

    /**
     * 50. Pow(x, n)
     * https://leetcode-cn.com/problems/powx-n/
     *
     * @param x
     * @param n
     * @return
     */
    public double myPow(double x, int n) {
        return n > 0 ? helper(x, n) : 1.0 / helper(x, -n);
    }

    private double helper(double x, int n) {
        if (n == 0) return 1;
        double half = helper(x, n / 2);
        return n % 2 == 0 ? half * half : half * half * x;
    }


    /**
     * 78. 子集
     * https://leetcode-cn.com/problems/subsets/submissions/
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> list = new ArrayList<>();
        dfs(0, nums, res, list);
        return res;
    }

    private void dfs(int index, int[] nums, List<List<Integer>> res, List<Integer> list) {
        for (int i = index; i < nums.length; i++) {
            res.add(new ArrayList(list));
            dfs(i + 1, nums, res, list);
            list.remove(list.size() - 1);
        }
    }

    /**
     * 17. 电话号码的字母组合
     * https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/
     *
     * @param digits
     * @return
     */
    String[] letters = new String[]{"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};

    public List<String> letterCombinations(String digits) {
        List<String> res = new ArrayList<>();
        if (digits.isEmpty()) return res;
        dfs(0, digits, res, new StringBuilder());
        return res;
    }

    private void dfs(int index, String digits, List<String> res, StringBuilder sb) {
        if (index == digits.length()) {
            res.add(sb.toString());
            return;
        }
        char c = digits.charAt(index);
        String letter = letters[c - '2'];
        for (int i = index; i < letter.length(); i++) {
            sb.append(letter.charAt(i));
            dfs(index + 1, digits, res, sb);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    public boolean wordPattern(String pattern, String s) {
        String[] words = s.split(" ");
        if (words.length != pattern.length()) return false;
        Map<Character, String> map = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            Character key = pattern.charAt(i);
            if (map.containsKey(key)) {
                if (!map.get(key).equals(words[i])) return false;
            } else if (map.containsValue(words[i])) {
                return false;
            }
            map.put(key, words[i]);
        }
        return true;
    }

    /**
     * 51. N 皇后
     * https://leetcode-cn.com/problems/n-queens/
     *
     * @param n
     * @return
     */
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        int[] queens = new int[n];
        Arrays.fill(queens, -1);
        Set<Integer> columnSet = new HashSet<>();
        Set<Integer> pieSet = new HashSet<>();
        Set<Integer> naSet = new HashSet<>();
        backtrack(res, queens, 0, n, columnSet, pieSet, naSet);
        return res;
    }

    private void backtrack(List<List<String>> res, int[] queens, int row, int n, Set<Integer> columnSet, Set<Integer> pieSet, Set<Integer> naSet) {
        if (row == n) {
            res.add(generateBroad(queens, n));
        }
        for (int i = 0; i < n; i++) {
            if (columnSet.contains(i)) continue;
            int pie = row + i;
            if (pieSet.contains(pie)) continue;
            int na = row - i;
            if (naSet.contains(na)) continue;

            queens[row] = i;
            columnSet.add(i);
            pieSet.add(pie);
            naSet.add(na);
            backtrack(res, queens, row + 1, n, columnSet, pieSet, naSet);
            queens[row] = -1;
            columnSet.remove(i);
            pieSet.remove(pie);
            naSet.remove(na);
        }
    }

    private List<String> generateBroad(int[] queens, int n) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            char[] chars = new char[n];
            Arrays.fill(chars, '.');
            chars[queens[i]] = 'Q';
            list.add(new String(chars));
        }
        return list;
    }


    /**
     * 297. 二叉树的序列化与反序列化
     * https://leetcode-cn.com/problems/serialize-and-deserialize-binary-tree/
     */
    public class Codec {

        public String serialize(TreeNode node, String s) {
            if (node == null) {
                s += "None,";
            } else {
                s += node.val + ",";
                s = serialize(node.left, s);
                s = serialize(node.right, s);
            }
            return s;

        }

        // Encodes a tree to a single string.
        public String serialize(TreeNode root) {
            return serialize(root, "");
        }

        public TreeNode deserialize(List<String> list){
            if(list.get(0).equals("None")){
                list.remove(0);
                return null;
            }
            TreeNode node = new TreeNode(Integer.valueOf(list.get(0)));
            list.remove(0);
            node.left = deserialize(list);
            node.right = deserialize(list);
            return node;
        }

        // Decodes your encoded data to tree.
        public TreeNode deserialize(String data) {
            String[] strArr = data.split(",");
            List<String> list = new LinkedList<>(Arrays.asList(strArr));
            return deserialize(list);
        }
    }
}
