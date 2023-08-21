package com.reyansh.batterylife.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.reyansh.batterylife.R

class PhoneFeaturesFragment : Fragment() {

    var packageManager: PackageManager? = null

    var tvWifi: TextView? = null
    var tvWifiDirect: TextView? = null
    var tvBluetooth: TextView? = null
    var tvBluetoothLE: TextView? = null
    var tvGPS: TextView? = null
    var tvCameraFlash: TextView? = null
    var tvCameraFront: TextView? = null
    var tvMicrophone: TextView? = null
    var tvNFC: TextView? = null
    var tvUSBHost: TextView? = null
    var tvUSBAccessory: TextView? = null
    var tvMultitouch: TextView? = null
    var tvAudioLowLatency: TextView? = null
    var tvAudioOutput: TextView? = null
    var tvProfessionalAudio: TextView? = null
    var tvConsumerIR: TextView? = null
    var tvGamepadSupport: TextView? = null
    var tvHIFISensor: TextView? = null
    var tvPrinting: TextView? = null
    var tvCDMA: TextView? = null
    var tvGSM: TextView? = null
    var tvFingerprint: TextView? = null
    var tvAppWidgets: TextView? = null
    var tvSIP: TextView? = null
    var tvSIPBasedVOIP: TextView? = null
    var tv_usb_host:TextView?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_phone_features, container, false)

        tvWifi = view.findViewById(R.id.tv_wifi)
        tvWifiDirect = view.findViewById(R.id.tv_wifi_direct)
        tvBluetooth = view.findViewById(R.id.tv_bluetooth)
        tvBluetoothLE = view.findViewById(R.id.tv_bluetooth_le)
        tvGPS = view.findViewById(R.id.tv_gps)
        tvCameraFlash = view.findViewById(R.id.tv_camera_flash)
        tvCameraFront = view.findViewById(R.id.tv_camera_front)
        tvMicrophone = view.findViewById(R.id.tv_microphone)
        tvNFC = view.findViewById(R.id.tv_nfc)
        tvUSBHost = view.findViewById(R.id.tv_usb_host)
        tvUSBAccessory = view.findViewById(R.id.tv_usb_accessory)
        tvMultitouch = view.findViewById(R.id.tv_multitouch)
        tvAudioLowLatency = view.findViewById(R.id.tv_audio_low_latency)
        tvAudioOutput = view.findViewById(R.id.tv_audio_output)
        tvProfessionalAudio = view.findViewById(R.id.tv_professional_audio)
        tvConsumerIR = view.findViewById(R.id.tv_consumer_ir)
        tvGamepadSupport = view.findViewById(R.id.tv_gamepad_support)
        tvHIFISensor = view.findViewById(R.id.tv_hifi_sensor)
        tvPrinting = view.findViewById(R.id.tv_printing)
        tvCDMA = view.findViewById(R.id.tv_cdma)
        tvGSM = view.findViewById(R.id.tv_gsm)
        tvFingerprint = view.findViewById(R.id.tv_fingerprint)
        tvAppWidgets = view.findViewById(R.id.tv_app_widgets)
        tvSIP = view.findViewById(R.id.tv_sip)
        tvSIPBasedVOIP = view.findViewById(R.id.tv_sip_based_voip)
        tv_usb_host=view.findViewById(R.id.tv_usb_host)
        packageManager = activity.packageManager
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getDeviceFeatures()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && isAdded) {
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceFeatures(): Unit {
        /** WIFI feature */
        val connManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        // WIFI
        if (mWifi.isAvailable) {
            tvWifi?.text = activity.getString(R.string.available)
        } else {
            tvWifi?.text = activity.getString(R.string.not_supported)
        }

        // WIFI Direct
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)!!) {
            tvWifiDirect?.text = activity.getString(R.string.available)
        } else {
            tvWifiDirect?.text = activity.getString(R.string.not_supported)
        }

        // Bluetooth
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)!!) {
            tvBluetooth?.text = activity.getString(R.string.available)
        } else {
            tvBluetooth?.text = activity.getString(R.string.not_supported)
        }

        // Bluetooth LE
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)!!) {
            tvBluetoothLE?.text = activity.getString(R.string.available)
        } else {
            tvBluetoothLE?.text = activity.getString(R.string.not_supported)
        }

        // GPS
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)!!) {
            tvGPS?.text = activity.getString(R.string.available)
        } else {
            tvGPS?.text = activity.getString(R.string.not_supported)
        }

        // Camera Flash
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)!!) {
            tvCameraFlash?.text = activity.getString(R.string.available)
        } else {
            tvCameraFlash?.text = activity.getString(R.string.not_supported)
        }

        // Camera Front
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)!!) {
            tvCameraFront?.text = activity.getString(R.string.available)
        } else {
            tvCameraFront?.text = activity.getString(R.string.not_supported)
        }

        // Microphone
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)!!) {
            tvMicrophone?.text = activity.getString(R.string.available)
        } else {
            tvMicrophone?.text = activity.getString(R.string.not_supported)
        }

        // NFC
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_NFC)!!) {
            tvNFC?.text = activity.getString(R.string.available)
        } else {
            tvNFC?.text = activity.getString(R.string.not_supported)
        }

        // USB Host
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_USB_HOST)!!) {
            tv_usb_host?.text = activity.getString(R.string.available)
        } else {
            tv_usb_host?.text = activity.getString(R.string.not_supported)
        }

        // USB Accessory
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_USB_ACCESSORY)!!) {
            tvUSBAccessory?.text = activity.getString(R.string.available)
        } else {
            tvUSBAccessory?.text = activity.getString(R.string.not_supported)
        }

        // Multitouch
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH)!!) {
            tvMultitouch?.text = activity.getString(R.string.available)
        } else {
            tvMultitouch?.text = activity.getString(R.string.not_supported)
        }

        // Audio low-latency
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_AUDIO_LOW_LATENCY)!!) {
            tvAudioLowLatency?.text = activity.getString(R.string.available)
        } else {
            tvAudioLowLatency?.text = activity.getString(R.string.not_supported)
        }

        // Audio Output
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)!!) {
            tvAudioOutput?.text = activity.getString(R.string.available)
        } else {
            tvAudioOutput?.text = activity.getString(R.string.not_supported)
        }

        // Professional Audio
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_AUDIO_PRO)!!) {
            tvProfessionalAudio?.text = activity.getString(R.string.available)
        } else {
            tvProfessionalAudio?.text = activity.getString(R.string.not_supported)
        }

        // Consumer IR
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_CONSUMER_IR)!!) {
            tvConsumerIR?.text = activity.getString(R.string.available)
        } else {
            tvConsumerIR?.text = activity.getString(R.string.not_supported)
        }

        // Gamepad Support
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_GAMEPAD)!!) {
            tvGamepadSupport?.text = activity.getString(R.string.available)
        } else {
            tvGamepadSupport?.text = activity.getString(R.string.not_supported)
        }

        // HIFI Sensor
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_HIFI_SENSORS)!!) {
            tvHIFISensor?.text = activity.getString(R.string.available)
        } else {
            tvHIFISensor?.text = activity.getString(R.string.not_supported)
        }

        // Printing
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_PRINTING)!!) {
            tvPrinting?.text = activity.getString(R.string.available)
        } else {
            tvPrinting?.text = activity.getString(R.string.not_supported)
        }

        // CDMA
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_CDMA)!!) {
            tvCDMA?.text = activity.getString(R.string.available)
        } else {
            tvCDMA?.text = activity.getString(R.string.not_supported)
        }

        // GSM
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_TELEPHONY_GSM)!!) {
            tvGSM?.text = activity.getString(R.string.available)
        } else {
            tvGSM?.text = activity.getString(R.string.not_supported)
        }

        // Finger-print
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)!!) {
            tvFingerprint?.text = activity.getString(R.string.available)
        } else {
            tvFingerprint?.text = activity.getString(R.string.not_supported)
        }

        // App Widgets
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_APP_WIDGETS)!!) {
            tvAppWidgets?.text = activity.getString(R.string.available)
        } else {
            tvAppWidgets?.text = activity.getString(R.string.not_supported)
        }

        // SIP
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_SIP)!!) {
            tvSIP?.text = activity.getString(R.string.available)
        } else {
            tvSIP?.text = activity.getString(R.string.not_supported)
        }

        // SIP based VOIP
        if (packageManager?.hasSystemFeature(PackageManager.FEATURE_SIP_VOIP)!!) {
            tvSIPBasedVOIP?.text = activity.getString(R.string.available)
        } else {
            tvSIPBasedVOIP?.text = activity.getString(R.string.not_supported)
        }
    }
}