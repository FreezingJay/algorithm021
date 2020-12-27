学习笔记

## 一、广度优先搜索和深度优先搜索

### 知识点笔记

1、在之前学习的二叉树里面，广度优先搜索对应的是层序遍历，深度优先搜索对应的是前序遍历。而除了二叉树以外，其它的数据结构也可以进行这两种搜索，例如图和状态集。

2、所谓的搜索遍历，就是每个节点都要访问一次，且仅访问一次

3、深度优先搜索遍历的代码模板如下：

```java
Set<Node> visited = new HashSet<>();
public void dfs(Node root){
  // terminator
  if(visited.contains(root)) return;
  // process current node
  visited.add(root);
  // drill down
  for(Node node : root.children){
    dfs(node);
  }
}
```

4、广度优先搜索遍历的代码模板如下：

```java
public void bfs(Node root){
  // 存放结点的队列
  Queue<Node> queue = new LinkedList<>();
  // 结点使用记录
  Set<Node> visited = new HashSet<>();
  if(root != null) {
    queue.offer(node);
  	visited.add(root);
  }
  while(!queue.isEmpty()){
    // 每层遍历的次数
    int size = queue.size();
    for(int i = 0; i < size; i++){
      Node node = queue.poll();
      if(visited.contains(node)) continue;
      // process current node
      visited.add(node);
      queue.offer(node);
    }
  }
}
```

### 习题笔记

1、[在每个树行中找最大值](https://leetcode-cn.com/problems/find-largest-value-in-each-tree-row/)这道题是可以用BFS做，也可以用DFS做。

用BFS做比较直观，就是层序遍历，然后记录每一层的最大值，基本上套上模板就可以了，代码如下：

```java
		public List<Integer> largestValues(TreeNode root) {
        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        if(root != null) queue.offer(root);
        while(!queue.isEmpty()){
            int size = queue.size();
            int max = Integer.MIN_VALUE;
            for(int i = 0; i < size; i++){
                TreeNode node = queue.poll();
                max = Math.max(max,node.val);
                if(node.left != null) queue.offer(node.left);
                if(node.right != null) queue.offer(node.right);
            }
            res.add(max);
        }
        return res;
    }
```

而DFS就相对绕一些，需要用一个map来记录遍历过的每一层的最大值，每遍历一个数，就要记录它属于哪一层，然后与map中保存的值进行比较，将较大的值存入map中，代码如下

```java
    List<Integer> res;
    Map<Integer,Integer> map;
    public List<Integer> largestValues(TreeNode root) {
        res = new ArrayList<>();
        map = new HashMap<>();
        if(root != null) {
            dfs(0,root);
        }
        for(int i = 0; i < map.size(); i++){
            res.add(map.get(i));
        }
        return res;
    }

    private void dfs(int index, TreeNode node){
        if(node == null){
            return;
        }
        int max = Math.max(node.val,map.getOrDefault(index,Integer.MIN_VALUE));
        map.put(index,max);
        dfs(index + 1,node.left);
        dfs(index + 1,node.right);
    }
```

2、[岛屿数量](https://leetcode-cn.com/problems/number-of-islands/)这道题的思想是相邻的1都算是同一个岛屿，即只要当前位置是1，其上下左右也是1则为相同的岛屿，利用DFS，遍历的时候将1置为2，表示已经遍历过了，然后遍历相邻的位置，如果是1也置为2，不为1就结束，这样遍历一个岛屿之后，其相邻的1就会都被置为2，减少遍历的次数，代码如下：

```java
    public int numIslands(char[][] grid) {
        int res = 0;
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
              	//遍历到一个岛屿，res+1,并将处理相邻的岛屿
                if(grid[i][j] == '1'){
                    res ++;
                    area(grid,i,j);
                }
            }
        }
        return res;
    }

    private void area(char[][] grid, int r, int c){
        //当前位置不在范围内或者不为1，直接结束 
        if(!inArea(grid,r,c) || grid[r][c] != '1') return;
      	//将当前位置置为2，表示遍历过了
        grid[r][c] = '2';
				// 遍历上下左右到位置
        area(grid,r-1,c);
        area(grid,r+1,c);
        area(grid,r,c-1);
        area(grid,r,c+1);
    }
		// 判断位置是否合法
    private boolean inArea(char[][] grid, int r, int c){
        return 0 <= r && r < grid.length && 0 <= c && c < grid[0].length;
    }
```

3、[扫雷游戏](https://leetcode-cn.com/problems/minesweeper/)这道题的思想与上一道相似，只不过这次不只上下左右四个方向，还有左上，左下，右上，右下一共八个方向，那么就可以用两个数组分别用来表示八个方位的x和y值的变化，然后在遍历之前，需要先尝试判断一下，相邻的方位是否有效，以及是否有雷，如果有雷，就需要将周围的雷数累计起来，将当前的值置为‘雷数’，否则直接置为‘B’，然后DFS相邻的还没有被遍历的位置，代码如下：

```java
		int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
    int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

    public char[][] updateBoard(char[][] board, int[] click) {
        int x = click[0], y = click[1];
        if (board[x][y] == 'M') {
            board[x][y] = 'X';
        } else {
            dfs(board, x, y);
        }
        return board;
    }

    private void dfs(char[][] board, int r, int c) {
        int count = 0;
        for (int i = 0; i < 8; i++) {
            int x = r + dx[i];
            int y = c + dy[i];
            if (!inBoard(board, x, y)) continue;
            if (board[x][y] == 'M') {
                count++;
            }
        }
        if (count > 0) {
            board[r][c] = (char) (count + '0');
            return;
        }
        board[r][c] = 'B';
        for (int i = 0; i < 8; i++) {
            int x = r + dx[i];
            int y = c + dy[i];
            if (!inBoard(board, x, y) || board[x][y] != 'E') continue;
            dfs(board, x, y);
        }
    }
```

还可以用BFS的方式进行遍历，只是这里需要手动维护一个集合来记录遍历过的位置，防止在下一次入队列的时候重复了，代码如下：

```java
		int[] dx = {-1, 0, 1, -1, 1, -1, 0, 1};
    int[] dy = {-1, -1, -1, 0, 0, 1, 1, 1};

    public char[][] updateBoard(char[][] board, int[] click) {
        int x = click[0], y = click[1];
        if (board[x][y] == 'M') {
            board[x][y] = 'X';
        } else {
            int row = board.length, column = board[0].length;
            Queue<int[]> queue = new LinkedList<>();
            boolean[][] visited = new boolean[row][column];
            queue.offer(click);
            visited[x][y] = true;
            while (!queue.isEmpty()) {
                int[] point = queue.poll();
                int i = point[0], j = point[1];
                int count = 0;
                for (int k = 0; k < 8; k++) {
                    int newX = i + dx[k];
                    int newY = i + dy[k];
                    if (!inBoard(board, newX, newY)) continue;
                    if (board[x][y] == 'M') count++;
                }
                if (count > 0) {
                    board[i][j] = (char) (count + '0');
                } else {
                    board[i][j] = 'B';
                    for (int k = 0; k < 8; k++) {
                        int newX = i + dx[k];
                        int newY = i + dy[k];
                        if (!inBoard(board, newX, newY) || board[newX][newY] != 'E' || visited[newX][newY])
                            continue;
                        visited[newX][newY] = true;
                        queue.offer(new int[]{newX, newY});
                    }
                }
            }
        }
        return board;
    }
```

4、[最小基因变化](https://leetcode-cn.com/problems/minimum-genetic-mutation/)和[单词接龙](https://leetcode-cn.com/problems/word-ladder/)、[单词接龙 II](https://leetcode-cn.com/problems/word-ladder-ii/)的思想类似，这里就以《单词接龙2》为例进行笔记记录，本质上来讲，都是将当前的字符串，在通过一次次的改变一个字符，且改变后的字符是在单词表中的，最终变为指定字符串，《最小基因变化》是只有4个字符进行变化，而《单词接龙》则有26个字符可以进行变化，这个获取变化的字符串倒是相差不大，都可以这么去获取：

```java
		private List<String> getNeighbors(String s){
        List<String> neighbors = new ArrayList<>();
        char[] chars = s.toCharArray();
        for(int i = 0; i < s.length(); i++){
          	// 缓存当前位置的字符
            char c = chars[i];
            for(char j = 'a' ; j <= 'z'; j++){
              	// 与当前字符一致的可以直接过滤
                if(c == j) continue;
              	// 将一个字符改为另一个字符
                chars[i] = j;
             		// 生成新的字符串
                String newStr = new String(chars);
              	// 判断新的字符串是否在字典中，是则添加到邻居列表中
                if(wordSet.contains(newStr)) neighbors.add(newStr);
            }
          	// 记得遍历一个位置完后将其重置为原来的字符
            chars[i] = c;
        }
        return neighbors;
    }
```

然后就是利用广度优先遍历，来遍历所有的可能，记录下来遍历的路径，需要注意的是，这里需要将遍历过的单词记录下来，因为这种遍历本质上是对图的遍历，如果不记录下遍历过的单词，容易进入死循环，导致超时，完整的代码如下：

```java
    Set<String> wordSet;
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> res = new ArrayList<>();
        wordSet = new HashSet<>(wordList);
      	// 如果最终的单词不在单词表中，那么是绝对变不到的
        if(!wordSet.contains(endWord)) return res;
      	// 记录遍历过得单词
        Set<String> visited = new HashSet<>();
      	// 判断是否已经找到一条完整的队列
        boolean isFound = false;
      	// 队里这里是记录遍历的路径
        Queue<List<String>> queue = new LinkedList<>();
        List<String> path = new ArrayList<>();
        path.add(beginWord);
        queue.offer(path);
      	// 单词表移除最初的单词，防止重复遍历
        wordSet.remove(beginWord);
        visited.add(beginWord);
        while(!queue.isEmpty()){
            int size = queue.size();
          	// 用于记录当前层遍历过的单词
            Set<String> subVisited = new HashSet<>();
            for(int i = 0; i < size; i++){
              	// 取出队首的路径
                List<String> p = queue.poll();
              	// 获取路径的最后一个单词
                String s = p.get(p.size() - 1);
              	// 遍历单词的所有合法邻居单词
                for(String neighbor : getNeighbors(s)){
                  	// 已经遍历过的直接过滤
                    if(visited.contains(neighbor)) continue;
                  	// 路径添加邻居单词
                    p.add(neighbor);
                    subVisited.add(neighbor);
                  	// 若此邻居单词是最终结果，将完整路径记录下来，并将isFound置为true，表示已经找到最短路径
                    if(neighbor.equals(endWord)){
                        isFound = true;
                        res.add(new ArrayList<>(p));
                    }
                  	// 将新路径添加到队尾中
                    queue.offer(new ArrayList<>(p));
                  	// 将当前邻居移除队列，进行下一个邻居的遍历
                    p.remove(neighbor);
                }
            }
          	// 汇总当前层遍历过的单词到总的记录中
            visited.addAll(subVisited);
          	// 如果已经找到最短路径了，就不用再遍历下一层了
            if(isFound) break;
        }
        return res;
    }

    private List<String> getNeighbors(String s){
        List<String> neighbors = new ArrayList<>();
        char[] chars = s.toCharArray();
        for(int i = 0; i < s.length(); i++){
          	// 缓存当前位置的字符
            char c = chars[i];
            for(char j = 'a' ; j <= 'z'; j++){
              	// 与当前字符一致的可以直接过滤
                if(c == j) continue;
              	// 将一个字符改为另一个字符
                chars[i] = j;
             		// 生成新的字符串
                String newStr = new String(chars);
              	// 判断新的字符串是否在字典中，是则添加到邻居列表中
                if(wordSet.contains(newStr)) neighbors.add(newStr);
            }
          	// 记得遍历一个位置完后将其重置为原来的字符
            chars[i] = c;
        }
        return neighbors;
    }
```

## 二、贪心算法

贪心算法是一种在每一步选择中都采取在当前状态下最好或最优（即最有利）的选择，从而希望导致结果是全局最好或最优的算法。但是实际上每一步选择都采取最优解，最终解也不一定就是最优解，所以需要视情况而定是否能是否贪心算法。

适用贪心算法的场景：问题能分解为子问题，子问题的最优解能递推到最终问题的最优解

### 习题笔记

1、[柠檬水找零](https://leetcode-cn.com/problems/lemonade-change/)这道题的思想是我们只能用5块钱和10块钱进行找零，那么当遇到5块钱的时候，不用找零，但要将当前拥有的5块钱的数量加1；当遇到10块钱的时候，就需要将10块钱的数量加1，5块钱的数量减1；当遇到20块钱的时候，就要看看当前有没有10块钱，有就优先用10块钱+5块钱进行找零，没有的话就只能用5块钱找零；每次找零后判断5块钱是否大于等于0，如果小于0，所以不够找，返回false；遍历到最后都没有返回false说明都能找零成功，返回true；

```java
    public boolean lemonadeChange(int[] bills) {
        if(bills == null || bills.length == 0) return false;
        int five = 0, ten = 0;
        for(int i = 0; i < bills.length; i++){
            if(bills[i] == 5){
                five ++;
            } else if(bills[i] == 10){
                five --;
                ten ++;
            } else if(ten > 0){
                ten --;
                five --;
            } else {
                five-=3;
            }
            if(five < 0) return false;
        }
        return true;
    }
```

2、[分发饼干](https://leetcode-cn.com/problems/assign-cookies/)这道题的思想是，将孩子的胃口数组和饼干数组都进行排序，那么可以选择先喂饱胃口小的孩子，如果当前饼干不满足小胃口的孩子，显然也不能满足大胃口的孩子，判断下一个饼干是否满足，以此类推，代码如下：

```java
    public int findContentChildren(int[] g, int[] s) {
        Arrays.sort(g);
        Arrays.sort(s);
        int res = 0;
      	// 记录当前要喂饱的孩子的下标
        int j = 0;
        for(int i = 0; i < s.length; i++){
          	// 没有孩子可以遍历了
            if(j == g.length) break;
          	// 当前饼干可以满足这个孩子
            if(s[i] >= g[j]){
                res ++;
                j++;
            }
        }
        return res;
    }
```

3、[模拟行走机器人](https://leetcode-cn.com/problems/walking-robot-simulation/)这道题的思想是，在一个坐标系中，以向北为基准0，东，南，西分别为1，2，3，那么当机器人收到向右转的指令时，即方向+1，当机器人收到向左转的时候，即方向+3，相当于向右转3次，可以以方向下标作为二维数组的行，列为相对当前位置即将走的位置：

```java
int[][] xy = {{0,1},{1,0},{0,-1},{-1,0}};// 往北走一步，往东走一步，从南走一步，往西走一步
```

遇到障碍物的时候，是不能走到障碍物上的，所以在走的时候，要先判断下一步是不是障碍物，没有才能继续走，是障碍物的话，这个指令就结束了，完整代码如下：

```java
   public int robotSim(int[] commands, int[][] obstacles) {
     		// x和y表示当前坐标，direction表示当前方向，res表示结果
        int x = 0, y = 0, direction = 0,res = 0;
     		// 每个方向代表的x值和y值的相对变化
        int[][] xy = {{0,1},{1,0},{0,-1},{-1,0}};
     		// 将障碍物的坐标用set记录下来，方便判断
        Set<String> obstacleSet = new HashSet<>();
        for(int[] obstacle : obstacles){
            obstacleSet.add(obstacle[0] + "," + obstacle[1]);
        }
        for(int com : commands){
            if(com == -2){
              	// 向左转
                direction = (direction + 3) % 4;
            } else if(com == -1){
              	// 向右转
                direction = (direction + 1) % 4;
            } else{
              	// 在下一步没遇到障碍物的时候以及当前指令走的步数还没走完的时候
                while(com-- > 0 && !obstacleSet.contains(x+xy[direction][0] + "," + (y+xy[direction][1]))){
                  	// 记录每走一步xy值的变化
                    x+= xy[direction][0];
                    y+= xy[direction][1];
                }
                res = Math.max(res,x*x+y*y);
            }
        }
        return res;
    }
```

4、[跳跃游戏](https://leetcode-cn.com/problems/jump-game/)这道题的主要思想是：取一个标识minStep，作为一个数能到达终点的最小步数，初始值为1
（1）、从后面往前推，如果要到达最后一个位置，倒数第二个数的最小步数应该要大于等于minStep；
（2）、如果倒数第二个数小于minStep，那么说明倒数第二个数无法到达终点，倒数第三个数就必须大于等于minStep+1，才能跳到终点
（3）、如果倒数第二个数大于等于minStep，那么就以倒数第二个为终点，重置minStep为1，判断倒数第三个数是否大于等于minStep；
（4）、如果遍历到最后，minStep等于1，那么就代表能到达终点，否则就意味着第一个数无法满足到达终点的最小步数。
代码如下：

```java
public boolean canJump(int[] nums) {
    if(nums.length < 2) return true;
    //初始化minStep
    int minStep = 1;
    //从倒数第二的数开始判断
    for (int i = nums.length - 2; i >= 0 ; i--) {
        if(nums[i] >= minStep){
            // 如果当前数大于等于minStep，说明能到达后面一个数，重置minStep为1
            minStep = 1;
        } else {
            // 否则说明当前数无法直接到达后一个数，minStep加1，判断前面一个数能否到达当前数的后一位
            minStep++;
        }
    }
    // minStep等于1说明前面的数能跳到终点
    return minStep == 1;
}
```

5、[跳跃游戏 II](https://leetcode-cn.com/problems/jump-game-ii/)这道题的主要思想是每次跳跃完之后所到达的位置y，与跳跃之前的位置x之间的那些位置(y-x)，它们所能跳跃的最远的位置z，就是下一次跳跃的起跳位置，代码如下：

```java
    public int jump(int[] nums) {
      	// end表示当前一次跳跃结束的位置，maxPosition表示当前能跳到的最远位置，now表示当前的位置
        int end = 0, maxPosition = 0, now = 0,res = 0;
      	// 最后一个位置不用跳了
        while(end < nums.length - 1){
          	// now + nums[now]表示当前位置能跳到的最远位置
            maxPosition = Math.max(maxPosition,now+nums[now]);
          	// 遍历到一次跳跃结束的位置，开始下一轮跳跃
            if(end == now++){
                end = maxPosition;
                res ++;
            }
        }
        return res;
    }
```

## 三、二分查找

二分查找的前提是1.目标函数的单调性（单调递增或递减）2.存在上下界 3.能够通过索引访问

即每次查找，都能将搜索访问逐渐缩小

代码的模板如下：

```java
public int binarySearch(int[] nums,int target){
  int left = 0, right = nums.length - 1;
  while(left <= right){
    int mid == (left + right) >>> 1;
    if(nums[mid] == target) return mid;
    if(nums[mid] > target){
      right = mid - 1;
    } else {
      left = mid + 1;
    }
  }
}
```

这里的模板是不能够直接套用的，需要根据不同的题目来更改判断条件的，主要需要更改的条件有：

1、left <= right和left < right

当使用left <= right的时候，意味着当循环结束了的时候，right > left，所有的索引都遍历过了还是没有找到；

当使用left < right的时候，意味着当循环结束了的时候，left == right，这里就需要判断最后一个索引对应的值是否是我们想要找的值；

2、int mid == (left + right) >>> 1 和 int mid == (left + right + 1) >>> 1（这里使用>>>逻辑右移是可以保证运算后的值不溢出）

当使用 (left + right) >>> 1的时候，表示取的是左中位数，即当数组长度为偶数的时候，取的时候偏左的索引，例如长度为2的左中位数为0

当使用(left + right + 1) >>> 1的时候，表示取的是有中位数，即当数组长度为偶数的时候，取的时候偏右的索引，例如2的右中位数为1

3、left = mid + 1 和 left = mid，right = mid - 1和 right = mid

若中位数取的是左中位数，则对应的left必须是mid+1，否则的话left的值就一直不变，就会进入死循环

若中位数取的是右中位数，则对应的right必须是mid-1，否则的话right的值就一直不变，就会进入死循环

至于为什么会出现left=mid和right=mid的情况，就是有时候我们无法判断mid的值是否在我们判断的子区间中，那么就还需要将其作为边界，继续判断

### 习题笔记

1、[x 的平方根](https://leetcode-cn.com/problems/sqrtx/)这道题的主要思想是找到一个数，它的平方是最接近target的，且结果是要向下取整的，意味着它的平方是要小于等于target的，由于我们肯定能找到这个数，所以循环判断条件可以设置为while(left <= right)，代码如下：

```java
    public int mySqrt(int x) {
      	// hi = x / 2 + 1是为了照顾x <= 1的情况，且由于x的平方根都是小于等于x/2的
        long lo = 0, hi = x / 2 + 1, res = 0;
        while(lo <= hi){
            long mid = (lo + hi) >>> 1;
            if(x >= mid*mid){
                res = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return (int)res;
    }
```

2、[搜索二维矩阵](https://leetcode-cn.com/problems/search-a-2d-matrix/)这道题的思想是可以将二维数组转为一维数组进行遍历，一维数组的总长度为row * column，那么初始边界可以这样设置left = 0,right = row * column，而二分之后每个元素的行可以转化为mid/column，列可以转化为mid%column，代码如下：

```java
    public boolean searchMatrix(int[][] matrix, int target) {
        if(matrix == null || matrix.length == 0) return false;
        int row = matrix.length, column = matrix[0].length;
        int left = 0, right = row * column - 1;
        while(left <= right){
            int mid = (left + right) >>> 1;
            int value = matrix[mid/column][mid%column];
            if(target == value) return true;
            if(target < value){
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return false;
    }
```

3、[搜索旋转排序数组](https://leetcode-cn.com/problems/search-in-rotated-sorted-array/)这道题的思想是旋转数组的旋转点，左边是单调递增的，右边也是单调递增的，且右边的所有值都小于旋转点最左边的值。

（1）首先需要判断搜索的值target是否大于等于最左边的值，是则应该在旋转点的的左边进行查找，否则应该在旋转点的右边进行查找

（2）其次要判断mid是在旋转点的左边还是右边，如果target在旋转点的左边，而mid在旋转点的右边，为了方便判断区间从哪边缩小，可以将mid设置为Integer.MAX_VALUE，模拟单调递增；同理如果target在旋转点的右边，而mid在旋转点的左边，则可以将mid设置为Integer.MIN_VALUE，模拟单调递减，代码如下：

```java
    public int search(int[] nums, int target) {
        int start = 0, end = nums.length - 1;
        while(start <= end){
          	// 取右中位数
            int mid = (start + end + 1) >>> 1;
            if(nums[mid] == target) return mid;
            if(target >= nums[0]){
                // 目标值大于等于最左边的值，说明在左半段
                // 若mid比最左边的值还小，说明mid在右半段，将mid的值置为Integer.MAX_VALUE
                if(nums[mid] < nums[0]){
                    nums[mid] = Integer.MAX_VALUE;
                }
            } else {
                // 目标值小于最左边的值，说明在右半段
                // 如果mid比最左边的值还大，说明mid在左半段，将mid的值置位Integer.MIN_VALUE
                if(nums[mid] >= nums[0]){
                    nums[mid] = Integer.MIN_VALUE;
                }
            }
            if(target > nums[mid]){
                start = mid + 1;
            } else {
                end = mid - 1;
            }

        }
        return -1;
```

4、[寻找旋转排序数组中的最小值](https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array/)这道题的思想是，要找到旋转数组的最小值。

（1）如果数组没有旋转的话，最小值就是第一个元素了；

（2）如果数组旋转了，那么旋转点右边的第一个值就是最小值

（3）所以要是mid大于等于搜索区间的最左边的值，意味着旋转点在mid的右边，left = mid + 1；

（4）要是mid 小于搜索区间的最左边的值，意味着旋转点在mid的左边，并且我们不能排除mid就是旋转点右边的第一个元素，所以right = mid，所以mid就必须取的是左中位数，代码如下：

```java
    public int findMin(int[] nums) {
        int lo = 0, hi = nums.length - 1;
        while(lo < hi){
            if(nums[lo] <= nums[hi]) break;
            int mid = (lo + hi) >>> 1;
            if(nums[mid] >= nums[lo]){
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return nums[lo];
    }
```





