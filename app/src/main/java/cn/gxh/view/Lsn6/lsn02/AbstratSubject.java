package cn.gxh.view.Lsn6.lsn02;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象被观察者
 * Created  by gxh on 2019/1/23 23:14
 */
public abstract class AbstratSubject {

    //保存观察者的集合
    private List<AbstratObserver> list = new ArrayList<>();

    public void attach(AbstratObserver observer) {
        list.add(observer);
    }

    public void detach(AbstratObserver observer) {
        //应该先判断再移除
        list.remove(observer);
    }

    //通知:最先注册的最先通知
    public void notify(String content) {
        for (AbstratObserver observer : list) {
            observer.update(content);
        }
    }
}
