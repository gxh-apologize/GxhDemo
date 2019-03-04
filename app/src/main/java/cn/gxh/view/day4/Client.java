package cn.gxh.view.day4;

/**
 * Created  by gxh on 2019/1/16 23:07
 */
public class Client {

    public void main(){

        //具体的调用代码
        Build build=new WorkBuild();
        Designer designer=new Designer();
        Room room=designer.build(build);


    }
}
