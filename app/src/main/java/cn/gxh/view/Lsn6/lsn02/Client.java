package cn.gxh.view.Lsn6.lsn02;

import cn.gxh.view.Lsn6.lsn01.Game;
import cn.gxh.view.Lsn6.lsn01.LoLGame;

/**
 * Created  by gxh on 2019/1/23 22:48
 */
public class Client {

    public static void main(String[] args){

        //建立被观察者
        AbstratSubject subject=new ContrereSubject();

        //建立观察者
        AbstratObserver observer1=new ConcreteObserver("001");
        AbstratObserver observer2=new ConcreteObserver("002");

        subject.attach(observer1);
        subject.attach(observer2);

        subject.notify("hahha");

    }
}
