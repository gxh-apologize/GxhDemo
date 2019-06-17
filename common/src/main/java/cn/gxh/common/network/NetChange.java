package cn.gxh.common.network;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created  by gxh on 2019/5/21 14:24
 */

@Target(ElementType.METHOD)//作用在方法上
@Retention(RetentionPolicy.RUNTIME)//jvm运行时通过反射获取注解的值
public @interface NetChange {
    NetType netType() default NetType.AUTO;
}
