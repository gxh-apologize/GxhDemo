package cn.gxh.property.ip;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.widget.TextView;

import com.blankj.utilcode.util.FileIOUtils;

import java.io.File;
import java.util.List;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.common.network.NetChange;
import cn.gxh.common.network.NetType;
import cn.gxh.common.network.NetworkManager;
import cn.gxh.view.R;
import cn.gxh.view.base.Global;

/**
 * Created  by gxh on 2019/5/5 16:28
 */
public class IpFragment extends BaseFragment {

    /**
     * 创建数据库和表
     */
    private PersonDBOpenHelper personDBOpenHelper;
    /**
     * 操作表
     */
    private PersonDBDao personDBDao;


    public static IpFragment newInstance() {
        return new IpFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_ip;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        TextView tvContent = findView(R.id.tv_fragment_ip_content);
//        String ipAddress = NetworkUtils.getIPAddress(true);
//        tvContent.setText(TextUtils.isEmpty(ipAddress) ? "没有获取到" : ipAddress);
    }

    @Override
    public void initListener() {


    }

    @Override
    public void initData() {

        NetworkManager.getInstance().initByBroadcast((Application) Global.mContext);
        NetworkManager.getInstance().registerObserver(this);

        saveLog("2019-12-23 12:22:33 ;");


        //设置值
        personDBOpenHelper = new PersonDBOpenHelper(Global.mContext);
        //获取一个读写的数据库
        SQLiteDatabase db = personDBOpenHelper.getWritableDatabase();
        personDBDao = new PersonDBDao(Global.mContext);


//        Person person = new Person(1001, "李易峰", "13521870266");
//        personDBDao.add(person);
//
//        Person person1 = new Person(1002, "百里屠苏", "13521870269");
//        personDBDao.add(person1);
//        Person person2 = new Person(1004, "木子李", "13521870290");
//        personDBDao.add(person2);


        List<Person> personList = personDBDao.query("李");
        Logger.d("gxh",personList.size()+"");
        for (Person p : personList) {
            Logger.d("gxh",p.getUsername()+";"+p.getPassword()+";"+p.getId());
        }
    }

    @NetChange(netType = NetType.AUTO)
    public void netChange(NetType netType) {
        Logger.d("gxh", "fragment:" + netType);
        switch (netType) {
            case AUTO:
                break;
            case WIFI:
                break;
            case CMNET:
            case CMWAP:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().unRegisterObserver(this);
    }


    /**
     * 保存日志
     *
     * @param content
     */
    public static void saveLog(String content) {
        String logPath = Environment.getExternalStorageDirectory() + File.separator + "alarm_log" + ".txt";
        FileIOUtils.writeFileFromString(logPath, content, true);
    }
}
