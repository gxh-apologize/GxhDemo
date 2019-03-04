package cn.gxh.base;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import me.yokeyword.fragmentation.SupportActivity;


/**
 * Created by GXH on 2018/1/11.
 */
public abstract class BaseActivity extends SupportActivity implements IUIOperation {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutRes());

        initView(savedInstanceState);
        initData();
        initListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void toActivity(Class clazz){
        Intent intent = new Intent(this,clazz);
        startActivity(intent);
    }

    public void toActivity(Class clazz, Bundle bundle){
        Intent intent = new Intent(this,clazz);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void toActivityForResult(Class clazz, Bundle bundle,int requestCode){
        Intent intent = new Intent(this,clazz);
        if (bundle != null){
            intent.putExtras(bundle);
        }
        startActivityForResult(intent,requestCode);
    }

    public void toActivity(Class clazz, String name,String value){
        Intent intent = new Intent(this,clazz);
        intent.putExtra(name,value);
        startActivity(intent);
    }
}
