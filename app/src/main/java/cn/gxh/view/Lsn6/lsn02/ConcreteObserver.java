package cn.gxh.view.Lsn6.lsn02;

/**
 * 具体观察者
 * Created  by gxh on 2019/1/23 23:15
 */
public class ConcreteObserver implements AbstratObserver {

    private String name;

    public ConcreteObserver(String name) {
        this.name = name;
    }

    @Override
    public void update(String content) {
        System.out.println(name + "得到了通知:" + content);
    }
}
