package cn.gxh.property.camera.preview.camerasurface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import cn.gxh.property.camera.cameracontroller.CameraController;
import cn.gxh.property.camera.cameracontroller.CameraControllerException;
import cn.gxh.property.camera.preview.Preview;


/**
 * Provides support for the surface used for the preview, using a SurfaceView.
 */
public class MySurfaceView extends SurfaceView implements CameraSurface {
    private static final String TAG = "MySurfaceView";

    public MySurfaceView(Context context, final Preview preview) {
        super(context);
        getHolder().addCallback(preview);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // deprecated
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setPreviewDisplay(CameraController camera_controller) {
        try {
            camera_controller.setPreviewDisplay(this.getHolder());
        } catch (CameraControllerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setVideoRecorder(MediaRecorder video_recorder) {
        video_recorder.setPreviewDisplay(this.getHolder().getSurface());
    }


    @Override
    public void onDraw(Canvas canvas) {
    }


    @Override
    public void setTransform(Matrix matrix) {
        throw new RuntimeException();
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
    }
}
