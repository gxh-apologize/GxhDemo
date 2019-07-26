package cn.gxh.base;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.gxh.classloader.ClassLoaderFragment;
import cn.gxh.print.PrintFragment;
import cn.gxh.property.FirstAdapter;
import cn.gxh.property.Lsn11.CompressFragment;
import cn.gxh.property.Lsn13.FlatcFragment;
import cn.gxh.property.Lsn14.ThreadFragment;
import cn.gxh.property.camera.CameraFragment;
import cn.gxh.property.gif.GifFragment;
import cn.gxh.property.ip.IpFragment;
import cn.gxh.property.lb.LbFragment;
import cn.gxh.property.lb2.Lb2Fragment;
import cn.gxh.property.map.MapFragment;
import cn.gxh.property.message.MessageFragment;
import cn.gxh.property.sensor.SensorFragment;
import cn.gxh.view.R;
import cn.gxh.view.base.Global;
import cn.gxh.view.base.MyApp;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created  by gxh on 2019/2/11 14:56
 */
public class FirstFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private FirstAdapter firstAdapter;

    public static FirstFragment newInstance() {
        Bundle args = new Bundle();
        FirstFragment fragment = new FirstFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_first;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        recyclerView = findView(R.id.rlv_fragment_first);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        firstAdapter = new FirstAdapter(null);
        firstAdapter.bindToRecyclerView(recyclerView);

    }

    @Override
    public void initListener() {

        firstAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                clickItem(position);
            }
        });
    }

    private void clickItem(int position) {
        SupportFragment supportFragment = null;
        switch (position) {
            case 0:
                break;
            case 1:
                supportFragment = CompressFragment.newInstance();
                break;
            case 2:
                supportFragment = FlatcFragment.newInstance();
                break;
            case 3:
                supportFragment = ThreadFragment.newInstance();
                break;
            case 4:
                supportFragment = GifFragment.newInstance();
                break;
            case 5:
                supportFragment = MessageFragment.newInstance();
                break;
            case 6:
                supportFragment = ClassLoaderFragment.newInstance();
                break;
            case 7:
                supportFragment = PrintFragment.newInstance();
                break;
            case 8:
                supportFragment = IpFragment.newInstance();
                break;
            case 9:
                supportFragment = cn.gxh.property.thread.ThreadFragment.newInstance();
                break;
            case 10:
                supportFragment = MapFragment.newInstance();
                break;
            case 11:
                supportFragment = CameraFragment.newInstance();
                break;
            case 12:
                supportFragment = SensorFragment.newInstance();
                break;
            case 13:
                supportFragment = Lb2Fragment.newInstance();
                break;
        }

        ((MainFragment) getParentFragment()).startBrotherFragment(supportFragment);
    }


    private void reStart(Context context){
        Intent intent = new Intent(context, SplashActivity.class);
        @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(
                context, 0, intent,
                Intent.FLAG_ACTIVITY_NEW_TASK);
        //退出程序
        AlarmManager mgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000,
                restartIntent); // 1秒钟后重启应用

        // 杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void initData() {

        List list = new ArrayList();
        list.add("Lsn1");
        list.add("Lsn11  Bitmap内存管理及优化");
        list.add("Lsn13  数据传输");
        list.add("Lsn14  多线程优化");
        list.add("gif  加载gif");
        list.add("通知");
        list.add("插件化");
        list.add("佳博打印机");
        list.add("ip地址");
        list.add("并发编程艺术");
        list.add("百度地图定位相关");
        list.add("Camera");
        list.add("感应器");
        list.add("轮播");
        firstAdapter.setNewData(list);
    }
}
