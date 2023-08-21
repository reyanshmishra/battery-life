package com.reyansh.batterylife;

import android.app.Application;

import com.reyansh.batterylife.Service.BatteryAccessibilityService;

public class Common extends Application {
    private static Common mContext;


    private BatteryAccessibilityService mBatteryAccessibilityService;

    public BatteryAccessibilityService getBatteryAccessibilityService() {
        return mBatteryAccessibilityService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Common getInstance() {
        return mContext;
    }
    public void setBatteryAccessibilityService(BatteryAccessibilityService batteryAccessibilityService) {
        mBatteryAccessibilityService = batteryAccessibilityService;
    }


}
