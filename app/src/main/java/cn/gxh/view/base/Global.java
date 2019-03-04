package cn.gxh.view.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * 封装一些常用操作，如：屏幕尺寸获取，主线程运行，toast
 * 
 * @author gxh
 */
public class Global {
	
	public static Context mContext;
	
	/** 屏幕的宽度 */
	public static int mScreenWidth;
	/** 屏幕的高度 */
	public static int mScreenHeight;
	/** 屏幕密度 */
	public static float mDensity;

	
	private static Handler mHandler = new Handler();
	
	public static void init(Context context) {
		mContext = context;
		initScreenSize();
	}

	/** 初始化屏幕参数 */
	private static void initScreenSize() {
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		mScreenWidth = dm.widthPixels;
		mScreenHeight = dm.heightPixels;
		mDensity = dm.density;
	}
	
	public static int dp2px(int dp) {
		return (int) (mDensity * dp + 0.5f);
	}
	
	public static Handler getHandler() {
		return mHandler;
	}
	
	/** 判断当前是否在主线程运行 */
	public static boolean isMainThread() {
		return Looper.getMainLooper() == Looper.myLooper();
	}

	/**
	 * 在主线程执行
	 * @param runnable
	 */
	public static void runInUIThread(Runnable runnable) {
		if (isMainThread()) {
			runnable.run();
		} else {
			mHandler.post(runnable);
		}
	}
	
	private static Toast mToast;
	
	/**
	 * 显示文本，可以在子线程运行
	 * 
	 * @param text
	 */
	public static void showToast(final String text) {
		runInUIThread(new Runnable() {

			@Override
			public void run() {
				if (mToast  == null) {
					mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
				}
				mToast.setText(text);
				mToast.show();
			}
		});
	}

	public static View inflate(int layout, ViewGroup parent) {
		return LayoutInflater.from(mContext).inflate(layout, parent, false);
	}

}
















