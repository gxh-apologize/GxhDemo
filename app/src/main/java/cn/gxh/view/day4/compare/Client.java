package cn.gxh.view.day4.compare;

import android.app.AlertDialog;

import cn.gxh.view.R;
import cn.gxh.view.base.Global;

/**
 * Created  by gxh on 2019/1/16 23:07
 */
public class Client {

    public void main() {

        //具体的调用代码
        new WorkBuild()
                .makeFllor()
                .makeWindow()
                .build();


        //
        AlertDialog.Builder builder = new AlertDialog.Builder(Global.mContext);
        builder.setIcon(R.mipmap.ic_launcher);

    }
}
