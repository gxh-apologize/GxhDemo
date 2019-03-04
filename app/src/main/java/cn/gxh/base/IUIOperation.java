package cn.gxh.base;

import android.os.Bundle;

/**
 * 界面操作方法封装
 */
public interface IUIOperation {

    /** 返回activity的布局文件 */
    int getLayoutRes();

    /** 查找子控件
     * @param savedInstanceState*/
    void initView(Bundle savedInstanceState);

    /** 初始化监听器 */
    void initListener();

    /** 初始化数据 */
    void initData();

    /**
     * 子类处理点击事件
     * @param v
     * @param id 点击的控件id
     */
   // void onClick(View v, int id);
}
