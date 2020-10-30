package cn.xmj;

public class DeadLockDemo {

    Object lockA = new Object();
    Object lockB = new Object();

    public static void main(String[] args) {
        final DeadLockDemo deadLockDemo = new DeadLockDemo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (deadLockDemo.lockA) {
                    System.out.println(Thread.currentThread().getName() + "获取了A锁");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(Thread.currentThread().getName() + "等待B锁");
                    synchronized (deadLockDemo.lockB) {
                        System.out.println(Thread.currentThread().getName() + "获取了B锁");
                    }
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (deadLockDemo.lockB) {
                    System.out.println(Thread.currentThread().getName() + "获取了B锁");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                    System.out.println(Thread.currentThread().getName() + "等待A锁");
                    synchronized (deadLockDemo.lockA) {
                        System.out.println(Thread.currentThread().getName() + "获取了A锁");
                    }
                }
            }
        }).start();
    }
}
