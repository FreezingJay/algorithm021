学习笔记

## 一、递归

这周的学习内容是递归，递归的代码去理解还是比较晦涩的，一开始总是不得其解，后来想起超哥视频里面说的，要拒绝人肉递归，只需要记住递归的模板，找到最近重复子问题，然后每一层的递归只处理当前逻辑即可，递归的模板如下：

```java
public void recur(int level, int param){
  //terminator 递归结束条件
  if(level > MAX_LEVEL){
    // process result 处理结果
    return;
  }
  
  // process current logic 处理当前层级的逻辑
  process(level,param);
  
  // drill down 下探到下一层去
  recur(level + 1, newParam);
  
  // restore current status 重置当前的状态
}
```

### 习题笔记

1、[验证二叉搜索树](https://leetcode-cn.com/problems/validate-binary-search-tree/)这道题中，可以利用二叉搜索树的特性，就是中序遍历的结果是单调递增的即可，默写出中序遍历的递归模板（左-根-右），需要满足的条件有：左子树为二叉搜索树，根值小于上一个值，右子树为二叉搜索树，代码如下：

```java
		// 这里初始化用Long.MIN_VALUE，是因为如果使用Integer.MIN_VALUE，那么如果测试用例中有结点为Integer.MIN_VALUE的话，会导致root.val <= preValue成立直接返回false
		long preValue = Long.MIN_VALUE;
    public boolean isValidBST(TreeNode root) {
        if(root == null) return true;
        if(!isValidBST(root.left)) return false;
        if(root.val <= preValue) return false;
        preValue = root.val;
        return isValidBST(root.right);
    }
```

2、[二叉树的最大深度](https://leetcode-cn.com/problems/maximum-depth-of-binary-tree/)这道题的主要思想是，对于当前结点来说，它的最大深度为左子树和右子树的深度的最大值，然后加上根节点的深度1，就是最大深度了，代码如下：

```java
    public int maxDepth(TreeNode root) {
        if(root == null) return 0;
        return Math.max(maxDepth(root.left),maxDepth(root.right)) + 1;
    }
```

3、[ 二叉树的最小深度](https://leetcode-cn.com/problems/minimum-depth-of-binary-tree/)这道题的主要思想是，对于当前结点来说，如果左子树是空的，就直接返回右子树的深度加1，如果右子树是空的，就直接返回左子树的深度加1，如果左右子树都不为空，那么就返回两棵子树深度的最小值加1，代码如下：

```java
		public int minDepth(TreeNode root) {
        if(root == null) return 0;
        if(root.left == null) return minDepth(root.right) + 1;
        if(root.right == null) return minDepth(root.left) + 1;
        return Math.min(minDepth(root.left),minDepth(root.right)) + 1;
    }
```

4、[二叉树的最近公共祖先](https://leetcode-cn.com/problems/lowest-common-ancestor-of-a-binary-tree/)这道题的主要思想是：

（1）如果根节点是p或者q，那么他们的最近公共祖先就是p或者q；

（2）如果在某个结点t的左子节点找到了p或者q，则返回该结点；

（3）如果在t的右子树中找到了q或者p，则t为结果；

（4）如果在t的右子树中没找到了q或者p，则返回t的父节点，往t的父结点的右子树继续遍历，直到找到q或者p，以此类推

（5）如果在t的左子树跟右子树都没有找到p或者q，则说明t不是他们的公共祖先，直接返回null

代码如下：

```java
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if(root == null || root == p || root == q) return root;
        TreeNode left = lowestCommonAncestor(root.left,p,q);
        TreeNode right = lowestCommonAncestor(root.right,p,q);
        if(left == null && right == null) return null;
        if(left == null) return right;
        if(right == null) return left;
        return root;
    }
```

5、[从前序与中序遍历序列构造二叉树](https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal/)这道题的主要思想是，二叉树的前序遍历是（根-左-右），中序遍历是（左-根-右）,说明取前序遍历的一个值root为根，那么在中序遍历中root左边的值就是其左子树，右边就是其右子树，以此类推，代码如下：

```java
    // 这里用map来存储前序中的值在中序里的位置，方便查找
    Map<Integer,Integer> map = new HashMap<>();
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        int n = inorder.length;
        for(int i = 0; i < n; i++){
            map.put(inorder[i],i);
        }
        return build(preorder,0,n-1,inorder,0,n-1);
    }

    private TreeNode build(int[] preorder, int preStart, int preEnd, int[] inorder, int inStart, int inEnd){
        if(preStart > preEnd) return null;
        TreeNode root = new TreeNode(preorder[preStart]);
        int inRoot = map.get(root.val);
      	// 根节点在中序里左边结点的数量
        int leftCount = inRoot - inStart;
        root.left = build(preorder,preStart + 1,preStart + leftCount,inorder,inStart,inRoot -1);
        root.right = build(preorder,preStart + leftCount + 1, preEnd, inorder, inRoot + 1, inEnd);
        return root;
    }
```

## 二、分治

分治的主要思想是想一个大问题拆分为几个小问题，小问题可以在再拆分为更小的问题，直到不能再拆分，然后解决好每一个小问题，再将其组合起来返回，代码模板如下：

```java
private static int divide_conquer(Problem problem) {
  
  if (problem == NULL) {
    // 问题不能再拆分了，返回最后处理的结果
    int res = process_last_result();
    return res;     
  }
  // 拆分子问题
  subProblems = split_problem(problem)
  
  // 下探处理子问题
  res0 = divide_conquer(subProblems[0])
  res1 = divide_conquer(subProblems[1])
  // 将处理完的子问题结果组合起来返回
  result = process_result(res0, res1);
  
  return result;
}
```

#### 习题笔记

1、[Pow(x, n)](https://leetcode-cn.com/problems/powx-n/)这道题的主要思想就是求x的n次方，拆解为求两个x的n/2次方相乘，直到n等于0，这里需要注意的问题就是：

（1）n可能为负数，那么结果可以转换为1/(x^-n)进行处理，否则二分的时候会把结果越变越大

（2）如果n为奇数，则结果需要乘多一次本身，否则直接返回两个子结果相乘的结果即可

代码如下：

```java
    public double myPow(double x, int n) {
        return n > 0 ? helper(x,n) : 1 / helper(x,-n);
    }

    private double helper(double x, int n){
        if(n == 0) return 1;
        double half = helper(x,n/2);
        return n % 2 == 0 ? half * half : half * half * x;
    }
```

2、[多数元素](https://leetcode-cn.com/problems/majority-element/)这道题有几种解法：

（1）将数组排序，取中间的数即可

（2）用哈希表存储每个数字出现的个数，然后存到优先队列（大顶堆）中，取第一个元素

（3）用分治的方法，将数组分为两个子数组，求两个子数组的众数。如果两个子数组的众数相等，即为结果；如果不相等，则判断两个众数在数组中出现的次数，出现多的为众数；如果子数组的长度为1，则该元素为众数，代码如下：

```java
		// 计算某个数在数组中出现的次数
		private int countNum(int[] nums, int num, int start, int end){
        int count = 0;
        for(int i = start ; i <= end; i ++){
            if(nums[i] == num) count++;
        }
        return count;
    }

    private int majorityElement(int[] nums,int start,int end){
      	// 子数组长度为1，直接返回该元素
        if(start == end){
            return nums[start];
        }
        // 分治判断子数组众数
        int mid = (start + end) / 2;
        int majorLeft = majorityElement(nums,start,mid);
        int majorRight = majorityElement(nums,mid+1,end);
				// 子数组众数相同，直接返回该众数
        if(majorLeft == majorRight) return majorLeft;
				// 子数组众数不同，判断各子数组众数在当前数组出现次数
        int countMajorLeft = countNum(nums,majorLeft,start,end);
        int countMajorRight = countNum(nums,majorRight,start,end);
				// 出现次数多的为真正的众数
        return countMajorLeft > countMajorRight ? majorLeft : majorRight;
    }

    public int majorityElement(int[] nums) {
        return majorityElement(nums,0,nums.length - 1);
    }
```

## 三、回溯

回溯的主要思想是试错，通过尝试分步去解决一个问题，在解决问题的时候，发现得不到最终的结果时，会取消前面几步的计算结果，然后尝试另外的分步去解决，其主要实现方式是递归，最重要的一步是在于取消前面的计算结果，算法中可以优化的地方在于如何剪枝，去除一些我们能知道的错误的分步，减少试错的次数。模板如下：

```java
public void backtrack(int level, int param){
  //terminator 递归结束条件
  if(level > MAX_LEVEL){
    // process result 处理结果
    return;
  }
  
  // process current logic 处理当前层级的逻辑
  process(level,param);
  
  // drill down 下探到下一层去
  recur(level + 1, newParam);
  
  // restore current status 重置当前的状态，重点
  restore()
}
```

#### 习题笔记

1、[电话号码的字母组合](https://leetcode-cn.com/problems/letter-combinations-of-a-phone-number/)这道题的主要思想是遍历字符串中的数字，再遍历每个数字代表的英文字母进行组合，利用暴力法的话，则有多少的数字就得嵌套多少层循环，而用回溯法的话，就是先取一个数字的代表的一个英文字母，添加到字符串中，然后下探到下一层与其它数字的所有英文字符进行组合；组合完了以后，删除该字符，取该数字的另一个英文字母，继续刚才的操作，以此类推，代码如下：

```java
		String[] letters = new String[]{"abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
    List<String> res;
    public List<String> letterCombinations(String digits) {
        res = new ArrayList<>();
        // 注意判空
        if(digits != null && digits.length() > 0){
            dfs(0,digits,new StringBuilder());
        }   
        return res;
    }

    private void dfs(int index, String digits,StringBuilder sb){
        // 最后一层，保存组装的结果，返回
        if(index >= digits.length()){
            res.add(sb.toString());
            return;
        }
        // 获取当前层级的数字
        char c = digits.charAt(index);
        // 获取当前层级数字代表的英文字母字符串
        // 由于0和1不参与，故这里是用数字的ASCII-2的ASCII即数字所代表的的字母字符串所在的位置
        String letter = letters[c - '2'];
        // 遍历当前层级数字所代表的英文字母字符串
        for(int i = 0; i < letter.length(); i++){
     				// 记录字符
            sb.append(letter.charAt(i));
            // 下探一层
            dfs(index+1,digits,sb);
            // 删除记录
            sb.deleteCharAt(sb.length() -1);
        }
    }
```

2、[N 皇后](https://leetcode-cn.com/problems/n-queens/)这道题的主要思想在于遍历每一层中每一列的位置是否合法，不会受到上一层皇后的攻击，而皇后的攻击范围是横、竖、撇、捺，故同一行只能有一个皇后，同一列只能有一个皇后，当前行当前列（row,column）的上一行的前一列（row-1,column-1），上一行的后一列（row,column+1）不能有皇后。基于这个思想，则需要有columnSet，pieSet，naSet三个集合来辅助记录被占用的列，撇，捺，代码如下：

```java
    List<List<String>> res;
    public List<List<String>> solveNQueens(int n) {
        res = new ArrayList<>();
      	// 记录被占用的列，撇，捺
        Set<Integer> columnSet = new HashSet<>();
        Set<Integer> pieSet = new HashSet<>();
        Set<Integer> naSet = new HashSet<>();
      	// 记录可以存放N皇后的结果
        int[] queens = new int[n];
        solveNQueens(0,n,queens,columnSet,pieSet,naSet);
        return res;
    }

    private void solveNQueens(int row,int n,int[] queens,Set<Integer> columnSet,Set<Integer> pieSet, Set<Integer> naSet){
      	// 最后一行记录完了，将结果打印后放入结果集并返回
        if(row == n) {
            res.add(genarateBroad(queens,n));
            return;
        }
      	// 遍历当前行每一列的位置
        for(int i = 0; i < n; i++){
          	// 如果该列被占用了，进入下一列
            if(columnSet.contains(i)) continue;
            int pie = row + i;
          	// 当前位置的撇已经被占用了，进入下一列
            if(pieSet.contains(pie)) continue;
            int na = row - i;
          	// 当前位置的捺已经被占用了，进入下一列
            if(naSet.contains(na)) continue;
          	// 当前位置可以存放皇后，保存记录
            queens[row] = i;
            columnSet.add(i);
            pieSet.add(pie);
            naSet.add(na);
          	// 下探到下一行进行记录
            solveNQueens(row + 1, n , queens,columnSet,pieSet,naSet);
          	// 清除当前列的记录，以便判断其它未判断的列是否符合记录
            queens[row] = -1;
            columnSet.remove(i);
            pieSet.remove(pie);
            naSet.remove(na);
        }
    }
		// 打印结果
    private List<String> genarateBroad(int[] queens, int n){
        List<String> list = new ArrayList<>();
        for(int i = 0; i < n; i++){
            char[] chars = new char[n];
            Arrays.fill(chars,'.');
            chars[queens[i]] = 'Q';
            list.add(new String(chars));
        }
        return list;
    }
```

3、[组合](https://leetcode-cn.com/problems/combinations/)这道题的主要思想是记录n个不同的数中，k个数组合在一起的种类，重点在于数是不同的，即第一个数只需与后面n-1个数进行组合，组合完了再与后面n-2的数进行组合，总的需要组合k层，代码如下：

```java
    List<List<Integer>> res;
    public List<List<Integer>> combine(int n, int k) {
        res = new ArrayList<>();
      	// 注意判断边界
        if(n < 1 || k < 1||n < k) return res;
        combine(1,n,k,new ArrayDeque<Integer>());
        return res;
    }

		// index表示当前层级
		// n表示整数的数量
		// k表示当前层需要组合的个数
		// deque这里采用队列方便添加删除
    private void combine(int index, int n, int k,Deque<Integer> deque){
      	// 最后一层已经组合完毕，将结果记录起来
        if(k == 0){
          	// 切记这里需要重新new一个ArrayList,否则回溯会将已经添加的结果一并修改
            res.add(new ArrayList<>(deque));
            return;
        }
      	// 每一层遍历的起始值都是从当前层开始，遍历到n-k+1，即后面的（k-1个）不用遍历到，因为后面没有数再与他们组合了
        for(int i = index; i <= n - k + 1; i++){
            // 添加记录到队列尾
            deque.addLast(i);
          	// 下探到下一层
            combine(i + 1, n, k - 1,deque);
          	// 回溯，删除队尾记录
            deque.pollLast();
        }
    }
```

4、[全排列](https://leetcode-cn.com/problems/permutations/)这道题的主要思想是，数字没有重复，每个数字可以放在每个位置上，那么就是说每一个位置都遍历数字列表，用一个数组来记录某个数字有没有被使用了，即放到了位置上，没有被使用的话就把它放到位置上去，已经被使用了就判断下一个数字，以此类推，代码如下：

```java
    List<List<Integer>> res;
    public List<List<Integer>> permute(int[] nums) {
        res = new ArrayList<>();
        dfs(0,nums,new boolean[nums.length],new ArrayDeque<Integer>());
        return res;
    }

    private void dfs(int index, int[] nums,boolean[] used, Deque<Integer> deque){
      	// 最后一层已经处理完毕，添加结果到结果集
        if(index == nums.length) {
            res.add(new ArrayList<>(deque));
            return;
        }
      	// 每一层都要遍历所有数字
        for(int i = 0; i < nums.length; i++){
          	// 数字已经被使用过了，判断下一个数字
            if(used[i]) continue;
          	// 数字没有被使用过，添加到队尾
            deque.addLast(nums[i]);
          	// 记录数字已经被使用了
            used[i] = true;
          	// 下探到下一层，即下一个位置
            dfs(index+1,nums,used,deque);
          	// 回溯，取消数字已经被使用的状态
            used[i] = false;
          	// 删除队尾的数字
            deque.pollLast();
        }
    }
```

5、[全排列 II](https://leetcode-cn.com/problems/permutations-ii/)这道题与上一道题的区别在于，这里的数字是有重复的，所以这里就需要进行剪枝了，而剪枝的重点就在于排序，将数组进行排序后，方便我们判断当前数字是否与上一个数字重复。如果重复了且上一个数字还没被使用（回溯原因），那么就要跳过该数字，不然就会产生重复记录，代码如下：

```java
    List<List<Integer>> res;
    public List<List<Integer>> permuteUnique(int[] nums) {
        res = new ArrayList<>();
      	// 切记要排序
        Arrays.sort(nums);
        dfs(0,nums,new boolean[nums.length],new ArrayDeque<Integer>());
        return res;
    }

    private void dfs(int index, int[] nums, boolean[] used,Deque<Integer> deque){
        if(index == nums.length){
            res.add(new ArrayList<>(deque));
            return;
        }
        for(int i = 0; i < nums.length; i++){
          	// 当前数字是否与上一个数字重复且上一个数字还没被使用
            if(used[i] || (i > 0 && nums[i - 1] == nums[i] && !used[i-1])) continue;
            used[i] = true;
            deque.addLast(nums[i]);
            dfs(index+1,nums,used,deque);
            used[i] = false;
            deque.pollLast();
        }
    }
```

## 四、总结

递归这种类型的题目，还是有规矩可循的，首先还是要熟练递归的模板，分治和回溯也都是基于递归的，然后再去找最近重复子问题，只关心当前层级的逻辑，其它层级的逻辑递归自然会帮你解决，切忌人肉递归，回溯的话就要注意清除当前层记录的状态，以及进行适当的剪枝。其余的，就是刷遍数了，遍数多了自然通了。

