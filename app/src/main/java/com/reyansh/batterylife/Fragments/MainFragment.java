package com.reyansh.batterylife.Fragments;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gelitenight.waveview.library.WaveView;
import com.timqi.sectorprogressview.ColorfulRingProgressView;
import com.reyansh.batterylife.Activities.OptimizeActivity;
import com.reyansh.batterylife.Model.SDCardInfo;
import com.reyansh.batterylife.R;
import com.reyansh.batterylife.StorageUtil;
import com.reyansh.batterylife.WaveHelper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by reyansh on 3/20/18.
 */

public class MainFragment extends Fragment {


    private WaveHelper mWaveHelper;
    @BindView(R.id.wave)
    WaveView mWaveView;

    int mBorderColor = Color.parseColor("#C999FF");
    int mBorderWidth = 10;

    @BindView(R.id.cpu_seekbar)
    ColorfulRingProgressView mCPUSeekbar;
    @BindView(R.id.storage_seekbar)
    ColorfulRingProgressView mStorageSeekbar;

    @BindView(R.id.ram_seekbar)
    ColorfulRingProgressView mRamSeekbar;

    @BindView(R.id.temperature_text)
    TextView mTemperatureText;

    @BindView(R.id.voltage_text)
    TextView mVoltageText;

    @BindView(R.id.capacity_text)
    TextView mCapacityText;

    @BindView(R.id.battery_status)
    TextView mBatteryChargingStatus;

    @BindView(R.id.optimise_button)
    Button mOptimize;

    @BindView(R.id.text_seekbar_storage)
    TextView mStorageSeekbarText;

    @BindView(R.id.text_seekbar_cpu)
    TextView mCpuSeekbarText;

    @BindView(R.id.text_seekbar_ram)
    TextView ramSeekbarText;

    @BindView(R.id.health_text)
    TextView mHealthText;

    @BindView(R.id.battery_percentage)
    TextView batteryPercentage;

    int batteryHealth = -1;
    int mBatteryLevel = -1;

    View mView;

    IntentFilter mIntentFilter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, mView);

        mWaveView.setBorder(mBorderWidth, mBorderColor);
        mWaveHelper = new WaveHelper(mWaveView);
        mWaveView.setWaveColor(Color.parseColor("#28C999FF"), Color.parseColor("#B895F9"));
        mWaveView.setShapeType(WaveView.ShapeType.CIRCLE);


        mOptimize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, OptimizeActivity.class));
            }
        });


        SDCardInfo mSDCardInfo = StorageUtil.getSDCardInfo();
        SDCardInfo mSystemInfo = StorageUtil.getSystemSpaceInfo(mContext);

        long nAvailaBlock;
        long TotalBlocks;

        if (mSDCardInfo != null) {
            nAvailaBlock = mSDCardInfo.free + mSystemInfo.free;
            TotalBlocks = mSDCardInfo.total + mSystemInfo.total;
        } else {
            nAvailaBlock = mSystemInfo.free;
            TotalBlocks = mSystemInfo.total;
        }

        float cputemp = getCpuTemp();
        mCPUSeekbar.setPercent(cputemp);


        float percentStore = (((TotalBlocks - nAvailaBlock) / (float) TotalBlocks) * 100);
        mStorageSeekbar.setPercent(percentStore);

        mStorageSeekbarText.setText(Html.fromHtml("<b> " + Math.round(percentStore) + " %</b><br>Storage"));

        mCpuSeekbarText.setText(Html.fromHtml("<b> " + Math.round(cputemp) + "°C</b><br>CPU"));

        mRamSeekbar.setPercent(availableRam());

        ramSeekbarText.setText(Html.fromHtml("<b> " + Math.round(availableRam()) + " %</b><br>RAM"));

        mIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(batteryReceiver, mIntentFilter);

        mCapacityText.setText(Html.fromHtml("<b>" + getmAh() + " mAh </b> <br> CAPACITY"));

        return mView;
    }


    BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        int scale = -1;
        float voltage = -1;
        int temp = -1;

        @Override
        public void onReceive(Context context, Intent intent) {
            if (!isAdded() && getActivity() == null) return;

            mBatteryLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
            voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
            batteryHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

            float volts = voltage / 1000f;
            mVoltageText.setText(Html.fromHtml("<b>" + volts + "V </b> <br> VOLTAGE"));
            int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);

            boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
            boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;

            if (usbCharge || acCharge) {
                mBatteryChargingStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
                mBatteryChargingStatus.setText("Charging");
            } else {
                mBatteryChargingStatus.setText("Discharging");
                mBatteryChargingStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
            }

            Log.e("BatteryManager", "mBatteryLevel is " + mBatteryLevel + "/" + scale + ", temp is " + temp + ", voltage is " + voltage + " Charging " + acCharge);

            float batteryTemp = temp / 10f;
            mTemperatureText.setText(Html.fromHtml("<b> " + batteryTemp + "°C</b><br>BATTERY<br>TEMP"));
            setBatteryHealth();
        }
    };

    Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void setBatteryHealth() {


        if (batteryHealth == 1) {
            mHealthText.setText(getString(R.string.unknown));
        } else if (batteryHealth == 2) {
            mHealthText.setText(getString(R.string.good));
            mHealthText.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else if (batteryHealth == 3) {
            mHealthText.setText(getString(R.string.over_heated));
            mHealthText.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        } else if (batteryHealth == 4) {
            mHealthText.setText(getString(R.string.dead));
            mHealthText.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        } else if (batteryHealth == 5) {
            mHealthText.setText(getString(R.string.over_voltage));
            mHealthText.setTextColor(ContextCompat.getColor(mContext, R.color.yellow));
        } else if (batteryHealth == 6) {
            mHealthText.setText(getString(R.string.failed));
            mHealthText.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        } else {
            mHealthText.setText(getString(R.string.cold));
        }
        float value = mBatteryLevel / 100f;
        String remainingBattery = String.valueOf(mBatteryLevel);
        batteryPercentage.setText(remainingBattery.concat("%"));
        mWaveHelper.setPercentage(value);
        mWaveHelper.start();

    }

    public int getmAh() {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            double batteryCapacity = (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");
            return (int) batteryCapacity;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    private int availableRam() {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) mContext.
                getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double percentAvail = mi.availMem / (double) mi.totalMem * 100.0;
        return 100 - ((int) percentAvail);
    }

    public float getCpuTemp() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            float temp = Integer.valueOf(line);

            if (temp > 80) {
                return getCpuTemp2();
            }
            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    private float getCpuTemp2() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("cat sys/class/hwmon/hwmonX/temp1_input");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line = reader.readLine();
            float temp = Integer.valueOf(line);

            return temp;

        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }


    @Override
    public void onResume() {
        super.onResume();
        mWaveHelper.start();

    }
}
