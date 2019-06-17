package cn.gxh.common.network;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkRequest;
import android.net.wifi.WifiManager;
import android.os.Build;

import cn.gxh.common.util.Constant;

/**
 * Created  by gxh on 2019/5/21 10:00
 */
public class NetworkManager {

    private static volatile NetworkManager instance;
    private static NetStateReceiver receiver;

    private Application application;

    private NetworkManager(){
        receiver=new NetStateReceiver();
    }

    public static NetworkManager getInstance(){
        if(instance==null){
            synchronized (NetworkManager.class) {
                if(instance==null){
                    instance=new NetworkManager();
                }
            }
        }
        return instance;
    }

    public void initByBroadcast(Application application){
        this.application=application;

        //广播注册
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(receiver, filter);
    }


    @SuppressLint("MissingPermission")
    public void initByCallBack(Application application){
        this.application=application;

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            ConnectivityManager.NetworkCallback networkCallback=new NetworkCallbackImpl();
            NetworkRequest.Builder builder=new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cmgr= (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cmgr!=null){
                cmgr.registerNetworkCallback(request,networkCallback);
            }
        }
    }


    @SuppressLint("MissingPermission")
    public void initByDiyCallBack(Application application,ConnectivityManager.NetworkCallback networkCallback){
        this.application=application;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            NetworkRequest.Builder builder=new NetworkRequest.Builder();
            NetworkRequest request = builder.build();
            ConnectivityManager cmgr= (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
            if(cmgr!=null){
                cmgr.registerNetworkCallback(request,networkCallback);
            }
        }
    }


    public Application getApplication(){
        return application;
    }

    public void registerObserver(Object object){
        receiver.registerObserver(object);
    }

    public void unRegisterObserver(Object object){
        receiver.unRegisterObserver(object);
    }
}
