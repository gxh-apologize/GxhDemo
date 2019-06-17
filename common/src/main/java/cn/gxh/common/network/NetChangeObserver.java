package cn.gxh.common.network;

/**
 * Created  by gxh on 2019/5/21 10:16
 */
public interface NetChangeObserver {
    //网络连接
    void onConnect(NetType type);

    //网络没有连接
    void onDisConnect();
}
