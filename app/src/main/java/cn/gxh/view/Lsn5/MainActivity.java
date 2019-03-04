package cn.gxh.view.Lsn5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import cn.gxh.view.R;
import cn.gxh.view.SpeechManger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //调用
        General general=new General();
        general.attach();

        /**
         * 皇帝不直接指挥军队，而是通过圣旨
         * 具体的攻击、撤退方法都是在具体的圣旨里，圣旨是接口，
         * 这里起到了隔离的作用
         *
         * apk的安装用的命令模式
         */

    }
}
