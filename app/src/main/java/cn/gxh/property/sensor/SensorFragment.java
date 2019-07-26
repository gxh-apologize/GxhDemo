package cn.gxh.property.sensor;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;

import com.blankj.utilcode.util.FileUtils;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.common.util.Constant;
import cn.gxh.property.db.GreenDaoHelper;
import cn.gxh.property.db.ImageUtil;
import cn.gxh.property.db.User;
import cn.gxh.view.R;

public class SensorFragment extends BaseFragment {

    Banner banner;
    String base64;

    public static SensorFragment newInstance() {
        return new SensorFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_sensor;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        banner=findView(R.id.banner);

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

//        SensorManager sensorManager= (SensorManager) mActivity.getSystemService(Context.SENSOR_SERVICE);
//        //获取当前设备支持的传感器列表
//        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
//
//        //遍历
//        for(Sensor sensor:allSensors){
//            Logger.d("gxh",sensor.getName());
//        }
//
//        double d=safeMultiply(Double.parseDouble("398.22"),Double.parseDouble("0.8"),0).doubleValue();
//        Logger.d("gxh",((17<<1))+"");
//
//
//        StringBuffer sb=new StringBuffer();
//        sb.append("hello");
//        Logger.d("gxh","length:"+sb.length());
//        sb.append(12);
//        Logger.d("gxh","length:"+sb.length());
//        sb.append(12.45);
//        Logger.d("gxh","length:"+sb.length());
//        sb.append("中文");
//        Logger.d("gxh","length:"+sb.length());
//
//        initBanner();


        GreenDaoHelper.getInstance().init(mActivity);


        //获取base64
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.liyifeng1);
        base64=ImageUtil.bitmapoTBase64(bitmap);


//        for(int i=0;i<100;i++){
//            User user=new User();
//            user.setId(null);
//            user.setUserid("00000"+i);
//            user.setName("百里屠苏_"+i);
//            user.setPassword("123456");
//            user.setAvatar(base64);
//
//            boolean isInsert= GreenDaoHelper.getInstance().insertUser(user);
//            Logger.d("gxh",isInsert+"__"+i);
//        }


       getUsers();

    }


    private void getUsers(){
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<100;i++){
                            User user=new User();
                            user.setId(null);
                            user.setUserid("00000"+i);
                            user.setName("百里屠苏_"+i);
                            user.setPassword("123456");
                            user.setAvatar(base64);

                            boolean isInsert= GreenDaoHelper.getInstance().insertUser(user);
                            Logger.d("gxh",isInsert+"__"+i);
                        }

                        Logger.d("gxh","hh");
                    }
                }
        ).start();


//        GreenDaoHelper.getInstance().getUserByCount();
//        Logger.d("gxh","hh");
    }


    public <T extends Number> BigDecimal safeMultiply(T b1, T b2, int scale) {
        if (null == b1 || null == b2) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(b1.doubleValue()).multiply(BigDecimal.valueOf(b2.doubleValue())).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }


    private ArrayList<String> list_path;
    private ArrayList<String> list_title;

    private void initBanner() {
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

        list_path.add("http://hytest.hyzschina.com/banner/1.png");
        list_title.add("1");

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.LEFT)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                //必须最后调用的方法，启动轮播图。
                .start();
        banner.startAutoPlay();
    }

    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            imageView.setAdjustViewBounds(true);
            Glide.with(context).load((String) path).fitCenter().into(imageView);
            //ImageLoaderUtils.display(context, imageView, (String) path);
        }
    }


//Gravity sensor  重力感应器
//Compass Magnetic field sensor
//Compass Orientation  sensor
//Light sensor  光线传感器
//Gyroscope sensor 陀螺仪传感器
//Game Rotation Vector Sensor  旋转矢量
//GeoMag Rotation Vector Sensor
//Linear Acceleration Sensor
//Rotation Vector Sensor
//Orientation Sensor  方向传感器




//MPL Gyroscope
//MPL Gyroscope - Wakeup
//MPL Raw Gyroscope
//MPL Raw Gyroscope - Wakeup
//MPL Accelerometer
//MPL Accelerometer - Wakeup
//MPL Magnetic Field
//MPL Orientation
//MPL Rotation Vector
//MPL Game Rotation Vector
//MPL Game Rotation Vector - Wakeup
//MPL Linear Acceleration
//MPL Gravity
//MPL Significant Motion
//MPL Step Detector
//MPL Step Counter
//MPL Geomagnetic Rotation Vector
//MTK Proximity
//MTK Proximity non
//MTK Light
}
