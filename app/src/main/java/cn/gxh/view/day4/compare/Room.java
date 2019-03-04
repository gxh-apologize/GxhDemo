package cn.gxh.view.day4.compare;

/**
 * 产品
 * Created  by gxh on 2019/1/16 23:03
 */
public class Room {

    private String fllor;
    private String window;


    public Room apply(WorkBuild.RoomParams params){
        fllor=params.fllor;
        window=params.window;
        return this;
    }

    @Override
    public String toString() {
        return "Room{" +
                "fllor='" + fllor + '\'' +
                ", window='" + window + '\'' +
                '}';
    }
}
