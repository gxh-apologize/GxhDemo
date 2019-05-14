package cn.gxh.base;

import android.Manifest;
import android.os.Bundle;

import java.util.List;

import cn.gxh.view.R;
import cn.gxh.view.base.Global;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created  by gxh on 2019/2/11 15:48
 */
public class SplashActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    @Override
    public int getLayoutRes() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        requestSavePermissions();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        toActivity(MainActivity.class);
        finish();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Global.showToast("读写权限已拒绝,请退出重试");
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }



    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestSavePermissions() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "需要打开文件读写的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        } else {
           toActivity(MainActivity.class);
           finish();
        }
    }
}
