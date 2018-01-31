package com.moriarty.base.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.moriarty.base.BaseApplication;

import java.util.UUID;


/**
 * 设备信息
 */
public class DeviceInfoUtils {


    private static final String PREFERENCES_DEVICE = "PREFERENCES_DEVICE";
    private static final String KEY_UNIQUEID = "unique_id";

    private static String UniqueId = null;// 内存中高缓存一个，不用每次拉网络的时候都访问磁盘去获取id

    public static void clearCache() {
        UniqueId = null;
    }

    /**
     * 获取唯一标识，如果没有创建，会自动创建一个；每次自动创建的值是不同的。
     *
     * @return
     */
    public static String getUniqueId() {
        if (!TextToolkit.isEmpty(UniqueId)) {
            return UniqueId;
        }
        SharedPreferences sp = BaseApplication.getApplication().getSharedPreferences(PREFERENCES_DEVICE, Context.MODE_PRIVATE);
        UniqueId = sp.getString(KEY_UNIQUEID, null);
        if (TextToolkit.isEmpty(UniqueId)) {
            UniqueId = createUniqueId();
        }
        return UniqueId;
    }


    /**
     * 创建唯一标识，如果已经创建过，再次调用会覆盖；每次创建的值都不同；
     */
    private static String createUniqueId() {
        SharedPreferences sp = BaseApplication.getApplication().getSharedPreferences(PREFERENCES_DEVICE, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        String id = UUID.randomUUID().toString();
        editor.putString(KEY_UNIQUEID, id);
        if (editor.commit()) {
            return id;
        } else {
            return null;
        }
    }

    /**
     * 获取屏幕的宽度和高度，以像素单位返回一个数组，数组[0]是宽度，数组[1]是高度
     *
     * @return
     */
    public static int[] getDeviceWidthAndHeightWithPixel() {
        Context context = BaseApplication.getApplication();
        if (context == null) {
            return new int[]{480, 800};
        }

        DisplayMetrics metric = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        return new int[]{width, height};
    }

    public static float getIconScale() {
        int width = getDeviceWidthAndHeightWithPixel()[0];
        if (width >= 1080) {
            return 1f;
        }
        if (width >= 720) {
            return 0.75f;
        }
        if (width >= 480) {
            return 0.5f;
        }
        return 0.5f;
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    public static boolean isNetworkAvailable() {
        Context context = BaseApplication.getApplication();
        if (context == null) {
            return false;
        }
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }

    /**
     * 获取当前手机NFC状况，NFC_NO_SUPPOERT：不支持, NFC_DISABLE：支持但没开启, NFC_ENABLE：支持且开启
     *
     * @return
     */
    public static NFCState getNFCState() {
        Context context = BaseApplication.getApplication();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null)
            return NFCState.NFC_NO_SUPPOERT;
        if (!nfcAdapter.isEnabled()) {
            return NFCState.NFC_DISABLE;
        }
        return NFCState.NFC_ENABLE;
    }

    public enum NFCState {
        NFC_NO_SUPPOERT, NFC_DISABLE, NFC_ENABLE
    }

    public static int getWxScale(int sourceWidth, int sourceHeight) {
        DisplayMetrics dm = BaseApplication.getApplication().getResources().getDisplayMetrics();
        float screenDensity = dm.density;
        int destWidthAndHeight = (int) (48 * screenDensity);
        return Math.max(Math.max(sourceWidth / destWidthAndHeight, sourceHeight / destWidthAndHeight), 1);
    }


    /**
     * 判断SIM卡
     *
     * @return
     */
    public static boolean hasSimCard() {
        Context context = BaseApplication.getApplication();
        if (context == null) {
            return false;
        }
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return manager.getSimState() != TelephonyManager.SIM_STATE_ABSENT;
    }

    /**
     */
    public static String getDeviceId() {
        Context context = BaseApplication.getApplication();

        String deviceId =null;
//        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//          deviceId = telephonyManager.getDeviceId();
//        if (!TextUtils.isEmpty(deviceId)) {
//            return deviceId;
//        }
//
//        if (TextUtils.isEmpty(deviceId)) {
//            WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//            if (wifiMan != null) {
//                WifiInfo wifiInf = wifiMan.getConnectionInfo();
//                if (wifiInf != null && wifiInf.getMacAddress() != null) {// 48位，如FA:34:7C:6D:E4:D7
//                    deviceId = wifiInf.getMacAddress().replaceAll(":", "");
//                    return deviceId;
//                }
//            }
//        }
//        if (TextUtils.isEmpty(deviceId)) {
//            deviceId = android.provider.Settings.Secure.getString(context.getContentResolver(),
//                    android.provider.Settings.Secure.ANDROID_ID);
//        }
        if (TextUtils.isEmpty(deviceId)) {
//            deviceId = UUID.randomUUID().toString().replaceAll("-", "");// UUID通用唯一识别码(Universally
            deviceId = getUniqueId();
        }
        return deviceId;
    }

    /**
     * @return
     */
    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * @return
     */
    public static String getPhoneModel() {
        return android.os.Build.MODEL;
    }

}