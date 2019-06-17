package cn.gxh.common.network;

import java.lang.reflect.Method;

/**
 * Created  by gxh on 2019/5/21 14:33
 */
//保存符合要求的网络注解方法
public class MethodManger {
    private Class<?> type;
    private NetType netType;//网络类型

    //需要执行的方法
    private Method method;

    public MethodManger(Class<?> type, NetType netType, Method method) {
        this.type = type;
        this.netType = netType;
        this.method = method;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public NetType getNetType() {
        return netType;
    }

    public void setNetType(NetType netType) {
        this.netType = netType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
