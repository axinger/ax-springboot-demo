## volatile

```text
是关键字,java虚拟机,轻量级同步机制,
1.保证可见性,
2.不保证原子性
3.禁止指令重排 

JMM, java内存模型,抽象的概念,不真实存在

JMM关于同步规定
1.线程解锁前,必须把共享变量的值刷新回主内存
2.线程加锁前,必须读取主内存的最新值到自己的工作内存
3.加锁解锁是同一把锁

主内存: 内存,数据共享,所有线程都可以访问
工作内存:读取内存的数据,到自己线程工作内存,不直接修改内存数据

cpu速度快于内存,中间有缓存

可见性: 内存机制,一个线程修改了主内存数据,其他线程可以看见主内存被修改

JMM三大特性
1.可见性:子线程修改值,主线程可以知道
2.原子性(synchronized 保证原子性,volatile不保证原子性)
3.有序性

volatile解决原子性,
 1.加synchronized,太重
 2.使用 AtomicXX类型数据,内部使用CAS unsafe

synchronized既能够保证可见性，又能保证原子性，而volatile只能保证可见性，无法保证原子性。

volatile和synchronized的区别
    volatile本质是在告诉jvm当前变量在寄存器（工作内存）中的值是不确定的，需要从主存中读取； synchronized则是锁定当前变量，只有当前线程可以访问该变量，其他线程被阻塞住。
    volatile仅能使用在变量级别；synchronized则可以使用在变量、方法、和类级别的
    volatile仅能实现变量的修改可见性，不能保证原子性；而synchronized则可以保证变量的修改可见性和原子性
    volatile不会造成线程的阻塞；synchronized可能会造成线程的阻塞。
    volatile标记的变量不会被编译器优化；synchronized标记的变量可以被编译器优化

```
