```text

corePoolSize: 核心线程数
maximumPoolSize:最大线程数
keepAliveTime:当线程数大于核心时，此为终止前多余的空闲线程等待新任务的最长时间
unit: 时间单位
workQueue: 阻塞队列及个数,是core核心数,能处理并发数
threadFactory: 创建线程的工厂,比如自定义设置前缀
handler: 拒绝策略,由于超出线程范围和队列容量而使执行被阻塞时所使用的处理程序

       public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue,
                              ThreadFactory threadFactory,
                              RejectedExecutionHandler handler)
                              
```

## 工作顺序

```text
一.线程池创建,准备好了core数量的核心线程池,准备接收任务
1.core满了,就将再进来的任务放入阻塞队列中.空闲的core就会自己去阻塞队列获取任务执行
2.阻塞队列满了,就直接开启新线程执行,最大只能max指定数量
3.max满了就用拒绝策略
4.max都执行完了,有空闲,在指定时间后释放max-core个线程
```

```text
例子:
core:7, max:20,queue:50, 100并发怎么分配
7个会立即得到执行,50个会进入队列,再开20-7=13个进行执行,剩下的100-50-20=30 就使用拒绝策略

core能处理50个,  max:20,就处理20个 共处理70个

core立即处理7个, 队列进入50个待处理, 还有13个线程开启线程处理13个,  7+50+13=70 就等于  max+queue
```

## 代码

### Executors

```text
Executors.newCachedThreadPool() //core是0,所有都回收
Executors.newFixedThreadPool();//固定大小,core=max,都不可回收
Executors.newScheduledThreadPool();//定时任务的线程池
Executors.newSingleThreadExecutor();//单线程的线程池,后台任务顺序执行
 ```

### CompletableFuture

```text
无返回值
CompletableFuture.runAsync(任务) 
CompletableFuture.runAsync(任务,指定线程池) 

有返回值
CompletableFuture.supplyAsync(任务)   
CompletableFuture.supplyAsync(任务,指定线程池) 
```

## 拒绝策略 4种

```text

https://www.jianshu.com/p/bf6fabb7b459

AbortPolicy 中止策略
默认的拒绝策略。直接抛出 java.util.concurrent.RejectedExecutionException异常
new ThreadPoolExecutor.AbortPolicy()
 
CallerRunsPolicy 将任务返还给调用者线程执行,让执行者调用方法,同步执行
new ThreadPoolExecutor.CallerRunsPolicy()

DiscardPolicy 直接抛弃无法处理的任务
直接抛弃无法处理的任务，不予处理不抛异常。如果业务汇总 `允许任务丢失`，这是最好的策略
new ThreadPoolExecutor.DiscardPolicy()

DiscardOldestPolicy 抛弃队列中等待最久的任务
抛弃队列中等待最久的任务，然后把当前任务加入队列中尝试再次提交当前任务
new ThreadPoolExecutor.DiscardOldestPolicy()

```

## 队列

```text
1 无界队列
2 有界队列
3 同步移交

在这里，我们主要对 ArrayBlockingQueue，PriorityBlockingQueue，DelayQueue，SynchronousQueue，LinkedTransferQueue，LinkedBlockingDeque的使用方法和应用场景做一个补充。

ArrayBlockingQueue：基于数组实现的阻塞队列，先进先出队列，有界队列。在创建时必须制定容量大小。并可以指定公平性与非公平性，默认情况下是非公平的，即不保证等待时间最长的队列最优先能够访问队列。
LinkedBlockingQueue：基于链表实现的阻塞队列，先进先出队列，有界队列。在创建时如果不指定容量大小，则默认大小为Integer.MAX_VALUE。
PriorityBlockingQueue：按照元素的优先级对元素进行排序，按照优先级顺序出队。并且该阻塞队列为无界阻塞队列，即容量没有上限（源码中它没有容器满的信号标志）。
DelayQueue：基于PriorityQueue的延时阻塞队列，无界队列。DelayQueue中的元素只有当其指定的延迟时间到了，才能够从队列中获取到该元素。因为DelayQueue是一个无界队列，所以往队列中插入数据的操作永远不会被阻塞，而只有获取数据的操作才会被阻塞。
SynchronousQueue：一个不存储元素的阻塞队列。
LinkedTransferQueue：一个由链表结构组成的无界阻塞队列。
LinkedBlockingDeque：一个由链表结构组成的双向阻塞队列。

```

```text
java线程池如何合理的设置大小 线程池究竟设置多大要看你的线程池执行的什么任务了，CPU密集型、IO密集型、混合型，任务类型不
同，设置的方式也不一样
任务一般分为:CPU密集型、IO密集型、混合型，对于不同类型的任务需要分配不同大小的线程池
1、CPU密集型
尽量使用较小的线程池，一般Cpu核心数+1
因为CPU密集型任务CPU的使用率很高，若开过多的线程，只能增加线程上下文的切换次数，带来额外 的开销
2、IO密集型 方法一:可以使用较大的线程池，一般CPU核心数 * 2
 
IO密集型CPU使用率不高，可以让CPU等待IO的时候处理别的任务，充分利用cpu时间 方法二:线程等待时间所占比例越高，需要越多线程。线程CPU时间所占比例越高，需要越少线程。
下面举个例子: 比如平均每个线程CPU运行时间为0.5s，而线程等待时间(非CPU运行时间，比如IO)为1.5s，CPU核 心数为8，那么根据上面这个公式估算得到:((0.5+1.5)/0.5)*8=32。这个公式进一步转化为:
最佳线程数目 = (线程等待时间与线程CPU时间之比 + 1)* CPU数目
3、混合型 可以将任务分为CPU密集型和IO密集型，然后分别使用不同的线程池去处理，按情况而定
```

## Java多线程：向线程传递参数的三种方法

```text
https://www.cnblogs.com/jpfss/p/10783847.html

一、通过构造方法传递数据 
二、通过变量和方法传递数据
三、通过回调函数传递数据
```

## 线程的几种状态

```text
线程在一定条件下，状态会发生变化。线程一共有以下几种状态:
1. 新建状态(New):新创建了一个线程对象。
2. 就绪状态(Runnable):线程对象创建后，其他线程调用了该对象的start()方法。该状态的线程位于 “可运行线程池”中，变得可运行，只等待获取CPU的使用权。即在就绪状态的进程除CPU之外，其 它的运行所需资源都已全部获得。
3. 运行状态(Running):就绪状态的线程获取了CPU，执行程序代码。
4. 阻塞状态(Blocked):阻塞状态是线程因为某种原因放弃CPU使用权，暂时停止运行。直到线程进入就绪状态，才有机会转到运行状态。
  阻塞的情况分三种:
    1. 等待阻塞:运行的线程执行wait()方法，该线程会释放占用的所有资源，JVM会把该线程放入 “等待池”中。进入这个状态后，是不能自动唤醒的，必须依靠其他线程调用notify()或 notifyAll()方法才能被唤醒，
    2. 同步阻塞:运行的线程在获取对象的同步锁时，若该同步锁被别的线程占用，则JVM会把该线 程放入“锁池”中。
    3. 其他阻塞:运行的线程执行sleep()或join()方法，或者发出了I/O请求时，JVM会把该线程置为 阻塞状态。当sleep()状态超时、join()等待线程终止或者超时、或者I/O处理完毕时，线程重新 转入就绪状态。
5. 死亡状态(Dead):线程执行完了或者因异常退出了run()方法，该线程结束生命周期。

```

## Java线程间通信的几种方式

```text
https://www.cnblogs.com/rouqinglangzi/p/9106298.html
1.wait和notify/notifyAll
2.使用Lock和Condition
3.使用阻塞队列控制线程通信
4.使用管道流进行线程通信(被阻塞队列替代)
```

```text
Java编程思想中有这样一句话：

当我们使用线程来同时运行多个任务时，可以通过使用锁(互斥)来同步两个任务的行为，从而使得一个任务不会干扰到另外一个任务，
这解决的是线程间彼此干涉的问题，现在我们需要来解决线程间彼此协调的问题，也就是线程间通信问题。
```

```text
1.wait和notify/notifyAll + synchronized 关键字

可以借助于Object类提供的wait()、notify()、notifyAll()三个方法，这三个方法属于Object类。但这三个方法必须由同步监视器对象来调用，这可分为两种情况
①对于用synchronized修饰的同步方法，因为该类的默认实例(this)就是同步监视器，所以可以在同步方法中直接调用这三个方法。
②对于用synchronized修饰的同步代码块，同步监视器是synchronized后括号里的对象，所以必须使用该对象调用这三个方法。
需要注意的是notify()方法只能随机唤醒一个线程，如果要唤醒所有线程，请使用notifyAll()。
```

```text
2.使用Lock和Condition

如果程序不用synchronized关键字来进行同步，而是用Lock对象来保证同步，则系统中不存在隐式的同步监视器，也就不能使用wait()、notify()、notifyAll()方法进行线程通信了。
使用Lock对象的方式，Java提供了一个Condition类来保持协调，使用Condition可以让那些已经得到Lock对象却无法继续执行的线程释放Lock对象，Condition对象也可以唤醒其他处于等待的线程。
Condition将同步监视器方法wait()、notify()、notifyAll()分解成截然不同的对象，以便通过将这些对象与Lock对象组合使用，为每个对象提供多个等待集(wait-set)。
在这种情况下，Lock替代了同步方法和同步代码块，Condition替代了同步监视器的功能。
Condition实例被绑定在一个Lock对象上。要获得特定Lock实例的Condition实例，调用Lock对象的newCondition()方法获得即可。Condition类提供了以下三个方法：
await():
signal():

signalAll():
```

```text
3.使用阻塞队列控制线程通信

Java5提供了一个BlockingQueue接口，虽然BlockingQueue也是Queue的子接口，但它的主要用途不是作为容器，而是作为线程同步的工具。
BlockingQueue具有一个特征：当生产者线程试图向BlockingQueue中放入元素时，如果该队列已满则线程被阻塞。而消费者线程 在取元素时，如果该队列已空则该线程被阻塞。
程序的两个线程通过交替向BlockingQueue中放入元素、取出元素，即可很好地控制线程的通信。
```

````text
4.使用管道流进行线程通信(被阻塞队列替代)
管道通信模型可以看成是生产者-消费者模型的变种，管道就是一个封装好的解决方案。管道基本上就是一个阻塞队列，所以可以用于线程间通信。管道在Java中对应的实现是PipedWriter类和PipedReader类。
但随着阻塞队列的出现，管道逐渐被替代。所以，在实际开发中，很少会使用到管道流。
````
