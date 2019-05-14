package cn.gxh.property.thread;

import java.util.concurrent.TimeUnit;

/**
 * Created  by gxh on 2019/5/6 15:25
 */
public class SleepUtils {

    public static void second(long seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
        }
    }
}
