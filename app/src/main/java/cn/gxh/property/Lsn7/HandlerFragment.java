package cn.gxh.property.Lsn7;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import cn.gxh.base.BaseFragment;
import cn.gxh.view.R;

/**
 * Created  by gxh on 2019/3/11 14:54
 */
public class HandlerFragment extends BaseFragment {
    @Override
    public int getLayoutRes() {
        return R.layout.fragment_handler;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
        //方式1:
        mHandler.post(runnable);

        //方式2:
        Message msg=Message.obtain();
        msg.what=1;
        mHandler.sendMessage(msg);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    //主线程执行的
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(runnable, 1000);
        }
    };



    private void startThread(){
       MyThread myThread=new MyThread();
       myThread.start();

       Message msg=Message.obtain();
       myThread.handler.sendMessage(msg);
    }

    class MyThread extends Thread{
        Handler handler;

        @Override
        public void run() {
            Looper.prepare();

            handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {

                }
            };

            Looper.loop();
        }
    }
}
