package cn.gxh.view.day4;

/**
 * 设计者
 *
 * Created  by gxh on 2019/1/16 22:57
 */
public class Designer {

    public Room build(Build build){
        build.makeFllor();
        build.makeWindow();
        return build.build();
    }

}
