package cn.gxh.base;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.gxh.property.FirstAdapter;
import cn.gxh.property.Lsn11.CompressFragment;
import cn.gxh.property.Lsn13.FlatcFragment;
import cn.gxh.property.Lsn14.ThreadFragment;
import cn.gxh.view.R;
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
                supportFragment=CompressFragment.newInstance();
                break;
            case 2:
                supportFragment= FlatcFragment.newInstance();
                break;
            case 3:
                supportFragment= ThreadFragment.newInstance();
                break;
        }

        ((MainFragment)getParentFragment()).startBrotherFragment(supportFragment);
    }

    @Override
    public void initData() {

        List list = new ArrayList();
        list.add("Lsn1");
        list.add("Lsn11  Bitmap内存管理及优化");
        list.add("Lsn13  数据传输");
        list.add("Lsn14  多线程优化");

        firstAdapter.setNewData(list);
    }
}
