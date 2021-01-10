学习笔记

## 动态规划

​		做了那么多动态规划的题之后，发现其实动态规划大部分情况下就是找到重复子问题后，将所有重复子问题的解穷举出来，存到数组中，然后再在数组中找到最优解。而在数组中，后一位存的基本上都跟前面几位相关，或前一位，或前两位，或上一位左一位等等。

​		其次就是，动态规划一般是用空间来换时间，所以在做题目的时候，可以先采用多维数组进行存储中间状态值，进而得出最优解，然后再观察是否可以降维处理，例如在记录中间状态值时，该值若只与前一个值相关。这样逐渐降维优化下来，空间复杂度进一步降低，而不是一开始就考虑用最小维。

动态规划的题解一般分为三个步骤

1、找到重复子问题

2、定义dp数组，确定初始值

3、找到状态转移方程（最难）

### 习题笔记

1、[64. 最小路径和](https://leetcode-cn.com/problems/minimum-path-sum/)

（1）重复子问题：grid[i]\[j] 的路径可以从grid[i]\[j-1]走过来，也可以从grid[i-1]\[j]走过来，那么最小和就是从这二者其中找到最小的，加上grid[i]\[j]的值

（2）定义dp数组：dp[i]\[j]表示grid[i]\[j]的最小路径和, 初始值dp[0...i]\[0] 和dp[0]\[0...j]的值可以由dp[0...i-1]\[0] 和dp[0]\[0...j-1]来确定

（3）状态转移方程：

dp[i]\[j] = Math.min(dp[i-1]\[j],dp[i]\[j-1])

代码如下:

```java
public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
  			// 初始化base case，利用原数组省了创建新的空间
        for(int i = 1; i < m; i++) grid[i][0] += grid[i-1][0];
        for(int i = 1; i < n; i++) grid[0][i] += grid[0][i-1];
        for(int i = 1; i < m; i++){
            for(int j = 1; j < n; j++){
                grid[i][j] += Math.min(grid[i-1][j],grid[i][j-1]);
            }
        }
        return grid[m-1][n-1];
    }
```

2、[91. 解码方法](https://leetcode-cn.com/problems/decode-ways/)

（1）重复子问题：若s[i]为0，判断s[i-1]的值：若s[i-1]为1或者2，则可以组合成10或20，可以匹配到26的字母里面，否则就匹配不了；若s[i]不为0，则当s[i-1]为1的时候，可以组合到11-19,当s[i-1]为2且s[i]小于7时，可以组合21-26，否则的话只能是s[i]自己去匹配。

（2）定义dp数组：dp[i+1]表示前i个字符能解码的方法组合数量

（3）状态转移方程：

if(s[i] == 0):

​     if(s[i-1] == 1,2) dp[i+1] = dp[i-1]\(只有一种组合翻译)

​     else  return 0

else:

​	if(s[i-1] == 1 || s[i-1] == 2 && s[i] < 7) dp[i+1] = dp[i] + dp[i-1] (单独翻译为dp[i]，组合翻译为dp[i-1])

​    else dp[i+1] = dp[i] (只能单独翻译)

代码如下：

```java
public int numDecodings(String s) {
        int n = s.length();
        int[] dp = new int[n + 1];
        dp[0] = 1;
        dp[1] = s.charAt(0) == '0' ? 0 : 1;
        for(int i = 1; i < n; i++){
            int cur = s.charAt(i) - '0';
            int pre = s.charAt(i-1) - '0';
            if(cur == 0){
                if(pre == 1 || pre == 2){
                    dp[i + 1] = dp [i - 1];
                } else {
                    return 0;
                }
            } else if (pre == 1 || (pre == 2 && cur < 7)){
                dp[i + 1] = dp[i] + dp[i - 1];
            } else {
                dp[i + 1] = dp[i];
            }
        }
        return dp[n];
    }
```

3、[221. 最大正方形](https://leetcode-cn.com/problems/maximal-square/)

（1）重复子问题：若（i，j）为1，以（i，j）为右下角，最小边长即为1，若（i-1，j-1），（i-1，j），（i，j-1）都为1，则最小边长增加1，以此类推

（2）定义dp数组：dp[i]\[j]表示以（i，j）为右下角，能找到的最大正方形边长，首行和首列初始值为矩阵的首行首列值

（3）状态转移方程：dp[i]\[j] = min（dp[i-1]\[j-1]，dp[i-1]\[j]，dp[i]\[j-1]）+ 1

```java
public int maximalSquare(char[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] dp = new int[m][n];
        int maxSide = 0;
        for(int i = 0; i < m; i++){
            dp[i][0] = matrix[i][0] == '1' ? 1:0;
            maxSide = Math.max(maxSide,dp[i][0]);
        }
        for(int i = 0; i < m; i++){
            for(int j = 1; j < n; j++){
                if(matrix[i][j] == '1'){
                    if(i == 0){
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = Math.min(dp[i-1][j-1],Math.min(dp[i-1][j],dp[i][j-1])) + 1;
                    }
                } 
                maxSide = Math.max(dp[i][j],maxSide);
            }
        }
        return maxSide * maxSide;
    }
```

未完待续。。。