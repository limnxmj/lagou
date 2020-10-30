**1.什么是CAS**
比较交换，包含内存值、预期值和新值，当内存值和预期值相等时，更新内存值为新值，否则不做处理。
CAS存在ABA、循环时间开销大和只能保证一个共享变量的原子操作

**2.CyclicBarrier和CountDownLatch区别**
CountDownLatch：一个或者多个线程，等待其他多个线程完成某件事情之后才能执行；只能使用一次
CyclicBarrier：多个线程互相等待，直到到达同一个同步点，再继续一起执行；可以多次使用。

**3.volatile关键字的作用**
volatile用于修饰实例变量和类变量，可以保证数据的可见性，禁止指令重排序

**4.如何正确的使用wait()?使用if还是while？**
wait和notify必须在synchronized方法或块中调用，调用wait方法会使当前线程阻塞，并且释放对象锁，需要其他线程调用notify和notifyAll唤醒wait的线程，wait被唤醒后并不是立即执行，只有当cpu重新分配时间片后才会执行，当线程获取到CPU开始执行的时候，其他条件可能没有满足，所以在处理前，while循环检测条件是否满足会更好

**5.为什么wait、nofity和nofityAll这些方法不放在Thread类当中？**
wait、notify和notifyAll方法在调用前都必须先获得对象的锁；
调用任意对象的wait方法导致线程阻塞，并且该对象上的锁被释放，阻塞时要释放占用的锁，而锁是任何对象都具有的；
调用对象的notify和notifyAll方法使调用该对象的wait方法而阻塞的线程解除阻塞

**6.synchronized和ReentrantLock的区别**
synchronized 是JVM层面的锁，ReentrantLock 是从jdk1.5后提供的API层面的锁；
synchronized 不需要用户去手动释放锁，synchronized 代码执行完后系统会自动让线程释放对锁的占用，ReentrantLock则需要用户去手动释放锁，如果没有手动释放锁，就可能导致死锁现象
synchronized是不可中断类型的锁，除非加锁的代码中出现异常或正常执行完成，ReentrantLock则可以中断，可通过trylock(long timeout,TimeUnit unit)设置超时方法或者将lockInterruptibly()放到代码块中，调用interrupt方法进行中断；
synchronized为非公平锁，ReentrantLock既可以选公平锁也可以选非公平锁；
ReentrantLock通过绑定Condition结合await()/singal()方法实现线程的精确唤醒，synchronized只能通过Object类的wait()/notify()/notifyAll()方法要么随机唤醒一个线程要么唤醒全部线程

**7.什么是AQS**
AQS同步器是用来构建锁和其他同步组件的基础框架，它的实现主要依赖一个int成员变量来表示同步状态以及通过一个FIFO队列构成等待队列。
1)同步组件的实现依赖于同步器AQS，在同步组件实现中，使用AQS的方式被推荐定义继承AQS的静态内存类；
2)AQS采用模板方法进行设计，AQS的protected修饰的方法需要由继承AQS的子类进行重写实现，当调用AQS的子类的方法时就会调用被重写的方法；
3)AQS负责同步状态的管理，线程的排队，等待和唤醒这些底层操作，而Lock等同步组件主要专注于实现同步语义；
4)在重写AQS的方式时，使用AQS提供的getState(),setState(),compareAndSetState()方法进行修改同步状态

**8.什么是Java内存模型**
Java内存模型描述了JVM中将共享变量存储到内存和从内存中读取变量这样的底层细节；
所有的共享变量都存储于主内存，每一个线程还存在自己的工作内存，工作内存保留了共享变量的副本；
线程对变量的操作(读，取)都必须在工作内存中完成，而不能直接读写主内存中的变量；
不同线程之间也不能直接访问对方工作内存中的变量，线程间变量的值的传递需要通过主内存中转来完成。

**9.什么是自旋**
死循环
