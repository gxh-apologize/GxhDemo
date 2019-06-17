package cn.gxh.base;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.microsoft.signalr.Action;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

import cn.gxh.service.AlarmService;
import cn.gxh.service.NetworkConnectChangedReceiver;
import cn.gxh.view.R;
import cn.gxh.view.SpeechManger;
import cn.gxh.view.base.Global;
import cn.gxh.view.base.MyApp;
import cn.gxh.view.base.RecordUtil;
import cn.gxh.view.base.SaveAccess;
import cn.gxh.view.base.SaveAccessRecord;
import cn.gxh.view.goo.GooView;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Logger.d("gxh", "initView");
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        loadRootFragment(R.id.flt_content, MainFragment.newInstance());

        registerNetworkConnectChangeReceiver();
    }


    private void upJson() {
        new Thread() {
            public void run() {
                HttpClient client = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://yzxy.hyzschina.com/api/mobile/SaveAccessRecord");
                //设置请求头， 设置请求体的数据类型,对于传递的是json数据，要写"Content-Type", "application/json" 类型
                httpPost.addHeader("Content-Type", "application/json");

                //需要删除的
                List<String> deleteList = new ArrayList();

                for (RecordUtil.OrderRegist orderRegist : MyApp.recordUtil.mRegister) {
                    String time = orderRegist.mTime;
                    Logger.d("gxh", "11111");
                    try {
                        //设置请求体,且告诉服务端传递的数据格式
                        HttpEntity entity = new StringEntity(new Gson().toJson(orderRegist.mCartList.get(time)));
                        httpPost.setEntity(entity);
                        // 执行请求
                        HttpResponse response = client.execute(httpPost);
                        if (response.getStatusLine().getStatusCode() == 200) {
                            // 取内容
                            String result = EntityUtils.toString(response.getEntity());
                            SaveAccess saveAccess = new Gson().fromJson(result, SaveAccess.class);

                            Logger.d("gxh", saveAccess.getData() + "");
                            if (saveAccess.getData()) {
                                //上传成功后则删除
                                deleteList.add(time);
                            } else {
                                //break;
                            }
                        } else {
                            break;//没有上传成功，则不再上传
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Logger.d("gxh", "22222");
                    }
                }

                Logger.d("gxh", "33333");
                //删除
                for (int i = 0; i < deleteList.size(); i++) {
                    MyApp.recordUtil.delete(deleteList.get(i), "/record.txt");
                }
            };
        }.start();
    }


    private void registerNetworkConnectChangeReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
//        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
//        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
//        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
//        NetworkConnectChangedReceiver mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
//        registerReceiver(mNetworkConnectChangedReceiver, filter);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("--------onDestroy111-------");
        Logger.d("gxh", "onDestroy111");
    }

}
