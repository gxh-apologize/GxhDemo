package cn.gxh.common.network;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created  by gxh on 2019/5/21 10:19
 */
public class NetworkUtils {

    /**
     * 判断网络是否可用
     * @return
     */
    public static boolean isNetworkAvailable(){
        ConnectivityManager connMgr= (ConnectivityManager) NetworkManager.getInstance().getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMgr==null){
            return false;
        }

        NetworkInfo[] allNetworkInfo = connMgr.getAllNetworkInfo();
        if(allNetworkInfo!=null){
            for(NetworkInfo info:allNetworkInfo){
                if(info.getState()==NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前网络类型
     * @return
     */
    public static NetType getNetType(){
        ConnectivityManager connMgr= (ConnectivityManager) NetworkManager.getInstance().getApplication()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connMgr==null){
            return NetType.NONE;
        }

        @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connMgr.getActiveNetworkInfo();
        if(activeNetworkInfo==null){
            return NetType.NONE;
        }

        int type = activeNetworkInfo.getType();
        if(type==ConnectivityManager.TYPE_MOBILE){
            if(activeNetworkInfo.getExtraInfo().toLowerCase().equals("cmnet")){
                return NetType.CMNET;
            }else {
                return NetType.CMWAP;
            }

        }else if(type==ConnectivityManager.TYPE_WIFI){
            return NetType.WIFI;
        }

        return NetType.NONE;
    }
}
