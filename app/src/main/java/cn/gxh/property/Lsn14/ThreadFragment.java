package cn.gxh.property.Lsn14;

import android.app.IntentService;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.property.Lsn13.Children;
import cn.gxh.property.Lsn13.City;
import cn.gxh.property.Lsn13.Result;
import cn.gxh.property.Lsn13.ResultList;
import cn.gxh.property.Lsn13.flatc.FlatBufferBuilder;
import cn.gxh.view.R;
import cn.gxh.view.base.Global;

/**
 * Created  by gxh on 2019/2/11 15:15
 */
public class ThreadFragment extends BaseFragment {

    public static ThreadFragment newInstance() {
        Bundle args = new Bundle();
        ThreadFragment fragment = new ThreadFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_thread;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        Button button1 = findView(R.id.btn_fragent_thread_01);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new MyAsyncTask("MyAsyncTask3").execute();

                startService();
            }
        });


        Button button2 = findView(R.id.btn_fragent_thread_02);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
//
//        Object lock = new Object();
//        new Producer(lock).start();
//        new Consumer(lock).start();

        //futureTask();

        //threadPool();

        //asyncTask();

        MyLoaderCallBack callBack = new MyLoaderCallBack();
        mActivity.getLoaderManager().initLoader(0, null, callBack);
    }

    // 查询指定的条目
    private static final String[] CALLLOG_PROJECTION = new String[]{CallLog.Calls._ID, CallLog.Calls.NUMBER,
            CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE, CallLog.Calls.DATE};

    private class MyLoaderCallBack implements LoaderManager.LoaderCallbacks<Cursor> {


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader loader = new CursorLoader(mActivity, CallLog.Calls.CONTENT_URI, CALLLOG_PROJECTION,
                    null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
            return loader;
        }

        //Loader检测到数据发生改变时，会自动执行新的载入获取最新数据
        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            if (data == null) return;
            //处理数据
            //adater.swapCursor(data);
        }

        //OnDestroy，自动停止load
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            //mAdapter.swapCursor(null);
        }
    }


    //---------------------IntentService

    private void startService() {
        Intent intent = new Intent(mActivity, MyIntentService.class);
        mActivity.startService(intent);
    }


    //---------------------


    //----------------------------
    private HandlerThread handlerThread = new HandlerThread("handler thread");
    private Handler myHandler;

    private void init() {
        //启动线程
        handlerThread.start();
        myHandler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //可以在这里执行异步任务,这里是在子线程执行的

                //更新UI
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
            }
        };
    }


    //----------------------------


    //产品
    static class ProductObject {
        public volatile static String value = null;
    }

    /**
     * 生产者线程
     * 等消费者消费完再生产
     */
    static class Producer extends Thread {

        Object lock;

        public Producer(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            //不断生产产品
            while (true) {
                synchronized (lock) {
                    if (ProductObject.value != null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    ProductObject.value = "Number:" + System.currentTimeMillis();
                    Logger.d("gxh", "生产:" + ProductObject.value);
                    lock.notify();
                }
            }
        }
    }

    /**
     * 消费者线程
     */
    static class Consumer extends Thread {

        Object lock;

        public Consumer(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    if (ProductObject.value == null) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Logger.d("gxh", "消费:" + ProductObject.value);
                    ProductObject.value = null;
                    lock.notify();//通知
                }
            }
        }
    }

    private void futureTask() {
        Task task = new Task();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task) {
            @Override
            protected void done() {
                //异步任务执行完毕回调这个方法，这里也可以获取到异步任务的返回值
                Logger.d("gxh", "done");
            }
        };
        //线程池去执行futureTask
        ExecutorService executors = Executors.newCachedThreadPool();
        executors.execute(futureTask);
        //获取异步任务的返回值
        try {
            //get阻塞等待异步任务执行完毕
            Integer result = futureTask.get();
            Logger.d("gxh", "result:" + result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    //封装一个计算任务，实现Callable接口
    static class Task implements Callable<Integer> {
        //返回异步任务的执行结果
        @Override
        public Integer call() throws Exception {
            int i = 0;
            for (; i < 10; i++) {
                Thread.sleep(500);
            }
            Logger.d("gxh", Thread.currentThread().getName() + ";" + i);
            return i;
        }
    }

    private void asyncTask() {
        //使用AsyncTask默认的线程池
        new MyAsyncTask("MyAsyncTask1").execute();
        new MyAsyncTask("MyAsyncTask2").execute();
        new MyAsyncTask("MyAsyncTask3").execute();
        new MyAsyncTask("MyAsyncTask4").execute();
        new MyAsyncTask("MyAsyncTask5").execute();
        new MyAsyncTask("MyAsyncTask6").execute();
        new MyAsyncTask("MyAsyncTask7").execute();
        new MyAsyncTask("MyAsyncTask8").execute();
        new MyAsyncTask("MyAsyncTask9").execute();
        new MyAsyncTask("MyAsyncTask10").execute();
        new MyAsyncTask("MyAsyncTask11").execute();
        new MyAsyncTask("MyAsyncTask12").execute();
        new MyAsyncTask("MyAsyncTask13").execute();
        new MyAsyncTask("MyAsyncTask14").execute();
        new MyAsyncTask("MyAsyncTask15").execute();

//        //使用自己指定的线程池，线程池扩容
//        Executor executor = Executors.newScheduledThreadPool(25);
//        new MyAsyncTask().executeOnExecutor(executor);
    }

    class MyAsyncTask extends DiyAsyncTask<String, Integer, String> {

        String name;

        public MyAsyncTask(String name) {
            this.name = name;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                if (this.name.equals("MyAsyncTask8")) {
                    Thread.sleep(3000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return this.name;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Logger.d("gxh", s + ";" + System.currentTimeMillis());
        }
    }


    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = Math.max(2, Math.min(CPU_COUNT - 1, 4));
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE_SECONDS = 1;
    //任务队列
    private static final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<Runnable>(20);
    //线程工厂
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            String name = "AsyncTask #" + mCount.getAndIncrement();
            Logger.d("gxh", "newThread:  " + name);
            return new Thread(r, name);
        }
    };

    private void threadPool() {
        Logger.d("gxh", "CPU_COUNT:" + CPU_COUNT + ";" + "CORE_POOL_SIZE:" + CORE_POOL_SIZE +
                ";MAXIMUM_POOL_SIZE:" + MAXIMUM_POOL_SIZE);

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_SECONDS, TimeUnit.SECONDS,
                sPoolWorkQueue, sThreadFactory);
        //threadPoolExecutor.allowCoreThreadTimeOut(true);
        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.execute(new MyTask());
        }
    }

    static class MyTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    //Logger.d("gxh",Thread.currentThread().getName()+";"+"1");
                    Thread.sleep(1000);
                    //Logger.d("gxh",Thread.currentThread().getName()+";"+"2");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //Logger.d("gxh",Thread.currentThread().getName()+";"+"1");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
