package cn.gxh.property.thread;

import android.os.Bundle;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.view.R;

/**
 * 并发编程艺术
 * Created  by gxh on 2019/5/6 15:19
 */
public class ThreadFragment extends BaseFragment {


    public static ThreadFragment newInstance() {
        return new ThreadFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_thread2;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        q5();
    }



    class Father {
        public synchronized void dosomething(){
            Logger.d("gxh","Father");
        }
    }

    class Child extends Father{
        public synchronized void dosomething(){
            Logger.d("gxh","Child");
            super.dosomething();
        }
    }


    private void q5(){
        //method3();
        //method4();
        new Child().dosomething();
    }

    public synchronized void method4(){
        Logger.d("gxh","method4");
        method5();
    }

    public synchronized void method5(){
        Logger.d("gxh","method5");
    }

    int a=0;
    public synchronized void method3(){
        Logger.d("gxh","method3,a="+a);
        if(a<3){
            a++;
            method3();
        }
    }


    private void q4() {
        MyRunnable2 myRunnable = new MyRunnable2();
        MyRunnable2 myRunnable1 = new MyRunnable2();

        Thread t1 = new Thread(myRunnable);
        Thread t2 = new Thread(myRunnable1);
        t1.start();
        t2.start();

        while (t1.isAlive() || t2.isAlive()) {

        }

        Logger.d("gxh", "执行完毕");
    }


    class MyRunnable2 implements Runnable {

        @Override
        public void run() {
            synchronized (MyRunnable2.class) {
                Logger.d("gxh", "我是类锁的.class形式,线程为:" + Thread.currentThread().getName());
                try {
                    int result = 2 / 0;
                }catch (Exception e){

                }

                Logger.d("gxh", Thread.currentThread().getName() + "运行结束");
            }
        }
    }

    static class MyRunnable1 implements Runnable {

        @Override
        public void run() {
//            method();
            method1();
        }

        public synchronized void method() {
            Logger.d("gxh", "我是对象锁的方法修饰符形式,线程为:" + Thread.currentThread().getName());
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Logger.d("gxh", Thread.currentThread().getName() + "运行结束");
        }

        public static synchronized void method1() {
            Logger.d("gxh", "我是对象锁的方法修饰符形式,线程为:" + Thread.currentThread().getName());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Logger.d("gxh", Thread.currentThread().getName() + "运行结束");
        }
    }


    class MyRunnable implements Runnable {

        Object lock1 = new Object();
        Object lock2 = new Object();

        @Override
        public void run() {

            synchronized (lock1) {
                Logger.d("gxh", "我是lock1锁代码块形式,线程为:" + Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Logger.d("gxh", Thread.currentThread().getName() + "lock1运行结束");
            }

            synchronized (lock2) {
                Logger.d("gxh", "我是lock2锁代码块形式,线程为:" + Thread.currentThread().getName());
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Logger.d("gxh", Thread.currentThread().getName() + "lock2运行结束");
            }
        }
    }


    private void q1() {
        // sleepThread不停的尝试睡眠
        Thread sleepThread = new Thread(new SleepRunner(), "SleepThread");
        sleepThread.setDaemon(true);
        // busyThread不停的运行
        Thread busyThread = new Thread(new BusyRunner(), "BusyThread");
        busyThread.setDaemon(true);
        sleepThread.start();
        busyThread.start();
        // 休眠5秒，让sleepThread和busyThread充分运行
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Logger.d("gxh", "q1:" + Thread.currentThread().getName());

        sleepThread.interrupt();
        busyThread.interrupt();
        System.out.println("SleepThread interrupted is " + sleepThread.isInterrupted());
        System.out.println("BusyThread interrupted is " + busyThread.isInterrupted());
        // 防止sleepThread和busyThread立刻退出
        SleepUtils.second(2);
    }

    static class SleepRunner implements Runnable {
        @Override
        public void run() {
            while (true) {
                Logger.d("gxh", "SleepRunner:" + Thread.currentThread().getName());
                SleepUtils.second(2);
            }
        }
    }

    static class BusyRunner implements Runnable {
        @Override
        public void run() {
            while (true) {

                Logger.d("gxh", "BusyRunner:" + Thread.currentThread().getName());
            }
        }
    }


    private void q2() {
        DateFormat format = new SimpleDateFormat("HH:mm:ss");
        Thread printThread = new Thread(new Runner(), "PrintThread");
        printThread.setDaemon(true);
        printThread.start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 将PrintThread进行暂停，输出内容工作停止
        printThread.suspend();
        System.out.println("main suspend PrintThread at " + format.format(new Date()));
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 将PrintThread进行恢复，输出内容继续
        printThread.resume();
        System.out.println("main resume PrintThread at " + format.format(new Date()));
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 将PrintThread进行终止，输出内容停止
        printThread.stop();
        System.out.println("main stop PrintThread at " + format.format(new Date()));
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Runner implements Runnable {
        @Override
        public void run() {
            DateFormat format = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                System.out.println(Thread.currentThread().getName() + " Run at " +
                        format.format(new Date()));
                SleepUtils.second(1);
            }
        }
    }
}
