package cn.gxh.property.screen;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import cn.gxh.base.BaseFragment;
import cn.gxh.view.R;

/**
 * 屏幕适配
 */
public class ScreenFragment extends BaseFragment {

    @Override
    public int getLayoutRes() {

        //设置全屏
        mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = mActivity.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        return R.layout.fragment_screen;
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
