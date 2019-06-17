package cn.gxh.view.Lsn6.lsn01;

import android.widget.ListView;

/**
 * 定义算法结构，还可以提供通用实现
 * Created  by gxh on 2019/1/23 22:43
 */
public abstract class Game {

    abstract void login();

    abstract void startPlay();

    abstract void stopPlay();

    public final void paly() {
        System.out.println("开机");
        login();
        startPlay();
        stopPlay();
    }
}
