package cn.gxh.view.base;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.gxh.base.Logger;
import cn.gxh.base.SplashActivity;
import cn.gxh.print.AidlUtil;
import cn.gxh.view.SpeechManger;

public class MyApp extends Application {


    //这个值是区别特定平板和其它设备的，因为其他设备调用jar包方法会抛异常
    public static boolean isCanSetIo=true;
    public static RecordUtil recordUtil;

    @Override
    public void onCreate() {
        super.onCreate();

        Global.init(this);

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(Global.mContext);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);


        recordUtil=new RecordUtil(this.getExternalCacheDir().getAbsolutePath());
        //ANRCacheHelper.registerANRReceiver(this);
        //CrashHandler.getInstance().init(this);
        //initCrash(this);

        //SpeechManger.getInstance().init(this);

        //初始化打印
        AidlUtil.getInstance().connectPrinterService(this);
    }

    private void initCrash(Context context){
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback() {
            public Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                map.put("Key", "Value");
                Logger.d("gxh","onCrashHandleStart:"+crashType);

                return map;
            }

            @Override
            public byte[] onCrashHandleStart2GetExtraDatas(int crashType, String errorType,
                                                           String errorMessage, String errorStack) {

                Logger.d("gxh","onCrashHandleStart2GetExtraDatas:"+crashType);
                //Native异常强制重启，不上传
                if(crashType==2){
                    reStart();
                }
                try {
                    return "Extra data.".getBytes("UTF-8");
                } catch (Exception e) {
                    return null;
                }
            }

        });

        CrashReport.initCrashReport(this, "b03aefd948", false,strategy);
    }

    private void reStart(){
        Intent intent = new Intent(this, SplashActivity.class);
        @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(
                this, 0, intent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        //退出程序
        AlarmManager mgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
                restartIntent); // 1秒钟后重启应用

        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
