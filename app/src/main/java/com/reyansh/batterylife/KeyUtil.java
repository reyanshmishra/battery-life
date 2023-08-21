
package com.reyansh.batterylife;

import android.content.ComponentName;
import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

public class KeyUtil {
    public static final String datePattern = "dd MMM yyyy HH:mm:ss z";
    public static final String KEY_MODE = "key_mode";

    /*** Sensor */
    public static final String KEY_SENSOR_NAME = "key_sensor_name";
    public static final String KEY_SENSOR_TYPE = "key_sensor_type";
    public static final String KEY_SENSOR_ICON = "key_sensor_icon";

    public static float KEY_LAST_KNOWN_HUMIDITY = 0f;

    public static final int KEY_CAMERA_CODE = 101;
    public static final int KEY_CALL_PERMISSION = 102;
    public static final int KEY_READ_PHONE_STATE = 103;
    public static final int IS_USER_COME_FROM_SYSTEM_APPS = 1;
    public static final int IS_USER_COME_FROM_USER_APPS = 2;




    public static boolean isAccessibilityServiceEnabled(Context context, Class<?> accessibilityService) {
        ComponentName expectedComponentName = new ComponentName(context, accessibilityService);

        String enabledServicesSetting = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
        if (enabledServicesSetting == null)
            return false;

        TextUtils.SimpleStringSplitter colonSplitter = new TextUtils.SimpleStringSplitter(':');
        colonSplitter.setString(enabledServicesSetting);

        while (colonSplitter.hasNext()) {
            String componentNameString = colonSplitter.next();
            ComponentName enabledService = ComponentName.unflattenFromString(componentNameString);

            if (enabledService != null && enabledService.equals(expectedComponentName))
                return true;
        }

        return false;
    }
}
