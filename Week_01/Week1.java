package com.freezing.leetcode.jike;

import java.util.HashMap;
import java.util.Map;

public class Week1 {
    /**
     * 283.移动零
     * https://leetcode-cn.com/problems/move-zeroes/
     *
     * @param nums
     */
    public void moveZeroes(int[] nums) {
        if (nums.length <= 1) return;
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                nums[j] = nums[i];
                if (i != j) nums[i] = 0;
                j++;
            }
        }
    }

    /**
     * 1.两数之和
     * https://leetcode-cn.com/problems/two-sum/
     *
     * @param nums
     * @param target
     * @return
     */
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int value = target - nums[i];
            if (map.containsKey(value)) {
                return new int[]{map.get(value), i};
            }
            map.put(nums[i], i);
        }
        return null;
    }

    /**
     * 26.删除排序数组中的重复项
     * https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array/
     *
     * @param nums
     * @return
     */
    public int removeDuplicates(int[] nums) {
        if (nums.length <= 1) return nums.length;
        int i = 0;
        for (int j = 1; j < nums.length; j++) {
            if (nums[i] != nums[j]) {
                nums[++i] = nums[j];
            }
        }
        return i + 1;
    }

    /**
     * 66. 加一
     * https://leetcode-cn.com/problems/plus-one/
     *
     * @param digits
     * @return
     */
    public int[] plusOne(int[] digits) {
        for (int i = digits.length - 1; i >= 0; i--) {
            digits[i] = ++digits[i] % 10;
            if (digits[i] != 0) return digits;
        }
        digits = new int[digits.length + 1];
        digits[0] = 1;
        return digits;
    }

    /**
     * 189. 旋转数组
     * https://leetcode-cn.com/problems/rotate-array/
     *
     * @param nums
     * @param k
     */
    public void rotate(int[] nums, int k) {
        if (nums.length < 2) return;
        k %= nums.length;
        reverse(nums, 0, nums.length - 1);
        reverse(nums, 0, k - 1);
        reverse(nums, k, nums.length - 1);
    }

    public void reverse(int[] nums, int start, int end) {
        while (start < end) {
            int temp = nums[start];
            nums[start++] = nums[end];
            nums[end--] = temp;
        }
    }

    /**
     * 88. 合并两个有序数组
     * https://leetcode-cn.com/problems/merge-sorted-array/
     *
     * @param nums1
     * @param m
     * @param nums2
     * @param n
     */
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int len1 = m - 1;
        int len2 = n - 1;
        int len = m + n - 1;
        while (len1 >= 0 && len2 >= 0) {
            nums1[len--] = nums1[len1] > nums2[len2] ? nums1[len1--] : nums2[len2--];
        }
        System.arraycopy(nums2, 0, nums1, 0, len2 + 1);
    }

    /**
     * 21. 合并两个有序链表
     * https://leetcode-cn.com/problems/merge-two-sorted-lists/
     * @param l1
     * @param l2
     * @return
     */
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode head = new ListNode(-1);
        ListNode prev = head;
        while(l1 != null && l2 != null){
            if(l1.val < l2.val){
                prev.next = l1;
                l1 = l1.next;
            } else {
                prev.next = l2;
                l2 = l2.next;
            }
            prev = prev.next;
        }
        prev.next = l1 == null? l2:l1;
        return head.next;
    }
}
