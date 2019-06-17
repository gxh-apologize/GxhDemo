package cn.gxh.property.camera.preview;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Pair;

import cn.gxh.property.camera.Camera3Activity;
import cn.gxh.property.camera.PreferenceKeys;

/**
 * Created by lixuekang on 2017/6/13.
 */

public class MyCameraInterface implements CameraInterface {

    private static final String TAG = "MyApplicationInterface";

    private final Camera3Activity cameraActivity;

    // camera properties which are saved in bundle, but not stored in preferences (so will be remembered if the app goes into background, but not after restart)
    private int cameraId = 0;
    private int zoom_factor = 0;
    private float focus_distance = 0.0f;

    public MyCameraInterface(Camera3Activity activity, Bundle savedInstanceState) {
        this.cameraActivity = activity;
        if (savedInstanceState != null) {
            cameraId = savedInstanceState.getInt("cameraId", 0);
            zoom_factor = savedInstanceState.getInt("zoom_factor", 0);
            focus_distance = savedInstanceState.getFloat("focus_distance", 0.0f);
        }
    }

    public void onSaveInstanceState(Bundle state) {
        state.putInt("cameraId", cameraId);
        state.putInt("zoom_factor", zoom_factor);
        state.putFloat("focus_distance", focus_distance);
    }

    @Override
    public Context getContext() {
        return cameraActivity;
    }


    @Override
    public int getCameraIdPref() {
        return cameraId;
    }

    @Override
    public String getSceneModePref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(PreferenceKeys.getSceneModePreferenceKey(), "auto");
    }

    @Override
    public String getColorEffectPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(PreferenceKeys.getColorEffectPreferenceKey(), "none");
    }

    @Override
    public String getWhiteBalancePref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(PreferenceKeys.getWhiteBalancePreferenceKey(), "auto");
    }

    @Override
    public String getISOPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(PreferenceKeys.getISOPreferenceKey(), "auto");
    }

    @Override
    public int getExposureCompensationPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String value = sharedPreferences.getString(PreferenceKeys.getExposurePreferenceKey(), "0");
        int exposure = 0;
        try {
            exposure = Integer.parseInt(value);
        } catch (NumberFormatException exception) {
        }
        return exposure;
    }

    @Override
    public Pair<Integer, Integer> getCameraResolutionPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String resolution_value = sharedPreferences.getString(PreferenceKeys.getResolutionPreferenceKey(cameraId), "");
        if (resolution_value.length() > 0) {
            // parse the saved size, and make sure it is still valid
            int index = resolution_value.indexOf(' ');
            if (index == -1) {
            } else {
                String resolution_w_s = resolution_value.substring(0, index);
                String resolution_h_s = resolution_value.substring(index + 1);
                try {
                    int resolution_w = Integer.parseInt(resolution_w_s);
                    int resolution_h = Integer.parseInt(resolution_h_s);
                    return new Pair<>(resolution_w, resolution_h);
                } catch (NumberFormatException exception) {
                }
            }
        }
        return null;
    }

    @Override
    public String getLockOrientationPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getString(PreferenceKeys.getLockOrientationPreferenceKey(), "none");
    }


    @Override
    public boolean getStartupFocusPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        return sharedPreferences.getBoolean(PreferenceKeys.getStartupFocusPreferenceKey(), true);
    }

    @Override
    public void setCameraIdPref(int cameraId) {
        this.cameraId = cameraId;
    }


    @Override
    public void setCameraResolutionPref(int width, int height) {
        String resolution_value = width + " " + height;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PreferenceKeys.getResolutionPreferenceKey(cameraId), resolution_value);
        editor.apply();
    }

    @Override
    public boolean onPictureTaken(final byte[] data) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    String[] path = FileUtils.filePreserve(data);
//                    if (path != null && null != path[0]) {
//                        if (cameraActivity == null) {
//                            return;
//                        }
//                        FileUtils.updateBitmapOri(path);
//                        cameraActivity.bundle.putString("path", path[0]);
//                        Intent intent = new Intent(cameraActivity, UpLoadActivity.class);
//                        intent.putExtras(cameraActivity.bundle);
//                        cameraActivity.startActivity(intent);
//                        cameraActivity.finish();
//                    } else {
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                cameraActivity.onBackPressed();
//                            }
//                        });
//                    }
                }
            }).start();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
