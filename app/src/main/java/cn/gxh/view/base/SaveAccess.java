package cn.gxh.view.base;

public class SaveAccess {
    private boolean data;
    private int state;
    private String message;

    public boolean getData() {
        return data;
    }
    public void setData(boolean data) {
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
