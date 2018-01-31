package com.moriarty.base.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.moriarty.base.encryption.AES;
import com.moriarty.base.log.L;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


/**
 * Created by yangming on 17/3/22.
 */

public class DeviceIdUtls {
    private static String filePath = File.separator + ".D";
    private static String uuid;

    public static String getUniversalID(Context context) {
        if (!TextUtils.isEmpty(uuid)) {
            return uuid;
        }
        try {
            String fileRootPath = getPath(context) + filePath;
            String aesUuid = FileUtils.readFile(fileRootPath);

            if (TextUtils.isEmpty(aesUuid)) {

                uuid = UUID.randomUUID().toString();

                if (!TextUtils.isEmpty(uuid)) {
                    saveUUID(context, AES.encrypt(uuid));
                }
            } else {
                String decryptUuid = AES.decrypt(aesUuid);
                if (TextUtils.isEmpty(decryptUuid)) {
                    uuid = UUID.randomUUID().toString();

                    if (!TextUtils.isEmpty(uuid)) {
                        saveUUID(context, AES.encrypt(uuid));
                    }
                } else {
                    uuid = decryptUuid;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (TextUtils.isEmpty(uuid)) {
                uuid = UUID.randomUUID().toString();
                L.e("Exception uuid " + uuid);
                return uuid;
            }
        }
        L.e("uuid " + uuid);
        return uuid;
    }

    private static void saveUUID(Context context, String UUID) {
        String fileRootPath = getPath(context) + filePath;

        FileUtils.writeFile(fileRootPath, UUID);
    }

    public static String getPath(Context context) {
        //首先判断是否有外部存储卡，如没有判断是否有内部存储卡，如没有，继续读取应用程序所在存储
//        String phonePicsPath = getExternalSdCardPath();
//        if (phonePicsPath == null) {
        String phonePicsPath = context.getFilesDir().getAbsolutePath();
//        }
        return phonePicsPath;
    }

    /**
     * 遍历 "system/etc/vold.fstab” 文件，获取全部的Android的挂载点信息
     *
     * @return
     */
    private static ArrayList<String> getDevMountList() {
        String[] toSearch = FileUtils.readFile("/system/etc/vold.fstab").split(" ");
        ArrayList<String> out = new ArrayList<>();
        for (int i = 0; i < toSearch.length; i++) {
            if (toSearch[i].contains("dev_mount")) {
                if (new File(toSearch[i + 2]).exists()) {
                    out.add(toSearch[i + 2]);
                }
            }
        }
        return out;
    }

    /**
     * 获取扩展SD卡存储目录
     * <p/>
     * 如果有外接的SD卡，并且已挂载，则返回这个外置SD卡目录
     * 否则：返回内置SD卡目录
     *
     * @return
     */
    public static String getExternalSdCardPath() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            return sdCardFile.getAbsolutePath();
        }

        String path = null;

        File sdCardFile = null;

        ArrayList<String> devMountList = getDevMountList();

        for (String devMount : devMountList) {
            File file = new File(devMount);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();

                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }

        if (path != null) {
            sdCardFile = new File(path);
            return sdCardFile.getAbsolutePath();
        }

        return null;
    }
}
