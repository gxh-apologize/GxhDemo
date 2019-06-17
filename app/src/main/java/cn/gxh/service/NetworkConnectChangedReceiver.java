package cn.gxh.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;

import cn.gxh.base.Logger;

/**
 * Created by GXH on 2018/3/27.
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Bundle extras = intent.getExtras();

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {//这个监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    //Global.showToast("WIFI已关闭");
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    //Global.showToast("WIFI关闭中");
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    Logger.d("gxh","WIFI_STATE_ENABLED");
                    //Global.showToast("WIFI已连接");
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    //Global.showToast("WIFI连接中");
                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    // Global.showToast("---");
                    break;
                //
            }
        }
        // 这个监听wifi的连接状态即是否连上了一个有效无线路由，当上边广播的状态是WifiManager.WIFI_STATE_DISABLING，和WIFI_STATE_DISABLED的时候，根本不会接到这个广播。
        // 在上边广播接到广播是WifiManager.WIFI_STATE_ENABLED状态的同时也会接到这个广播，当然刚打开wifi肯定还没有连接到有效的无线
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
            String bssid = intent.getStringExtra(WifiManager.EXTRA_BSSID);
            if (null != parcelableExtra) {
                NetworkInfo networkInfo = (NetworkInfo) parcelableExtra;
                NetworkInfo.State state = networkInfo.getState();
                Logger.d("gxh","11111222");
                if (state == NetworkInfo.State.CONNECTED) {
                    Logger.d("gxh","llllll");
                }
            }
        }
    }

}
