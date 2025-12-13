# 类加载

```text
ava中的类加载器负载加载来自文件系统、网络或者其他来源的类文件。jvm的类加载器默认使用的是双亲委派模式。
三种默认的类加载器Bootstrap ClassLoader、Extension ClassLoader和System ClassLoader（Application ClassLoader）
每一个中类加载器都确定了从哪一些位置加载文件。于此同时我们也可以通过继承java.lang.classloader实现自己的类加载器。
```

# cglib动态代理和jdk动态代理的区别

| 名称      | 备注                                                    |
|:--------|:------------------------------------------------------| 
| 静态代理    | 简单，代理模式，是动态代理的理论基础。常见使用在代理模式                          |
| jdk动态代理 | 需要有顶层接口才能使用，但是在只有顶层接口的时候也可以使用，常见是mybatis的mapper文件是代理。 

使用反射完成。使用了动态生成字节码技术。 | | cglib动态代理| 可以直接代理类，使用字节码技术，不能对 final类进行继承。使用了动态生成字节码技术。|

# String

```text
String 不可变, 底层char[]存储,因为 final 修饰了,所以不可变
StringBuffer 可变,线程安全,效率低,底层char[]存储,默认长度16,
StringBuilder 可变,线程不安全,效率高,底层char[]存储
```

# List

```text
Collection 子接口 List
 1.ArrayList 主要实现类,线程不安全,效率高,底层使用Object[], elementData存储
 2.LinkedList,对频繁插入,删除操作,效率高,底层使用双向链表,
 3.Vector 古老实现类,线程安全,效率低
 
 目前比较常用的构建线程安全的List有三种方法：

    使用Vector容器
    使用Collections的静态方法synchronizedList(List< T> list)
    采用CopyOnWriteArrayList容器
    
```

# Set

```text
Set<E> extends Collection
1.HashSet,主要实现类,线程不安全,效率高,可以存储null值;底层是 HashMap,存在在数组中
2.LinkedHashSet,链表,HashSet子类,遍历其内部,可以按照添加顺序,维护了一个添加顺序表
3.TreeSet,二叉树,红黑树,同一个类new对象

 无序性: 不等于随机性,  存在数组中,hash 散列后存放位置,和存放时候不一样,
    所以是无序性,但存放后,位置一般固定
 不可重复性: 先判断 hashCode ,再判断 equal,保证添加的元素按照equals()判断时,不能返回true
 
 CopyOnWriteArraySet 线程安全
```

# Map

```text
1.HashMap 主要实现类,线程不安全,效率高,可以存储null的key和value
    子类 LinkedHashMap,添加顺序遍历
    JDK7: 数组+链表
    JDK8: 数组+链表+红黑树,底层Node[]数组,
     当数组的某一个索引位置上的元素以链表形式存在的数据个数,大于8,且数组长度大于64,
     此索引位置上的数据改为红黑树存储
    
2. Hashtable 古老的, 线程安全,效率低
    子类 Properties 处理配置文件,key,value 都是String类型
3.TreeMap,遍历key会排序

4. ConcurrentHashMap是线程安全的和在并发环境下不需要加额外的同步,分段锁

    在JDK1.7中ConcurrentHashMap采用了数组+Segment+分段锁的方式实现。
    在数组基础上增加了一层Segment，一个Segment对应数组的一段，
    这样对某段进行的操作只需要锁住对应段，不影响其他段的操作。
    其中，Segment继承了ReentrantLock并实现了序列化接口，说明Segment的锁是可重入的
    
    在JDK1.8中ConcurrentHashMap，数据结构上，首先整体上是数组+链表+红黑树的结构与HashMap保持一致，
    其次取消了Segment分段锁的数据结构，取而代之的是Node，
    Node的value和next都是由volatile关键字进行修饰，可以保证可见性。
    将细化的粒度从段进一步降低到节点。线程安全实现上，采用CAS+Synchronized替代Segment分段锁。


```
