学习笔记

## 一、位运算

主要记住一些指定位置的位运算，类似公式这样子：

\1. 将 x 最右边的 n 位清零：x & (~0 << n)

\2. 获取 x 的第 n 位值（0 或者 1）： (x >> n) & 1

\3. 获取 x 的第 n 位的幂值：x & (1 << n)

\4. 仅将第 n 位置为 1：x | (1 << n)

\5. 仅将第 n 位置为 0：x & (~ (1 << n))

\6. 将 x 最高位至第 n 位（含）清零：x & ((1 << n) - 1)

\7. 将x最后一位1清零：x & (x - 1)

\8. 获取x最后一位1的位置（将除最后一位1以外的所谓位置为0）：x & -x

\9. 判断奇偶：（x & 1）== 1（奇） （x & 1）== 0（偶）

### 习题笔记

1、[190. 颠倒二进制位](https://leetcode-cn.com/problems/reverse-bits/)这道题的主要思想就是将原二进制位从右往左依次获取出来，然后填充到一个新的二进制的末尾去，代码如下：

```java
public class Solution {
    // you need treat n as an unsigned value
    public int reverseBits(int n) {
        int res = 0;
        for(int i = 0; i < 32; i++){
          	// 每次结果都往左移一位，腾出位置给n取出来的二进制位
            res <<= 1;
            res |= (n >> i) & 1;
        }
        return res;
    }
}
```

2、[338. 比特位计数](https://leetcode-cn.com/problems/counting-bits/)这道题采用的是dp+位运算，定义dp[i]表示i的二进制数中1的数量，而i&(i-1)表示将i的最后一位1置为0，即i&(i-1) 将比i少一个1，得出状态方程为dp[i] = dp[i&(i-1)] + 1，代码如下：

```java
class Solution {
    public int[] countBits(int num) {
        int[] dp = new int[num+1];
        for(int i = 1; i <= num; i++){
            dp[i] = dp[i&(i-1)] + 1;
        }
        return dp;
    }
}
```

3、[51. N 皇后](https://leetcode-cn.com/problems/n-queens/)这道题之前是用set来存储填过的位置，这里可以替换为用位运算来存储，代码如下：

```java
class Solution {
    List<List<String>> res;
    public List<List<String>> solveNQueens(int n) {
        res = new ArrayList<>();
        dfs(new int[n],0,n,0,0,0);
        return res;
    }

    public void dfs(int[] queens,int row, int n,int col,int pie,int na){
        if(row == n){
            res.add(generateBoard(queens));
            return;
        }
      	// （1<<n）-1 是为了转化一个长度为n的，每位上都是1的二进制数，用于定位可以放置皇后的位置
        // 这里用于定位所有可选的位置，这里有一步取反，千万不要忽视了！
        // 上面我们用 1 表示不可选的位置，但是这里我们取反后，用1表示可选的位置
        int availablePositions = ((1<<n)-1) & ~(col|pie|na);
      	// 开始检查每个可选的位置
        while(availablePositions != 0){
          	// 定位最后一个1的位置，这个操作可以自己手写验证一下（不要忘了把负数转成补码）
            // 这个定位的意思是，生成的这个二进制数只有最后一个1还为1，其他位都变成了0
            int pos = availablePositions&(-availablePositions);
          	// 将最后一个1置为1
            availablePositions&=(availablePositions-1);
          	// 这个方法是统计一个二进制数中所有的“1”的个数，可以将pos转换为真正的位置数
            int colNum = Integer.bitCount(pos-1);
            queens[row] = colNum;
          	// 沿着这个位置向下搜索，并记录好已经填充过的列撇捺，
          	// 其中撇的话在下一行需要向左移1位，捺需要向右移1位，才能真正表示撇和捺的位置
            dfs(queens,row+1,n,col|pos,(pie|pos)<<1,(na|pos)>>1);
        }
    }

    public List<String> generateBoard(int[] queens){
        int n = queens.length;
        List<String> list = new ArrayList<>();
        for(int i = 0; i < n; i++){
            char[] chars = new char[n];
            Arrays.fill(chars,'.');
            chars[queens[i]] = 'Q';
            list.add(String.valueOf(chars));
        }
        return list;
    }
}
```

## 二、LRU缓存机制

LRU全称为Least Recently Used，即最近最少使用，意思是在这个缓存中，会缓存最近使用的数据，而里面最久未使用的会在缓存容量不足的时候被移除，实现方式是用一个map和一个双向链表实现，代码如下：

```java
class LRUCache {
		// 双向链表
    class DLinkNode {
        int key,value;
        DLinkNode prev,next;
        public DLinkNode(){}
        public DLinkNode(int key,int value){
            this.key = key;
            this.value = value;
        }
    }

  	// size表示当前的容量，capacity表示最大的容量
    int size,capacity;
  	// 用一个虚拟的头和尾结点，方便判断插入和删除的位置
    DLinkNode head,tail;
  	// 哈希表用来存储key对应的结点
    Map<Integer,DLinkNode> cacheMap;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        size = 0;
        cacheMap = new HashMap<>();
        head = new DLinkNode();
        tail = new DLinkNode();
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
      	// 获取key在map里面对应的结点
        DLinkNode node = cacheMap.get(key);
 				// 结点不存在直接返回-1
        if(node == null) return -1;
      	// 结点存在则将结点移动到头部来
        moveToHead(node);
      	// 返回结点值
        return node.value;
    }
    
    public void put(int key, int value) {
        DLinkNode node = cacheMap.get(key);
        if(node == null){
          	// 结点不存在则创建一个结点，并将其添加到map中，且添加到头部去
            node = new DLinkNode(key,value);
            cacheMap.put(key,node);
            addToHead(node);
          	// 若添加新结点后超出容量，则将尾结点移除，并从map中移除
            if(++size > capacity){
                DLinkNode removeNode = removeTail();
                cacheMap.remove(removeNode.key);
                size--;
            }
        } else {
          	// 结点存在直接更新结点值，并将结点移动到头部去
            node.value = value;
            moveToHead(node);
        }
    }

    public void addToHead(DLinkNode node){
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }

    public void removeNode(DLinkNode node){
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }

    public void moveToHead(DLinkNode node){
        removeNode(node);
        addToHead(node);
    }

    public DLinkNode removeTail(){
        DLinkNode node = tail.prev;
        removeNode(node);
        return node;
    }
}

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
```

## 三、排序

1、比较类排序：通过比较来决定元素间的相对次序，由于其时间复杂度不能突破O(nlogn)，因此也称为非线性时间比较类排序。

**初级排序（O(n^2)）：**

（1）选择排序：在未排序序列中找到最小（大）元素，存放到排序序列的起始位置，然后，再从剩余未排序元素中继续寻找最小（大）元素，然后放到已排序序列的末尾。以此类推，直到所有元素均排序完毕。

```java
		public void selectSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++) {
                min = a[min] < a[j] ? min : j;
            }
            swap(a, i, min);
        }
    }
    public void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
```

（2）插入排序：通过构建有序序列，对于未排序数据，在已排序序列中从后向前扫描，找到相应位置并插入。

```java
    public void insertSort(int[] a) {
        for (int i = 1; i < a.length; i++) {
            int num = a[i];
            int j = i - 1;
            while (j >= 0 && num < a[j]) {
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = num;
        }
    }
```

（3）冒泡排序：重复地走访过要排序的数列，一次比较两个元素，如果它们的顺序错误就把它们交换过来，这样每次遍历都会将最大/最小的元素浮到顶端

```java
    public void bubbleSort(int[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = 0; j < a.length - i - 1; j++) {
                if (a[j] > a[j + 1]) swap(a, j, j + 1);
            }
        }
    }
    public void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
```

**高级排序（N*LogN）:**

（1）快速排序：通过一趟排序将待排记录分隔成独立的两部分，其中一部分记录的关键字均比另一部分的关键字小，则可分别对这两部分记录继续进行排序，以达到整个序列有序。

```java
   public void quickSort(int[] array, int begin, int end) {
        if (end <= begin) return;
        int pivot = partition(array, begin, end);
        quickSort(array, begin, pivot - 1);
        quickSort(array, pivot + 1, end);
    }

    public int partition(int[] a, int begin, int end) {
        int pivot = end, counter = begin;
        for (int i = begin; i < end; i++) {
            if (a[i] < a[pivot]) {
                swap(a, i, counter);
                counter++;
            }
        }
        swap(a, pivot, counter);
        return counter;
    }

    public void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
```

（2）归并排序：将已有序的子序列合并，得到完全有序的序列；即先使每个子序列有序，再使子序列段间有序。若将两个有序表合并成一个有序表，称为2-路归并

```java
    public void mergeSort(int[] a) {
        mergeSort(a, 0, a.length - 1);
    }

    public void mergeSort(int[] a, int left, int right) {
        if (right <= left) return;
        int mid = (left + right) >>> 1;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        int i = left, j = mid + 1, k = 0;
        int[] tmp = new int[right - left + 1];
        while (i <= mid && j <= right) tmp[k++] = a[i] < a[j] ? a[i++] : a[j++];
        while (i <= mid) tmp[k++] = a[i++];
        while (j <= right) tmp[k++] = a[j++];
        System.arraycopy(tmp, 0, a, left, tmp.length);
    }
```

（3）堆排序：将数组元素依次建立小顶堆，然后依次取堆顶元素，并删除

```java
    public void heapSort(int[] a) {
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        for (int i = 0; i < a.length; i++) {
            priorityQueue.add(a[i]);
        }
        for (int i = 0; i < a.length; i++) {
            a[i] = priorityQueue.poll();
        }
    }
```



2、非比较类排序：不通过比较来决定元素间的相对次序，它可以突破基于比较排序的时间下界，以线性时间运行，因此也称为线性时间非比较类排序。

（1）计数排序：计数排序要求输入的数据必须是有确定范围的整数。将输入的数据值转化为键存储在额外开辟的数组空间中；然后依次把计数大于 1 的填充回原数组

（2）桶排序：假设输入数据服从均匀分布，将数据分到有限数量的桶里，每个桶再分别排序（有可能再使用别的排序算法或是以递归方式继续使用桶排序进行排）。

（3）基数排序：按照低位先排序，然后收集；再按照高位排序，然后再收集；依次类推，直到最高位。有时候有些属性是有优先级顺序的，先按低优先级排序，再按高优先级排序。

### 习题笔记

1、[1122. 数组的相对排序](https://leetcode-cn.com/problems/relative-sort-array/)这道题的主要思想就是用计数排序，先开辟一个新数组tmp，先将arr1中的元素转化为key，出现的次数转化为value填充进tmp中，再根据arr2中的顺序从tmp取出填回arr1，最后将arr2没有的数也依次填回arr1中：

```java
class Solution {
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
      	// 题目表示arr1和arr2最大长度不会大于1000，所以开辟的新数组长度为1001即可
        int[] tmp = new int[1001];
        int pos = 0;
      	// 记录arr1中的数字出现的次数
        for(int num : arr1){
            tmp[num]++;
        }
      	// 根据arr2中的数字顺序，从tmp中取出对应的所有数字，填回arr1
        for(int num : arr2){
            while(tmp[num]-- > 0){
                arr1[pos++] = num;
            }
        }
      	// 将剩余没有出现在arr2中的数字也依次取出填回arr1
        for(int i = 0; i < tmp.length; i++){
            while(tmp[i]-- > 0){
                arr1[pos++] = i;
            }
        }
        return arr1;
    }
}
```

2、[242. 有效的字母异位词](https://leetcode-cn.com/problems/valid-anagram/)这道题也是一样用计数排序的思想，开辟一个新数组tmp用于记录s中的每个字符出现的次数，再遍历t，将t中出现的字符在tmp中减去对应的次数，最后判断tmp中所有字符所代表的位置的值是否都为0，但凡有一个不为0则返回false，否则返回true：

```java
class Solution {
    public boolean isAnagram(String s, String t) {
        if(s == null | t == null || s.length() != t.length()) return false;
        int[] tmp = new int[26];
        for(int i = 0; i < s.length(); i++){
            tmp[s.charAt(i)-'a']++;
            tmp[t.charAt(i)-'a']--;
        }
        for(int i = 0; i < tmp.length; i++){
            if(tmp[i] != 0) return false;
        }
        return true;
    }
}
```

3、[56. 合并区间](https://leetcode-cn.com/problems/merge-intervals/)这道题的思想是先将集合中的区间，按照左边界进行排序，然后依次判断当前区间与结果集最后一个区间是否重叠，即当前区间的右边界是否大于结果集最后一个区间的左边界，是则代表重叠，则合并，修改结果集最后一个区间的右边界为两个区间右边界的最大值，否则不合并，直接添加到结果集中去：

```java
class Solution {
    public int[][] merge(int[][] intervals) {
        if(intervals == null || intervals.length == 0) return new int[0][2];
        List<int[]> res = new ArrayList<>();
        Arrays.sort(intervals,new Comparator<int[]>(){
            public int compare(int[] o1, int[] o2){
                return o1[0] - o2[0];
            }
        });
        for(int i = 0; i < intervals.length; i++){
            int left = intervals[i][0], right = intervals[i][1];
            if(res.isEmpty() || res.get(res.size()-1)[1] < left){
                res.add(new int[]{left,right});
            } else {
                int[] lastArr = res.get(res.size()-1);
                lastArr[1] = Math.max(right,lastArr[1]);
            }
        }
        return res.toArray(new int[res.size()][0]);
    }
}
```

4、[493. 翻转对](https://leetcode-cn.com/problems/reverse-pairs/)这道题的思想是：利用归并排序的思想，假设对于数组 nums[l..r] 而言，我们已经分别求出了子数组 nums[l..m]与nums[m+1..r] 的翻转对数目，并已将两个子数组分别排好序，则 nums[l..r] 中的翻转对数目，就等于两个子数组的翻转对数目之和，加上左右端点分别位于两个子数组的翻转对数目。

```java
class Solution {
    public int reversePairs(int[] nums) {
        if(nums == null || nums.length < 2) return 0;
        return mergeSort(nums,0,nums.length -1);
    }
		
    public int mergeSort(int[] nums, int left,int right){
        if(left >= right) return 0;
        int mid = (left + right) >> 1;
      	// 计算子区间的翻转对数量
        int count = mergeSort(nums,left,mid) + mergeSort(nums,mid+1,right);
      	// 合并子区间排序结果，并统计当前区间的翻转对数量
      	// 此时nums[left,mid]和nums[mid+1,right]已经都是有序的了
      	// i，j分别代表正常归并排序的两个子区间左边界，k是合并结果的index
      	// p是用于记录翻转对的位置
        int i = left, p = left, k = 0;
        int[] tmp = new int[right - left + 1];
        for(int j = mid+1;j <= right; j++){
          	// 记录对于nums[j]，翻转对开始的位置
            while(p <= mid && nums[p] <= 2*(long)nums[j]) p++;
          	// 将nums[i]小于等于nums[j]的先填充到tmp中去
            while(i <= mid && nums[i] <= nums[j]) tmp[k++] = nums[i++];
          	// 轮到nums[j]最小了，填充nums[j]到tmp
            tmp[k++] = nums[j];
          	// 统计对于nums[j]，翻转对的数量，并将结果累加到count中
            count += mid - p + 1;
        }
      	// 继续合并未合并完的数组
        while(i <= mid) tmp[k++] = nums[i++];
      	// 将合并之后的有序数组tmp复制回nums中
        System.arraycopy(tmp,0,nums,left,tmp.length);
        return count;
    }
}
```

