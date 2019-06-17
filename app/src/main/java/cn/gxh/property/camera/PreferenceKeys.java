package cn.gxh.property.camera;

/** Stores all of the string keys used for SharedPreferences.
 */
public class PreferenceKeys {

    public static String getUseCamera2PreferenceKey() {
    	return "preference_use_camera2";
    }


    public static String getResolutionPreferenceKey(int cameraId) {
    	return "camera_resolution_" + cameraId;
    }
    
    public static String getExposurePreferenceKey() {
    	return "preference_exposure";
    }

    public static String getColorEffectPreferenceKey() {
    	return "preference_color_effect";
    }

    public static String getSceneModePreferenceKey() {
    	return "preference_scene_mode";
    }

    public static String getWhiteBalancePreferenceKey() {
    	return "preference_white_balance";
    }

    public static String getISOPreferenceKey() {
    	return "preference_iso";
    }
    
    public static String getQualityPreferenceKey() {
    	return "preference_quality";
    }
    
    public static String getCamera2FakeFlashPreferenceKey() {
    	return "preference_camera2_fake_flash";
    }

    public static String getShowWhenLockedPreferenceKey() {
    	return "preference_show_when_locked";
    }

    public static String getStartupFocusPreferenceKey() {
    	return "preference_startup_focus";
    }

    public static String getKeepDisplayOnPreferenceKey() {
    	return "preference_keep_display_on";
    }

//    public static String getMaxBrightnessPreferenceKey() {
//    	return "preference_max_brightness";
//    }

    public static String getLockOrientationPreferenceKey() {
    	return "preference_lock_orientation";
    }
    
    public static String getBurstModePreferenceKey() {
    	return "preference_burst_mode";
    }
    

}
