package cn.gxh.property.thread;

/**
 * Created  by gxh on 2019/5/8 11:02
 */
public class Reverse {

    public void method(){
        synchronized (this){
            System.out.println("00000000000");
        }
    }

    public synchronized void method1(){
        System.out.println("00000000000");
    }
}
