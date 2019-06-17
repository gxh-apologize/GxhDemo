package cn.gxh.property.camera.preview;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.camera2.DngCreator;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.util.Pair;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.gxh.base.Logger;
import cn.gxh.property.camera.cameracontroller.CameraController;
import cn.gxh.property.camera.cameracontroller.CameraController1;
import cn.gxh.property.camera.cameracontroller.CameraControllerException;
import cn.gxh.property.camera.cameracontroller.CameraControllerManager;
import cn.gxh.property.camera.cameracontroller.CameraControllerManager1;
import cn.gxh.property.camera.preview.camerasurface.CameraSurface;
import cn.gxh.property.camera.preview.camerasurface.MySurfaceView;


/**
 * Created by lixuekang on 2017/6/12.
 */

public class Preview implements SurfaceHolder.Callback, TextureView.SurfaceTextureListener, PreviewInterface {
    private static final String TAG = "Preview";
    private int PREVIEW_WIDTH = 640;
    private int PREVIEW_HEIGHT = 480;
    private byte[] nv21;
    private byte[] buffer;
    private boolean mStopTrack;
    private boolean mFlag;
    protected float mStart = 0.5f;
    protected float mEnd = 0.791f;

    private int mSoundSate;
    private static final int sFarSTATE = 0;
    private static final int sCloseSTATE = 1;
    private static final int sSUITABLE = 2;
    private MediaPlayer mMediaPlayer;
    private Object mObject = new Object();
    private AudioManager mAm;
    private int mCurrentVolume;

    public int num = 0;

    private final CameraInterface applicationInterface;
    private CameraSurface cameraSurface;
    private boolean set_preview_size;
    private int preview_w, preview_h;
    private boolean set_textureview_size;
    private int textureview_w, textureview_h;
    private boolean app_is_paused = true;
    private boolean has_surface;
    private CameraControllerManager camera_controller_manager;
    private CameraController camera_controller;
    private static final int PHASE_NORMAL = 0;
    private static final int PHASE_TAKING_PHOTO = 2;
    private static final int PHASE_PREVIEW_PAUSED = 3; // the paused state after taking a photo
    private volatile int phase = PHASE_NORMAL; // must be volatile for test project reading the state
    private boolean is_preview_started;
    private int current_rotation; // orientation relative to camera's orientation (used for parameters.setRotation())
    private List<String> supported_flash_values; // our "values" format
    private int current_flash_index = -1; // this is an index into the supported_flash_values array, or -1 if no flash modes available

    private List<String> supported_focus_values; // our "values" format
    private int current_focus_index = -1; // this is an index into the supported_focus_values array, or -1 if no focus modes available
    private boolean continuous_focus_move_is_started;
    private boolean supports_iso_range;
    private List<String> exposures;
    private int min_exposure;
    private int max_exposure;

    private List<CameraController.Size> sizes;
    private int current_size_index = -1; // this is an index into the sizes array, or -1 if sizes not yet set
    private int focus_success = FOCUS_DONE;
    private static final int FOCUS_WAITING = 0;
    private static final int FOCUS_SUCCESS = 1;
    private static final int FOCUS_FAILED = 2;
    private static final int FOCUS_DONE = 3;
    private String set_flash_value_after_autofocus = "";
    private boolean take_photo_after_autofocus; // set to take a photo when the in-progress autofocus has completed; if setting, remember to call camera_controller.setCaptureFollowAutofocusHint()
    private boolean successfully_focused;
    private long successfully_focused_time = -1;

    /* If the user touches to focus in continuous mode, we switch the camera_controller to autofocus mode.
     * autofocus_in_continuous_mode is set to true when this happens; the runnable reset_continuous_focus_runnable
     * switches back to continuous mode.
     */
    private final Handler reset_continuous_focus_handler = new Handler();
    private Runnable reset_continuous_focus_runnable;
    private boolean autofocus_in_continuous_mode;


    public Preview(CameraInterface applicationInterface) {
        this.applicationInterface = applicationInterface;
        nv21 = new byte[PREVIEW_WIDTH * PREVIEW_HEIGHT * 2];
        mMediaPlayer = new MediaPlayer();
        //initVolume();
        //initVoiceTip();
    }

    public void refreshPreview(ViewGroup parent) {
        parent.removeAllViews();
        this.cameraSurface = new MySurfaceView(getContext(), this);
        camera_controller_manager = new CameraControllerManager1();
        parent.addView(cameraSurface.getView());
    }

//    //初始化音量
//    private void initVolume() {
//        mAm = (AudioManager) Global.mContext.getSystemService(Context.AUDIO_SERVICE);
//        mCurrentVolume = mAm.getStreamVolume(AudioManager.STREAM_MUSIC);
//        int voice = mAm.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        if (mAm.isWiredHeadsetOn()) {//是否插入了耳机
//            if (SharePrefUtil.create(Global.mContext).getHeadSetVolume() != 0) {
//                mAm.setStreamVolume(AudioManager.STREAM_MUSIC, SharePrefUtil.create(Global.mContext).getHeadSetVolume(), AudioManager.ADJUST_SAME);
//                return;
//            }
//            mAm.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (0.4 * voice), AudioManager.ADJUST_SAME);
//        } else {
//            if (SharePrefUtil.create(Global.mContext).getVolume() != 0) {
//                //设置音量
//                mAm.setStreamVolume(AudioManager.STREAM_MUSIC, SharePrefUtil.create(Global.mContext).getVolume(), AudioManager.ADJUST_SAME);
//                return;
//            }
//            mAm.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (0.7 * voice), AudioManager.ADJUST_SAME);
//        }
//    }

//    //请平视后置摄像头
//    private void initVoiceTip() {
//        try {
//            AssetFileDescriptor afd = Global.mContext.getResources().openRawResourceFd(R.raw.init);
//            if (afd == null) return;
//            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//            afd.close();
//            mMediaPlayer.prepare();
//            mMediaPlayer.start();
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    private void resetVolume() {
//        float scale = (float) mAm.getStreamVolume(AudioManager.STREAM_MUSIC) / mAm.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
//        if (mAm.isWiredHeadsetOn()) {
//            if (scale < 0.1) {
//                SharePrefUtil.create(Global.mContext).saveHeadSetVolume((int) (0.1 * mAm.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
//            } else if (scale > 0.6) {
//                SharePrefUtil.create(Global.mContext).saveHeadSetVolume((int) (0.6 * mAm.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
//            } else {
//                SharePrefUtil.create(Global.mContext).saveHeadSetVolume(mAm.getStreamVolume(AudioManager.STREAM_MUSIC));
//            }
//        } else {
//            if (scale < 0.3) {
//                SharePrefUtil.create(Global.mContext).saveVolume((int) (0.3 * mAm.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
//            } else if (scale > 0.9) {
//                SharePrefUtil.create(Global.mContext).saveVolume((int) (0.9 * mAm.getStreamMaxVolume(AudioManager.STREAM_MUSIC)));
//            } else {
//                SharePrefUtil.create(Global.mContext).saveVolume(mAm.getStreamVolume(AudioManager.STREAM_MUSIC));
//            }
//        }
//        mAm.setStreamVolume(AudioManager.STREAM_MUSIC, mCurrentVolume, AudioManager.ADJUST_SAME);
    }

    private Resources getResources() {
        return cameraSurface.getView().getResources();
    }

    public View getView() {
        return cameraSurface.getView();
    }


    public void clearFocusAreas() {
        if (camera_controller == null) {
            return;
        }
        // don't cancelAutoFocus() here, otherwise we get sluggish zoom behaviour on Camera2 API
        camera_controller.clearFocusAndMetering();
        focus_success = FOCUS_DONE;
        successfully_focused = false;
    }

    private void mySurfaceCreated() {
        this.has_surface = true;
        this.openCamera();
    }

    private void mySurfaceDestroyed() {
        this.has_surface = false;
        this.closeCamera();
    }

    private void mySurfaceChanged() {
        // surface size is now changed to match the aspect ratio of camera preview - so we shouldn't change the preview to match the surface size, so no need to restart preview here
        if (camera_controller == null) {
            return;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mySurfaceCreated();
//        cameraSurface.getView().setWillNotDraw(false); // see http://stackoverflow.com/questions/2687015/extended-surfaceviews-ondraw-method-never-called
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        holder.removeCallback(this);
        mySurfaceDestroyed();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        if (holder.getSurface() == null) {
            return;
        }
        mySurfaceChanged();
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture arg0, int width, int height) {
        this.set_textureview_size = true;
        this.textureview_w = width;
        this.textureview_h = height;
        mySurfaceCreated();
        configureTransform();
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture arg0) {
        this.set_textureview_size = false;
        this.textureview_w = 0;
        this.textureview_h = 0;
        mySurfaceDestroyed();
        return true;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture arg0, int width, int height) {
        this.set_textureview_size = true;
        this.textureview_w = width;
        this.textureview_h = height;
        mySurfaceChanged();
        configureTransform();
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture arg0) {
    }

    private void configureTransform() {
        if (camera_controller == null || !this.set_preview_size || !this.set_textureview_size)
            return;
        int rotation = getDisplayRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, this.textureview_w, this.textureview_h);
        RectF bufferRect = new RectF(0, 0, this.preview_h, this.preview_w);
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) textureview_h / preview_h,
                    (float) textureview_w / preview_w);
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        cameraSurface.setTransform(matrix);
    }

    private Context getContext() {
        return applicationInterface.getContext();
    }

    private void closeCamera() {
        removePendingContinuousFocusReset();
        focus_success = FOCUS_DONE;
        synchronized (this) {
            // synchronise for consistency (keep FindBugs happy)
            take_photo_after_autofocus = false;
            // no need to call camera_controller.setCaptureFollowAutofocusHint() as we're closing the camera
        }
        set_flash_value_after_autofocus = "";
        successfully_focused = false;
        // n.b., don't reset has_set_location, as we can remember the location when switching camera
        if (continuous_focus_move_is_started) {
            continuous_focus_move_is_started = false;
        }
        if (camera_controller != null) {
            // need to check for camera being non-null again - if an error occurred stopping the video, we will have closed the camera, and may not be able to reopen
            if (camera_controller != null) {
                camera_controller.setPreviewCallback(null);
                pausePreview();
                camera_controller.release();
                camera_controller = null;
            }
        }
    }

    public void pausePreview() {
        this.phase = PHASE_NORMAL;
        this.is_preview_started = false;
        this.setPreviewPaused(false);
        if (camera_controller == null) {
            return;
        }
        camera_controller.stopPreview();
    }


    /**
     *
     * Try to open the camera. Should only be called if camera_controller==null.
     */
    private void openCamera() {
//       openCamera()->setupCamera()->setPreviewSize()
        set_preview_size = false;
        preview_w = 0;
        preview_h = 0;
        focus_success = FOCUS_DONE;
        synchronized (this) {
            // synchronise for consistency (keep FindBugs happy)
            take_photo_after_autofocus = false;
            // no need to call camera_controller.setCaptureFollowAutofocusHint() as we're opening the camera
        }
        set_flash_value_after_autofocus = "";
        successfully_focused = false;
        supports_iso_range = false;
        exposures = null;
        min_exposure = 0;
        max_exposure = 0;
        sizes = null;
        current_size_index = -1;
        supported_flash_values = null;
        current_flash_index = -1;
        supported_focus_values = null;
        current_focus_index = -1;
        if (!this.has_surface) {
            return;
        }
        if (this.app_is_paused) {
            return;
        }
        try {
            int cameraId = applicationInterface.getCameraIdPref();
            Log.d("gxh", cameraId + ";" + camera_controller_manager.getNumberOfCameras());
//            if (cameraId < 0 || cameraId >= camera_controller_manager.getNumberOfCameras()) {
//                cameraId = 1;
//                applicationInterface.setCameraIdPref(cameraId);
//            }
            CameraController.ErrorCallback cameraErrorCallback = new CameraController.ErrorCallback() {
                public void onError() {
                    if (camera_controller != null) {
                        camera_controller = null;
                    }
                }
            };
            camera_controller = new CameraController1(cameraId, cameraErrorCallback);
        } catch (CameraControllerException e) {
            e.printStackTrace();
            camera_controller = null;
            Log.d("gxh", "CameraControllerException");
        }
        if (camera_controller != null) {
            cameraSurface.setPreviewDisplay(camera_controller);
            setupCamera();
        }
    }

    public void setupCamera() {
        if (camera_controller == null) {
            return;
        }
        boolean do_startup_focus = applicationInterface.getStartupFocusPref();
        setupCameraParameters();
        setPreviewSize();
        this.setCameraDisplayOrientation();
        startCameraPreview();
        if (do_startup_focus) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tryAutoFocus(false); // so we get the autofocus when starting up - we do this on a delay, as calling it immediately means the autofocus doesn't seem to work properly sometimes (at least on Galaxy Nexus)
                }
            }, 500);
        }
    }

    private void setupCameraParameters() {
        {
            String value = applicationInterface.getSceneModePref();
            camera_controller.setSceneMode(value);
        }
        {
            CameraController.CameraFeatures camera_features = camera_controller.getCameraFeatures();
            this.sizes = camera_features.picture_sizes;
            supported_flash_values = camera_features.supported_flash_values;
            supported_focus_values = camera_features.supported_focus_values;
            this.supports_iso_range = camera_features.supports_iso_range;
            this.min_exposure = camera_features.min_exposure;
            this.max_exposure = camera_features.max_exposure;
        }
        {
            {
                String value = applicationInterface.getColorEffectPref();
                camera_controller.setColorEffect(value);
            }
            {
                String value = applicationInterface.getWhiteBalancePref();
                camera_controller.setWhiteBalance(value);
            }
            // must be done before setting flash modes, as we may remove flash modes if in manual mode
            String value = applicationInterface.getISOPref();
            if (supports_iso_range) {
                // now set the desired ISO mode/value
                if (value.equals("auto")) {
                    camera_controller.setManualISO(false, 0);
                } else {
                    // try to parse the supplied manual ISO value
                    try {
                        int iso = Integer.parseInt(value);
                        camera_controller.setManualISO(true, iso);
                    } catch (NumberFormatException exception) {
                        camera_controller.setManualISO(false, 0);
                    }
                }
            } else {
                camera_controller.setISO(value);
            }
            {
                exposures = null;
                if (min_exposure != 0 || max_exposure != 0) {
                    exposures = new ArrayList<>();
                    for (int i = min_exposure; i <= max_exposure; i++) {
                        exposures.add("" + i);
                    }
                    int exposure = applicationInterface.getExposureCompensationPref();
                    if (exposure < min_exposure || exposure > max_exposure) {
                        exposure = 0;
                        if (exposure < min_exposure || exposure > max_exposure) {
                            exposure = min_exposure;
                        }
                    }
                    camera_controller.setExposureCompensation(exposure);
                }
            }

            {
                current_size_index = -1;
                Pair<Integer, Integer> resolution = applicationInterface.getCameraResolutionPref();
                if (resolution != null) {
                    int resolution_w = resolution.first;
                    int resolution_h = resolution.second;
                    // now find size in valid list
                    for (int i = 0; i < sizes.size() && current_size_index == -1; i++) {
                        CameraController.Size size = sizes.get(i);
                        if (size.width == resolution_w && size.height == resolution_h) {
                            current_size_index = i;
                        }
                    }
                }

                if (current_size_index == -1) {
                    // set to largest
                    CameraController.Size current_size = null;
                    for (int i = 0; i < sizes.size(); i++) {
                        CameraController.Size size = sizes.get(i);
                        if (current_size == null || size.width * size.height > current_size.width * current_size.height) {
                            current_size_index = i;
                            current_size = size;
                        }
                    }
                }
                if (current_size_index != -1) {
                    CameraController.Size current_size = sizes.get(current_size_index);
                    applicationInterface.setCameraResolutionPref(current_size.width, current_size.height);
                }
            }
            {
                current_flash_index = -1;
                if (supported_flash_values != null && supported_flash_values.size() > 1) {
                    if (supported_flash_values.contains("flash_on"))
                        updateFlash("flash_on");
                    else
                        updateFlash("flash_off");
                } else {
                    supported_flash_values = null;
                }
            }
            {
                current_focus_index = -1;
                if (supported_focus_values != null && supported_focus_values.size() > 1) {
                    setFocusPref(true);
                } else {
                    supported_focus_values = null;
                }
            }
        }
    }

    private void setPreviewSize() {
        if (camera_controller == null) {
            return;
        }
        if (is_preview_started) {
            throw new RuntimeException(); // throw as RuntimeException, as this is a programming error
        }
        this.cancelAutoFocus();
        if (sizes != null && sizes.size() > 0) {
            CameraController.Size new_size = null;
            new_size = getPictureSize(sizes);
            Log.d("gxh", "setPictureSize:" + new_size.width + ";" + new_size.height);
            camera_controller.setPictureSize(new_size.width, new_size.height);
        }
        camera_controller.setPreviewCallback(new Camera.PreviewCallback() {
            @Override
            public void onPreviewFrame(byte[] data, Camera camera) {
                //System.arraycopy(data, 0, nv21, 0, data.length);
                buffer = data;
                Logger.d("gxh", "Camera:" + camera.getParameters().getPreviewSize().width + ";"
                        + camera.getParameters().getPreviewSize().height);
            }
        });
        camera_controller.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
    }


    public void saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + "pics" + File.separator + System.currentTimeMillis() + ".jpg");//将要保存图片的路径
        Log.d("gxh", file.getAbsolutePath());
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private CameraController.Size getPictureSize(List<CameraController.Size> sizeList) {
        if (sizeList == null || sizeList.size() == 0) {
            return null;
        }
        Collections.sort(sizeList, new Comparator<CameraController.Size>() {
            @Override
            public int compare(CameraController.Size size1, CameraController.Size size2) {
                return size1.height * size1.width - size2.height * size2.width;
            }
        });
        int temp = 2448 * 3264;
        for (CameraController.Size size : sizeList) {
            Logger.d("gxh", "size:" + size.width + ";" + size.height);
            if (size.width * size.height >= temp) {
                return size;
            }
        }
        return sizeList.get(sizeList.size() - 1);
    }

    /**
     * Returns the ROTATION_* enum of the display relative to the natural device orientation.
     */
    public int getDisplayRotation() {
        // gets the display rotation (as a Surface.ROTATION_* constant), taking into account the getRotatePreviewPreferenceKey() setting
        Activity activity = (Activity) this.getContext();
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        return rotation;
    }

    /**
     * Returns the rotation in degrees of the display relative to the natural device orientation.
     */
    private int getDisplayRotationDegrees() {
        int rotation = getDisplayRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
            default:
                break;
        }
        return degrees;
    }


    //-------------设置预览方向,如果你的应用是竖屏应用，就必须通过这个API将Camera的预览方向旋转90
    //这个api仅仅改变的是预览方向，并不会影响到PreviewCallback回调、生成的JPEG图片和录像文件的方向，这些数据的方向依然会跟图像Sensor的方向一致
    // for the Preview - from http://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation(int)
    // note, if orientation is locked to landscape this is only called when setting up the activity, and will always have the same orientation
    public void setCameraDisplayOrientation() {
        if (camera_controller == null) {
            return;
        }
        //int degrees = getDisplayRotationDegrees();
        // note the code to make the rotation relative to the camera sensor is done in camera_controller.setDisplayOrientation()
        camera_controller.setDisplayOrientation(0);//Demo是90
    }

    // for taking photos - from http://developer.android.com/reference/android/hardware/Camera.Parameters.html#setRotation(int)
    private void onOrientationChanged(int orientation) {
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN)
            return;
        if (camera_controller == null) {
            return;
        }
        orientation = (orientation + 45) / 90 * 90;
        int new_rotation;
        int camera_orientation = camera_controller.getCameraOrientation();
        if (camera_controller.isFrontFacing()) {
            new_rotation = (camera_orientation - orientation + 360) % 360;
        } else {
            new_rotation = (camera_orientation + orientation) % 360;
        }
        if (new_rotation != current_rotation) {
            this.current_rotation = new_rotation;
        }
    }

    private int getDeviceDefaultOrientation() {
        WindowManager windowManager = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);
        Configuration config = getResources().getConfiguration();
        int rotation = windowManager.getDefaultDisplay().getRotation();
        if (((rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) &&
                config.orientation == Configuration.ORIENTATION_LANDSCAPE)
                || ((rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) &&
                config.orientation == Configuration.ORIENTATION_PORTRAIT)) {
            return Configuration.ORIENTATION_LANDSCAPE;
        } else {
            return Configuration.ORIENTATION_PORTRAIT;
        }
    }

    /* Returns the rotation (in degrees) to use for images/videos, taking the preference_lock_orientation into account.
     */
    private int getImageVideoRotation() {
        String lock_orientation = applicationInterface.getLockOrientationPref();
        if (lock_orientation.equals("landscape")) {
            int camera_orientation = camera_controller.getCameraOrientation();
            int device_orientation = getDeviceDefaultOrientation();
            int result;
            if (device_orientation == Configuration.ORIENTATION_PORTRAIT) {
                // should be equivalent to onOrientationChanged(270)
                if (camera_controller.isFrontFacing()) {
                    result = (camera_orientation + 90) % 360;
                } else {
                    result = (camera_orientation + 270) % 360;
                }
            } else {
                // should be equivalent to onOrientationChanged(0)
                result = camera_orientation;
            }
            return result;
        } else if (lock_orientation.equals("portrait")) {
            int camera_orientation = camera_controller.getCameraOrientation();
            int result;
            int device_orientation = getDeviceDefaultOrientation();
            if (device_orientation == Configuration.ORIENTATION_PORTRAIT) {
                // should be equivalent to onOrientationChanged(0)
                result = camera_orientation;
            } else {
                // should be equivalent to onOrientationChanged(90)
                if (camera_controller.isFrontFacing()) {
                    result = (camera_orientation + 270) % 360;
                } else {
                    result = (camera_orientation + 90) % 360;
                }
            }
            return result;
        }
        return this.current_rotation;
    }

    public static int[] chooseBestPreviewFps(List<int[]> fps_ranges) {
        int selected_min_fps = -1, selected_max_fps = -1;
        for (int[] fps_range : fps_ranges) {
            int min_fps = fps_range[0];
            int max_fps = fps_range[1];
            if (max_fps >= 30000) {
                if (selected_min_fps == -1 || min_fps < selected_min_fps) {
                    selected_min_fps = min_fps;
                    selected_max_fps = max_fps;
                } else if (min_fps == selected_min_fps && max_fps > selected_max_fps) {
                    selected_min_fps = min_fps;
                    selected_max_fps = max_fps;
                }
            }
        }

        if (selected_min_fps != -1) {

        } else {
            // just pick the widest range; if more than one, pick the one with highest max
            int selected_diff = -1;
            for (int[] fps_range : fps_ranges) {
                int min_fps = fps_range[0];
                int max_fps = fps_range[1];
                int diff = max_fps - min_fps;
                if (selected_diff == -1 || diff > selected_diff) {
                    selected_min_fps = min_fps;
                    selected_max_fps = max_fps;
                    selected_diff = diff;
                } else if (diff == selected_diff && max_fps > selected_max_fps) {
                    selected_min_fps = min_fps;
                    selected_max_fps = max_fps;
                    selected_diff = diff;
                }
            }
        }
        return new int[]{selected_min_fps, selected_max_fps};
    }


    private void setPreviewFps() {
        List<int[]> fps_ranges = camera_controller.getSupportedPreviewFpsRange();
        if (fps_ranges == null || fps_ranges.size() == 0) {
            return;
        }
        int[] selected_fps;
        selected_fps = chooseBestPreviewFps(fps_ranges);
        camera_controller.setPreviewFpsRange(selected_fps[0], selected_fps[1]);
    }

    private void setFocusPref(boolean auto_focus) {
        updateFocus("focus_mode_continuous_picture", auto_focus);
    }


    private boolean updateFlash(String flash_value) {
        if (supported_flash_values != null) {
            int new_flash_index = supported_flash_values.indexOf(flash_value);
            if (new_flash_index != -1) {
                updateFlash(new_flash_index);
                return true;
            }
        }
        return false;
    }

    private void updateFlash(int new_flash_index) {
        if (supported_flash_values != null && new_flash_index != current_flash_index) {
            current_flash_index = new_flash_index;
            String flash_value = supported_flash_values.get(current_flash_index);
            this.setFlash(flash_value);
        }
    }

    private void setFlash(String flash_value) {
        set_flash_value_after_autofocus = ""; // this overrides any previously saved setting, for during the startup autofocus
        if (camera_controller == null) {
            return;
        }
        cancelAutoFocus();
        camera_controller.setFlashValue(flash_value);
    }

    private boolean updateFocus(String focus_value, boolean auto_focus) {
        if (this.supported_focus_values != null) {
            int new_focus_index = supported_focus_values.indexOf(focus_value);
            if (new_focus_index != -1) {
                updateFocus(new_focus_index, auto_focus);
                return true;
            }
        }
        return false;
    }


    private void updateFocus(int new_focus_index, boolean auto_focus) {
        if (this.supported_focus_values != null && new_focus_index != current_focus_index) {
            current_focus_index = new_focus_index;
            String focus_value = supported_focus_values.get(current_focus_index);
            this.setFocusValue(focus_value, auto_focus);
        }
    }

    /**
     * This returns the flash mode indicated by the UI, rather than from the camera parameters.
     */
    public String getCurrentFocusValue() {
        if (camera_controller == null) {
            return null;
        }
        if (this.supported_focus_values != null && this.current_focus_index != -1)
            return this.supported_focus_values.get(current_focus_index);
        return null;
    }

    private void setFocusValue(String focus_value, boolean auto_focus) {
        if (camera_controller == null) {
            return;
        }
        cancelAutoFocus();
        removePendingContinuousFocusReset(); // this isn't strictly needed as the reset_continuous_focus_runnable will check the ui focus mode when it runs, but good to remove it anyway
        autofocus_in_continuous_mode = false;
        camera_controller.setFocusValue(focus_value);
        setupContinuousFocusMove();
        clearFocusAreas();
        if (auto_focus && !focus_value.equals("focus_mode_locked")) {
            tryAutoFocus(false);
        }
    }

    private void setupContinuousFocusMove() {
        if (continuous_focus_move_is_started) {
            continuous_focus_move_is_started = false;
        }
        String focus_value = current_focus_index != -1 ? supported_focus_values.get(current_focus_index) : null;
        if (camera_controller != null && focus_value != null && focus_value.equals("focus_mode_continuous_picture")) {
            camera_controller.setContinuousFocusMoveCallback(new CameraController.ContinuousFocusMoveCallback() {
                @Override
                public void onContinuousFocusMove(boolean start) {
                    if (start != continuous_focus_move_is_started) { // filter out repeated calls with same start value
                        continuous_focus_move_is_started = start;
                    }
                }
            });
        } else if (camera_controller != null) {
            camera_controller.setContinuousFocusMoveCallback(null);
        }
    }

    /**
     * User has clicked the "take picture" button (or equivalent GUI operation).
     */
    public void takePicturePressed() {
        if (camera_controller == null) {
            this.phase = PHASE_NORMAL;
            return;
        }
        if (!this.has_surface) {
            this.phase = PHASE_NORMAL;
            return;
        }
        if (this.phase == PHASE_TAKING_PHOTO) {
            return;
        }
        this.startCameraPreview();
        takePicture();
    }

    /**
     * Initiate "take picture" command. In video mode this means starting video command. In photo mode this may involve first
     * autofocusing.
     */
    private void takePicture() {
        this.phase = PHASE_TAKING_PHOTO;
        synchronized (this) {
            take_photo_after_autofocus = false;
        }
        if (camera_controller == null) {
            this.phase = PHASE_NORMAL;
            return;
        }
        if (!this.has_surface) {
            this.phase = PHASE_NORMAL;
            return;
        }
        takePhoto(false);
    }

    /**
     * Take photo. The caller should aready have set the phase to PHASE_TAKING_PHOTO.
     */
    private void takePhoto(boolean skip_autofocus) {
        String current_ui_focus_value = getCurrentFocusValue();
        if (autofocus_in_continuous_mode) {
            synchronized (this) {
                // as below, if an autofocus is in progress, then take photo when it's completed
                if (focus_success == FOCUS_WAITING) {
                    take_photo_after_autofocus = true;
                    camera_controller.setCaptureFollowAutofocusHint(true);
                } else {
                    // when autofocus_in_continuous_mode==true, it means the user recently touched to focus in continuous focus mode, so don't do another focus
                    takePhotoWhenFocused();
                }
            }
        } else if (camera_controller.focusIsContinuous()) {
            // we call via autoFocus(), to avoid risk of taking photo while the continuous focus is focusing - risk of blurred photo, also sometimes get bug in such situations where we end of repeatedly focusing
            // this is the case even if skip_autofocus is true (as we still can't guarantee that continuous focusing might be occurring)
            // note: if the user touches to focus in continuous mode, we camera controller may be in auto focus mode, so we should only enter this codepath if the camera_controller is in continuous focus mode
            CameraController.AutoFocusCallback autoFocusCallback = new CameraController.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success) {
                    takePhotoWhenFocused();
                }
            };
            camera_controller.autoFocus(autoFocusCallback, true);
        } else if (skip_autofocus || this.recentlyFocused()) {
            takePhotoWhenFocused();
        } else if (current_ui_focus_value != null && (current_ui_focus_value.equals("focus_mode_auto") || current_ui_focus_value.equals("focus_mode_macro"))) {
            // n.b., we check focus_value rather than camera_controller.supportsAutoFocus(), as we want to discount focus_mode_locked
            synchronized (this) {
                if (focus_success == FOCUS_WAITING) {
                    // Needed to fix bug (on Nexus 6, old camera API): if flash was on, pointing at a dark scene, and we take photo when already autofocusing, the autofocus never returned so we got stuck!
                    // In general, probably a good idea to not redo a focus - just use the one that's already in progress
                    take_photo_after_autofocus = true;
                    camera_controller.setCaptureFollowAutofocusHint(true);
                } else {
                    focus_success = FOCUS_DONE; // clear focus rectangle for new refocus
                    CameraController.AutoFocusCallback autoFocusCallback = new CameraController.AutoFocusCallback() {
                        @Override
                        public void onAutoFocus(boolean success) {
                            ensureFlashCorrect(); // need to call this in case user takes picture before startup focus completes!
                            takePhotoWhenFocused();
                        }
                    };
                    camera_controller.autoFocus(autoFocusCallback, true);
                }
            }
        } else {
            takePhotoWhenFocused();
        }
    }

    /**
     * Take photo, assumes any autofocus has already been taken care of, and that applicationInterface.cameraInOperation(true) has
     * already been called.
     * Note that even if a caller wants to take a photo without focusing, you probably want to call takePhoto() with skip_autofocus
     * set to true (so that things work okay in continuous picture focus mode).
     */
    private void takePhotoWhenFocused() {
        if (camera_controller == null) {
            this.phase = PHASE_NORMAL;
            return;
        }
        if (!this.has_surface) {
            this.phase = PHASE_NORMAL;
            return;
        }
        final String focus_value = current_focus_index != -1 ? supported_focus_values.get(current_focus_index) : null;
        if (focus_value != null && focus_value.equals("focus_mode_locked") && focus_success == FOCUS_WAITING) {
            // make sure there isn't an autofocus in progress - can happen if in locked mode we take a photo while autofocusing - see testTakePhotoLockedFocus() (although that test doesn't always properly test the bug...)
            // we only cancel when in locked mode and if still focusing, as I had 2 bug reports for v1.16 that the photo was being taken out of focus; both reports said it worked fine in 1.15, and one confirmed that it was due to the cancelAutoFocus() line, and that it's now fixed with this fix
            // they said this happened in every focus mode, including locked - so possible that on some devices, cancelAutoFocus() actually pulls the camera out of focus, or reverts to preview focus?
            cancelAutoFocus();
        }
        removePendingContinuousFocusReset(); // to avoid switching back to continuous focus mode while taking a photo - instead we'll always make sure we switch back after taking a photo
        focus_success = FOCUS_DONE; // clear focus rectangle if not already done
        successfully_focused = false; // so next photo taken will require an autofocus
        CameraController.PictureCallback pictureCallback = new CameraController.PictureCallback() {
            private boolean success = false; // whether jpeg callback succeeded

            public void onStarted() {
            }

            public void onCompleted() {
                is_preview_started = false; // preview automatically stopped due to taking photo on original Camera API
                phase = PHASE_NORMAL; // need to set this even if remaining burst photos, so we can restart the preview
                phase = PHASE_NORMAL;
                boolean pause_preview = false;
                if (pause_preview && success) {
                    if (is_preview_started) {
                        // need to manually stop preview on Android L Camera2
                        camera_controller.stopPreview();
                        is_preview_started = false;
                    }
                    setPreviewPaused(true);
                } else {
                    if (!is_preview_started) {
                        // we need to restart the preview; and we do this in the callback, as we need to restart after saving the image
                        // (otherwise this can fail, at least on Nexus 7)
                        startCameraPreview();
                    }
                }
                continuousFocusReset(); // in case we took a photo after user had touched to focus (causing us to switch from continuous to autofocus mode)
                if (camera_controller != null && focus_value != null && (focus_value.equals("focus_mode_continuous_picture"))) {
                    camera_controller.cancelAutoFocus(); // needed to restart continuous focusing
                }
            }

            public void onPictureTaken(byte[] data) {
                if (!applicationInterface.onPictureTaken(data)) {
                    success = false;
                } else {
                    success = true;
                }
            }

            public void onRawPictureTaken(DngCreator dngCreator, Image image) {

            }

            public void onBurstPictureTaken(List<byte[]> images) {

            }

            public void onFrontScreenTurnOn() {
            }
        };
        CameraController.ErrorCallback errorCallback = new CameraController.ErrorCallback() {
            public void onError() {
                phase = PHASE_NORMAL;
                startCameraPreview();
            }
        };
        {

            //Demo中是90，这里实际适配这款魔镜，我改成了270，拍成照正常
            camera_controller.setRotation(270);
            //camera_controller.setRotation(getImageVideoRotation());
            camera_controller.takePicture(pictureCallback, errorCallback);
        }
    }

    private void tryAutoFocus(final boolean startup) {
        if (camera_controller == null) {

        } else if (!this.has_surface) {

        } else if (!this.is_preview_started) {

        } else if (this.isTakingPhoto()) {

        } else {
            // it's only worth doing autofocus when autofocus has an effect (i.e., auto or macro mode)
            // but also for continuous focus mode, triggering an autofocus is still important to fire flash when touching the screen
            if (camera_controller.supportsAutoFocus()) {
                set_flash_value_after_autofocus = "";
                String old_flash_value = camera_controller.getFlashValue();
                if (startup && old_flash_value.length() > 0 && !old_flash_value.equals("flash_off") && !old_flash_value.equals("flash_torch")) {
                    set_flash_value_after_autofocus = old_flash_value;
                    camera_controller.setFlashValue("flash_off");
                }
                CameraController.AutoFocusCallback autoFocusCallback = new CameraController.AutoFocusCallback() {
                    @Override
                    public void onAutoFocus(boolean success) {
                        autoFocusCompleted(success, false);
                    }
                };

                this.focus_success = FOCUS_WAITING;
                this.successfully_focused = false;
                camera_controller.autoFocus(autoFocusCallback, false);
            }
        }
    }

    /**
     * If the user touches the screen in continuous focus mode, we switch the camera_controller to autofocus mode.
     * After the autofocus completes, we set a reset_continuous_focus_runnable to switch back to the camera_controller
     * back to continuous focus after a short delay.
     * This function removes any pending reset_continuous_focus_runnable.
     */
    private void removePendingContinuousFocusReset() {
        if (reset_continuous_focus_runnable != null) {
            reset_continuous_focus_handler.removeCallbacks(reset_continuous_focus_runnable);
            reset_continuous_focus_runnable = null;
        }
    }

    /**
     * If the user touches the screen in continuous focus mode, we switch the camera_controller to autofocus mode.
     * This function is called to see if we should switch from autofocus mode back to continuous focus mode.
     * If this isn't required, calling this function does nothing.
     */
    private void continuousFocusReset() {
        if (camera_controller != null && autofocus_in_continuous_mode) {
            autofocus_in_continuous_mode = false;
            // check again
            String current_ui_focus_value = getCurrentFocusValue();
            if (current_ui_focus_value != null && !camera_controller.getFocusValue().equals(current_ui_focus_value) && camera_controller.getFocusValue().equals("focus_mode_auto")) {
                camera_controller.cancelAutoFocus();
                camera_controller.setFocusValue(current_ui_focus_value);
            }
        }
    }

    private void cancelAutoFocus() {
        if (camera_controller != null) {
            camera_controller.cancelAutoFocus();
            autoFocusCompleted(false, true);
        }
    }

    private void ensureFlashCorrect() {
        // ensures flash is in correct mode, in case where we had to turn flash temporarily off for startup autofocus
        if (set_flash_value_after_autofocus.length() > 0 && camera_controller != null) {
            camera_controller.setFlashValue(set_flash_value_after_autofocus);
            set_flash_value_after_autofocus = "";
        }
    }

    private void autoFocusCompleted(boolean success, boolean cancelled) {
        if (cancelled) {
            focus_success = FOCUS_DONE;
        } else {
            focus_success = success ? FOCUS_SUCCESS : FOCUS_FAILED;
        }
        ensureFlashCorrect();
        synchronized (this) {
            if (take_photo_after_autofocus) {
                take_photo_after_autofocus = false;
                takePhotoWhenFocused();
            }
        }
    }

    public void startCameraPreview() {
        if (camera_controller != null && !this.isTakingPhoto() && !is_preview_started) {
            setPreviewFps();
            try {
                camera_controller.startPreview();
            } catch (CameraControllerException e) {
                e.printStackTrace();
                return;
            }
            this.is_preview_started = true;
        }
        this.setPreviewPaused(false);
        this.setupContinuousFocusMove();
    }

    private void setPreviewPaused(boolean paused) {
        if (paused) {
            this.phase = PHASE_PREVIEW_PAUSED;
            // shouldn't call applicationInterface.cameraInOperation(true), as should already have done when we started to take a photo (or above when exiting immersive mode)
        } else {
            this.phase = PHASE_NORMAL;
//            applicationInterface.cameraInOperation(false);
        }
    }


    public void onResume() {
        this.app_is_paused = false;
        cameraSurface.onResume();
        this.openCamera();

        //人脸识别
        runFaceDector();
    }

    public void onPause() {
        this.app_is_paused = true;
        this.closeCamera();
        cameraSurface.onPause();
        pause();
    }

    public boolean isTakingPhoto() {
        return this.phase == PHASE_TAKING_PHOTO;
    }


    private boolean recentlyFocused() {
        return this.successfully_focused && System.currentTimeMillis() < this.successfully_focused_time + 5000;
    }

    @Override
    public void runFaceDector() {
        mStopTrack = false;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mStopTrack) {

                    if (null == buffer) {
                        continue;
                    }

//
//                    final Bbox bbox = DetectManager.Detect(1920, 1080, buffer);
//
//                    Log.d("gxh-bbox", (bbox == null ? "null" : bbox.eyeDistance + ""));
//
//                    if (bbox == null) {
//                        continue;
//                    }
//                    float scale = bbox.eyeDistance / 480;
//                    System.out.println("FUCK2:" + scale);
//                    if (mFlag) {
////                        mStart = 0.22f;
////                        mEnd = 0.35f;
////                        mStart = 0.375f;
////                        mEnd = 0.5f;
////
////                        mStart = 0.33f;
////                        mEnd = 0.458f;
//
//                        mStart = 0.5f;
//                        mEnd = 0.791f;
//
//                    }
//                    if (scale < mStart) {
//                        mFlag = false;
//                        playCloser();
//                        Log.d("gxh", "playCloser");
//                    } else if (scale > mEnd) {
//                        mFlag = false;
//                        playFarther();
//                        Log.d("gxh", "playFarther");
//                    } else {
//                        mFlag = true;
//                        playSuit();
//                        Log.d("gxh", "playSuit");
//                    }
                }
            }
        }).start();
    }

    @Override
    public void pause() {
        mFlag = false;
        mStopTrack = true;
    }

    @Override
    public void destroy() {
        mStopTrack = true;
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
        resetVolume();
    }


    @Override
    public void restart() {
        mStopTrack = false;
    }

//    private void playCloser() {
//        if (mMediaPlayer != null && mSoundSate == sFarSTATE && mMediaPlayer.isPlaying()) {
//            return;
//        } else {
//            mSoundSate = sFarSTATE;
//            reset();
//            try {
//                AssetFileDescriptor afd = Global.mContext.getResources().openRawResourceFd(R.raw.near);
//                if (afd == null) return;
//                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private void playFarther() {
//        if (mMediaPlayer != null && mMediaPlayer.isPlaying() && mSoundSate == sCloseSTATE) {
//            return;
//        } else {
//            mSoundSate = sCloseSTATE;
//            reset();
//            try {
//                AssetFileDescriptor afd = Global.mContext.getResources().openRawResourceFd(R.raw.far);
//                if (afd == null) return;
//                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
//        }
//    }
//
//    private void playSuit() {
//        if (mMediaPlayer == null) {
//            return;
//        }
//        if (mMediaPlayer != null && mMediaPlayer.isPlaying() && mSoundSate == sSUITABLE) {
//            return;
//        } else {
//            mSoundSate = sSUITABLE;
//            reset();
//            try {
//                AssetFileDescriptor afd = Global.mContext.getResources().openRawResourceFd(R.raw.suit);
//                if (afd == null) return;
//                mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
//                afd.close();
//                mMediaPlayer.prepare();
//                mMediaPlayer.start();
//                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(final MediaPlayer mp) {
//                        if (mp == mMediaPlayer) {
//                            synchronized (mObject) {
//                                mMediaPlayer.stop();
//                                mMediaPlayer.reset();
//                                mMediaPlayer = null;
//                                takePicturePressed();
//                                mStopTrack = true;
//                            }
//                        }
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


    private void reset() {
        if (mMediaPlayer != null) {
            synchronized (mObject) {
                if (mMediaPlayer == null)
                    return;
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                }
                mMediaPlayer.reset();
                mMediaPlayer.setOnPreparedListener(null);
                mMediaPlayer.setOnCompletionListener(null);
            }
        }
    }

    private Bitmap getBitmap(byte[] data) {

        //Camera.Size previewSize = camera.getParameters().getPreviewSize();
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        YuvImage yuvimage = new YuvImage(
                data,
                ImageFormat.NV21,
                1920,
                1080,
                null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvimage.compressToJpeg(new Rect(0, 0,
                1920,
                1080), 100, baos);// 80--JPG图片的质量[0-100],100最高
        byte[] bytes = baos.toByteArray();
        //将rawImage转换成bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

    }


    public static String saveBitmap(Bitmap mBitmap) {
        File filePic;

        String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "images";
        try {
            filePic = new File(savePath + File.separator + System.currentTimeMillis() + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic.getAbsolutePath();
    }

}
