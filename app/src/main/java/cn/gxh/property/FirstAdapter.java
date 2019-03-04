package cn.gxh.property;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.gxh.view.R;

/**
 * Created  by gxh on 2019/2/11 15:28
 */
public class FirstAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public FirstAdapter(@Nullable List<String> data) {
        super(R.layout.item_first, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv_item_first_name, item);
    }
}
