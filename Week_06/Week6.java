package com.freezing.leetcode.jike;

import java.util.Arrays;

public class Week6 {

    /**
     * 64. 最小路径和
     * https://leetcode-cn.com/problems/minimum-path-sum/
     *
     * @param grid
     * @return
     */
    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
        for (int i = 1; i < m; i++) dp[i][0] = dp[i - 1][0] + grid[i][0];
        for (int i = 1; i < n; i++) dp[0][i] = dp[0][i - 1] + grid[0][i];
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }
        return dp[m - 1][n - 1];
    }

    /**
     * 91. 解码方法
     * https://leetcode-cn.com/problems/decode-ways/
     *
     * @param s
     * @return
     */
    public int numDecodings(String s) {
        if (s.length() == 0 || s.charAt(0) == '0') return 0;
        int[] dp = new int[s.length() + 1];
        dp[0] = 1;
        dp[1] = s.charAt(0) == '0' ? 0 : 1;
        for (int i = 1; i < s.length(); i++) {
            int cur = s.charAt(i) - '0';
            int pre = s.charAt(i - 1) - '0';
            if (cur == 0) {
                if (pre == 1 || pre == 2) {
                    dp[i + 1] = dp[i - 1];
                } else {
                    return 0;
                }
            } else if (pre == 1 || (pre == 2 && cur <= 6)) {
                dp[i + 1] = dp[i] + dp[i - 1];
            } else {
                dp[i + 1] = dp[i];
            }
        }
        return dp[s.length()];
    }

    /**
     * 221. 最大正方形
     * https://leetcode-cn.com/problems/maximal-square/
     *
     * @param matrix
     * @return
     */
    public int maximalSquare(char[][] matrix) {
        if (matrix == null || matrix.length < 1 || matrix[0].length < 1) return 0;
        int m = matrix.length, n = matrix[0].length;
        int[][] dp = new int[m + 1][n + 1];
        int side = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') {
                    dp[i + 1][j + 1] = Math.min(dp[i][j], Math.min(dp[i + 1][j], dp[i][j + 1])) + 1;
                    side = Math.max(side, dp[i + 1][j + 1]);
                }
            }
        }
        return side * side;
    }

    /**
     * 621. 任务调度器
     * https://leetcode-cn.com/problems/task-scheduler/
     *
     * @param tasks
     * @param n
     * @return
     */
    public int leastInterval(char[] tasks, int n) {
        int[] count = new int[26];
        for (int i = 0; i < tasks.length; i++) count[tasks[i] - 'A']++;
        Arrays.sort(count);
        int idle = count[25] - 1;
        int maxCount = idle * n + count[25];
        for (int i = 24; i >= 0 && count[i] == count[25]; i--) maxCount++;
        return Math.max(maxCount, tasks.length);
    }

    /**
     * 647. 回文子串
     * https://leetcode-cn.com/problems/palindromic-substrings/
     *
     * @param s
     * @return
     */
    public int countSubstrings(String s) {
        if (s == null || s.length() == 0) return 0;
        int n = s.length();
        boolean[][] dp = new boolean[n][n];
        int res = s.length();
        for (int i = 0; i < n; i++) dp[i][i] = true;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = j - i == 1 || dp[i + 1][j - 1];
                }
                if (dp[i][j]) res++;
            }
        }
        return res;
    }

    /**
     * 32. 最长有效括号
     * https://leetcode-cn.com/problems/longest-valid-parentheses/
     *
     * @param s
     * @return
     */
    public int longestValidParentheses(String s) {
        int n = s.length();
        int[] dp = new int[n];
        int max = 0;
        for (int i = 1; i < n; i++) {
            if (s.charAt(i) == ')') {
                if (s.charAt(i - 1) == '(') {
                    if (i >= 2) {
                        dp[i] = dp[i - 2] + 2;
                    } else {
                        dp[i] = 2;
                    }
                } else if (i - dp[i - 1] - 1 >= 0 && s.charAt(i - dp[i - 1] - 1) == '(') {
                    if (i - dp[i - 1] - 2 >= 0) {
                        dp[i] = dp[i - 1] + 2 + dp[i - dp[i - 1] - 2];
                    } else {
                        dp[i] = dp[i - 1] + 2;
                    }
                }
            }
            max = Math.max(max, dp[i]);
        }
        return max;
    }

    /**
     * 363. 矩形区域不超过 K 的最大数值和
     * https://leetcode-cn.com/problems/max-sum-of-rectangle-no-larger-than-k/
     * @param matrix
     * @param k
     * @return
     */
    public int maxSumSubmatrix(int[][] matrix, int k) {
        int m = matrix.length,n = matrix[0].length,max = Integer.MIN_VALUE;
        for(int l = 0; l < n; l++){
            int[] rowSums = new int[m];
            for(int r = l; r < n; r++){
                for(int i = 0; i < m; i++){
                    rowSums[i] += matrix[i][r];
                }
                max = Math.max(dpmax(rowSums,k),max);
                if(max == k) return max;
            }
        }
        return max;
    }
    private int dpmax(int[] dp,int k){
        int max = dp[0],pre = 0;
        for (int i = 0; i < dp.length; i++) {
            pre = Math.max(dp[i],dp[i] + pre);
            max = Math.max(pre,max);
        }
        if(max <= k) return max;
        max = Integer.MIN_VALUE;
        for (int i = 0; i < dp.length; i++) {
            int sum = 0;
            for (int j = i; j < dp.length; j++) {
                sum += dp[j];
                if(sum == k) return sum;
                if(sum > max && sum < k) max = sum;
            }
        }
        return max;
    }

    public static void main(String[] args){
        Week6 week6 = new Week6();
        week6.maxSumSubmatrix(new int[][]{{2,2,-1}},3);
    }
}
