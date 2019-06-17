package cn.gxh.common.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.gxh.common.util.Constant;
import cn.gxh.common.util.Logger;

/**
 * Created  by gxh on 2019/5/21 10:04
 */
public class NetStateReceiver extends BroadcastReceiver {
    private NetType netType;
    private Map<Object, List<MethodManger>> netMaps;

    public NetStateReceiver() {
        netType = NetType.NONE;
        netMaps = new HashMap<>();
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        if (intent.getAction().equalsIgnoreCase(Constant.ANDROID_NET_CHANGE_ACTION)) {
            netType = NetworkUtils.getNetType();
            Logger.d(Constant.TAG, "net change:" + netType);
            if (NetworkUtils.isNetworkAvailable()) {
                Logger.d(Constant.TAG, "net available");
            } else {
                Logger.d(Constant.TAG, "net not available");
            }

            post(netType);
        }
    }

    /**
     * 全局通知
     *
     * @param netType
     */
    private void post(NetType netType) {
        Set<Object> set = netMaps.keySet();
        for (Object object : set) {
            List<MethodManger> list = netMaps.get(object);
            if (list != null) {
                for (MethodManger methodManger : list) {
                    if (methodManger.getType().isAssignableFrom(netType.getClass())) {
                        switch (methodManger.getNetType()) {
                            case AUTO:
                                invoke(methodManger, object, netType);
                                break;

                            case WIFI:
                                if (netType == NetType.WIFI || netType == NetType.NONE) {
                                    invoke(methodManger, object, netType);
                                }
                                break;
                            case CMNET:
                                if (netType == NetType.CMNET || netType == NetType.NONE) {
                                    invoke(methodManger, object, netType);
                                }
                                break;
                            case CMWAP:
                                if (netType == NetType.CMWAP || netType == NetType.NONE) {
                                    invoke(methodManger, object, netType);
                                }
                                break;
                            case NONE:
                                if (netType == NetType.NONE) {
                                    invoke(methodManger, object, netType);
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 反射执行方法
     * @param methodManger
     * @param object
     * @param netType
     */
    public void invoke(MethodManger methodManger, Object object, NetType netType) {
        try {
            //执行方法
            methodManger.getMethod().invoke(object, netType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册需要接收消息的activity、fragment等
     * @param object
     */
    public void registerObserver(Object object) {
        List<MethodManger> methodList = netMaps.get(object);
        if (methodList == null) {
            methodList = findMethod(object);
            netMaps.put(object, methodList);
        }
    }

    /**
     * 解绑
     * @param object
     */
    public void unRegisterObserver(Object object) {
        if (!netMaps.isEmpty()) {
            netMaps.remove(object);
        }
    }

    /**
     * 反射找到方法
     *
     * @param object
     * @return
     */
    public List<MethodManger> findMethod(Object object) {
        List<MethodManger> methodList = new ArrayList<>();
        Class<?> cls = object.getClass();
        Method[] methods = cls.getMethods();
        for (Method method : methods) {
            //获取方法的注解
            NetChange annotation = method.getAnnotation(NetChange.class);
            if (annotation == null) {
                continue;
            }

            //方法参数校验
            Class<?>[] parameterTypes = method.getParameterTypes();
            if (parameterTypes.length != 1) {
                throw new RuntimeException(method.getName() + " should have one parameter only");
            }

            Class<?> parameterType = parameterTypes[0];
            if(!parameterType.isAssignableFrom(NetType.class)){
                throw new RuntimeException(method.getName() + " parameter error");
            }


            MethodManger methodManger = new MethodManger(parameterType,
                    annotation.netType(), method);
            methodList.add(methodManger);
        }
        return methodList;
    }
}
