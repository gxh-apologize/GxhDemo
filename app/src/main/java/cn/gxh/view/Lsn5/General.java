package cn.gxh.view.Lsn5;

/**
 * 将军
 * Created  by gxh on 2019/1/22 22:54
 */
public class General {


    private Command attachCommand;


    public General (){
        Soldier soldier=new Soldier();
        Army army=new Army(soldier);

        attachCommand=new AttachCommand(army);
    }

    /**
     * 皇帝调用将军的攻击方法
     */
    public void attach(){
        attachCommand.excute();
    }

    public void back(){
        attachCommand.back();
    }
}
