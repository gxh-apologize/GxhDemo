package cn.gxh.property.Lsn11;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import java.io.File;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.FirstFragment;
import cn.gxh.view.R;

/**
 * Created  by gxh on 2019/2/11 15:15
 */
public class CompressFragment extends BaseFragment {

    String rootPath = Environment.getExternalStorageDirectory() + File.separator + "DCIM";
    //3120*4208   5.32M
    String filePath = rootPath + File.separator + "P80908-152111.jpg";

    public static CompressFragment newInstance() {
        Bundle args = new Bundle();
        CompressFragment fragment = new CompressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_compress;
    }

    @Override
    public void initView(Bundle savedInstanceState) {


        Button button = findView(R.id.btn_fragent_compress);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CompressUtil.qualityCompress(filePath, new File(rootPath, "quality.jpg"), 50);
                CompressUtil.compressBySampleSize(filePath, new File(rootPath, "sample.jpg"), 8);
            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

    }
}
