package cn.gxh.view.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import cn.gxh.base.Logger;

/**
 * Created  by gxh on 2019/3/29 14:12
 */
public class ANRCacheHelper {

    private static MyReceiver myReceiver;

    public static void registerANRReceiver(Context context) {
        myReceiver = new MyReceiver();
        context.registerReceiver(myReceiver, new IntentFilter(ACTION_ANR));
    }

    public static void unregisterANRReceiver(Context context) {
        if (myReceiver == null) {
            return;
        }
        context.unregisterReceiver(myReceiver);
    }

    private static final String ACTION_ANR = "android.intent.action.ANR";

    private static class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_ANR)) { // to do } } }
                Logger.d("gxh","ANR");
            }
        }
    }
}
