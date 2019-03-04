package cn.gxh.property.Lsn13;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import cn.gxh.base.BaseFragment;
import cn.gxh.property.Lsn11.CompressUtil;
import cn.gxh.property.Lsn13.flatc.FlatBufferBuilder;
import cn.gxh.view.R;
import cn.gxh.view.base.Global;

/**
 * Created  by gxh on 2019/2/11 15:15
 */
public class FlatcFragment extends BaseFragment {

    public static FlatcFragment newInstance() {
        Bundle args = new Bundle();
        FlatcFragment fragment = new FlatcFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_flatc;
    }

    @Override
    public void initView(Bundle savedInstanceState) {


        Button button1 = findView(R.id.btn_fragent_flatc_01);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serialize();
            }
        });


        Button button2 = findView(R.id.btn_fragent_flatc_02);
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

    }

    /**
     * 序列化
     */
    private void serialize() {
        FlatBufferBuilder builder = new FlatBufferBuilder();
        //县区
        int children1 = Children.createChildren(builder, builder.createString("1001"),
                builder.createString("海淀区"), 1, true, 10000009);
        int children2 = Children.createChildren(builder, builder.createString("1002"),
                builder.createString("朝阳区"), 2, true, 10000008);

        int[] childrens = new int[2];
        childrens[0] = children1;
        childrens[1] = children2;
        int childrenVector = City.createChildrenVector(builder, childrens);

        //市
        int city = City.createCity(builder, builder.createString("101"),
                builder.createString("北京市"), childrenVector);

        int[] cities = new int[2];
        cities[0] = city;
        int cityVector = Result.createCityVector(builder, cities);
        int result = Result.createResult(builder, builder.createString("100"),
                builder.createString("北京市1"), cityVector);

        int[] results=new int[2];
        results[0]=result;
        int resultVector = ResultList.createResultVector(builder, results);


        ResultList.startResultList(builder);
        ResultList.addResult(builder, resultVector);
        int rootResult = ResultList.endResultList(builder);
        ResultList.finishResultListBuffer(builder, rootResult);

        //-------保存数据到文件------
        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard, "address.txt");
        if (file.exists()) {
            file.delete();
        }

        ByteBuffer byteBuffer = builder.dataBuffer();
        FileOutputStream fos = null;
        FileChannel channel = null;
        try {
            fos = new FileOutputStream(file);
            channel = fos.getChannel();
            while (byteBuffer.hasRemaining()) {
                channel.write(byteBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }

                if (channel != null) {
                    channel.close();
                }
            } catch (Exception e) {
            }
        }


        //------读取文件--------
        FileInputStream fis = null;
        FileChannel readChannel = null;
        try {
            fis = new FileInputStream(file);
            readChannel = fis.getChannel();
            ByteBuffer byteBuffer1 = ByteBuffer.allocate(1024);
            int readBytes = 0;
            while ((readBytes = readChannel.read(byteBuffer1)) != -1) {

            }

            //把指针回到最初的状态
            byteBuffer1.flip();
            ResultList resultList = ResultList.getRootAsResultList(byteBuffer1);
            Global.showToast(resultList.result(0).id()+";"+resultList.result(0).title());
            Log.d("gxh",resultList.result(0).id()+";"+resultList.result(0).title());

        } catch (Exception e) {

        }finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (readChannel != null) {
                    readChannel.close();
                }
            } catch (Exception e) {
            }
        }

    }
}
