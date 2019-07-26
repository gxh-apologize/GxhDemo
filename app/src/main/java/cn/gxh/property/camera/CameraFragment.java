package cn.gxh.property.camera;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.RectF;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import java.io.File;
import java.io.FileOutputStream;

import cn.gxh.base.BaseFragment;
import cn.gxh.base.Logger;
import cn.gxh.view.R;

/**
 * Created  by gxh on 2019/5/22 14:28
 */
public class CameraFragment extends BaseFragment implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private int mCameraId;//摄像头id
    private Camera.Parameters parameters;
    private Camera camera;
    private ImageView ivPicture;
    private MyOrientationEventListener orientationEventListener;
    //SurfaceView宽高
    private int surfaceWidth;
    private int surfaceHeight;
    private FaceView faceView;


    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_camera;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        surfaceView = findView(R.id.surfaceView);
        ivPicture = findView(R.id.iv_fragment_camera_takepicture);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        faceView = findView(R.id.faceview);
    }


    @Override
    public void initListener() {

        ivPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        if (data == null) {
                            return;
                        }

                        String dirPath = mActivity.getExternalCacheDir().getAbsolutePath();
                        final String picturePath = dirPath + File.separator + System.currentTimeMillis() + ".jpg";
                        try {
                            FileOutputStream fileOutputStream = new FileOutputStream(new File(picturePath));
                            fileOutputStream.write(data);
                            fileOutputStream.close();
                        } catch (Exception error) {

                        } finally {
                            camera.startPreview();
                        }
                    }
                });
            }
        });

    }

    @Override
    public void initData() {

    }


    @Override
    public void onResume() {
        super.onResume();
        Logger.d("gxh", "onResume");
        //startPreview();
    }

    /**
     * 开启预览
     */
    private void startPreview() {
        //打开摄像头
        camera = open();
        setupCameraParameters();
        setCameraDisplayOrientation();
        setPictureOrientation();
        setPreviewSize();

        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.setPreviewCallback(this);
            camera.startPreview();
            faceDetch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void faceDetch() {
        camera.startFaceDetection();
        camera.setFaceDetectionListener(new Camera.FaceDetectionListener() {
            @Override
            public void onFaceDetection(Camera.Face[] faces, Camera camera) {

                Logger.d("gxh", "onFaceDetection:" + faces.length + ";" + Thread.currentThread().getName());
                if (faces == null || faces.length == 0) {
                    return;
                }

                Camera.Face face = faces[0];

                Matrix matrix = new Matrix();
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(mCameraId, info);
                // Need mirror for front camera.
                boolean mirror = (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
                matrix.setScale(mirror ? -1 : 1, 1);
                // This is the value for android.hardware.Camera.setDisplayOrientation.
                matrix.postRotate(displayOrientation(mActivity));
                // Camera driver coordinates range from (-1000, -1000) to (1000, 1000).
                // UI coordinates range from (0, 0) to (width, height).
                matrix.postScale(surfaceWidth / 2000f, surfaceHeight / 2000f);
                matrix.postTranslate(surfaceWidth / 2f, surfaceHeight / 2f);

                RectF srcRect=new RectF(face.rect);
                RectF dstRect =new RectF(0f, 0f, 0f, 0f);
                matrix.mapRect(dstRect,srcRect);

                faceView.drawFace(dstRect);
            }
        });
    }

    /**
     * 设置预览大小
     */
    private void setPreviewSize() {
        Point bestPreview = CameraPreviewUtils.getBestPreview(parameters, new Point(surfaceWidth, surfaceHeight));
        Logger.d("gxh", "bestPreview:" + surfaceWidth + ";" + surfaceHeight + ";" +
                bestPreview.x + ";" + bestPreview.y + ";" + surfaceView.getMeasuredWidth() + ";" +
                surfaceView.getMeasuredHeight());
        parameters.setPreviewSize(bestPreview.x, bestPreview.y);
        camera.setParameters(parameters);
    }

    private void setPictureOrientation() {
        orientationEventListener = new MyOrientationEventListener(mActivity);
        orientationEventListener.enable();
    }

    private class MyOrientationEventListener extends OrientationEventListener {

        public MyOrientationEventListener(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            //Logger.d("gxh", "orientation:" + orientation);

            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN)
                return;
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(mCameraId, info);
            orientation = (orientation + 45) / 90 * 90;
            int rotation = 0;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                rotation = (info.orientation - orientation + 360) % 360;
            } else {
                rotation = (info.orientation + orientation) % 360;
            }

            parameters.setRotation(rotation);
            camera.setParameters(parameters);
        }
    }


    private void setupCameraParameters() {
        if (parameters == null) {
            parameters = camera.getParameters();
        }

        //设置保存的图片格式
        parameters.setPictureFormat(PixelFormat.JPEG);
        //设置预览的图片格式
        parameters.setPreviewFormat(ImageFormat.NV21);

        //设置闪光灯模式
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
        //设置对焦模式
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        //设置场景模式
        parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);

        camera.setParameters(parameters);
    }

    /**
     * 设置预览方向
     */
    private void setCameraDisplayOrientation() {
        int degress = displayOrientation(mActivity);
        camera.setDisplayOrientation(degress);

    }

    private int displayOrientation(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay().getRotation();
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
                degrees = 0;
                break;
        }

        int result = 0;

        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }

        Logger.d("gxh", degrees + ";" + result + ";" + info.orientation);
        return result;
    }

    /**
     * 打开相机
     *
     * @return
     */
    private Camera open() {
        Camera camera;
        //获取摄像头数量
        int numCameras = Camera.getNumberOfCameras();
        if (numCameras == 0) {
            Logger.d("gxh", "摄像头数量" + numCameras);
            return null;
        }

        int index = 0;
        while (index < numCameras) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(index, cameraInfo);
            Logger.d("gxh", index + ";" + cameraInfo.facing);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                break;
            }
            index++;
        }

        if (index < numCameras) {
            camera = Camera.open(index);
            mCameraId = index;
        } else {
            camera = Camera.open(0);
            mCameraId = 0;
        }
        return camera;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Logger.d("gxh", "surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Logger.d("gxh", "surfaceChanged");
        surfaceWidth = width;
        surfaceHeight = height;
        startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Logger.d("gxh", "surfaceDestroyed");
//        camera.release();
    }

    //视频流的回调
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Logger.d("gxh", "onPreviewFrame");
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Logger.d("gxh", newConfig.orientation + "");
        displayOrientation(mActivity);
    }


    private void stopPreview() {
        if (camera != null) {
            try {
                camera.setErrorCallback(null);
                camera.setPreviewCallback(null);
                camera.stopFaceDetection();
                camera.stopPreview();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                camera.release();
                camera = null;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPreview();
    }
}
