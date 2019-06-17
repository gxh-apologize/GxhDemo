package cn.gxh.property.camera.preview;

import android.content.Context;
import android.util.Pair;

/**
 * Created by lixuekang on 2017/6/12.
 */

public interface CameraInterface {

    Context getContext();

    int getCameraIdPref();

    String getSceneModePref();

    String getColorEffectPref();

    String getWhiteBalancePref();

    String getISOPref();

    int getExposureCompensationPref();

    Pair<Integer, Integer> getCameraResolutionPref();

    String getLockOrientationPref();

    boolean getStartupFocusPref();

    void setCameraIdPref(int cameraId);

    void setCameraResolutionPref(int width, int height);

    boolean onPictureTaken(byte[] data);
}
