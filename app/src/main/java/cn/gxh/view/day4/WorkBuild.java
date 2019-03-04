package cn.gxh.view.day4;

/**
 * 具体建造者持有对产品的引用
 * Created  by gxh on 2019/1/16 23:01
 */
public class WorkBuild implements Build{

    Room room=new Room();
    @Override
    public void makeFllor() {
        room.setFllor("木质地板");
    }

    @Override
    public void makeWindow() {
        room.setWindow("落地窗");
    }

    @Override
    public Room build() {
        return room;
    }
}
