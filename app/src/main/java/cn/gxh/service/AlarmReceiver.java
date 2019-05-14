package cn.gxh.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.util.Calendar;

import cn.gxh.base.Logger;
import cn.gxh.view.base.Global;

import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //api19以上重写定时，以达到循环的效果
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setWindow();
        }

        String action=intent.getAction();
        String time=intent.getStringExtra("time");
        Logger.d("gxh","onReceive:"+action+";"+time);
    }

//    private void upJson() {
//        new Thread() {
//            public void run() {
//                HttpClient client = new DefaultHttpClient();
//                HttpPost httpPost = new HttpPost(Constant.ACCESS_RECORD);
//                //设置请求头， 设置请求体的数据类型,对于传递的是json数据，要写"Content-Type", "application/json" 类型
//                httpPost.addHeader("Content-Type", "application/json");
//
//                //需要删除的
//                List<String> deleteList=new ArrayList();
//
//                for (RecordUtil.OrderRegist orderRegist : MyApp.recordUtil.mRegister) {
//                    String time = orderRegist.mTime;
//                    try {
//                        //设置请求体,且告诉服务端传递的数据格式
//                        HttpEntity entity = new StringEntity(new Gson().toJson(orderRegist.mCartList.get(time)));
//                        httpPost.setEntity(entity);
//                        // 执行请求
//                        HttpResponse response = client.execute(httpPost);
//                        if (response.getStatusLine().getStatusCode() == 200) {
//                            // 取内容
//                            String result = EntityUtils.toString(response.getEntity());
//                            SaveAccess saveAccess = new Gson().fromJson(result, SaveAccess.class);
//                            if (saveAccess.getData()) {
//                                //上传成功后则删除
//                                deleteList.add(time);
//                            } else {
//                                break;
//                            }
//                        } else {
//                            break;//没有上传成功，则不再上传
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                //删除
//                for(int i=0;i<deleteList.size();i++) {
//                    MyApp.recordUtil.delete(deleteList.get(i), Constant.RECORD);
//                }
//            };
//        }.start();
//    }


    private void setWindow() {
        AlarmManager am;
        PendingIntent pi1;
        PendingIntent pi2;
        PendingIntent pi3;
        am = (AlarmManager) Global.mContext.getSystemService(ALARM_SERVICE);
        //周期性执行的定时任务
        Intent i = new Intent(Global.mContext, AlarmReceiver.class);

        pi1 = PendingIntent.getBroadcast(Global.mContext, 111, i, 0);
        pi2 = PendingIntent.getBroadcast(Global.mContext, 222, i, 0);
        pi3 = PendingIntent.getBroadcast(Global.mContext, 333, i, 0);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setExact(AlarmManager.RTC_WAKEUP, getCalendar(8, 55, 0).getTimeInMillis(), pi1);
            am.setExact(AlarmManager.RTC_WAKEUP, getCalendar(14, 5, 0).getTimeInMillis(), pi2);
            am.setExact(AlarmManager.RTC_WAKEUP, getCalendar(20, 5, 0).getTimeInMillis(), pi3);
        }

//        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(8, 55, 0).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi1);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(14, 5, 0).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi2);
//        am.setRepeating(AlarmManager.RTC_WAKEUP, getCalendar(20, 5, 0).getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi3);
    }

    private Calendar getCalendar(int hour, int minute, int second) {
        Calendar instance = Calendar.getInstance();
        instance.add(instance.DATE,1);
        instance.set(Calendar.HOUR_OF_DAY, hour);//小时
        instance.set(Calendar.MINUTE, minute);//分钟
        instance.set(Calendar.SECOND, second);//秒
        return instance;
    }


    private void deleteVisitor() {
//        List<VisitorBean> visitorBeanList = query();
//        if (!visitorBeanList.isEmpty()) {
//            for (int i = 0; i < visitorBeanList.size(); i++) {
//                //比较结束时间与当前时间
//                String currentTime = TimeUtil.getStringByFormat(System.currentTimeMillis(),
//                        TimeUtil.dateFormatYMDHMS);
//                int result = TimeUtil.compare_date(visitorBeanList.get(i).getEndtime(), currentTime);
//                if (result == -1) {//表示过期
//                    MyApp.baseDao2.delete(visitorBeanList.get(i));
//                    FileUtil.deleteFile(visitorBeanList.get(i).getAvatarstr());
//                }
//            }
//        }

        //同步数据到缓存
        //MyApp.visitorList = query();
    }

//    private List<VisitorBean> query() {
////        VisitorBean bean = new VisitorBean();
////        bean.setUser_Id(1);
////        return MyApp.baseDao2.query(bean);
//    }
}
