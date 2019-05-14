package cn.gxh.property.map;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.view.R;
import cn.gxh.view.base.Global;

/**
 * Created  by gxh on 2019/5/10 14:30
 */
public class Map1Fragment extends BaseFragment {


    private MapView mapView;
    private BaiduMap map;
    private LocationClient locationClient;
    private Point centerPoint;
    private boolean isFirstLoc = true;
    //地理编码
    private GeoCoder geoCoder;
    //当前经纬度
    private double lantitude;
    private double longtitude;
    //Poi列表
    private List<PoiInfo> poiList = new ArrayList<>();
    private PoiAdapter poiAdapter;

    public static Map1Fragment newInstance() {
        return new Map1Fragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_map1;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        RecyclerView recyclerView = findView(R.id.rlv_fragment_map1);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        poiAdapter = new PoiAdapter(null);
        recyclerView.setAdapter(poiAdapter);

        mapView = findView(R.id.bmapView);
        map = mapView.getMap();

        //地理编码
        geoCoder = GeoCoder.newInstance();
    }

    @Override
    public void initListener() {

        //地图状态改变
        map.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {

            }
        });

        //点击监听
        map.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        //长按
        map.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

            }
        });

        //点击覆盖物
        map.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                return false;
            }
        });

        //触摸地图
        map.setOnMapTouchListener(new BaiduMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    // 获取当前MapView中心屏幕坐标相应的地理坐标
                    LatLng currentLatLng;
                    currentLatLng = map.getProjection().fromScreenLocation(
                            centerPoint);
                    // 发起反地理编码检索
                    geoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                            .location(currentLatLng));
                }
            }
        });

        //地图加载完成
        map.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {

            }
        });

        //地图定位图标点击事件监听接口
        map.setOnMyLocationClickListener(new BaiduMap.OnMyLocationClickListener() {
            @Override
            public boolean onMyLocationClick() {
                return false;
            }
        });

        //地理编码监听
        geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (geoCodeResult == null || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有检索到结果
                    return;
                } else {
                    double latitude = geoCodeResult.getLocation().latitude;
                    double longitude = geoCodeResult.getLocation().longitude;
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //没有找到检索结果
                    return;
                } else {

                    //当前
                    PoiInfo currentPoiInfo = new PoiInfo();
                    currentPoiInfo.address = reverseGeoCodeResult.getAddress();
                    currentPoiInfo.location = reverseGeoCodeResult.getLocation();
                    currentPoiInfo.name = "[位置]";

                    //刷新页面
                    poiList.clear();
                    poiList.add(currentPoiInfo);
                    poiList.addAll(reverseGeoCodeResult.getPoiList());
                    poiAdapter.setNewData(poiList);
                }
            }
        });

        poiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                click(position);
            }
        });


    }



    @Override
    public void initData() {
        //定位
        initLocationClient();
        setLocationConfig();

        //map可以设置三种地图类型，以及路况，这里忽略

        setMapConfig();
        //开启地图定位图层
        map.setMyLocationEnabled(true);

        //中心坐标
        centerPoint = map.getMapStatus().targetScreen;


        locationClient.start();//开始定位
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        locationClient.stop();
        mapView.onDestroy();
        geoCoder.destroy();
        super.onDestroy();
    }

    private void setMapConfig() {
        MyLocationConfiguration config = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, false,
                null, 0xAAFFFF88, 0xAA00FF00
        );
        map.setMyLocationConfiguration(config);


        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f);
        map.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    private void initLocationClient() {
        locationClient = new LocationClient(Global.mContext);
        locationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                //mapView 销毁后不在处理新接收的位置
                if (bdLocation == null || map == null) {
                    return;
                }

                Logger.d("gxh", bdLocation.getAddrStr());
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(bdLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(bdLocation.getDirection()).latitude(bdLocation.getLatitude())
                        .longitude(bdLocation.getLongitude()).build();
                map.setMyLocationData(locData);

                lantitude = bdLocation.getLatitude();
                longtitude = bdLocation.getLongitude();

                LatLng currentLatLng = new LatLng(lantitude, longtitude);

                // 是否第一次定位
                if (isFirstLoc) {
                    isFirstLoc = false;
                    // 实现动画跳转
                    MapStatusUpdate u = MapStatusUpdateFactory
                            .newLatLng(currentLatLng);
                    map.animateMapStatus(u);

                    geoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                            .location(currentLatLng));
                    return;
                }
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
        option.setScanSpan(0);
        option.setOpenGps(true);

        //需要位置信息,返回省市县街道等
        option.setIsNeedAddress(true);
        //位置描述
        option.setIsNeedLocationDescribe(true);
        //获取周边POI信息  需要联网
        option.setIsNeedLocationPoiList(true);

        locationClient.setLocOption(option);
    }


    private void click(int position) {
        PoiInfo poiInfo = poiAdapter.getData().get(position);
        map.clear();

        //动画跳转
        MapStatusUpdate u=MapStatusUpdateFactory.newLatLng(poiInfo.getLocation());
        map.animateMapStatus(u);

        //加入覆盖物
        BitmapDescriptor selectPic= BitmapDescriptorFactory.fromResource(R.drawable.circle);
        OverlayOptions oo=new MarkerOptions().position(poiInfo.getLocation())
                .icon(selectPic).anchor(0.5f,0.5f);
        map.addOverlay(oo);

        //发起地理编码
        geoCoder.reverseGeoCode((new ReverseGeoCodeOption())
                .location(poiInfo.getLocation()));

    }
}
