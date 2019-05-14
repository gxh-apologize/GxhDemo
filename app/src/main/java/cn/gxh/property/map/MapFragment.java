package cn.gxh.property.map;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import java.util.List;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.base.MainActivity;
import cn.gxh.property.thread.ThreadFragment;
import cn.gxh.view.R;
import cn.gxh.view.base.Global;

/**
 * 百度地图相关
 * Created  by gxh on 2019/5/10 12:06
 */
public class MapFragment extends BaseFragment {

    public static MapFragment newInstance() {
        return new MapFragment();
    }


    private LocationClient locationClient;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_map;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Button btn01 = findView(R.id.btn_01);
        btn01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationClient.start();
            }
        });

        Button btn02 = findView(R.id.btn_02);
        btn02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(Map1Fragment.newInstance());
            }
        });

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        initLocationClient();
        setLocationConfig();
        //seNotification();
    }

    private void initLocationClient() {
        locationClient = new LocationClient(Global.mContext);
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                double latitude = bdLocation.getLatitude();    //获取纬度信息
                double longitude = bdLocation.getLongitude();    //获取经度信息
                float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f

                String coorType = bdLocation.getCoorType();  //获取经纬度坐标类型

                int errorCode = bdLocation.getLocType();

                //地址 : 中国北京市大兴区地盛西路
                String address = bdLocation.getAddrStr();
                //位置描述 : 在北京经开北工大软件园附近
                String describe = bdLocation.getLocationDescribe();

                Logger.d("gxh", latitude + ";" + longitude + ";" + radius + ";" + coorType + ";" + errorCode + ";" + address + ";" + describe);

                //POI
                List<Poi> poiList = bdLocation.getPoiList();
                for (Poi poi : poiList) {
                    Logger.d("gxh", poi.getName() + ";" + poi.getRank());
                }
            }

            //如果我没有自己打开gps，这个方法没有回调
            //如果我使用wifi和数据流量，回调这个方法 161,1
            @Override
            public void onLocDiagnosticMessage(int i, int i1, String s) {
                super.onLocDiagnosticMessage(i, i1, s);
                //161;1;NetWork location successful, open gps will be better!
                Logger.d("gxh",i+";"+i1+";"+s);
            }
        });
    }

    private void setLocationConfig() {
        LocationClientOption option = new LocationClientOption();
        //定位模式:高精度、低功耗、仅用设备
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //设置返回经纬度坐标
        option.setCoorType("bd09ll");
        //设置发起定位请求的间隔
        option.setScanSpan(1000);
        option.setOpenGps(true);//还需要自己打开gps，使用gps定位会比较准

        //需要位置信息,返回省市县街道等
        option.setIsNeedAddress(true);
        //位置描述
        option.setIsNeedLocationDescribe(true);
        //获取周边POI信息  需要联网
        option.setIsNeedLocationPoiList(true);

        locationClient.setLocOption(option);
    }

    /**
     * 这个是对8.0后台定位频率限制所做的适配
     * 如果我们不需要后台定位，可以不用这个
     */
    private void seNotification() {
        //开启前台定位服务：

        Notification.Builder builder = new Notification.Builder(mActivity.getApplicationContext());
        //获取一个Notification构造器

        Intent nfIntent = new Intent(mActivity.getApplicationContext(), MainActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(mActivity, 0, nfIntent, 0)) // 设置PendingIntent
                .setContentTitle("正在进行后台定位") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                .setContentText("后台定位通知") // 设置上下文内容
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = null;
        notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        locationClient.enableLocInForeground(1001, notification);// 调起前台定位
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.stop();
    }
}
