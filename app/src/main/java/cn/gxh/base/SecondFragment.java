package cn.gxh.base;

import android.os.Bundle;

import cn.gxh.view.R;

/**
 * Created  by gxh on 2019/2/11 14:56
 */
public class SecondFragment extends BaseFragment {

    public static SecondFragment newInstance() {
        Bundle args = new Bundle();
        SecondFragment fragment = new SecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_second;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
