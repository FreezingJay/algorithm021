学习笔记

1，解题的时候要注意好边界问题

2，用自己的思路解题后，需要看看是否有多余无效的步骤可以省略掉，例如『加一』这道题，末位加一没有进位就可以直接返回了，不用再去遍历前面的位数了

3，要关注输入的值是否会使数组溢出，例如『旋转数组』这道题的k就会大于数组的长度

4、对于不熟悉的系统函数，可以用自己的方式去实现，避免使用出错，还难以找出原因，例如『合并两个有序数组』，题解中使用了System.arraycopy()的函数来合并剩下的数组，由于对参数位置弄错了，导致结果一直不对，最后还是用for循环来解决了这个问题

5、链表的操作一般都要新建一个节点来记录头结点

## Queue源码分析

Queue是一个接口，继承自Collection，包含的Collection的所有方法，以及以下额外的6个方法，从中可以看出对队列的操作基本上包括有三种：添加、移除、访问，而每种操作失败后又有两种返回的方式，一种是直接返回false或者null，一种是直接抛出异常，具体使用哪一种，则需要看具体使用的场景

### 1 boolean add(E e);

若队列的容量还没满，则添加一个元素到队列中，并返回true；
若队列的容量已经满了，则抛出一个异常IllegalStateException。

### 2 boolean offer(E e);

若队列的容量还没满，则添加一个元素到队列中，并返回true；
若队列的容量已经满了，则不添加元素到队列中，并返回false。

### 3 E remove();

若队列不空，则将队列的头返回，并从队列中移除；
若队列为空，则抛出一个异常NoSuchElementException。

### 4 E poll();

若队列不空，则将队列的头返回，并从队列中移除；
若队列为空，则返回null。

### 5 E element();

若队列不空，将队列的头返回，但不从队列中移除；
若队列为空，则抛出一个异常NoSuchElementException。

### 6 E peek();

若队列不空，将队列的头返回，但不从队列中移除；
若队列为空，则返回null。


## PriorityQueue源码分析

继承自AbstractQueue类，而AbstractQueue类实现了Queue的接口，其中的元素是有顺序的；
如果传入的类型是基本类型的话，则默认是按照升序排列的；
如果传入的类型是自定义类型的话，则需要自定义排序器；
默认的容量是11，也可以自己传入容量，但最小容量为1，否则就会抛出IllegalArgumentException异常；
内部主要是维护一个数组来存放传入的数据。

主要的方法有以下几个：

peek()//返回队首元素
poll()//返回队首元素，队首元素出队列
add()//添加元素
size()//返回队列元素个数
isEmpty()//判断队列是否为空，为空返回true,不空返回false

主要需要分析的是添加元素和移除元素对队列的排序操作

### 1 boolean add(E e) （同 boolean add(E e)）;

往队列中添加元素，并将元素按照排序规则插入到对应的位置；
```
    public boolean offer(E e) {
        if (e == null)
            throw new NullPointerException();
        modCount++;
        int i = size;
        if (i >= queue.length)
            grow(i + 1);
        size = i + 1;
        if (i == 0)
            queue[0] = e;
        else
            siftUp(i, e);
        return true;
    }
```
1）if和else，分别执行对象判空和容量判断
2）执行siftUp(i, e)，i是原有队列长度，e是要入队的元素。

siftUp是堆中调整元素位置的一种方法，可以看出这里的优先队列是使用最大/最小堆实现的。接着看siftUp：
```
    private void siftUp(int k, E x) {
        if (comparator != null)
            siftUpUsingComparator(k, x);
        else
            siftUpComparable(k, x);
    }
```
看看使用了comparator的方法，k是原有队列长度，x是入队元素，queue是队列，comparator是比较器：
```
    private void siftUpUsingComparator(int k, E x) {
        while (k > 0) {
            int parent = (k - 1) >>> 1;
            Object e = queue[parent];
            if (comparator.compare(x, (E) e) >= 0)
                break;
            queue[k] = e;
            k = parent;
        }
        queue[k] = x;
    }
```
1）k>0，队列长度大于0
2）parent = k - 1 >>> 1; 即(k-1)/2，表示最后一个非叶子节点的位置
3）e为父节点，x是入队元素，x可以看做放在最后一个位置。如果compare(x, e) < 0，则执行元素往上走的方法。
注：在siftUp中，如果是最小堆，那么应该是较小的元素往上走，如果是最大堆，则应该是较大的元素往上走。

由于源码中新入队元素x是在第1个参数的位置，因此最大/最小优先队列主要根据第1个参数的大小关系来判断。
```
//对于最大堆，当x>e时，让x上升，则 x>e时返回负数，即
int compare(Integer x, Integer e){
 return x > e ? -1 : 1;
}
//对于最小堆，当x<e时，让compare(x, e) < 0，即
int compare(Integer x, Integer e){
 return x < e ? -1 : 1; // return x.compareTo(e);
}
```
结论：
```
// 最小优先队列，直接 return o1.compareTo(o2);
PriorityQueue<Integer> queue = new PriorityQueue<Integer>(new Comparator<Integer>(){
 @Override
 public int compare(Integer o1, Integer o2){
 return o1 < o2 ? -1 : 1;
 /* e.g., return o1.compare(o2); */
 }
});
// 最大优先队列，则反过来 return o2.compareTo(o1);
```

### 2 E poll()

将队首元素返回，并从队列中移除

```
    public E poll() {
        if (size == 0)
            return null;
        int s = --size;
        modCount++;
        E result = (E) queue[0];
        E x = (E) queue[s];
        queue[s] = null;
        if (s != 0)
            siftDown(0, x);
        return result;
    }
```
将队列长度-1，返回队首元素，并将元素从队列中移除，将剩下的元素重新进行排序
```
    private void siftDown(int k, E x) {
        if (comparator != null)
            siftDownUsingComparator(k, x);
        else
            siftDownComparable(k, x);
    }
    private void siftDownUsingComparator(int k, E x) {
        int half = size >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            Object c = queue[child];
            int right = child + 1;
            if (right < size &&
                comparator.compare((E) c, (E) queue[right]) > 0)
                c = queue[child = right];
            if (comparator.compare(x, (E) c) <= 0)
                break;
            queue[k] = c;
            k = child;
        }
        queue[k] = x;
    }
```

