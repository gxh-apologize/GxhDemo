package cn.gxh.classloader;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;

import dalvik.system.DexClassLoader;

/**
 * Created  by gxh on 2019/4/22 16:38
 */
public class PluginManager {

    private static PluginManager mInstance;
    private static Context mContext;
    private static File mOptFile;
    private static HashMap<String,PluginInfo> mPluginMap;
    private PluginManager(Context context){
        this.mContext=context;
        this.mOptFile=context.getDir("opt",mContext.MODE_PRIVATE);
        mPluginMap=new HashMap<>();
    }

    //获取单例对象
    public static PluginManager getInstance(Context context){
        if(mInstance==null){
            synchronized (PluginManager.class){
                if(mInstance==null){
                    mInstance=new PluginManager(context);
                }
            }
        }
        return mInstance;
    }


    //为对应的插件apk创建DexClassLoader
    private static DexClassLoader createPluginDexClassLoader(String apkPath){

      DexClassLoader classLoader=new DexClassLoader(apkPath,mOptFile.getAbsolutePath(),
              null,null);
      return classLoader;
    }

    //为对应的插件apk创建AssetManager
    private static AssetManager createPluginAssetManager(String apkPath){
        try {
            AssetManager assetManager=AssetManager.class.newInstance();
            Method method=assetManager.getClass().getMethod("AddAssetPath",String.class);
            method.invoke(assetManager,apkPath);
            return assetManager;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    //为对应的插件apk创建Resources
    private static Resources createPluginResources(String apkPath){

        AssetManager assetManager = createPluginAssetManager(apkPath);
        //宿主Resources
        Resources superResources=mContext.getResources();
        Resources pluginResource=new Resources(assetManager,superResources.getDisplayMetrics(),
                superResources.getConfiguration());
        return pluginResource;
    }

    public static PluginInfo loadApk(String apkPath){
        if(mPluginMap.get(apkPath)!=null){
            return mPluginMap.get(apkPath);
        }

        PluginInfo pluginInfo=new PluginInfo();
        pluginInfo.dexClassLoader=createPluginDexClassLoader(apkPath);
        pluginInfo.assetManager=createPluginAssetManager(apkPath);
        pluginInfo.resources=createPluginResources(apkPath);
        mPluginMap.put(apkPath,pluginInfo);
        return pluginInfo;
    }
}
