package cn.gxh.property.lb2;

import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;
import cn.gxh.base.BaseFragment;
import cn.gxh.view.R;

public class Lb2Fragment extends BaseFragment {

    private List<String> list;

    private Banner banner;

    public static Lb2Fragment newInstance() {
        return new Lb2Fragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_lb2;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        banner=findView(R.id.banner);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        initLbData();
        initView();
    }


    private void initLbData(){

        list = new ArrayList<>();
        list.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        list.add("https://tva2.sinaimg.cn/large/007DFXDhgy1g53sh4gqr5j30ia0wi40d.jpg");
        list.add("https://tva4.sinaimg.cn/large/007DFXDhgy1g53sio9viqj30ia0wijt5.jpg");
        list.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        list.add("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4");
        list.add("https://tva4.sinaimg.cn/large/007DFXDhgy1g53sjw6hl3j30ia0witbh.jpg");
    }

    private void initView(){
        banner.setDataList(list);//设置数据
        banner.setImgDelyed(5000);//设置图片延迟时间
        banner.startBanner();
        banner.startAutoPlay();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
