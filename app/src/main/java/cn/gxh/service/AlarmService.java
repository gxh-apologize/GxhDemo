package cn.gxh.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.TimeUtils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import cn.gxh.base.TimeUtil;

public class AlarmService extends Service {

    private AlarmManager am;
    private PendingIntent pi1;
    private PendingIntent pi2;
    private PendingIntent pi3;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initAlarm();
    }

    private void initAlarm() {
        am = (AlarmManager) getSystemService(ALARM_SERVICE);
        //周期性执行的定时任务
        Intent i = new Intent(this, AlarmReceiver.class);
        i.putExtra("time", "time");

        pi1 = PendingIntent.getBroadcast(this, 111, i, 0);
        pi2 = PendingIntent.getBroadcast(this, 222, i, 0);
        pi3 = PendingIntent.getBroadcast(this, 333, i, 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Calendar instance = Calendar.getInstance();
//            int currentHour = instance.get(Calendar.HOUR_OF_DAY);
//            int currentMinute = instance.get(Calendar.MINUTE);
//
//            if (currentHour <= 8 && currentMinute <= 55) {
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(8, 55, 0,false).getTimeInMillis(), 1000, pi1);
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(14, 5, 0,false).getTimeInMillis(), 1000, pi2);
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(20, 5, 0,false).getTimeInMillis(), 1000, pi3);
//            }else if(currentHour <= 14 && currentMinute <= 5){
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(14, 5, 0,false).getTimeInMillis(), 1000, pi2);
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(20, 5, 0,false).getTimeInMillis(), 1000, pi3);
//            }else if(currentHour <= 16 && currentMinute <= 57){
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(16, 57, 0,false).getTimeInMillis(), 1000, pi3);
//            }else {
//                //只能定时明天的了
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(8, 55, 0,true).getTimeInMillis(), 1000, pi1);
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(14, 5, 0,true).getTimeInMillis(), 1000, pi2);
//                am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(20, 5, 0,true).getTimeInMillis(), 1000, pi3);
//            }

            am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(17, 18, 0,false).getTimeInMillis(), 1000, pi2);
            am.setWindow(AlarmManager.RTC_WAKEUP, getCalendar(17, 20, 0,false).getTimeInMillis(), 1000, pi3);
        }

//        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(8,55,0).getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi1);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(14,5,0).getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi2);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(20,5,0).getTimeInMillis(),AlarmManager.INTERVAL_DAY, pi3);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;//系统回收后不重启
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        am.cancel(pi1);
        am.cancel(pi2);
        am.cancel(pi3);
    }

    private Calendar getCalendar(int hour, int minute, int second,boolean isAdd) {
        Calendar instance = Calendar.getInstance();
        if(isAdd){
            instance.add(instance.DATE,1);
        }

        instance.set(Calendar.HOUR_OF_DAY, hour);//小时
        instance.set(Calendar.MINUTE, minute);//分钟
        instance.set(Calendar.SECOND, second);//秒
        return instance;
    }
}

