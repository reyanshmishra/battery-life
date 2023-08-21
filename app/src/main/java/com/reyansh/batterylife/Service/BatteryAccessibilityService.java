package com.reyansh.batterylife.Service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import com.reyansh.batterylife.Common;

import java.util.List;

public class BatteryAccessibilityService extends AccessibilityService {
    public boolean mFromThis = false;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!mFromThis) {
            return;
        }
        if (AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED == event.getEventType()) {
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) {
                return;
            }

            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId("com.android.settings:id/right_button");
            for (AccessibilityNodeInfo node : list) {
                if (node.isEnabled()) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                } else {
                    performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    Toast.makeText(this, "App hibernated.", Toast.LENGTH_SHORT).show();
                }
            }

            list = nodeInfo.findAccessibilityNodeInfosByViewId("android:id/button1");
            for (AccessibilityNodeInfo node : list) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
        }
    }

    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        info.notificationTimeout = 100;
        info.feedbackType = AccessibilityEvent.TYPES_ALL_MASK;
        setServiceInfo(info);
        Common.getInstance().setBatteryAccessibilityService(this);
    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}