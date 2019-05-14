package cn.gxh.print;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.jb.sdk.aidl.JBPrinterConnectCallback;
import com.jb.sdk.aidl.JBPrinterRealStatusCallback;
import com.jb.sdk.aidl.JBService;
import com.jb.sdk.io.GpDevice;
import com.jb.sdk.io.PortParameter;
import com.jb.sdk.service.JbPrintService;

import java.util.ArrayList;
import java.util.List;

import cn.gxh.base.Logger;

/**
 * Created  by gxh on 2019/4/19 13:53
 */
public class AidlUtil {
    private static AidlUtil mAidlUtil = new AidlUtil();
    private Context context;
    private JBService mService;

    private int printerConnStatus = 0;//打印机连接状态
    private int REQUEST_PRINTER_STATUS=110;

    private AidlUtil() {

    }

    public static AidlUtil getInstance() {
        return mAidlUtil;
    }

    /**
     * 连接服务
     *
     * @param context context
     */
    public void connectPrinterService(Context context) {
        this.context = context.getApplicationContext();
        Intent intent = new Intent(context, JbPrintService.class);
        context.getApplicationContext().startService(intent);
        context.getApplicationContext().bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }


    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = JBService.Stub.asInterface(service);
            try {
                mService.registerConnectCallback(new ConnectCallback());
                mService.registerPrinterStatusCallback(new QueryPrinterRealStatus());
            } catch (RemoteException e) {
                Logger.d("gxh", "注册回调失败");
                e.printStackTrace();
            }
        }
    };

    //连接状态回调
    private class ConnectCallback extends JBPrinterConnectCallback.Stub {
        @Override
        public void onConnecting(int i) throws RemoteException {
            printerConnStatus = 1;
        }

        @Override
        public void onDisconnect(int i) throws RemoteException {
            printerConnStatus = 0;
        }

        @Override
        public void onConnected(int i) throws RemoteException {
            printerConnStatus = 2;
        }
    }

    //打印机状态回调
    private class QueryPrinterRealStatus extends JBPrinterRealStatusCallback.Stub {

        @Override
        public void onPrinterRealStatus(int id, int status, int requestCode) throws RemoteException {
            if(requestCode==REQUEST_PRINTER_STATUS){
                printerRealStatus(id, status, requestCode);
            }
        }
    }

    /**
     * 打印机实时状态
     *
     * @param id
     * @param status
     * @param requestCode
     */
    private void printerRealStatus(int id, int status, int requestCode) {
        String str = "打印机";
        if (status == GpDevice.STATE_NO_ERR) {
            str = str + "正常";
        } else {
            if ((byte) (status & GpDevice.STATE_OFFLINE) > 0) {
                str += "脱机";
            }
            if ((byte) (status & GpDevice.STATE_PAPER_ERR) > 0) {
                str += "缺纸";
            }
            if ((byte) (status & GpDevice.STATE_COVER_OPEN) > 0) {
                str += "开盖";
            }
            if ((byte) (status & GpDevice.STATE_ERR_OCCURS) > 0) {
                str += "出错";
            }
            if ((byte) (status & GpDevice.STATE_TIMES_OUT) > 0) {
                str += "查询超时";
            }
        }
        Logger.d("gxh", str);
    }

    /**
     * 获取打印机状态
     * 对应GpPort的状态:0 未知   1:连接中   2:已连接
     *
     * @param connectId
     */
    public void getPrinterStatus(int connectId) {
        if (mService == null) {
            return;
        }
        try {
            printerConnStatus = mService.getPrinterConnectStatus(connectId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取保存的打印机连接状态
     *
     * @param connectId
     * @return
     */
    public int getSavePrinterConnStatus(int connectId) {
        getPrinterStatus(connectId);
        return printerConnStatus;
    }

    /**
     * 查询打印机状态
     * 以回调方式返回
     * 只有打印机已连接(连接状态为2)这个方法调用才有意义，否则也不会回调
     *
     * @param connectId
     */
    public void queryPrinterStatus(int connectId) {
        if (mService == null) {
            return;
        }
        try {
            mService.queryPrinterStatus(connectId, 10 * 1000, REQUEST_PRINTER_STATUS);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 连接打印机
     *
     * @param connectType 连接方式 0:wifi  1:蓝牙  2:usb
     * @param connectId   id为打印服务操作的打印机的id，最大可以操作3台
     * @param ip
     * @param mac
     */
    public void connectPrinter(int connectType, int connectId, String ip, String mac) {
        if (mService == null) {
            return;
        }
        try {
            switch (connectType) {
                case 0:
                    mService.openPort(connectId, PortParameter.ETHERNET, ip, 9100);
                    break;
                case 1:
                    mService.openPort(connectId, PortParameter.BLUETOOTH, mac, 0);
                    break;
                case 2:
                    mService.openPort(connectId, PortParameter.USB, "/dev/bus/usb/001/003", 0);
                    break;
            }
        } catch (RemoteException e) {
            Logger.d("gxh", e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 关闭连接打印机
     *
     * @param printId
     */
    public void closeConnect(int printId) {
        if (mService == null) {
            return;
        }
        try {
            mService.closePort(printId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取打印模式
     *
     * @param printId
     * @return 0:未知  1:票据模式  2:标签模式
     */
    public int getCommandTypes(int printId) {
        int type = GpDevice.UNKNOWN_COMMAND;
        if (mService == null) {
            return type;
        }
        try {
            type = mService.getPrinterCommandType(printId);
        } catch (RemoteException e) {
            e.printStackTrace();
        } finally {
            return type;
        }
    }

    /**
     * 打印测试页
     *
     * @param printId
     */
    public void printTestPage(int printId) {
        if (mService == null) {
            return;
        }
        try {
            mService.printeTestPage(printId);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //字节数组组合操作1
    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }

    //字节数组组合操作2
    public static byte[] byteMerger(byte[][] byteList) {

        int length = 0;
        for (int i = 0; i < byteList.length; i++) {
            length += byteList[i].length;
        }
        byte[] result = new byte[length];

        int index = 0;
        for (int i = 0; i < byteList.length; i++) {
            byte[] nowByte = byteList[i];
            for (int k = 0; k < byteList[i].length; k++) {
                result[index] = nowByte[k];
                index++;
            }
        }
        for (int i = 0; i < index; i++) {
            // CommonUtils.LogWuwei("", "result[" + i + "] is " + result[i]);
        }
        return result;
    }

    /**
     * 快速打印订单
     *
     * @param orderDetailBean
     * @param isRefund
     */
    public void printOrderQ(OrderDetailBean orderDetailBean, boolean isRefund) {
        if (mService == null) {
            return;
        }
        try {
            mService.sendReceiptCommand(0, getOrderData(orderDetailBean, isRefund));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    //销售单
    public byte[] getOrderData(OrderDetailBean orderDetailBean, boolean isRefund) {

        if (orderDetailBean == null) {
            return new byte[1];
        }

        byte[] init = ESCUtil.init_printer();
        byte[] next = ESCUtil.nextLine(1);
        byte[] middle = ESCUtil.middle();
        byte[] left = ESCUtil.left();
        byte[] end = new byte[]{0x1d, 0x56, 0x42};


        byte[] line = new byte[]{
                0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
                0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d,
                0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d, 0x2d
        };

        int totalNum = 0;
        List list = new ArrayList();
        for (int i = 0; i < orderDetailBean.getResult().getCommdityDetailsList().size(); i++) {
            OrderDetailBean.ResultBean.CommdityDetailsListBean commdityDetailsListBean = orderDetailBean.getResult().getCommdityDetailsList().get(i);

            totalNum = totalNum + commdityDetailsListBean.getCount();

            //商品名
            list.add(ESCUtil.strToBytes(commdityDetailsListBean.getGoodsType() == 4 ?
                    "(限时折扣)" + commdityDetailsListBean.getGoodName() : commdityDetailsListBean.getGoodName()));
            //换行
            list.add(next);

            list.add(ESCUtil.strToBytes(
                    ESCUtil.printThreeData(commdityDetailsListBean.getOriginalUnitPriceStr(),
                            "x" + commdityDetailsListBean.getCount(),
                            commdityDetailsListBean.getPresentTotalStr())
            ));
            list.add(next);
            list.add(ESCUtil.strToBytes(commdityDetailsListBean.getPresentUnitPriceStr()));
            list.add(next);
            list.add(line);
            list.add(next);
        }

        list.add(ESCUtil.strToBytes("总    数: " + totalNum));
        list.add(next);
        list.add(ESCUtil.strToBytes("合    计: " + orderDetailBean.getResult().getOriginalPriceStr()));
        list.add(next);

        //优惠
        list.add(ESCUtil.strToBytes("优    惠: "));
        list.add(getGoodsPreference(orderDetailBean.getResult().getAmountReduceStr(),
                orderDetailBean.getResult().getSourceList()));

        //计算优惠
        if (!ListUtil.isEmpty(orderDetailBean.getResult().getSourceList())) {
            for (OrderDetailBean.ResultBean.SourceListBean bean : orderDetailBean.getResult().getSourceList()) {
                list.add(next);
                list.add(getFormatPreferenceText(bean));
            }
        }


        byte[][] content = new byte[list.size()][1024];
        for (int i = 0; i < list.size(); i++) {
            content[i] = (byte[]) list.get(i);
        }

        byte[][] cmdBytes = {
                init,
                next,
                middle,
                ESCUtil.fontSize(),
                ESCUtil.boldOn(),
                ESCUtil.strToBytes("测试店铺"),

                next,
                ESCUtil.boldOff(),
                left,

                init,
                ESCUtil.strToBytes("销售单" + (isRefund ? "(已退款)" : "")),
                next,
                ESCUtil.strToBytes("销售单号:" + orderDetailBean.getResult().getOrderNumber()),
                next,
                ESCUtil.strToBytes("交易时间:" + orderDetailBean.getResult().getCreationTimeStr()),
                next,
                ESCUtil.strToBytes("收 银 员:" + orderDetailBean.getResult().getCreatorUserPhone()),
                next,
                ESCUtil.strToBytes("顾    客:" + orderDetailBean.getResult().getCustomerPhone()),
                next, next,
                ESCUtil.strToBytes("商品名称"),
                next,
                ESCUtil.strToBytes(ESCUtil.printThreeData("单价", "数量", "金额")),
                next,
                line,
                next,

                byteMerger(content),

                left,

                next,
                line,
                ESCUtil.strToBytes("赠送积分: " + (
                        TextUtils.isEmpty(orderDetailBean.getResult().getRewardIntegral()) ? "0" :
                                orderDetailBean.getResult().getRewardIntegral())),
                next,
                ESCUtil.strToBytes("可用积分: " + orderDetailBean.getResult().getIntegralAmount()),
                next,
                line,

                next,
                left,
                ESCUtil.strToBytes("应    收: "),
                ESCUtil.strToBytes(orderDetailBean.getResult().getPresentPriceStr()),
                next,
                ESCUtil.strToBytes("实    收: "),
                ESCUtil.strToBytes(orderDetailBean.getResult().getActualPriceStr()),
                next,
                ESCUtil.strToBytes("找    零: "),
                ESCUtil.strToBytes(orderDetailBean.getResult().getOddChange() + ""),
                next,

                ESCUtil.strToBytes("支付方式: "),
                ESCUtil.strToBytes(orderDetailBean.getResult().getPayTypeStr()),
                next, next,

                middle,
                ESCUtil.strToBytes("1积分=1元"),
                next,
                middle,
                ESCUtil.strToBytes("谢谢惠顾,欢迎下次光临!"),
                next,
                left,
                init,

                next, next,
                end,
                next, next, next
        };
        return byteMerger(cmdBytes);
    }


    /**
     * 计算优惠
     *
     * @param amountReduceStr
     * @param sourceList
     * @return
     */
    private static byte[] getGoodsPreference(String amountReduceStr,
                                             List<OrderDetailBean.ResultBean.SourceListBean> sourceList) {
        if (ListUtil.isEmpty(sourceList)) {
            return ESCUtil.strToBytes(amountReduceStr);
        }

        for (OrderDetailBean.ResultBean.SourceListBean bean : sourceList) {
            if (bean.getPreferentialType() != 2) {
                amountReduceStr = CalculateUtil.safeSubtract(true,
                        CalculateUtil.toBigDecimal(amountReduceStr),
                        CalculateUtil.toBigDecimal(bean.getPreferentialPrice())).doubleValue() + "";
            }
        }
        return ESCUtil.strToBytes(amountReduceStr);
    }


    /**
     * 格式化
     *
     * @param bean
     * @return
     */
    private static byte[] getFormatPreferenceText(OrderDetailBean.ResultBean.SourceListBean bean) {
        StringBuilder sbStr = new StringBuilder();
        String name = bean.getPreferentialTypeStr().trim();
        if (name.length() == 2) {
            sbStr.append(name.charAt(0));
            sbStr.append("    ");
            sbStr.append(name.charAt(1));
        } else if (name.length() == 3) {
            sbStr.append(name.charAt(0));
            sbStr.append(" ");
            sbStr.append(name.charAt(1));
            sbStr.append(" ");
            sbStr.append(name.charAt(2));
        } else {
            sbStr.append(name);
        }

        String price = bean.getPreferentialPrice();
        if (bean.getPreferentialType() == 1 && bean.getPreferentialDiscountType() == 1) {//整单
            price = bean.getPreferentialDiscountPrice() + "折(" + price + ")";
        }
        return ESCUtil.strToBytes(sbStr.toString() + ": " + price);
    }


    /**
     * 一些情况的的测试说明：
     * 1.已连接，安卓设备关掉蓝牙，打印机连接状态为0，回调ConnectCallback
     * 2.已连接，打印机关机，打印机状态为0，回调ConnectCallback。但是这有个时间差，打印机状态不会立即改变。
     * 3.上述两种情况后，开蓝牙或者打印机开机，不会再影响打印机状态。需要代码重新连接。
     * 4.只有在打印机连接状态为2(已连接)情况下，其它操作才有意义(包括查询打印机状态，但是不包括获取打印机连接状态那个方法)。
     *
     */

}
