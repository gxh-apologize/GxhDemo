package cn.gxh.property.camera.cameracontroller;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusMoveCallback;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Provides support using Android's original camera API
 * android.hardware.Camera.
 */
@SuppressWarnings("deprecation")
public class CameraController1 extends CameraController {
    private static final String TAG = "CameraController1";

    private Camera camera;
    private final Camera.CameraInfo camera_info = new Camera.CameraInfo();
    private String iso_key;
    private final ErrorCallback camera_error_cb;




    /**
     * Opens the camera device.
     *
     * @param cameraId        Which camera to open (must be between 0 and CameraControllerManager1.getNumberOfCameras()-1).
     * @param camera_error_cb onError() will be called if the camera closes due to serious error. No more calls to the CameraController1 object should be made (though a new one can be created, to try reopening the camera).
     * @throws CameraControllerException if the camera device fails to open.
     */
    public CameraController1(int cameraId, final ErrorCallback camera_error_cb) throws CameraControllerException {
        super(cameraId);
        this.camera_error_cb = camera_error_cb;
        try {
            camera = Camera.open(cameraId);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new CameraControllerException();
        }
        if (camera == null) {
            // Although the documentation says Camera.open() should throw a RuntimeException, it seems that it some cases it can return null
            // I've seen this in some crashes reported in Google Play; also see:
            // http://stackoverflow.com/questions/12054022/camera-open-returns-null
            throw new CameraControllerException();
        }
        try {
            Camera.getCameraInfo(cameraId, camera_info);
        } catch (RuntimeException e) {
            // Had reported RuntimeExceptions from Google Play
            // also see http://stackoverflow.com/questions/22383708/java-lang-runtimeexception-fail-to-get-camera-info
            e.printStackTrace();
            this.release();
            throw new CameraControllerException();
        }
        final CameraErrorCallback camera_error_callback = new CameraErrorCallback();
        camera.setErrorCallback(camera_error_callback);
    }

    @Override
    public void onError() {
        Log.e(TAG, "onError");
        if (this.camera != null) { // I got Google Play crash reports due to camera being null in v1.36
            this.camera.release();
            this.camera = null;
        }
        if (this.camera_error_cb != null) {
            // need to communicate the problem to the application
            this.camera_error_cb.onError();
        }
    }

    private class CameraErrorCallback implements Camera.ErrorCallback {
        @Override
        public void onError(int error, Camera cam) {
            // n.b., as this is potentially serious error, we always log even if MyDebug.LOG is false
            Log.e(TAG, "camera onError: " + error);
            if (error == Camera.CAMERA_ERROR_SERVER_DIED) {
                Log.e(TAG, "    CAMERA_ERROR_SERVER_DIED");
                CameraController1.this.onError();
            } else if (error == Camera.CAMERA_ERROR_UNKNOWN) {
                Log.e(TAG, "    CAMERA_ERROR_UNKNOWN ");
            }
        }
    }

    public void release() {
        camera.release();
        camera = null;
    }

    private Camera.Parameters getParameters() {
        return camera.getParameters();
    }

    private void setCameraParameters(Camera.Parameters parameters) {
        try {
            camera.setParameters(parameters);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    private List<String> convertFlashModesToValues(List<String> supported_flash_modes) {
        List<String> output_modes = new ArrayList<>();
        if (supported_flash_modes != null) {
            // also resort as well as converting
            if (supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                output_modes.add("flash_off");
            }
            if (supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
                output_modes.add("flash_auto");
            }
            if (supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_ON)) {
                output_modes.add("flash_on");
            }
            if (supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                output_modes.add("flash_torch");
            }
            if (supported_flash_modes.contains(Camera.Parameters.FLASH_MODE_RED_EYE)) {
                output_modes.add("flash_red_eye");
            }
        }

        // Samsung Galaxy S7 at least for front camera has supported_flash_modes: auto, beach, portrait?!
        // so rather than checking supported_flash_modes, we should check output_modes here
        // this is always why we check whether the size is greater than 1, rather than 0 (this also matches
        // the check we do in Preview.setupCameraParameters()).
        if (output_modes.size() > 1) {

        } else {
            if (isFrontFacing()) {
                output_modes.clear(); // clear any pre-existing mode (see note above about Samsung Galaxy S7)
                output_modes.add("flash_off");
                output_modes.add("flash_frontscreen_on");
            } else {
                // probably best to not return any modes, rather than one mode (see note about about Samsung Galaxy S7)
                output_modes.clear();
            }
        }

        return output_modes;
    }

    private List<String> convertFocusModesToValues(List<String> supported_focus_modes) {
        List<String> output_modes = new ArrayList<>();
        if (supported_focus_modes != null) {
            // also resort as well as converting
            if (supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                output_modes.add("focus_mode_auto");
            }
            if (supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_INFINITY)) {
                output_modes.add("focus_mode_infinity");
            }
            if (supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_MACRO)) {
                output_modes.add("focus_mode_macro");
            }
            if (supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                output_modes.add("focus_mode_locked");
            }
            if (supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_FIXED)) {
                output_modes.add("focus_mode_fixed");
            }
            if (supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_EDOF)) {
                output_modes.add("focus_mode_edof");
            }
            if (supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                output_modes.add("focus_mode_continuous_picture");
            }
            if (supported_focus_modes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                output_modes.add("focus_mode_continuous_video");
            }
        }
        return output_modes;
    }

    public String getAPI() {
        return "Camera";
    }

    public CameraFeatures getCameraFeatures() {
        Camera.Parameters parameters = this.getParameters();
        CameraFeatures camera_features = new CameraFeatures();
        // get available sizes
        List<Camera.Size> camera_picture_sizes = parameters.getSupportedPictureSizes();
        camera_features.picture_sizes = new ArrayList<>();
        //camera_features.picture_sizes.add(new CameraController.Size(1920, 1080)); // test
        for (Camera.Size camera_size : camera_picture_sizes) {
            camera_features.picture_sizes.add(new CameraController.Size(camera_size.width, camera_size.height));
        }

        //camera_features.supported_flash_modes = parameters.getSupportedFlashModes(); // Android format
        List<String> supported_flash_modes = parameters.getSupportedFlashModes(); // Android format
        camera_features.supported_flash_values = convertFlashModesToValues(supported_flash_modes); // convert to our format (also resorts)

        List<String> supported_focus_modes = parameters.getSupportedFocusModes(); // Android format
        camera_features.supported_focus_values = convertFocusModesToValues(supported_focus_modes); // convert to our format (also resorts)
        camera_features.max_num_focus_areas = parameters.getMaxNumFocusAreas();

        camera_features.is_exposure_lock_supported = parameters.isAutoExposureLockSupported();
        camera_features.min_exposure = parameters.getMinExposureCompensation();
        camera_features.max_exposure = parameters.getMaxExposureCompensation();
        camera_features.exposure_step = getExposureCompensationStep();
        camera_features.supports_expo_bracketing = (camera_features.min_exposure != 0 && camera_features.max_exposure != 0); // require both a darker and brighter exposure, in order to support expo bracketing

        List<Camera.Size> camera_preview_sizes = parameters.getSupportedPreviewSizes();
        camera_features.preview_sizes = new ArrayList<>();
        for (Camera.Size camera_size : camera_preview_sizes) {
            camera_features.preview_sizes.add(new CameraController.Size(camera_size.width, camera_size.height));
        }
        return camera_features;
    }

    // important, from docs:
    // "Changing scene mode may override other parameters (such as flash mode, focus mode, white balance).
    // For example, suppose originally flash mode is on and supported flash modes are on/off. In night
    // scene mode, both flash mode and supported flash mode may be changed to off. After setting scene
    // mode, applications should call getParameters to know if some parameters are changed."
    public SupportedValues setSceneMode(String value) {
        String default_value = getDefaultSceneMode();
        Camera.Parameters parameters = this.getParameters();
        List<String> values = parameters.getSupportedSceneModes();
        /*{
			// test
			values = new ArrayList<>();
			values.add("auto");
		}*/
        SupportedValues supported_values = checkModeIsSupported(values, value, default_value);
        if (supported_values != null) {
            String scene_mode = parameters.getSceneMode();
            // if scene mode is null, it should mean scene modes aren't supported anyway
            if (scene_mode != null && !scene_mode.equals(supported_values.selected_value)) {
                parameters.setSceneMode(supported_values.selected_value);
                setCameraParameters(parameters);
            }
        }
        return supported_values;
    }

    public SupportedValues setColorEffect(String value) {
        String default_value = getDefaultColorEffect();
        Camera.Parameters parameters = this.getParameters();
        List<String> values = parameters.getSupportedColorEffects();
        SupportedValues supported_values = checkModeIsSupported(values, value, default_value);
        if (supported_values != null) {
            String color_effect = parameters.getColorEffect();
            // have got nullpointerexception from Google Play, so now check for null
            if (color_effect == null || !color_effect.equals(supported_values.selected_value)) {
                parameters.setColorEffect(supported_values.selected_value);
                setCameraParameters(parameters);
            }
        }
        return supported_values;
    }

    public SupportedValues setWhiteBalance(String value) {
        String default_value = getDefaultWhiteBalance();
        Camera.Parameters parameters = this.getParameters();
        List<String> values = parameters.getSupportedWhiteBalance();
        if (values != null) {
            // Some devices (e.g., OnePlus 3T) claim to support a "manual" mode, even though this
            // isn't one of the possible white balances defined in Camera.Parameters.
            // Since the old API doesn't support white balance temperatures, and this mode seems to
            // have no useful effect, we remove it to avoid confusion.
            while (values.contains("manual")) {
                values.remove("manual");
            }
        }
        SupportedValues supported_values = checkModeIsSupported(values, value, default_value);
        if (supported_values != null) {
            String white_balance = parameters.getWhiteBalance();
            // if white balance is null, it should mean white balances aren't supported anyway
            if (white_balance != null && !white_balance.equals(supported_values.selected_value)) {
                parameters.setWhiteBalance(supported_values.selected_value);
                setCameraParameters(parameters);
            }
        }
        return supported_values;
    }

    @Override
    public boolean setWhiteBalanceTemperature(int temperature) {
        // not supported for CameraController1
        return false;
    }

    @Override
    public SupportedValues setISO(String value) {
        Camera.Parameters parameters = this.getParameters();
        // get available isos - no standard value for this, see http://stackoverflow.com/questions/2978095/android-camera-api-iso-setting
        String iso_values = parameters.get("iso-values");
        if (iso_values == null) {
            iso_values = parameters.get("iso-mode-values"); // Galaxy Nexus
            if (iso_values == null) {
                iso_values = parameters.get("iso-speed-values"); // Micromax A101
                if (iso_values == null)
                    iso_values = parameters.get("nv-picture-iso-values"); // LG dual P990
            }
        }
        List<String> values = null;
        if (iso_values != null && iso_values.length() > 0) {
            String[] isos_array = iso_values.split(",");
            // split shouldn't return null
            if (isos_array.length > 0) {
                // remove duplicates (OnePlus 3T has several duplicate "auto" entries)
                HashSet<String> hashSet = new HashSet<>();
                values = new ArrayList<>();
                // use hashset for efficiency
                // make sure we alo preserve the order
                for (String iso : isos_array) {
                    if (!hashSet.contains(iso)) {
                        values.add(iso);
                        hashSet.add(iso);
                    }
                }
            }
        }

        iso_key = "iso";
        if (parameters.get(iso_key) == null) {
            iso_key = "iso-speed"; // Micromax A101
            if (parameters.get(iso_key) == null) {
                iso_key = "nv-picture-iso"; // LG dual P990
                if (parameters.get(iso_key) == null) {
                    if (Build.MODEL.contains("Z00"))
                        iso_key = "iso"; // Asus Zenfone 2 Z00A and Z008: see https://sourceforge.net/p/opencamera/tickets/183/
                    else
                        iso_key = null; // not supported
                }
            }
        }
		/*values = new ArrayList<>();
		//values.add("auto");
		//values.add("ISO_HJR");
		values.add("ISO50");
		values.add("ISO64");
		values.add("ISO80");
		values.add("ISO100");
		values.add("ISO125");
		values.add("ISO160");
		values.add("ISO200");
		values.add("ISO250");
		values.add("ISO320");
		values.add("ISO400");
		values.add("ISO500");
		values.add("ISO640");
		values.add("ISO800");
		values.add("ISO1000");
		values.add("ISO1250");
		values.add("ISO1600");
		values.add("ISO2000");
		values.add("ISO2500");
		values.add("ISO3200");
		values.add("auto");
		//values.add("400");
		//values.add("800");
		//values.add("1600");
		iso_key = "iso";*/
        if (iso_key != null) {
            if (values == null) {
                // set a default for some devices which have an iso_key, but don't give a list of supported ISOs
                values = new ArrayList<>();
                values.add("auto");
                values.add("50");
                values.add("100");
                values.add("200");
                values.add("400");
                values.add("800");
                values.add("1600");
            }
            SupportedValues supported_values = checkModeIsSupported(values, value, getDefaultISO());
            if (supported_values != null) {
                parameters.set(iso_key, supported_values.selected_value);
                setCameraParameters(parameters);
            }
            return supported_values;
        }
        return null;
    }

    @Override
    public void setManualISO(boolean manual_iso, int iso) {
        // not supported for CameraController1
    }

    @Override
    public void setPictureSize(int width, int height) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setPictureSize(width, height);
        setCameraParameters(parameters);
    }

    @Override
    public void setPreviewSize(int width, int height) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setPreviewFormat(ImageFormat.NV21);
        parameters.setPreviewSize(width, height);
        setCameraParameters(parameters);
    }

    @Override
    public void setPreviewCallback(Camera.PreviewCallback callback) {
        camera.setPreviewCallback(callback);
    }

    @Override
    public void setOptimiseAEForDRO(boolean optimise_ae_for_dro) {
        // not supported for CameraController1
    }


    public void setJpegQuality(int quality) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setJpegQuality(quality);
        setCameraParameters(parameters);
    }


    public int getExposureCompensation() {
        Camera.Parameters parameters = this.getParameters();
        return parameters.getExposureCompensation();
    }

    private float getExposureCompensationStep() {
        float exposure_step;
        Camera.Parameters parameters = this.getParameters();
        try {
            exposure_step = parameters.getExposureCompensationStep();
        } catch (Exception e) {
            // received a NullPointerException from StringToReal.parseFloat() beneath getExposureCompensationStep() on Google Play!
            e.printStackTrace();
            exposure_step = 1.0f / 3.0f; // make up a typical example
        }
        return exposure_step;
    }

    // Returns whether exposure was modified
    public boolean setExposureCompensation(int new_exposure) {
        Camera.Parameters parameters = this.getParameters();
        int current_exposure = parameters.getExposureCompensation();
        if (new_exposure != current_exposure) {
            parameters.setExposureCompensation(new_exposure);
            setCameraParameters(parameters);
            return true;
        }
        return false;
    }

    public void setPreviewFpsRange(int min, int max) {
        try {
            Camera.Parameters parameters = this.getParameters();
            parameters.setPreviewFpsRange(min, max);
            setCameraParameters(parameters);
        } catch (RuntimeException e) {
            // can get RuntimeException from getParameters - we don't catch within that function because callers may not be able to recover,
            // but here it doesn't really matter if we fail to set the fps range
            Log.e(TAG, "setPreviewFpsRange failed to get parameters");
            e.printStackTrace();
        }
    }

    public List<int[]> getSupportedPreviewFpsRange() {
        Camera.Parameters parameters = this.getParameters();
        try {
            return parameters.getSupportedPreviewFpsRange();
        } catch (StringIndexOutOfBoundsException e) {
			/* Have had reports of StringIndexOutOfBoundsException on Google Play on Sony Xperia M devices
				at android.hardware.Camera$Parameters.splitRange(Camera.java:4098)
				at android.hardware.Camera$Parameters.getSupportedPreviewFpsRange(Camera.java:2799)
				*/
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setFocusValue(String focus_value) {
        Camera.Parameters parameters = this.getParameters();
        switch (focus_value) {
            case "focus_mode_auto":
            case "focus_mode_locked":
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                break;
            case "focus_mode_infinity":
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_INFINITY);
                break;
            case "focus_mode_macro":
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_MACRO);
                break;
            case "focus_mode_fixed":
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
                break;
            case "focus_mode_edof":
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
                break;
            case "focus_mode_continuous_picture":
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                break;
            case "focus_mode_continuous_video":
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                break;
            default:
                break;
        }
        setCameraParameters(parameters);
//        ToastUtils.showToast("Log:" + parameters.getFocusMode());
    }

    private String convertFocusModeToValue(String focus_mode) {
        // focus_mode may be null on some devices; we return ""
        String focus_value = "";
        if (focus_mode == null) {
            // ignore, leave focus_value at ""
        } else if (focus_mode.equals(Camera.Parameters.FOCUS_MODE_AUTO)) {
            focus_value = "focus_mode_auto";
        } else if (focus_mode.equals(Camera.Parameters.FOCUS_MODE_INFINITY)) {
            focus_value = "focus_mode_infinity";
        } else if (focus_mode.equals(Camera.Parameters.FOCUS_MODE_MACRO)) {
            focus_value = "focus_mode_macro";
        } else if (focus_mode.equals(Camera.Parameters.FOCUS_MODE_FIXED)) {
            focus_value = "focus_mode_fixed";
        } else if (focus_mode.equals(Camera.Parameters.FOCUS_MODE_EDOF)) {
            focus_value = "focus_mode_edof";
        } else if (focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            focus_value = "focus_mode_continuous_picture";
        } else if (focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
            focus_value = "focus_mode_continuous_video";
        }
        return focus_value;
    }

    @Override
    public String getFocusValue() {
        // returns "" if Parameters.getFocusMode() returns null
        Camera.Parameters parameters = this.getParameters();
        String focus_mode = parameters.getFocusMode();
        // getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play
        return convertFocusModeToValue(focus_mode);
    }

    @Override
    public boolean setFocusDistance(float focus_distance) {
        // not supported for CameraController1!
        return false;
    }

    private String convertFlashValueToMode(String flash_value) {
        String flash_mode = "";
        switch (flash_value) {
            case "flash_off":
                flash_mode = Camera.Parameters.FLASH_MODE_OFF;
                break;
            case "flash_auto":
                flash_mode = Camera.Parameters.FLASH_MODE_AUTO;
                break;
            case "flash_on":
                flash_mode = Camera.Parameters.FLASH_MODE_ON;
                break;
            case "flash_torch":
                flash_mode = Camera.Parameters.FLASH_MODE_TORCH;
                break;
            case "flash_red_eye":
                flash_mode = Camera.Parameters.FLASH_MODE_RED_EYE;
                break;
            case "flash_frontscreen_on":
                flash_mode = Camera.Parameters.FLASH_MODE_OFF;
                break;
        }
        return flash_mode;
    }

    public void setFlashValue(String flash_value) {
        Camera.Parameters parameters = this.getParameters();
        if (parameters.getFlashMode() == null) {
            return;
        }
        final String flash_mode = convertFlashValueToMode(flash_value);
        if (flash_mode.length() > 0 && !flash_mode.equals(parameters.getFlashMode())) {
            if (parameters.getFlashMode().equals(Camera.Parameters.FLASH_MODE_TORCH) && !flash_mode.equals(Camera.Parameters.FLASH_MODE_OFF)) {
                // workaround for bug on Nexus 5 and Nexus 6 where torch doesn't switch off until we set FLASH_MODE_OFF
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                setCameraParameters(parameters);
                // need to set the correct flash mode after a delay
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (camera != null) { // make sure camera wasn't released in the meantime (has a Google Play crash as a result of this)
                            Camera.Parameters parameters = getParameters();
                            parameters.setFlashMode(flash_mode);
                            setCameraParameters(parameters);
                        }
                    }
                }, 100);
            } else {
                parameters.setFlashMode(flash_mode);
                setCameraParameters(parameters);
            }
        }
    }

    private String convertFlashModeToValue(String flash_mode) {
        // flash_mode may be null, meaning flash isn't supported; we return ""
        String flash_value = "";
        if (flash_mode == null) {
            // ignore, leave focus_value at ""
        } else if (flash_mode.equals(Camera.Parameters.FLASH_MODE_OFF)) {
            flash_value = "flash_off";
        } else if (flash_mode.equals(Camera.Parameters.FLASH_MODE_AUTO)) {
            flash_value = "flash_auto";
        } else if (flash_mode.equals(Camera.Parameters.FLASH_MODE_ON)) {
            flash_value = "flash_on";
        } else if (flash_mode.equals(Camera.Parameters.FLASH_MODE_TORCH)) {
            flash_value = "flash_torch";
        } else if (flash_mode.equals(Camera.Parameters.FLASH_MODE_RED_EYE)) {
            flash_value = "flash_red_eye";
        }
        return flash_value;
    }

    public String getFlashValue() {
        // returns "" if flash isn't supported
        Camera.Parameters parameters = this.getParameters();
        String flash_mode = parameters.getFlashMode(); // will be null if flash mode not supported
        return convertFlashModeToValue(flash_mode);
    }

    public void setRotation(int rotation) {
        Camera.Parameters parameters = this.getParameters();
        parameters.setRotation(rotation);
        setCameraParameters(parameters);
    }


    public void clearFocusAndMetering() {
        Camera.Parameters parameters = this.getParameters();
        boolean update_parameters = false;
        if (parameters.getMaxNumFocusAreas() > 0) {
            parameters.setFocusAreas(null);
            update_parameters = true;
        }
        if (parameters.getMaxNumMeteringAreas() > 0) {
            parameters.setMeteringAreas(null);
            update_parameters = true;
        }
        if (update_parameters) {
            setCameraParameters(parameters);
        }
    }


    @Override
    public boolean supportsAutoFocus() {
        Camera.Parameters parameters = this.getParameters();
        String focus_mode = parameters.getFocusMode();
        // getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play from the below line (v1.7),
        // on Galaxy Tab 10.1 (GT-P7500), Android 4.0.3 - 4.0.4; HTC EVO 3D X515m (shooteru), Android 4.0.3 - 4.0.4
        if (focus_mode != null && (focus_mode.equals(Camera.Parameters.FOCUS_MODE_AUTO) || focus_mode.equals(Camera.Parameters.FOCUS_MODE_MACRO))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean focusIsContinuous() {
        Camera.Parameters parameters = this.getParameters();
        String focus_mode = parameters.getFocusMode();
        // getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play from the below line (v1.7),
        // on Galaxy Tab 10.1 (GT-P7500), Android 4.0.3 - 4.0.4; HTC EVO 3D X515m (shooteru), Android 4.0.3 - 4.0.4
        if (focus_mode != null && (focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) || focus_mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))) {
            return true;
        }
        return false;
    }


    @Override
    public void setPreviewDisplay(SurfaceHolder holder) throws CameraControllerException {
        try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CameraControllerException();
        }
    }

    @Override
    public void setPreviewTexture(SurfaceTexture texture) throws CameraControllerException {
        try {
            camera.setPreviewTexture(texture);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CameraControllerException();
        }
    }

    @Override
    public void startPreview() throws CameraControllerException {
        try {
            camera.startPreview();
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new CameraControllerException();
        }
    }

    @Override
    public void stopPreview() {
        camera.stopPreview();
    }

    @Override
    public void autoFocus(final CameraController.AutoFocusCallback cb, boolean capture_follows_autofocus_hint) {
        Camera.AutoFocusCallback camera_cb = new Camera.AutoFocusCallback() {
            boolean done_autofocus = false;

            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                // in theory we should only ever get one call to onAutoFocus(), but some Samsung phones at least can call the callback multiple times
                // see http://stackoverflow.com/questions/36316195/take-picture-fails-on-samsung-phones
                // needed to fix problem on Samsung S7 with flash auto/on and continuous picture focus where it would claim failed to take picture even though it'd succeeded,
                // because we repeatedly call takePicture(), and the subsequent ones cause a runtime exception
                if (!done_autofocus) {
                    done_autofocus = true;
                    cb.onAutoFocus(success);
                }
            }
        };
        try {
            camera.autoFocus(camera_cb);
        } catch (RuntimeException e) {
            // just in case? We got a RuntimeException report here from 1 user on Google Play:
            // 21 Dec 2013, Xperia Go, Android 4.1
            e.printStackTrace();
            // should call the callback, so the application isn't left waiting (e.g., when we autofocus before trying to take a photo)
            cb.onAutoFocus(false);
        }
    }

    @Override
    public void setCaptureFollowAutofocusHint(boolean capture_follows_autofocus_hint) {
        // unused by this API
    }

    @Override
    public void cancelAutoFocus() {
        try {
            camera.cancelAutoFocus();
        } catch (RuntimeException e) {
            // had a report of crash on some devices, see comment at https://sourceforge.net/p/opencamera/tickets/4/ made on 20140520
            e.printStackTrace();
        }
    }

    @Override
    public void setContinuousFocusMoveCallback(final ContinuousFocusMoveCallback cb) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // setAutoFocusMoveCallback() requires JELLY_BEAN
            try {
                if (cb != null) {
                    camera.setAutoFocusMoveCallback(new AutoFocusMoveCallback() {
                        @Override
                        public void onAutoFocusMoving(boolean start, Camera camera) {
                            cb.onContinuousFocusMove(start);
                        }
                    });
                } else {
                    camera.setAutoFocusMoveCallback(null);
                }
            } catch (RuntimeException e) {
                // received RuntimeException reports from some users on Google Play - seems to be older devices, but still important to catch!
                e.printStackTrace();
            }
        }
    }

    private static class TakePictureShutterCallback implements Camera.ShutterCallback {
        // don't do anything here, but we need to implement the callback to get the shutter sound (at least on Galaxy Nexus and Nexus 7)
        @Override
        public void onShutter() {

        }
    }


    private void takePictureNow(final CameraController.PictureCallback picture, final ErrorCallback error) {
        final Camera.ShutterCallback shutter = new TakePictureShutterCallback();
        final Camera.PictureCallback camera_jpeg = picture == null ? null : new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera cam) {
                picture.onPictureTaken(data);
                picture.onCompleted();
            }
        };

        if (picture != null) {
            picture.onStarted();
        }
        try {
            camera.takePicture(shutter, null, camera_jpeg);
        } catch (RuntimeException e) {
            // just in case? We got a RuntimeException report here from 1 user on Google Play; I also encountered it myself once of Galaxy Nexus when starting up
            e.printStackTrace();
            error.onError();
        }
    }

    public void takePicture(final CameraController.PictureCallback picture, final ErrorCallback error) {
        takePictureNow(picture, error);
    }

    public void setDisplayOrientation(int degrees) {
        camera.setDisplayOrientation(degrees);
    }


    public int getCameraOrientation() {
        return camera_info.orientation;
    }

    public boolean isFrontFacing() {
        return (camera_info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

}
