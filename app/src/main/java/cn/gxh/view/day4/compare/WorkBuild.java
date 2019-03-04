package cn.gxh.view.day4.compare;

/**
 * 具体建造者持有对产品的引用
 * Created  by gxh on 2019/1/16 23:01
 */
public class WorkBuild {

    Room room=new Room();
    private RoomParams roomParams=new RoomParams();

    public WorkBuild makeFllor() {
        roomParams.fllor="木质地板";
        return this;
    }


    public WorkBuild makeWindow() {
        roomParams.window="落地窗";
        return this;
    }


    public Room build() {
        return room.apply(roomParams);
    }

    class RoomParams{
        public String fllor;
        public String window;

    }

}
