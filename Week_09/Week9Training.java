package com.freezing.leetcode.jike;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Week9Training {

    /**
     * 709. 转换成小写字母
     * https://leetcode-cn.com/problems/to-lower-case/
     *
     * @param str
     * @return
     */
    public String toLowerCase(String str) {
        if (str == null) return null;
        char[] chars = str.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] >= 'A' && chars[i] <= 'Z') {
                chars[i] += 32;
            }
        }
        return String.valueOf(chars);
    }

    /**
     * 58. 最后一个单词的长度
     * https://leetcode-cn.com/problems/length-of-last-word/
     *
     * @param s
     * @return
     */
    public int lengthOfLastWord(String s) {
        if (s == null) return 0;
        int start = s.length() - 1, end = s.length() - 1;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == ' ') {
                if (start != end) {
                    return end - start;
                }
                start = i - 1;
                end = i - 1;
            } else {
                start--;
            }
        }
        return end - start;
    }

    /**
     * 771. 宝石与石头
     * https://leetcode-cn.com/problems/jewels-and-stones/
     *
     * @param jewels
     * @param stones
     * @return
     */
    public int numJewelsInStones(String jewels, String stones) {
        int count = 0;
        char[] chars = new char[256];
        for (char s : stones.toCharArray()) {
            chars[s]++;
        }
        for (char j : jewels.toCharArray()) {
            count += chars[j];
        }
        return count;
    }

    /**
     * 14. 最长公共前缀
     * https://leetcode-cn.com/problems/longest-common-prefix/
     *
     * @param strs
     * @return
     */
    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        Arrays.sort(strs);
        int len = strs[0].length();
        for (int i = 0; i < len; i++) {
            char c = strs[0].charAt(i);
            for (int j = 1; j < strs.length; j++) {
                if (i == strs[j].length() || c != strs[j].charAt(i)) {
                    return strs[0].substring(0, i);
                }
            }
        }
        return strs[0];
    }

    /**
     * 344. 反转字符串
     * https://leetcode-cn.com/problems/reverse-string/
     *
     * @param s
     */
    public void reverseString(char[] s) {
        if (s == null || s.length == 0) return;
        int len = s.length;
        for (int i = 0; i < len / 2; i++) {
            char tmp = s[i];
            s[i] = s[len - i - 1];
            s[len - i - 1] = tmp;
        }
    }

    /**
     * 49. 字母异位词分组
     * https://leetcode-cn.com/problems/group-anagrams/
     *
     * @param strs
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public List<List<String>> groupAnagrams(String[] strs) {
        Map<String, List<String>> map = new HashMap<>();
        for (String str : strs) {
            char[] keyArr = str.toCharArray();
            ;
            Arrays.sort(keyArr);
            String key = String.valueOf(keyArr);
            List<String> list = map.getOrDefault(key, new ArrayList<String>());
            list.add(str);
            map.put(key, list);
        }
        return new ArrayList<>(map.values());
    }

    /**
     * 125. 验证回文串
     * https://leetcode-cn.com/problems/valid-palindrome/
     *
     * @param s
     * @return
     */
    public boolean isPalindrome(String s) {
        if (s == null || s.length() == 0) return true;
        int i = 0, j = s.length() - 1;
        char left, right;
        while (i <= j) {
            while (i < j && !Character.isLetterOrDigit(s.charAt(i))) {
                i++;
            }
            left = s.charAt(i);
            while (i < j && !Character.isLetterOrDigit(s.charAt(j))) {
                j--;
            }
            right = s.charAt(j);
            if (Character.toLowerCase(left) != Character.toLowerCase(right)) return false;
            i++;
            j--;
        }
        return true;
    }

    /**
     * 680. 验证回文字符串 Ⅱ
     * https://leetcode-cn.com/problems/valid-palindrome-ii/
     *
     * @param s
     * @return
     */
    public boolean validPalindrome(String s) {
        int i = 0, j = s.length() - 1;
        for (; i < j && s.charAt(i) == s.charAt(j); i++, j--) ;
        return isPalindrome(s, i + 1, j) || isPalindrome(s, i, j - 1);
    }

    private boolean isPalindrome(String s, int i, int j) {
        for (; i < j && s.charAt(i) == s.charAt(j); i++, j--) ;
        return i >= j;
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
                if (text1.charAt(i - 1) == text2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 10. 正则表达式匹配
     * https://leetcode-cn.com/problems/regular-expression-matching/
     *
     * @param s
     * @param p
     * @return
     */
    public boolean isMatch(String s, String p) {
        if (p.length() == 0) return s.length() == 0;
        int m = s.length(), n = p.length();
        char[] chars = s.toCharArray(), charp = p.toCharArray();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int i = 2; i <= n; i++) {
            dp[0][i] = charp[i - 1] == '*' && dp[0][i - 2];
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (chars[i - 1] == charp[j - 1] || charp[j - 1] == '.') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (j >= 2 && charp[j - 1] == '*') {
                    dp[i][j] = dp[i][j - 2] || ((chars[i - 1] == charp[j - 2] || charp[j - 2] == '.') && dp[i - 1][j]);
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 746. 使用最小花费爬楼梯
     * https://leetcode-cn.com/problems/min-cost-climbing-stairs/
     *
     * @param cost
     * @return
     */
    public int minCostClimbingStairs(int[] cost) {
        int n = cost.length;
        int[] dp = new int[n + 1];
        dp[0] = cost[0];
        dp[1] = cost[1];
        for (int i = 2; i <= cost.length; i++) {
            int currentCost = i == cost.length ? 0 : cost[i];
            dp[i] = Math.min(dp[i - 2], dp[i - 1]) + currentCost;
        }
        return dp[n];
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
        char[] chars1 = word1.toCharArray(), chars2 = word2.toCharArray();
        int[][] dp = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0 || j == 0) {
                    dp[i][j] = i + j;
                } else if (chars1[i - 1] == chars2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1])) + 1;
                }
            }
        }
        return dp[m][n];
    }
}
