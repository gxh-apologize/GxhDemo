package cn.gxh.view.Lsn5;

/**
 * Created  by gxh on 2019/1/22 22:56
 */
public class AttachCommand implements Command {

    private Army army;


    public AttachCommand(Army army) {
        this.army = army;
    }

    @Override
    public void excute() {
        army.attach();
    }

    @Override
    public void back() {
        army.back();
    }
}
