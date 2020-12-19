package com.freezing.leetcode.jike;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Week3 {
    /**
     * 236. 二叉树的最近公共祖先
     * https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/
     *
     * @param root
     * @param p
     * @param q
     * @return
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        if (left == null && right == null) return null;
        if (left == null) return right;
        if (right == null) return left;
        return root;
    }

    /**
     * 105. 从前序与中序遍历序列构造二叉树
     * https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
     *
     * @param preorder
     * @param inorder
     * @return
     */
    Map<Integer, Integer> inorderRootIndexMap;

    public TreeNode buildTree(int[] preorder, int[] inorder) {
        inorderRootIndexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderRootIndexMap.put(inorder[i], i);
        }
        return build(preorder, 0, preorder.length - 1, inorder, 0, inorder.length - 1);
    }

    private TreeNode build(int[] preorder, int pStart, int pEnd, int[] inorder, int iStart, int iEnd) {
        if (pStart > pEnd) return null;
        TreeNode root = new TreeNode(preorder[pStart]);
        int rootIndex = inorderRootIndexMap.get(root.val);
        int leftNum = rootIndex - iStart;
        root.left = build(preorder, pStart + 1, pStart + leftNum, inorder, iStart, rootIndex - 1);
        root.right = build(preorder, pStart + leftNum + 1, pEnd, inorder, rootIndex + 1, iEnd);
        return root;
    }

    /**
     * 77. 组合
     * https://leetcode-cn.com/problems/combinations/
     *
     * @param n
     * @param k
     * @return
     */
    public List<List<Integer>> combine(int n, int k) {
        if (n < k || k < 1) return new ArrayList<>();
        List<List<Integer>> res = new ArrayList<>();
        combine(1, k, n, res, new ArrayList<Integer>());
        return res;
    }

    private void combine(int index, int k, int n, List<List<Integer>> res, List<Integer> list) {
        if (0 == k) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = index; i <= n - k + 1; i++) {
            list.add(i);
            System.out.println(list.toString());
            combine(i + 1, k - 1, n, res, list);
            list.remove(list.size() - 1);
        }
//        if(list.size() == k){
//            res.add(new ArrayList<>(list));
//            return;
//        }
//        for(int i = index; i <= n; i++){
//            list.add(i);
//            System.out.println(list.toString());
//            combine(i+1,k,n,res,list);
//            list.remove(list.size() -1);
//        }
    }

    /**
     * 46. 全排列
     * https://leetcode-cn.com/problems/permutations/
     *
     * @param nums
     * @return
     */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        dfs(0, nums, new boolean[nums.length], res, new ArrayList<Integer>());
        return res;
    }

    private void dfs(int index, int[] nums, boolean[] used, List<List<Integer>> res, List<Integer> list) {
        if (index == nums.length) {
            res.add(new ArrayList<>(list));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            list.add(nums[i]);
            used[i] = true;
            dfs(index + 1, nums,used, res, list);
            used[i] = false;
            list.remove(list.size() - 1);
        }
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new ArrayList<>();
        // 切记要先排序
        Arrays.sort(nums);
        Deque<Integer> deque = new ArrayDeque<>();
        dfsUnique(0, nums, new boolean[nums.length], res, deque);
        return res;
    }

    private void dfsUnique(int index, int[] nums, boolean[] used, List<List<Integer>> res, Deque<Integer> deque) {
        if (index == nums.length) {
            res.add(new ArrayList<>(deque));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i]) continue;
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) continue;
            deque.addLast(nums[i]);
            used[i] = true;
            dfsUnique(index + 1, nums, used, res, deque);
            used[i] = false;
            deque.pollLast();
        }
    }

    public static void main(String[] args) {
        Week3 week3 = new Week3();
        week3.combine(4, 2);
    }
}
