学习笔记

## 一、字典树

### 理论笔记

1、基本结构

字典树即Trie树，又称单词查找树或键树，是一种树形结构，主要应用于统计和排序大量的字符串，能最大限度地减少无谓的字符串比较，查询效率比哈希表高

2、基本性质

（1）结点本身不存完整的单词；

（2）从根节点到某一节点，路径上经过的字符连接起来就是该结点对应的字符串；

（3）每个节点的所有子结点路径代表的字符都不相同

（4）一次构建，多次查询

3、核心思想

空间换时间，利用字符串的公共前缀来降低查询时间的开销

4、代码模板

```java
public class Trie {
  
  	class TrieNode {
    	public boolean isEnd = false;
    	public TrieNode[] next = new TrieNode[26];
		}
  
    private TrieNode root;

    /**
     * 初始化
     */
    public Trie() {
        root = new TrieNode();
    }

    /**
     * 插入字符串，构建字典树
     */
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.next[c - 'a'] == null) {
                node.next[c - 'a'] = new TrieNode();
            }
            node = node.next[c - 'a'];
        }
        node.isEnd = true;
    }

    /**
     * 判断字符串是否在该字典树中
     */
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.next[c - 'a'] == null) return false;
            node = node.next[c - 'a'];
        }
        return node.isEnd;
    }

    /**
     * 判断前缀是否在该字典树中
     */
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (node.next[c - 'a'] == null) return false;
            node = node.next[c - 'a'];
        }
        return true;
    }
}

```

### 习题笔记

1、[212. 单词搜索 II](https://leetcode-cn.com/problems/word-search-ii/)这道题的思想是字典树+DFS，先用单词列表构建字典树，在DFS中向上下左右四个方向扩散遍历，判断遍历的结果是否在字典树中，代码如下：

```java
class Solution {
    Set<String> res;
    TrieNode root;
    int[] dx = {0, 0, -1, 1};
    int[] dy = {-1, 1, 0, 0};

    public List<String> findWords(char[][] board, String[] words) {
        int m = board.length, n = board[0].length;
        res = new HashSet<>();
        root = new TrieNode();
      	// 构建字典树
        for(String word : words){
            insert(word);
        }
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
              	//遍历board中的字符，若该字符是单词表中某个单词的第一个字符，进入DFS
                if(root.next[board[i][j] - 'a'] == null) continue;
                dfs(board,root,new boolean[m][n],i,j,new StringBuilder());
            }
        }
        return new ArrayList<>(res);
    }

    public void dfs(char[][] board, TrieNode node,boolean[][] visited,int r,int c,StringBuilder sb){
        sb.append(board[r][c]);
        visited[r][c] = true;
        node = node.next[board[r][c] - 'a'];
      	// 该结点是单词的结束点，记录该单词到结果集
        if(node.isEnd) res.add(sb.toString());
      	// 向四个方向扩散查找下一个字符
        for(int i = 0; i < dx.length; i++){
            int x = r+dx[i], y = c + dy[i];
   					// 超过边界或者该字符访问过了或者该字符不在后面的字典树中
            if(!inBoard(board,x,y) || visited[x][y] || node.next[board[x][y] - 'a'] == null) continue;
            dfs(board,node,visited,x,y,sb);
        }
      	// 回溯
        sb.deleteCharAt(sb.length() - 1);
        visited[r][c] = false;
    }

    public boolean inBoard(char[][] board, int r, int c){
        return 0 <= r && r < board.length && 0 <= c && c < board[0].length;
    }

    public void insert(String word){
        if(root == null) return;
        TrieNode node = root;
        for(char c : word.toCharArray()){
            if(node.next[c-'a'] == null){
                node.next[c-'a'] = new TrieNode();
            }
            node = node.next[c-'a'];
        }
        node.isEnd =true;
    }

    public class TrieNode {
        public boolean isEnd = false;
        public TrieNode[] next = new TrieNode[26];
    }
}
```

## 二、并查集

构建一个数据结构，用于解决组团配对问题，属于同一个团体的，他们的代表是同一个，初始创建的时候，每个人都是自己的代表，直到合并后拥有共同的代表，主要的操作有：

**makeSet(s)**：建立一个并查集，其中包含s个单元素集合

**unionSet(x,y)**：若x，y不在同一个集合，则把元素x和元素y所在的集合合并

**find(x)**：找到元素x所在集合的代表，可用于判断两个元素是否在同一个集合中，并且在查找的过程中可以进行路径压缩，即直接将该元素的上级直接改为集合的代表，减少下一次的判断次数。

代码模板如下：

```java
public class UnionFind {
  	// 集合个数
    private int count;
  	// 用数组来表示集合的关系，下标表示元素，值表示元素的上级
    private int[] parent;

  	// 初始化
    public UnionFind(int n) {
        count = n;
        parent = new int[n];
      	// 每个元素初始都是自己的代表
        for (int i = 0; i < n; i++) parent[i] = i;
    }
		
  	// 寻找元素所在集合的代表
    public int find(int p){
      	// 若该元素不是自己的代表，则找到其代表
        while(p != parent[p]){
          	// 路径压缩，将元素的上级指向上级的上级，直到上级为该集合的代表
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }
		// 合并两个元素
    public void union(int p, int q){
        int rootP = find(p);
        int rootQ = find(q);
      	// 两个元素的集合代表相同则不操作
        if(rootP == rootQ) return;
      	// 将一个元素的上级改为另一个元素，实现合并
        parent[rootP] = rootQ;
      	// 减少当前并查集的集合数
        count--;
    }
		// 返回该并查集的集合数
    public int getCount() {
        return count;
    }
}
```

### 习题笔记

1、[547. 省份数量](https://leetcode-cn.com/problems/number-of-provinces/)这道题的主要思想就是利用并查集，将相连的两个省份合并到一个集合中去，最后得出并查集的个数就是题解，代码如下：

```java
class Solution {
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        UnionFind uf = new UnionFind(n);
        for(int i = 0; i < n; i++){
            for(int j = i+1; j < n; j++){
                if(isConnected[i][j] == 1) uf.union(i,j);
            }
        }
        return uf.getCount();
    }
}

class UnionFind{
    private int count;
    private int[] parent;

    public UnionFind(int n){
        count = n;
        parent = new int[n];
        for(int i = 0; i < n; i++){
            parent[i] = i;
        }
    }

    public int find(int p){
        while(p != parent[p]){
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public void union(int p, int q){
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP == rootQ) return;
        parent[rootP] = rootQ;
        count --;
    }

    public int getCount(){
        return count;
    }
}
```

2、[200. 岛屿数量](https://leetcode-cn.com/problems/number-of-islands/)这道题用并查集的方式来解决，就是将二维数组转化为一个并查集，然后将相邻的陆地1合并到一个集合中，并计算水的数量，最后用并查集的集合数减去水数，就是岛屿的数量了。这里二维数组转化为并查集一维数组之后，二维数组的位置（i，j）对应的一维数组的位置就是n * i + j，其中n是二维数组的列数，代码如下：

```java
class Solution {
    public int numIslands(char[][] grid) {
        int m = grid.length, n = grid[0].length;
        UnionFind uf = new UnionFind(m*n);
        int spaces = 0;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(grid[i][j] == '1'){
                  	// 当前位置为岛屿的话，判断相邻的右边和下边是否也是岛屿，是则合并
                    int currentIndex = getIndex(n,i,j);
                    if(i + 1 < m && grid[i+1][j] == '1') uf.union(currentIndex,getIndex(n,i+1,j));
                    if(j + 1 < n && grid[i][j+1] == '1') uf.union(currentIndex,getIndex(n,i,j+1));
                } else {
                  	// 当前位置为水，统计水的双
                    spaces++;
                }
            }
        }
        return uf.getCount() - spaces;
    }
		// 获取二维数组在一维数组对应的位置
    public int getIndex(int n, int i, int j){
        return n * i + j;
    }
}

class UnionFind{
    private int count;
    private int[] parent;

    public UnionFind(int n){
        count = n;
        parent = new int[n];
        for(int i = 0; i < n; i++){
            parent[i] = i;
        }
    }

    public int find(int p){
        while(p != parent[p]){
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public void union(int p, int q){
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP == rootQ) return;
        parent[rootP] = rootQ;
        count --;
    }

    public int getCount(){
        return count;
    }
}
```

3、[130. 被围绕的区域](https://leetcode-cn.com/problems/surrounded-regions/)这道题的思想跟上一道的岛屿数量相似，不过这里需要注意的，如果当前的O是与边界的O相连的，那么就不进行变换，所以我们需要判断当前的O是否与边界的O相连，用一个比较巧妙的方法就是使用一个虚拟的结点，用来合并所有与边界O相连的O，代码如下：

```java
class Solution {
    int[] dx = {-1,1,0,0};
    int[] dy = {0,0,-1,1};
    public void solve(char[][] board) {
        if(board == null || board.length == 0) return;
        int m = board.length, n = board[0].length;
      	// 这里用m*n+1，就是要用最后的m*n作为与边界O相连O的集合
        UnionFind uf = new UnionFind(m*n+1);
      	// 虚拟结点，用于记录与边界O相连的O
        int dummy = m*n;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(board[i][j] == 'O'){
                  	// 判断是否为边界
                    boolean isEdge = i == 0 || i == m-1 || j == 0 || j == n-1;
                    if(isEdge){
                      	// 初始化合并边界的O
                        uf.union(dummy,getIndex(n,i,j));
                    } else {
                      	// 合并当前位置四周的O
                        for(int k = 0; k < dx.length; k++){
                            int x = i + dx[k], y = j + dy[k];
                            if(!inBoard(m,n,x,y) || board[x][y] != 'O') continue;
                            uf.union(getIndex(n,i,j),getIndex(n,x,y));
                        }
                    }
                }
            }
        }
				// 遍历判断每个O是否与边界相连，不是则改为X
        for(int i = 0; i < m; i++){
            for(int j = 0; j < n; j++){
                if(board[i][j] == 'O' && uf.find(getIndex(n,i,j)) != uf.find(dummy)){
                    board[i][j] = 'X';
                }
            }
        }
    }

    public int getIndex(int n, int i, int j){
        return n * i + j;
    }

    public boolean inBoard(int m,int n, int i, int j){
        return 0 <= i && i < m && 0 <= j && j < n;
    }
}

public class UnionFind {
    private int count;
    private int[] parent;

    public UnionFind(int n) {
        count = n;
        parent = new int[n];
        for (int i = 0; i < n; i++) parent[i] = i;
    }

    public int find(int p){
        while(p != parent[p]){
            parent[p] = parent[parent[p]];
            p = parent[p];
        }
        return p;
    }

    public void union(int p, int q){
        int rootP = find(p);
        int rootQ = find(q);
        if(rootP == rootQ) return;
        parent[rootP] = rootQ;
        count--;
    }

    public int getCount() {
        return count;
    }
}
```

## 三、剪枝

剪枝就是在DFS中，判断哪些遍历的结果是错误的，将这些错误的分支剪去，减少遍历的次数，提高遍历效率，最常用的方式就是用回溯法，采用试错的方式，当发现现有的分步答案得不到正确的结果时，取消前面几步的计算，再通过其它分支去寻找解决的答案，在最坏的情况下，回溯法会导致复杂度为指数时间的计算。

### 习题笔记

1、[36. 有效的数独](https://leetcode-cn.com/problems/valid-sudoku/)这道题的思想就是，判断当前行、列和3x3九宫格中是否有重复的数字，有则不是有效的数独，可以采用三个二维数组分别来表示每一行、列、九宫格中的数字是否已经被使用过了，代码如下：

```java
class Solution {
    public boolean isValidSudoku(char[][] board) {
      	// 第二维用10主要是方便存1-9而已
        boolean[][] rowUsed = new boolean[9][10];
        boolean[][] colUsed = new boolean[9][10];
        boolean[][] boxUsed = new boolean[9][10];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == '.') continue;
                int num = board[i][j] - '0';
              	// 九宫格的位置计算
                int boxIndex = i / 3 * 3 + j / 3;
                if(rowUsed[i][num] || colUsed[j][num] || boxUsed[boxIndex][num]) return false;
                rowUsed[i][num] = true;
                colUsed[j][num] = true;
                boxUsed[boxIndex][num] = true;
            }
        }
        return true;
    }
}
```

2、[37. 解数独](https://leetcode-cn.com/problems/sudoku-solver/)这道题是上一道题的加强版，这里是需要将最终的结果给解出来，所以就需要用到DFS来遍历每一个格子，然后尝试填入每一个合法的数字，如果最终不能得出结果，那就要回溯前面填入的数字，重新填入其它合法的数字，代码如下：

```java
class Solution {
    public void solveSudoku(char[][] board) {
        boolean[][] rowUsed = new boolean[9][10];
        boolean[][] colUsed = new boolean[9][10];
        boolean[][] boxUsed = new boolean[9][10];
      	// 初始化三个数组的值，有填入数字的就将对应位置的值设为true
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                if(board[i][j] == '.') continue;
                int num = board[i][j] - '0';
                int boxIndex = i / 3 * 3 + j / 3;
                rowUsed[i][num] = true;
                colUsed[j][num] = true;
                boxUsed[boxIndex][num] = true;
            }
        }
      	// 开始尝试解决
        dfs(board,rowUsed,colUsed,boxUsed,0,0);
    }

    public boolean dfs(char[][] board, boolean[][] rowUsed, boolean[][] colUsed, boolean[][] boxUsed, int row, int col){
        if(col == board[0].length){
          	// 如果一列填完了，那么进行下一行从头开始填
            col = 0;
          	// 如果最后一行都填完了，那就代表找到结果叻
            if(++row == board.length) return true;
        }
      	// 当前位置已经有数字了，直接进入下一个位置
        if(board[row][col] != '.') return dfs(board,rowUsed,colUsed,boxUsed,row,col+1);
        for(int i = 1; i <= 9; i++){
            int boxIndex = row / 3 * 3 + col / 3;
          	// 判断当前数字是否可以填入当前位置
            boolean canUse = !rowUsed[row][i] && !colUsed[col][i] && !boxUsed[boxIndex][i];
            if(canUse){
              	// 尝试填入数字
                board[row][col] = (char)(i + '0');
                rowUsed[row][i] = true;
                colUsed[col][i] = true;
                boxUsed[boxIndex][i] = true;
              	// 进入下一个位置的处理，如果接下来的位置都能处理成功，代表当前位置的填入数字是正确的
                if(dfs(board,rowUsed,colUsed,boxUsed,row,col+1)) return true;
              	// 如果接下来的位置有不能处理成功的，那么回溯重置当前位置填入的数字，尝试用另外的数字去填入到当前位置
                board[row][col] = '.';
                rowUsed[row][i] = false;
                colUsed[col][i] = false;
                boxUsed[boxIndex][i] = false;
            }
        }
        return false;
    }
}
```

## 四、双向BFS

双向BFS是在BFS的基础上，在从起点开始往后遍历的时候，同时也尝试从终点开始往前遍历，若二者最终能够相遇，能遍历到相同的结点，则遍历结束，用这种方式可以减少扩散的结点数，提高遍历的效率，代码模板如下：

```java
class Solution {
    public int twoEndedBFS(String beginWord, String endWord, List<String> wordList) {
        Set<String> wordSet = new HashSet<>(wordList);
        if(!wordSet.contains(endWord)) return 0;
        Set<String> visited = new HashSet<>();
      	// 用两个set来替换Queue
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        int count = 1;
        beginSet.add(beginWord);
        endSet.add(endWord);
      	// 循环遍历两个set，直到两个set中有一个为空为止
        while(!beginSet.isEmpty() && !endSet.isEmpty()){
            count++;
          	// 每次都只处理结点少的那个set，减少后面遍历的次数
            if(beginSet.size() > endSet.size()){
                Set<String> tmp = beginSet;
                beginSet = endSet;
                endSet = tmp;
            }
            Set<String> newBeginSet = new HashSet<>();
            for(String word : beginSet){
                if(changeLetter(wordSet,word,visited,newBeginSet,endSet)) return count;
            }
            beginSet = newBeginSet;
        }
        return 0;
    }

    public boolean changeLetter(Set<String> wordSet, String s, Set<String> visited, Set<String> newBeginSet, Set<String> endSet){
        char[] chars = s.toCharArray();
        for(int i = 0; i < s.length(); i++){
            char c = chars[i];
            for(char j = 'a'; j <= 'z'; j++){
                if(c == j) continue;
                chars[i] = j;
                String newWord = String.valueOf(chars);
              	// 变换的结果来endSet中，代表相遇了
                if(endSet.contains(newWord)) return true;
                if(visited.contains(newWord) || !wordSet.contains(newWord)) continue;
                newBeginSet.add(newWord);
                visited.add(newWord);
            }
            chars[i] = c;
        }
        return false;
    }
}
```

## 五、高级树

平衡二叉树是一种特殊的搜索二叉树，搜索二叉树的查找效率只与树的高度相关，而所谓平衡，即每个结点的左右子树高度差相差不大，那么意味着搜索效率就更高了。平衡二叉树是采用的平衡因子来判断左右子树的高度差，且一般在生成插入的时候就进行判断是否平衡，如果不平衡则需要通过旋转来进行平衡，主要的旋转操作有以下四种：

**左旋**：右右子树，将当前结点A的右子结点B替换当前结点，A结点作为结点B的左结点，如果B结点有左子树的话，还应将其左子树作为A结点的右子树；

**右旋**：左左子树，将当前结点A的左子结点B替换当前结点，A结点作为结点B的右结点，如果B结点有右子树的话，还应将其右子树作为A结点的左子树；

**左右旋**：左右子树，先将A结点的左子节点B和B的右子节点C进行左旋，则C变成A的左子节点，B变成C的左子节点，这时该子树就变成左左子树了，再进行右旋操作即可；

**右左旋**：右左子树，先将A结点的右子节点B和B的左子节点C进行右旋，则C变成A的右子节点，B变成C的右子节点，这时该子树就变成右右子树了，再进行左旋操作即可。

### 1、AVL树

AVL树是一种极端的平衡二叉树，它的平衡因子严格保持是-1，0，1，意味着虽然它的查找效率很高，但是在插入的时候可能需要频繁地进行旋转操作。

### 2、红黑树

红黑树是一种近似平衡的二叉搜索树，它能够确保任何一个结点的左右子树的高度差小于两倍。具体来说，红黑树是满足如下条件的二叉搜索树： 

• 每个结点要么是红色，要么是黑色

• 根结点是黑色

• 每个叶结点（NIL结点，空结点）是黑色的。 

• 不能有相邻接的两个红色结点 

• 从任一结点到其每个叶子的所有路径都包含相同数目的黑色结点。

与AVL树相比，AVL树的搜索效率更高，但是红黑树生成和插入结点的效率更高。

所以如果读操作非常多，写操作非常少的情况下，用AVL树，比如数据库等，而如果写操作非常多，读操作非常少则用红黑树比如map，set等。