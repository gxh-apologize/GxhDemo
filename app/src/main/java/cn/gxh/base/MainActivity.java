package cn.gxh.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import cn.gxh.view.R;
import cn.gxh.view.SpeechManger;
import cn.gxh.view.goo.GooView;

public class MainActivity extends BaseActivity {

    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        loadRootFragment(R.id.flt_content, MainFragment.newInstance());

    }
}
