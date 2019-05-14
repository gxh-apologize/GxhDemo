package cn.gxh.property.ip;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;

import cn.gxh.base.BaseFragment;
import cn.gxh.print.PrintFragment;
import cn.gxh.view.R;

/**
 * Created  by gxh on 2019/5/5 16:28
 */
public class IpFragment extends BaseFragment {

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
        String ipAddress = NetworkUtils.getIPAddress(true);
        tvContent.setText(TextUtils.isEmpty(ipAddress) ? "没有获取到" : ipAddress);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
