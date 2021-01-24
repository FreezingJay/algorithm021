package com.freezing.leetcode.jike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Week8 {
    /**
     * 191. 位1的个数
     * https://leetcode-cn.com/problems/number-of-1-bits/
     *
     * @param n
     * @return
     */
    public int hammingWeight(int n) {
        int count = 0;
        while (n != 0) {
            count++;
            n &= (n - 1);
        }
        return count;
    }

    /**
     * 231. 2的幂
     * https://leetcode-cn.com/problems/power-of-two/
     *
     * @param n
     * @return
     */
    public boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }

    /**
     * 190. 颠倒二进制位
     * https://leetcode-cn.com/problems/reverse-bits/
     *
     * @param n
     * @return
     */
    public int reverseBits(int n) {
        int res = 0;
        for (int i = 0; i < 32; i++) {
            res <<= 1;
            res |= (n >> i) & 1;
        }
        return res;
    }

    /**
     * 338. 比特位计数
     * https://leetcode-cn.com/problems/counting-bits/description/
     *
     * @param num
     * @return
     */
    public int[] countBits(int num) {
        int[] res = new int[num + 1];
        for (int i = 0; i <= num; i++) {
            int count = 0;
            int current = i;
            while (current > 0) {
                count++;
                current &= (current - 1);
            }
            res[i] = count;
        }
        return res;
    }

    /**
     * 51. N 皇后
     * https://leetcode-cn.com/problems/n-queens/description/
     *
     * @param n
     * @return
     */
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> res = new ArrayList<>();
        dfs(new int[n], 0, n, 0, 0, 0, res);
        return res;
    }

    public void dfs(int[] queens, int row, int n, int column, int pie, int na, List<List<String>> res) {
        if (row == n) {
            res.add(generateBoard(queens));
            return;
        }
        int availableLocations = ((1 << n) - 1) & (~(column | pie | na));
        while (availableLocations != 0) {
            int position = availableLocations & -availableLocations;
            availableLocations &= (availableLocations - 1);
            int colNum = Integer.bitCount(position - 1);
            queens[row] = colNum;
            dfs(queens, row + 1, n, column | position, (pie | position) << 1, (na | position) >> 1, res);
        }
    }

    public List<String> generateBoard(int[] queens) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < queens.length; i++) {
            char[] chars = new char[queens.length];
            Arrays.fill(chars, '.');
            chars[queens[i]] = 'Q';
            list.add(String.valueOf(chars));
        }
        return list;
    }

    /**
     * 52. N皇后 II
     * https://leetcode-cn.com/problems/n-queens-ii/description/
     */
    int count;

    public int totalNQueens(int n) {
        count = 0;
        dfs(new int[n], 0, n, 0, 0, 0);
        return count;
    }

    public void dfs(int[] queens, int row, int n, int column, int pie, int na) {
        if (row == n) {
            count++;
            return;
        }
        int availablePositions = ((1 << n) - 1) & ~(column | pie | na);
        while (availablePositions != 0) {
            int pos = availablePositions & -availablePositions;
            availablePositions &= (availablePositions - 1);
            int colNum = Integer.bitCount(pos - 1);
            queens[row] = colNum;
            dfs(queens, row + 1, n, column | pos, (pie | pos) << 1, (na | pos) >> 1);
        }
    }

    /**
     * 1122. 数组的相对排序
     * https://leetcode-cn.com/problems/relative-sort-array/
     *
     * @param arr1
     * @param arr2
     * @return
     */
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
        int[] res = new int[1001];
        int pos = 0;
        for (int num : arr1) {
            res[num]++;
        }
        for (int num : arr2) {
            while (res[num] > 0) {
                arr1[pos++] = num;
                res[num]--;
            }
        }
        for (int i = 0; i < res.length; i++) {
            while (res[i] > 0) {
                arr1[pos++] = i;
                res[i]--;
            }
        }
        return arr1;
    }

    /**
     * 242. 有效的字母异位词
     * https://leetcode-cn.com/problems/valid-anagram/
     *
     * @param s
     * @param t
     * @return
     */
    public boolean isAnagram(String s, String t) {
        if (s == null || t == null || s.length() != t.length()) return false;
        int[] arr = new int[26];
        for (char c : s.toCharArray()) {
            arr[c - 'a']++;
        }
        for (char c : t.toCharArray()) {
            arr[c - 'a']--;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 0) return false;
        }
        return true;
    }

    /**
     * 56. 合并区间
     * https://leetcode-cn.com/problems/merge-intervals/
     *
     * @param intervals
     * @return
     */
    public int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return new int[0][2];
        List<int[]> res = new ArrayList<>();
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });
        for (int i = 0; i < intervals.length; i++) {
            int l = intervals[i][0], r = intervals[i][1];
            if (res.isEmpty() || res.get(res.size() - 1)[1] < l) {
                res.add(new int[]{l, r});
            } else {
                res.get(res.size() - 1)[1] = Math.max(res.get(res.size() - 1)[1], r);
            }
        }
        return res.toArray(new int[res.size()][]);
    }

    /**
     * 493. 翻转对
     * https://leetcode-cn.com/problems/reverse-pairs/
     *
     * @param nums
     * @return
     */
    public int reversePairs(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        return mergeSort(nums, 0, nums.length - 1);
    }

    public int mergeSort(int[] nums, int left, int right) {
        if (right <= left) return 0;
        int mid = (left + right) >>> 1;
        int count = mergeSort(nums, left, mid) + mergeSort(nums, mid + 1, right);
        int[] tmp = new int[right - left + 1];
        int i = left, k = 0, l = left;
        for (int j = mid + 1; j <= right; j++) {
            while (l <= mid && nums[l] <= 2 * (long) nums[j]) l++;
            while (i <= mid && nums[i] < nums[j]) tmp[k++] = nums[i++];
            tmp[k++] = nums[j];
            count += mid - l + 1;
        }
        while (i <= mid) tmp[k++] = nums[i++];
        System.arraycopy(tmp, 0, nums, left, tmp.length);
        return count;
    }

    public static void main(String[] args) {
        Week8 week8 = new Week8();
//        week8.reverseBits(43261596);
        System.out.println(65 & -65);
    }
}
