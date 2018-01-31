package com.moriarty.base.manager;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.moriarty.base.BaseApplication;
import com.moriarty.base.encryption.AES;
import com.moriarty.base.util.PackageUtils;
import com.moriarty.base.util.SDCardUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;



/**
 * APP 文件管理
 */

public class FileManager {

    private static final String BASE_FILE_PATH = new File(
            Environment.getExternalStorageDirectory(), "EBPFortune")
            .getAbsolutePath();

    // 获取图片下载文件夹
    public static File getImageSaveDir() {

        File imageSaveDir = new File(BASE_FILE_PATH, "image");
        if (!imageSaveDir.exists())
            imageSaveDir.mkdirs();

        return imageSaveDir;
    }

    // 获取图片下载文件夹
    public static File getAppSaveDir() {
//        File imageSaveDir = new File(BASE_FILE_PATH, "app");
        File imageSaveDir = new File(BaseApplication.getApplication().getExternalFilesDir(null), "app");
        if (!imageSaveDir.exists())
            imageSaveDir.mkdirs();
        return imageSaveDir;
    }

    public static void deleteOldApk() {
        if (!SDCardUtils.isSDCardEnable())
            return;
        File appSaveDir = getAppSaveDir();
        for (File f : appSaveDir.listFiles()) {
            String regex = "EBPFortune_.*\\.apk";
            if (f.getName().matches(regex)) {
                String verCode = f.getName().replace("EBPFortune_", "")
                        .replace(".apk", "");
                if (Integer.parseInt(verCode) <= PackageUtils
                        .getVersionCode(BaseApplication.getApplication())) {
                    f.delete();
                }
            }
        }
    }

//    public static String getAppSaveFileName(int versionCode) {
//        return "ebpFortune_" + versionCode + ".apk";
//    }

    public static String getAppSaveFileName(String versionName) {
        return "EBPFortune_" + versionName + ".apk";
    }

    public static File getCacheDir(Context context) {

        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return context.getExternalCacheDir();
        }
        return context.getCacheDir();
    }


    public static File getChanelSaveFile(Context context) {
        String chanelFileName = ".qd";
        File chanelSaveFile;
        if (SDCardUtils.isSDCardEnable()) {
//            chanelSaveFile = new File(BASE_FILE_PATH, chanelFileName);
            chanelSaveFile = new File(BaseApplication.getApplication().getExternalFilesDir(null), chanelFileName);
        } else {
            chanelSaveFile = new File(context.getFilesDir(), chanelFileName);
        }

        return chanelSaveFile;
    }

    public static void saveChanelToFile(Context context, String chanel) {
        File chanelSaveFile = getChanelSaveFile(context);
        try {
            if (!chanelSaveFile.exists()) {
                if (!chanelSaveFile.getParentFile().exists()) {
                    chanelSaveFile.getParentFile().mkdirs();
                }
                chanelSaveFile.createNewFile();
            }

            FileOutputStream fos = new FileOutputStream(chanelSaveFile);
            fos.write(AES.encrypt(chanel).getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getChanelFromFile(Context context) {
        File chanelSaveFile = getChanelSaveFile(context);
        if (!chanelSaveFile.exists()) {
            return null;
        }
        try {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(chanelSaveFile)));
            String readLine = bf.readLine();
            if (!TextUtils.isEmpty(readLine)) {
                return AES.decrypt(readLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
