学习笔记

## 高级动态规划

高级动态规划的复杂度来源，主要是

1. 状态拥有更多维度（二维、三维、或者更多、甚至需要压缩）
2. 状态方程更加复杂

在做题的时候可以将多个与结果相关的属性作为一维来处理它的状态，然后再看看是否可以进行状态压缩，减少维度

### 习题笔记

1、[10. 正则表达式匹配](https://leetcode-cn.com/problems/regular-expression-matching/)

最近重复子问题：判断s中的第i个字符s[i]与p中的第j个字符p[j]是否相同，如果相同或者p[j]=='.'，则当前两个字符能匹配上，否则若p[j]=='*'，则要判断有两种匹配方式，一种是 *匹配0个，即把p[j-1]也给去掉不匹配了，一种是匹配上s[i]

状态数组dp：dp[i]\[j]表示s的前i个字符是否与p的前j个字符匹配上

状态转移方程：dp[i]\[j] = dp[i-1]\[j-1] （s[i-1] == p[j-1] || p[j-1] == '.'）

​									= dp[i]\[j-2] || (s[i-1] == p[j-2] || p[j-2] == '.')&& dp[i-1]\[j] （j >= 2 && p[j-1] == '*'）

代码如下：

```java
class Solution {
    public boolean isMatch(String s, String p) {
        if(p.length() == 0) return s.length() == 0;
        int m = s.length(), n = p.length();
        char[] chars = s.toCharArray(), charp = p.toCharArray();
        boolean[][] dp = new boolean[m+1][n+1];
      	// 初始化数组
        dp[0][0] = true;
        for(int i = 2; i <= n; i++){
          	// 字符*的前面得有字符才能消除前一个字符
            dp[0][i] = charp[i-1] == '*' && dp[0][i-2];
        }
        for(int i = 1; i <= m; i++){
            for(int j = 1; j <= n; j++){
                if(chars[i-1] == charp[j-1] || charp[j-1] == '.'){
                    // 当前字符能匹配上
                    dp[i][j] = dp[i-1][j-1];
                } else if(j >= 2 && charp[j-1] == '*'){
                  	// 当前字符是*，要么直接把*和前一个字符去掉，要么前一个字符能和s当前字符匹配上，则p保持不动，继续匹配上一个字符
                    dp[i][j] = dp[i][j-2] || ((chars[i-1] == charp[j-2] || charp[j-2] == '.') && dp[i-1][j]);
                }
            }
        }
        return dp[m][n];
    }
}
```

2、[44. 通配符匹配](https://leetcode-cn.com/problems/wildcard-matching/)这道题与上一道题的思想是差不多的，主要的区别在于*变成可以匹配任意字符串，即不能讲前一个字符消除，代码如下：

```java
public boolean isMatch(String s, String p) {
        if(p.length() == 0) return s.length() == 0;
        int m = s.length(), n = p.length();
        char[] chars = s.toCharArray(), charp = p.toCharArray();
        boolean[][] dp = new boolean[m+1][n+1];
        dp[0][0] = true;
  			// 只要当前字符是*，则表示是空字符串，可以匹配，否则就不能匹配空的s
        for(int i = 1; i <= n && charp[i-1] == '*'; i++){
            dp[0][i] = true;
        }
        for(int i = 1; i <= m; i++){
            for(int j = 1; j <= n; j++){
                if(chars[i-1] == charp[j-1] || charp[j-1] == '?'){
                    dp[i][j] = dp[i-1][j-1];
                } else if(charp[j-1] == '*'){
                  	// 若p当前的字符是*，那么可以将*匹配为空字符串，也可以将*匹配s的当前字符串
                    dp[i][j] = dp[i][j-1] | dp[i-1][j];
                }
            }
        }
        return dp[m][n];
    }
```

## 字符串算法

常见的字符串算法，就是翻转字符串，字母异位，回文子串，公共子序列等等；

翻转字符串：将字符的位置替换，采用双指针的方式，用i和j分别指向字符串的首尾位置，然后i不断往前遍历，j不断往后遍历，在遍历的同时进行交换，直到i==j结束

字母异位：若两个字符串里面每个字母的个数都一样，则这两个字符串互为异位词，可以采用大小为256的数组来存储这些记录，从而进行判断

回文子串：若字符串的首尾字母是一样的，且往中间推也满足这样的性质，则这个字符串为回文子串，这类算法基本上也是采用双指针的方式来进行遍历判断；

子序列：子序列的意思就是从字符串中依次取出字符来组成一个新的字符串，且这个字符的相对顺序不变

需要注意的是，在java中s.charAt(i) 的效率比char[i]的效率要低，所以在进行遍历的时候，最好还是采用char[i]的方式提高效率

### 习题笔记

1、[151. 翻转字符串里的单词](https://leetcode-cn.com/problems/reverse-words-in-a-string/)这道题的主要思想是从后面开始遍历，遍历到空格符或者第一个字符，则表示后面的是一个完整的单词，进行记录

```java
class Solution {
    public String reverseWords(String s) {
        StringBuilder sb = new StringBuilder();
        char[] a = s.toCharArray();
        int start = a.length - 1, end = a.length - 1;
        for(int i = a.length - 1; i >= -1; i--){
            if(i == -1 || a[i] == ' '){
                if(start != end){
                    sb.append(s.substring(start+1,end+1)).append(" ");
                }
                start = i-1;
                end = i-1;
            } else {
                start--;
            }
        }
        return sb.toString().trim();
    }
}
```

2、[8. 字符串转换整数 (atoi)](https://leetcode-cn.com/problems/string-to-integer-atoi/)这道题的思想就是从左边开始遍历，先过滤空格，然后判断符号，接着再来计算转换数字的值，需要注意的是，如果转换为数字后溢出了，则需要处理为Integer.MIN_VALUE或Integer.MAX_VALUE

```java
class Solution {
    public int myAtoi(String s) {
        boolean isNeg = false;
        int i = 0, res = 0;
        char[] a = s.toCharArray();
      	// 过滤空格符
        while(i < a.length && a[i] == ' ') i++;
      	// 整个字符串都是空格符，直接返回0
        if(i == a.length) return res;
      	// 判断符号
        if(a[i] == '-'){
            i++;
            isNeg = true;
        } else if(a[i] == '+'){
            i++;
        }
      	// 计算数字
        while(i < a.length && Character.isDigit(a[i])){
            int cur = a[i++] - '0';
          	// 若这个数字加上之后溢出了，则直接返回最大值或最小值
            if(res > (Integer.MAX_VALUE - cur) / 10){
                return isNeg ? Integer.MIN_VALUE : Integer.MAX_VALUE;
            }
          	// 否则将这个数字加到最后一位
            res = res * 10 + cur;
        }
        return isNeg ? -res : res;
    }
}
```

3、[5. 最长回文子串](https://leetcode-cn.com/problems/longest-palindromic-substring/) 这道题是dp和字符串相结合的典型题目，根据回文子串的特性，可以得出状态数组dp[i]\[j]表示在字符串s中，i到j组成的子串是否为回文子串，状态方程数组为：

dp[i]\[j] = dp[i+1]\[j-1] （s[i] == s[j]）

```java
class Solution {
    public String longestPalindrome(String s) {
        if(s.length() < 2) return s;
        int n = s.length(),start = 0,maxLen = 1;
        char[] a = s.toCharArray();
        boolean[][] dp = new boolean[n][n];
      	// 每个字符自己本身是回文子串
        for(int i = 0; i < n; i++) dp[i][i] = true;
        for(int i = n - 2; i >= 0; i--){
            for(int j = i+1; j < n; j++){
                if(a[i] == a[j]){
                  	// j-i<=2 表示字符串长度小于等于3，减去首尾两个字符，最多就剩1个字符，肯定是回文子串
                    if(j - i <= 2){
                        dp[i][j] = true;
                    } else {
                      // 若i,j长度大于3，则需要判断减去首尾两个字符的时候，该子串是否为回文子串
                        dp[i][j] = dp[i+1][j-1];
                    }
                  	// 记录最长回文子串的长度和起始位置
                    if(dp[i][j] && j - i + 1 > maxLen){
                        start = i;
                        maxLen = j - i + 1;
                    }
                } 
            }
        }
        return s.substring(start,start+maxLen);
    }
}
```

