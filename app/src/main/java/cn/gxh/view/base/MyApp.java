package cn.gxh.view.base;

import android.app.Application;

import cn.gxh.view.SpeechManger;

public class MyApp extends Application {


    //这个值是区别特定平板和其它设备的，因为其他设备调用jar包方法会抛异常
    public static boolean isCanSetIo=true;

    @Override
    public void onCreate() {
        super.onCreate();

        Global.init(this);

        //SpeechManger.getInstance().init(this);
    }
}
