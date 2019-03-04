package cn.gxh.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import me.yokeyword.fragmentation.SupportFragment;

public abstract class BaseFragment extends SupportFragment implements IUIOperation{


    public View root;
    public Activity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(getLayoutRes(), null);
            initView(savedInstanceState);
            initListener();
            initData();
        }
        return root;
    }


    /**
     * 查找子控件，可以省略强转操作
     */
    public <T> T findView(int id) {
        T view = (T) root.findViewById(id);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
