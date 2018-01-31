package com.moriarty.base.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.util.UUID;

/**
 * Created by yang on 2015/7/27.
 */
public class DeviceUtil {
    /**
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String deviceId = telephonyManager.getDeviceId();
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }

        if (TextUtils.isEmpty(deviceId)) {
            WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiMan != null) {
                @SuppressLint("MissingPermission") WifiInfo wifiInf = wifiMan.getConnectionInfo();
                if (wifiInf != null && wifiInf.getMacAddress() != null) {// 48位，如FA:34:7C:6D:E4:D7
                    deviceId = wifiInf.getMacAddress().replaceAll(":", "");
                    return deviceId;
                }
            }
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        }
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString().replaceAll("-", "");// UUID通用唯一识别码(Universally
        }
        return deviceId;
    }

    /**
     *
     * @return
     */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     *
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }
}
