package cn.gxh.common.network;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.support.annotation.RequiresApi;

import cn.gxh.common.util.Constant;
import cn.gxh.common.util.Logger;

/**
 * Created  by gxh on 2019/5/21 15:21
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class NetworkCallbackImpl extends ConnectivityManager.NetworkCallback {
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network);
        Logger.d(Constant.TAG, "net available");
    }

    @Override
    public void onLost(Network network) {
        super.onLost(network);
        Logger.d(Constant.TAG, "net lost");
    }

    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network, networkCapabilities);

        if (networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
            if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Logger.d(Constant.TAG, "net change: wifi");
            } else {
                Logger.d(Constant.TAG, "net change: other");
            }
        }
    }
}
