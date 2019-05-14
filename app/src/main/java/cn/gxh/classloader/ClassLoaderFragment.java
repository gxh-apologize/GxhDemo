package cn.gxh.classloader;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.view.R;
import dalvik.system.DexClassLoader;

/**
 * 加载插件中的类
 * Created  by gxh on 2019/4/22 13:40
 */
public class ClassLoaderFragment extends BaseFragment {

    public static ClassLoaderFragment newInstance() {
        return new ClassLoaderFragment();
    }


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_class_loader;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String apkPath = path + File.separator + "HmMall-debug.apk";
        loadApk(apkPath);
    }

    private void loadApk(String apkPath) {

        File potDir = mActivity.getDir("opt", Context.MODE_PRIVATE);
        DexClassLoader classLoader = new DexClassLoader(apkPath,
                potDir.getAbsolutePath(), null, mActivity.getClassLoader());
        try {
            Class cls = classLoader.loadClass("com.itheima.heimamall.util.MD5Util");
            if (cls != null) {
                Object instance = cls.newInstance();
                Method method = cls.getDeclaredMethod("md5", String.class);
                Object result = method.invoke(instance, "hahahahhahahaha");
                Logger.d("gxh", "result:" + result.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.d("gxh", e.toString());
        }
    }
}
