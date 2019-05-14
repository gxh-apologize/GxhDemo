package cn.gxh.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import cn.gxh.base.Logger;

/**
 * Created  by gxh on 2019/4/24 15:35
 */
public class BlueToothReceiver extends BroadcastReceiver {

    List<BluetoothDevice> devices = new ArrayList<>();

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            devices.add(device);
            Logger.d("gxh","search:"+device.getName());
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            for (BluetoothDevice device : devices) {
                Logger.d("gxh",device.getName()+";"+device.getAddress());
            }
        }
    }
}
