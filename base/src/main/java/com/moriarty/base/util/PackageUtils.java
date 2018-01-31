package com.moriarty.base.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.moriarty.base.BaseApplication;


public class PackageUtils {


    /**
     * 获取当前版本名称
     */
    public static String getCurrentVersionName() {
        Context context = BaseApplication.getApplication();
        if (context == null) {
            return "";
        }
        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packInfo == null) {
            return "";
        }
        String version = packInfo.versionName;
        if (version.contains(".DEBUG")) {
            version = version.replace(".DEBUG", "");
        } else if (version.contains(".TEST")) {
            version = version.replace(".TEST", "");
        }

        return version;
    }

    /**
     * 获取当前版本号
     */
    public static int getCurrentVersionCode() {
        Context context = BaseApplication.getApplication();

        // 获取packagemanager的实例
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * 获取渠道名称
     *
     * @return
     */
//    public static String getChannel() {
//
//        String chanel = FileManager.getChanelFromFile(BaseApplication.getApplication());
//        if (TextUtils.isEmpty(chanel)) {
//            chanel = AnalyticsFactory.getInstance().getChannelId(BaseApplication.getApplication());
//            FileManager.saveChanelToFile(BaseApplication.getApplication(), chanel);
//        }
//        return chanel;
//
////        Context context = BaseApplication.getApplication();
////        ApplicationInfo appInfo = null;
////        String channle = " ";
////        try {
////            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
//////            channle = String.valueOf(appInfo.metaData.getString("UMENG_CHANNEL"));
////            channle = String.valueOf(appInfo.metaData.getString("TD_CHANNEL_ID"));
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return channle;
//    }


    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称
     */
//    public static String getVersionName(Context context) {
//        String versionName = null;
//
//        try {
//            PackageManager packageManager = context.getPackageManager();
//            PackageInfo packageInfo = packageManager.getPackageInfo(
//                    context.getPackageName(), 0);
//            versionName = packageInfo.versionName;
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        return versionName;
//    }

    /**
     * 获取本app的版本号
     *
     * @param context
     * @return
     */

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

}
