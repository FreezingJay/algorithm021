学习笔记

1、map主要是要确定key和value的对应关系，set是无序的集合

2、树的面试题解法一般都是递归，主要是因为树本身的定义，子节点也是树结构，就是有重复性，符合递归的特性。

3、二叉树指的是整棵树中，每个结点都最多只有两个子节点;

4、需要熟练掌握树的前序，中序，后序，层序遍历的写法：

前序：根-左-右

```java
void preorder(TreeNode node){
  if(node != null){
    println(node.val);
    preorder(node.left);
    preorder(node.right);
  }
}

```

中序：左-根-右

```java
void inorder(TreeNode node){
  if(node != null){
  	inorder(node.left);
    println(node.val);
    inorder(node.right);
  }
}
```

后序：左-右-根

```java
void postorder(TreeNode node){
  if(node != null){
  	postorder(node.left);
    postorder(node.right);
    println(node.val);
  }
}
```

层序：从上到下，从左到右（用队列来实现，先进先出）

```java
List<List<Integer>> levelOrder(TreeNode root) {
    List<List<Integer>> res = new ArrayList<>();
    Deque<TreeNode> deque = new ArrayDeque<>();
    if(root != null) deque.add(root);
    while(!deque.isEmpty()){
        int size = deque.size();
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < size; i++){
            Node node = deque.pollFirst();
            list.add(node.val);
            if(node.left != null) deque.addLast(node.left);
            if(node.right != null) deque.addLast(node.right);
        }
        res.add(list);
    }
    return res;
}
```

5、堆是一个接口，分为大顶堆和小顶堆，即堆顶的元素始终都是最大/最小的，最常用的是二叉堆（不是最好的实现方式），获取堆顶元素的时间复杂度为O(1)，添加和删除元素的时间复杂度为O(logn)，在java中使用的是PriorityQueue来实现二叉堆，可以通过传入自定义的Comparator来实现堆的排序方式。