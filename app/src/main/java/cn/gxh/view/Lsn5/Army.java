package cn.gxh.view.Lsn5;

/**
 * 军队
 * Created  by gxh on 2019/1/22 22:57
 */
public class Army {

    private Soldier soldier;

    public Army(Soldier soldier) {
        this.soldier = soldier;
    }


    public void attach() {
        this.soldier.setAttach("攻击敌人");
    }

    public void back(){
        this.soldier.setBack("撤退吧");
    }
}
