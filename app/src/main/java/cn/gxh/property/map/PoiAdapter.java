package cn.gxh.property.map;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.gxh.view.R;

/**
 * 购物车
 */
public class PoiAdapter extends BaseQuickAdapter<PoiInfo, BaseViewHolder> {


    public PoiAdapter(@Nullable List<PoiInfo> data) {
        super(R.layout.item_map_poi, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiInfo item) {

        helper.setText(R.id.tv_item_poi_name, item.getName());
        helper.setText(R.id.tv_item_poi_address, item.getAddress());

    }


}
