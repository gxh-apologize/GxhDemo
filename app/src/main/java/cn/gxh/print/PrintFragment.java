package cn.gxh.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jb.sdk.command.GpUtils;
import com.jb.sdk.service.JbPrintService;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.classloader.ClassLoaderFragment;
import cn.gxh.view.R;
import cn.gxh.view.base.Global;

/**
 * 佳博打印机
 * Created  by gxh on 2019/4/24 14:14
 */
public class PrintFragment extends BaseFragment {

    //    String mac="79:5B:78:71:C9:FF";
//    String mac="53:52:F8:82:5F:6C";
    String mac = "DC:0D:30:36:9F:35";
    String content = "{\"result\":{\"orderState\":3,\"orderStateStr\":\"交易成功\",\"orderNumber\":\"E20190424141442682400041\",\"customerPhone\":\"13231212123\",\"creationTime\":\"2019-04-24 14:14:42\",\"creationTimeStr\":\"2019-04-24 14:14:42\",\"creatorUserPhone\":\"13501115426\",\"commdityDetailsList\":[{\"goodsId\":null,\"goodsType\":3,\"goodName\":\"直接付款\",\"goodCode\":\"636860861685790357\",\"originalUnitPrice\":45.85,\"presentPrice\":0.0,\"originalUnitPriceStr\":\"45.85\",\"presentUnitPrice\":45.85,\"presentUnitPriceStr\":\"45.85\",\"presentTotal\":45.85,\"presentTotalStr\":\"45.85\",\"count\":1,\"id\":\"c569dacc-c950-4844-906e-08d695514a04\"},{\"goodsId\":null,\"goodsType\":0,\"goodName\":\"佑天兰奢华面膜\",\"goodCode\":\"P19022237119443\",\"originalUnitPrice\":136.00,\"presentPrice\":0.0,\"originalUnitPriceStr\":\"136.00\",\"presentUnitPrice\":109.00,\"presentUnitPriceStr\":\"109.00\",\"presentTotal\":436.00,\"presentTotalStr\":\"436.00\",\"count\":4,\"id\":\"c6b2e200-ff4e-4688-88dc-7fd153634be7\"},{\"goodsId\":null,\"goodsType\":0,\"goodName\":\"春雨补水面膜\",\"goodCode\":\"P19021856439331\",\"originalUnitPrice\":120.00,\"presentPrice\":0.0,\"originalUnitPriceStr\":\"120.00\",\"presentUnitPrice\":96.00,\"presentUnitPriceStr\":\"96.00\",\"presentTotal\":192.00,\"presentTotalStr\":\"192.00\",\"count\":2,\"id\":\"43e1e121-23f8-4f89-8f09-b2cccab37630\"}],\"amountCount\":7,\"amountPrice\":301.85,\"isIntegralProtect\":false,\"amountPriceStr\":\"301.85\",\"amountReduce\":156.85,\"amountReduceStr\":\"156.85\",\"payType\":5,\"payTypeStr\":\"标记支付_自有微信\",\"presentPrice\":673.00,\"presentPriceStr\":\"673.00\",\"originalPrice\":829.85,\"originalPriceStr\":\"829.85\",\"actualPrice\":673.00,\"actualPriceStr\":\"673.00\",\"oddChange\":\"0.00\",\"rewardIntegral\":0,\"consumeIntegral\":null,\"integralAmount\":35,\"sourceList\":[{\"preferentialType\":3,\"preferentialDiscountType\":0,\"preferentialDiscountPrice\":0.85,\"preferentialTypeStr\":\"抹零\",\"preferentialPrice\":0.85}],\"id\":\"d1d3f333-c767-4201-9fed-ff5325fda4b8\"},\"targetUrl\":null,\"success\":true,\"error\":null,\"unAuthorizedRequest\":false,\"__abp\":true}\n";
    private BluetoothManager bluetoothmanger;

    public static PrintFragment newInstance() {
        return new PrintFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_print;
    }

    @Override
    public void initView(Bundle savedInstanceState) {


        TextView tvContent=findView(R.id.tv_content);
        new Thread(new Runnable() {
            @Override
            public void run() {
                tvContent.setText("hhhhhhhh");
            }
        }).start();

        ((Button) findView(R.id.btn_conn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AidlUtil.getInstance().connectPrinter(1, 0, null, mac);
            }
        });

        ((Button) findView(R.id.btn_ceshi)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AidlUtil.getInstance().printTestPage(0);
            }
        });


        ((Button) findView(R.id.btn_mode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("gxh", AidlUtil.getInstance().getCommandTypes(0) + "");
            }
        });

        ((Button) findView(R.id.btn_order)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AidlUtil.getInstance().printOrderQ(new Gson().fromJson(content, OrderDetailBean.class), false);
            }
        });

        ((Button) findView(R.id.btn_status)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AidlUtil.getInstance().getPrinterStatus(0);
            }
        });

        ((Button) findView(R.id.btn_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AidlUtil.getInstance().closeConnect(0);
            }
        });


        ((Button) findView(R.id.btn_query_status)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AidlUtil.getInstance().queryPrinterStatus(0);
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {



//        BlueToothReceiver receiver=new BlueToothReceiver();
//        mActivity.registerReceiver(receiver,makeFilter());
//
//        startSearch();

    }

    private void startSearch() {
        BluetoothManager bluetoothmanger = (BluetoothManager) mActivity.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothmanger.getAdapter();
        if (bluetoothAdapter == null) {
            Global.showToast("该设备不支持蓝牙");
            return;
        }

        //判断蓝牙是否开启
        if (!bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.enable();
//            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enabler, 1);
        }

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();

    }


    public IntentFilter makeFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙状态改变的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND);//找到设备的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描的广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        return filter;
    }


}
