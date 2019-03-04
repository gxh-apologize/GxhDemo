package cn.gxh.property.Lsn14;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created  by gxh on 2019/3/4 13:49
 */
public class MyIntentService extends IntentService {

    //至少要有一个空的构造方法
    public MyIntentService() {
        super("");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public MyIntentService(String name) {
        super(name);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
    }


    //子线程执行
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
