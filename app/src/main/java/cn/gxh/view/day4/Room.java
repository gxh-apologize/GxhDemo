package cn.gxh.view.day4;

/**
 * 产品
 * Created  by gxh on 2019/1/16 23:03
 */
public class Room {

    private String fllor;
    private String window;

    public String getFllor() {
        return fllor;
    }

    public void setFllor(String fllor) {
        this.fllor = fllor;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    @Override
    public String toString() {
        return "Room{" +
                "fllor='" + fllor + '\'' +
                ", window='" + window + '\'' +
                '}';
    }
}
