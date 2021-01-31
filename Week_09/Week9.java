package com.freezing.leetcode.jike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Week9 {
    /**
     * 387. 字符串中的第一个唯一字符
     * https://leetcode-cn.com/problems/first-unique-character-in-a-string/
     *
     * @param s
     * @return
     */
    public int firstUniqChar(String s) {
        if (s == null || s.length() == 0) return -1;
        int[] tmp = new int[256];
        for (char c : s.toCharArray()) {
            tmp[c]++;
        }
        for (int i = 0; i < s.length(); i++) {
            if (tmp[s.charAt(i)] == 1) return i;
        }
        return -1;
    }

    /**
     * 541. 反转字符串 II
     * https://leetcode-cn.com/problems/reverse-string-ii/
     *
     * @param s
     * @param k
     * @return
     */
    public String reverseStr(String s, int k) {
        char[] a = s.toCharArray();
        for (int start = 0; start < s.length(); start += 2 * k) {
            int i = start, j = Math.min(start + k - 1, a.length - 1);
            while (i < j) {
                char tmp = a[i];
                a[i++] = a[j];
                a[j--] = tmp;
            }
        }
        return String.valueOf(a);
    }

    /**
     * 151. 翻转字符串里的单词
     * https://leetcode-cn.com/problems/reverse-words-in-a-string/
     *
     * @param s
     * @return
     */
    public String reverseWords(String s) {
        String[] words = s.trim().split(" +");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            sb.append(words[i]).append(" ");
        }
        return sb.toString().trim();
    }

    /**
     * 557. 反转字符串中的单词 III
     * https://leetcode-cn.com/problems/reverse-words-in-a-string-iii/
     *
     * @param s
     * @return
     */
    public String reverseWords3(String s) {
        int start = 0, end = 0;
        char[] a = s.toCharArray();
        for (int i = 0; i < a.length; i++) {
            if (a[i] == ' ') {
                end--;
                swap(a, start, end);
                start = i + 1;
                end = i + 1;
            } else {
                end++;
            }
        }
        swap(a, start, end - 1);
        return String.valueOf(a);
    }

    private void swap(char[] a, int start, int end) {
        while (start < end) {
            char tmp = a[start];
            a[start++] = a[end];
            a[end--] = tmp;
        }
    }

    /**
     * 917. 仅仅反转字母
     * https://leetcode-cn.com/problems/reverse-only-letters/
     *
     * @param S
     * @return
     */
    public String reverseOnlyLetters(String S) {
        int i = 0, j = S.length() - 1;
        char[] a = S.toCharArray();
        for (; i < j; i++, j--) {
            while (i < j && !Character.isLetter(a[i])) i++;
            while (i < j && !Character.isLetter(a[j])) j--;
            char tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
        return String.valueOf(a);
    }

    /**
     * 205. 同构字符串
     * https://leetcode-cn.com/problems/isomorphic-strings/
     *
     * @param s
     * @param t
     * @return
     */
    public boolean isIsomorphic(String s, String t) {
        int[] preIndexOfs = new int[256];
        int[] preIndexOft = new int[256];
        for (int i = 0; i < s.length(); i++) {
            if (preIndexOfs[s.charAt(i)] != preIndexOft[t.charAt(i)]) return false;
            preIndexOfs[s.charAt(i)] = i + 1;
            preIndexOft[t.charAt(i)] = i + 1;
        }
        return true;
    }

    /**
     * 8. 字符串转换整数 (atoi)
     * https://leetcode-cn.com/problems/string-to-integer-atoi/
     *
     * @param s
     * @return
     */
    public int myAtoi(String s) {
        int i = 0, n = s.length(), res = 0;
        boolean isNeg = false;
        char[] chars = s.toCharArray();
        while (i < n && chars[i] == ' ') i++;
        if (i == n) return 0;
        if (chars[i] == '-') {
            i++;
            isNeg = true;
        } else if (chars[i] == '+') {
            i++;
        } else if (!Character.isDigit(chars[i])) {
            return 0;
        }
        while (i < n && Character.isDigit(chars[i])) {
            int cur = chars[i++] - '0';
            if (res > (Integer.MAX_VALUE - cur) / 10) {
                return isNeg ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
            res = res * 10 + cur;
        }
        return isNeg ? -res : res;
    }

    /**
     * 438. 找到字符串中所有字母异位词
     * https://leetcode-cn.com/problems/find-all-anagrams-in-a-string/
     *
     * @param s
     * @param p
     * @return
     */
    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> res = new ArrayList<>();
        if (s == null || s.length() < p.length()) return res;
        int sLen = s.length(), pLen = p.length();
        int left = 0, right = 0;
        int[] count = new int[256], window = new int[256];
        for (char c : p.toCharArray()) {
            count[c]++;
        }
        while (right < sLen) {
            char curR = s.charAt(right++);
            window[curR]++;
            while (window[curR] > count[curR]) {
                window[s.charAt(left++)]--;
            }
            if (right - left == pLen) {
                res.add(left);
            }
        }
        return res;
    }

    /**
     * 5. 最长回文子串
     * https://leetcode-cn.com/problems/longest-palindromic-substring/
     *
     * @param s
     * @return
     */
    public String longestPalindrome(String s) {
        int n = s.length();
        if (n < 2) return s;
        int maxLen = 1, start = 0;
        boolean[][] dp = new boolean[n][n];
        for (int i = 0; i < n; i++) dp[i][i] = true;
        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    if (j - i <= 2) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                    if (dp[i][j] && j - i + 1 > maxLen) {
                        maxLen = j - i + 1;
                        start = i;
                    }
                }
            }
        }
        return s.substring(start, start + maxLen);
    }

    /**
     * 44. 通配符匹配
     * https://leetcode-cn.com/problems/wildcard-matching/
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
        for (int i = 1; i <= n && charp[i - 1] == '*'; i++) {
            dp[0][i] = true;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (chars[i - 1] == charp[j - 1] || charp[j - 1] == '?') {
                    dp[i][j] = dp[i - 1][j - 1];
                } else if (charp[j - 1] == '*') {
                    dp[i][j] = dp[i][j - 1] | dp[i - 1][j];
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 115. 不同的子序列
     * https://leetcode-cn.com/problems/distinct-subsequences/
     *
     * @param s
     * @param t
     * @return
     */
    public int numDistinct(String s, String t) {
        int m = t.length(), n = s.length();
        int[][] dp = new int[m + 1][n + 1];
        char[] charS = s.toCharArray(), charT = t.toCharArray();
        for (int j = 0; j <= n; j++) dp[0][j] = 1;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (charS[j - 1] == charT[i - 1]) {
                    dp[i][j] = dp[i][j - 1] + dp[i - 1][j - 1];
                } else {
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    /**
     * 91. 解码方法
     * https://leetcode-cn.com/problems/decode-ways/
     *
     * @param s
     * @return
     */
    public int numDecodings(String s) {
        int n = s.length();
        int[] dp = new int[n + 1];
        char[] a = s.toCharArray();
        dp[0] = 1;
        dp[1] = a[0] == '0' ? 0 : 1;
        for (int i = 1; i < n; i++) {
            int cur = a[i] - '0';
            int pre = a[i - 1] - '0';
            if (cur == 0) {
                if (pre == 1 || pre == 2) {
                    dp[i + 1] = dp[i - 1];
                } else {
                    return 0;
                }
            } else if (pre == 1 || (pre == 2 && cur < 7)) {
                dp[i + 1] = dp[i] + dp[i - 1];
            } else {
                dp[i + 1] = dp[i];
            }
        }
        return dp[n];
    }

    /**
     * 300. 最长递增子序列
     * https://leetcode-cn.com/problems/longest-increasing-subsequence/
     *
     * @param nums
     * @return
     */
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        int res = 1;
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[i] > nums[j]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            res = Math.max(dp[i], res);
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
        if (s.length() < 2) return 0;
        char[] a = s.toCharArray();
        int[] dp = new int[a.length];
        int res = 0;
        for (int i = 1; i < a.length; i++) {
            if (a[i] == ')') {
                if (a[i - 1] == '(') {
                    if (i > 2) {
                        dp[i] = dp[i - 2] + 2;
                    } else {
                        dp[i] = 2;
                    }
                } else if (i - dp[i - 1] - 1 >= 0 && a[i - dp[i - 1] - 1] == '(') {
                    if (i - dp[i - 1] - 2 >= 0) {
                        dp[i] = dp[i - 1] + dp[i - dp[i - 1] - 2] + 2;
                    } else {
                        dp[i] = dp[i - 1] + 2;
                    }
                }
                res = Math.max(dp[i], res);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        Week9 week9 = new Week9();
//        week9.reverseWords("  hello world  ");
//        week9.isIsomorphic("egg", "add");
        week9.myAtoi("9223372036854775808");
    }
}
