package cn.gxh.classloader;

import android.content.res.AssetManager;
import android.content.res.Resources;

import dalvik.system.DexClassLoader;

/**
 * Created  by gxh on 2019/4/22 16:36
 */
public class PluginInfo {

    public DexClassLoader dexClassLoader;
    public AssetManager assetManager;
    public Resources resources;

    //activity、service等等

}
