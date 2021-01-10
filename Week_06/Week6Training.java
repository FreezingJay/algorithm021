package com.freezing.leetcode.jike;

import java.util.Arrays;
import java.util.List;

public class Week6Training {

    public int climbStairs(int n) {
        System.out.println("climbStairs1");
        int[] steps = new int[]{1, 2};
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < 2; j++) {
                int step = steps[j];
                if (i < step) continue;
                dp[i] += dp[i - step];
                System.out.println("dp[" + i + "] = " + dp[i]);
            }
        }
        return dp[n];
    }

    public int climbStairs2(int n) {
        System.out.println("climbStairs2");
        int[] steps = new int[]{1, 2};
        int[] dp = new int[n + 1];
        dp[0] = 1;
        for (int j = 0; j < 2; j++) {
            for (int i = 1; i <= n; i++) {
                int step = steps[j];
                if (i < step) continue;
                dp[i] += dp[i - step];
                System.out.println("dp[" + i + "] = " + dp[i]);
            }
        }
        return dp[n];
    }

    /**
     * 1143. 最长公共子序列
     * https://leetcode-cn.com/problems/longest-common-subsequence/
     *
     * @param text1
     * @param text2
     * @return
     */
    public int longestCommonSubsequence(String text1, String text2) {
        int m = text1.length(), n = text2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = text1.charAt(i - 1) == text2.charAt(j - 1) ?
                        dp[i - 1][j - 1] + 1 :
                        Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[m][n];
    }

    /**
     * 120. 三角形最小路径和
     * https://leetcode-cn.com/problems/triangle/
     *
     * @param triangle
     * @return
     */
    public int minimumTotal(List<List<Integer>> triangle) {
        int[] dp = new int[triangle.get(triangle.size() - 1).size()];
        for (int i = triangle.size() - 1; i >= 0; i--) {
            for (int j = 0; j < triangle.get(i).size(); j++) {
                List<Integer> row = triangle.get(i);
                if (i == triangle.size() - 1) {
                    dp[j] = row.get(j);
                } else {
                    dp[j] = row.get(j) + Math.min(dp[j], dp[j + 1]);
                }
            }
        }
        return dp[0];
    }

    /**
     * 53. 最大子序和
     * https://leetcode-cn.com/problems/maximum-subarray/
     * @param nums
     * @return
     */
    public int maxSubArray(int[] nums) {
        int max = nums[0], pre = 0;
        for(int num : nums){
            pre = Math.max(num,num+pre);
            max = Math.max(pre,max);
        }
        return max;
    }

    /**
     * 152. 乘积最大子数组
     * https://leetcode-cn.com/problems/maximum-product-subarray/
     *
     * @param nums
     * @return
     */
    public int maxProduct(int[] nums) {
        int res = nums[0], max = nums[0], min = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int mx = max, mn = min;
            max = Math.max(mx * nums[i], Math.max(mn * nums[i], nums[i]));
            min = Math.min(mn * nums[i], Math.min(mx * nums[i], nums[i]));
            res = Math.max(res, max);
        }
        return res;
    }


    /**
     * 322. 零钱兑换
     * https://leetcode-cn.com/problems/coin-change/
     *
     * @param coins
     * @param amount
     * @return
     */
    public int coinChange(int[] coins, int amount) {
        if (amount == 0) return 0;
        if (coins == null || coins.length == 0) return -1;
        int[] dp = new int[amount + 1];
        dp[0] = 0;
        for (int i = 1; i < amount + 1; i++) {
            int min = 0;
            for (int j = 0; j < coins.length; j++) {
                if (i < coins[j]) continue;
                min = Math.min(min, dp[i - coins[j]]);
            }
            dp[i] = min + 1;
        }
        return dp[amount];
    }

    /**
     * 518. 零钱兑换 II
     * https://leetcode-cn.com/problems/coin-change-2/
     * @param amount
     * @param coins
     * @return
     */
    public int change(int amount, int[] coins) {
        int[] dp = new int[amount + 1];
        dp[0] = 1;
        for(int i = 0; i < coins.length; i++){
            for(int j = 1; j <= amount; j++){
                if(j < coins[i]) continue;
                dp[j] += dp[j-coins[i]];
            }
        }
        return dp[amount];
    }

    /**
     * 198. 打家劫舍
     * https://leetcode-cn.com/problems/house-robber/
     *
     * @param nums
     * @return
     */
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        int first = 0, second = 0;
        for (int i = 0; i < n; i++) {
            int tmp = second;
            second = Math.max(first + nums[i], second);
            first = tmp;
        }
        return second;
    }

    /**
     * 213. 打家劫舍 II
     * https://leetcode-cn.com/problems/house-robber-ii/description/
     *
     * @param nums
     * @return
     */
    public int rob2(int[] nums) {
        int n = nums.length;
        if (n == 0) return 0;
        if (n == 1) return nums[0];
        return Math.max(myRob(Arrays.copyOfRange(nums, 0, n - 1)),
                myRob(Arrays.copyOfRange(nums, 1, n)));
    }

    private int myRob(int[] nums) {
        int first = 0, second = 0, tmp;
        for (int i = 0; i < nums.length; i++) {
            tmp = second;
            second = Math.max(first + nums[i], second);
            first = tmp;
        }
        return second;
    }

    /**
     * 121. 买卖股票的最佳时机
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock/#/description
     * @param prices
     * @return
     */
    public int maxProfit1(int[] prices) {
        if(prices == null || prices.length < 2) return 0;
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][1] = -prices[0];
        for(int i = 1; i < n; i++){
            dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i-1][1], - prices[i]);
        }
        return dp[n-1][0];
    }

    /**
     * 123. 买卖股票的最佳时机 III
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iii/
     *
     * @param prices
     * @return
     */
    public int maxProfit3(int[] prices) {
        if (prices == null || prices.length < 2) return 0;
        int n = prices.length;
        int[][][] dp = new int[n][3][2];
        for (int i = 1; i <= 2; i++) {
            dp[0][i][0] = 0;
            dp[0][i][1] = -prices[0];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= 2; j++) {
                dp[i][j][0] = Math.max(dp[i - 1][j][0], dp[i - 1][j][1] + prices[i]);
                dp[i][j][1] = Math.max(dp[i - 1][j][1], dp[i - 1][j - 1][0] - prices[i]);
            }
        }
        return dp[n - 1][2][0];
    }

    /**
     * 188. 买卖股票的最佳时机 IV
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-iv/
     * @param k
     * @param prices
     * @return
     */
    public int maxProfit(int k, int[] prices) {
        if(prices == null || prices.length < 2) return 0;
        int n = prices.length;
        if(k >= n/2){
            return myProfit(prices);
        }
        int[][][] dp = new int[n][k+1][2];
        for(int i = 1; i <= k; i++) dp[0][i][1] = -prices[0];
        for(int i = 1; i < n; i++){
            for(int j = 1; j <= k; j++){
                dp[i][j][0] = Math.max(dp[i-1][j][0], dp[i-1][j][1] + prices[i]);
                dp[i][j][1] = Math.max(dp[i-1][j][1], dp[i-1][j-1][0] - prices[i]);
            }
        }
        return dp[n-1][k][0];
    }

    private int myProfit(int[] prices){
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][1] = -prices[0];
        for(int i = 1; i < n; i++){
            dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i]);
        }
        return dp[n-1][0];
    }

    /**
     * 309. 最佳买卖股票时机含冷冻期
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-cooldown/
     * @param prices
     * @return
     */
    public int maxProfit(int[] prices) {
        if(prices == null || prices.length < 2) return 0;
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][1] = -prices[0];
        for(int i = 1; i < n; i++){
            dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i]);
            dp[i][1] = Math.max(dp[i-1][1], (i>=2?dp[i-2][0]:0) - prices[i]);
        }
        return dp[n-1][0];
    }

    /**
     * 714. 买卖股票的最佳时机含手续费
     * https://leetcode-cn.com/problems/best-time-to-buy-and-sell-stock-with-transaction-fee/
     * @param prices
     * @param fee
     * @return
     */
    public int maxProfit(int[] prices, int fee) {
        if(prices == null || prices.length < 2) return 0;
        int n = prices.length;
        int[][] dp = new int[n][2];
        dp[0][1] = -prices[0];
        for(int i = 1; i < n; i++){
            dp[i][0] = Math.max(dp[i-1][0], dp[i-1][1] + prices[i] - fee);
            dp[i][1] = Math.max(dp[i-1][1], dp[i-1][0] - prices[i]);
        }
        return dp[n-1][0];
    }

    /**
     * 72. 编辑距离
     * https://leetcode-cn.com/problems/edit-distance/
     *
     * @param word1
     * @param word2
     * @return
     */
    public int minDistance(String word1, String word2) {
        int m = word1.length(), n = word2.length();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int i = 0; i <= n; i++) dp[0][i] = i;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1.charAt(i - 1) == word2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                }
            }
        }
        return dp[m][n];
    }

    public static void main(String[] args) {
        Week6Training training = new Week6Training();
//        training.maximalSquare(new char[][]{{'1', '0', '1', '0', '0'}, {'1', '0', '1', '1', '1'}, {'1', '1', '1', '1', '1'}, {'1', '0', '0', '1', '0'}});
//        training.climbStairs(3);
//       training.climbStairs2(3);
        training.maxProfit3(new int[]{1, 2, 3, 4, 5});
    }
}
