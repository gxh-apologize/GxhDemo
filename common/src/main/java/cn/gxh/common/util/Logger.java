package cn.gxh.common.util;
import android.util.Log;


/**
 * Created by gxh on 2016/8/5.
 * 日志工具类,上线前需  DEBUG=false
 */
public class Logger {

    public static boolean DEBUG = true;

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.e(tag, msg);
        }
    }

    public static void e(Object object, String msg) {
        if (DEBUG) {
            Log.e(object.getClass().getSimpleName(), msg);
        }
    }

    public static void d(Object object, String msg) {
        if (DEBUG) {
            Log.d(object.getClass().getSimpleName(), msg);
        }
    }
}
