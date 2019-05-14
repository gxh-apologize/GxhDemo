package cn.gxh.property.gif;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.view.R;

/**
 * Created  by gxh on 2019/3/5 10:29
 */
public class GifFragment extends BaseFragment {

    public static GifFragment newInstance() {
        return new GifFragment();
    }

    private ImageView ivGif;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_gif;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        ivGif = findView(R.id.iv_gif);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        Glide.with(this).load(R.drawable.face)
                .diskCacheStrategy(DiskCacheStrategy.ALL).into(ivGif);
        ceshi();
    }

    private void ceshi() {
        String str = "40.13";
        if(str.endsWith(".00")){
            //str = Integer.parseInt(str)+"";
            str=String.format("%.0f", Double.valueOf(str));
        }else {
            str = Double.valueOf(str)+"";
        }

        Logger.d("gxh", str);
    }
}
