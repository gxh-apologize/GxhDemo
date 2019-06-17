package cn.gxh.property.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.renderscript.RenderScript;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.Locale;
import cn.gxh.base.BaseActivity;
import cn.gxh.property.camera.preview.MyCameraInterface;
import cn.gxh.property.camera.preview.Preview;
import cn.gxh.view.R;
import cn.gxh.view.base.Global;


/**
 * usb摄像头预览
 */
public class Camera3Activity extends BaseActivity {
    //@Bind(R.id.flt_activity_camera)
    FrameLayout frameLayout;
   // @Bind(R.id.tv_activity_camera_photo)
    TextView tvPhoto;


    private MyCameraInterface applicationInterface;
    private Preview preview;
    private AudioManager mAudioManager;
    public Bundle bundle = new Bundle();

    @Override
    public int getLayoutRes() {
        return R.layout.activity_camera3;
    }


    @Override
    public void initView(Bundle savedInstanceState) {

        frameLayout=findViewById(R.id.flt_activity_camera);
        tvPhoto=findViewById(R.id.tv_activity_camera_photo);

        try {
            applicationInterface = new MyCameraInterface(this, savedInstanceState);
            setWindowFlagsForCamera();
            preview = new Preview(applicationInterface);
            //initContent();
            mAudioManager = (AudioManager) Global.mContext.getSystemService(Context.AUDIO_SERVICE);
            setDeviceDefaults();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void initListener() {


    }

    @Override
    public void initData() {

    }


    @Override
    protected void onResume() {
        super.onResume();
        preview.refreshPreview(((ViewGroup) findViewById(R.id.flt_activity_camera)));
        preview.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        preview.onPause();
    }


    @Override
    protected void onDestroy() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RenderScript.releaseAllContexts();
        }
        super.onDestroy();
        preview.destroy();
    }

    public void setWindowFlagsForCamera() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // keep screen active - see http://stackoverflow.com/questions/2131948/force-screen-on
        if (sharedPreferences.getBoolean(PreferenceKeys.getKeepDisplayOnPreferenceKey(), true)) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (sharedPreferences.getBoolean(PreferenceKeys.getShowWhenLockedPreferenceKey(), true)) {
            // keep Open Camera on top of screen-lock (will still need to unlock when going to gallery or settings)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        }
    }


    void setDeviceDefaults() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean is_samsung = Build.MANUFACTURER.toLowerCase(Locale.US).contains("samsung");
        boolean is_oneplus = Build.MANUFACTURER.toLowerCase(Locale.US).contains("oneplus");
        if (is_samsung || is_oneplus) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(PreferenceKeys.getCamera2FakeFlashPreferenceKey(), true);
            editor.apply();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (this.applicationInterface != null) {
            applicationInterface.onSaveInstanceState(state);
        }
    }


//    @OnClick(R.id.tv_activity_camera_photo)
//    public void onViewClicked() {
//        preview.takePicturePressed();
//    }
}
